package com.nabep.gaslift;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** GasLift NABEP - balance de gas. Todos los recursos se cargan desde APK, sin INTERNET. */
public final class MainActivity extends Activity {
    private static final String HOST="appassets.androidplatform.net";
    private static final int OPEN_FILE=401;
    private static final int SAVE_FILE=402;
    private WebView web;
    private android.webkit.ValueCallback<Uri[]> picker;
    private byte[] pendingBytes;
    private String pendingName;

    @Override
    @SuppressLint({"SetJavaScriptEnabled","JavascriptInterface"})
    public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(16,47,80));
        getWindow().setNavigationBarColor(Color.rgb(16,47,80));
        web=new WebView(this);
        setContentView(web);
        web.setBackgroundColor(Color.rgb(243,246,250));
        WebSettings ws=web.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setAllowFileAccess(false);
        ws.setAllowContentAccess(true);
        ws.setAllowFileAccessFromFileURLs(false);
        ws.setAllowUniversalAccessFromFileURLs(false);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        ws.setDefaultTextEncodingName("UTF-8");
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        web.addJavascriptInterface(new NativeBridge(),"GasLiftAndroid");
        web.setWebViewClient(new LocalClient());
        web.setWebChromeClient(new LocalChrome());
        web.loadUrl("https://"+HOST+"/index.html");
    }

    private WebResourceResponse blank(){
        return new WebResourceResponse("text/plain","UTF-8",new ByteArrayInputStream(new byte[0]));
    }
    private String mime(String file){
        if(file.endsWith(".html"))return "text/html";
        if(file.endsWith(".js"))return "application/javascript";
        if(file.endsWith(".css"))return "text/css";
        if(file.endsWith(".png"))return "image/png";
        if(file.endsWith(".webp"))return "image/webp";
        if(file.endsWith(".jpg")||file.endsWith(".jpeg"))return "image/jpeg";
        if(file.endsWith(".webmanifest")||file.endsWith(".json"))return "application/json";
        return "text/plain";
    }

    private final class LocalClient extends WebViewClient {
        @Override public WebResourceResponse shouldInterceptRequest(WebView view,WebResourceRequest request) {
            Uri uri=request.getUrl();
            if(!"https".equalsIgnoreCase(uri.getScheme()) || !HOST.equals(uri.getHost()))return blank();
            String path=uri.getPath();
            if(path==null||path.equals("/"))path="/index.html";
            if(path.contains("..")||path.indexOf('\\')>=0)return blank();
            String file=path.substring(1);
            try {
                InputStream stream=getAssets().open(file);
                String type=mime(file);
                return new WebResourceResponse(type,(type.startsWith("text/")||type.equals("application/javascript")||type.equals("application/json"))?"UTF-8":null,stream);
            } catch(IOException e){return blank();}
        }

        @Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest request) {
            Uri uri=request.getUrl();
            return !("https".equalsIgnoreCase(uri.getScheme())&&HOST.equals(uri.getHost()));
        }

        @Override public void onPageFinished(WebView view,String url) {
            super.onPageFinished(view,url);
            // Adaptar las funciones HTML de exportar / imprimir a opciones nativas Android.
            String bridge="(function(){"
                +"window.downloadFile=function(name,content,type){try{var b=btoa(unescape(encodeURIComponent(String(content))));"
                +"GasLiftAndroid.saveFile(name,type||'text/plain',b);}catch(e){alert('No se pudo guardar el archivo');}};"
                +"window.print=function(){GasLiftAndroid.printReport();};"
                +"})();";
            view.evaluateJavascript(bridge,null);
        }
    }

    private final class LocalChrome extends WebChromeClient {
        @Override public boolean onShowFileChooser(WebView view,android.webkit.ValueCallback<Uri[]> cb,FileChooserParams params){
            if(picker!=null)picker.onReceiveValue(null);
            picker=cb;
            Intent i=new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("*/*");
            i.addCategory(Intent.CATEGORY_OPENABLE);
            try{startActivityForResult(Intent.createChooser(i,"Importar Excel, CSV o JSON"),OPEN_FILE);}
            catch(ActivityNotFoundException ex){picker.onReceiveValue(null);picker=null;toast("No hay selector de archivos");}
            return true;
        }
        @Override public boolean onJsAlert(WebView v,String url,String msg,JsResult result){
            new AlertDialog.Builder(MainActivity.this).setTitle("GasLift NABEP").setMessage(msg)
               .setPositiveButton("Aceptar",(d,w)->result.confirm()).setOnCancelListener(d->result.cancel()).show();
            return true;
        }
        @Override public boolean onJsConfirm(WebView v,String url,String msg,JsResult result){
            new AlertDialog.Builder(MainActivity.this).setTitle("GasLift NABEP").setMessage(msg)
               .setPositiveButton("Sí",(d,w)->result.confirm())
               .setNegativeButton("No",(d,w)->result.cancel()).setOnCancelListener(d->result.cancel()).show();
            return true;
        }
        @Override public boolean onJsPrompt(WebView v,String url,String msg,String def,JsPromptResult result){
            EditText field=new EditText(MainActivity.this);
            field.setSingleLine(true);
            field.setText(def);
            new AlertDialog.Builder(MainActivity.this).setTitle("GasLift NABEP").setMessage(msg).setView(field)
               .setPositiveButton("Aceptar",(d,w)->result.confirm(field.getText().toString()))
               .setNegativeButton("Cancelar",(d,w)->result.cancel()).setOnCancelListener(d->result.cancel()).show();
            return true;
        }
    }

    public final class NativeBridge {
        @JavascriptInterface public void printReport(){
            runOnUiThread(()->web.postDelayed(()->{
                PrintManager printer=(PrintManager)getSystemService(PRINT_SERVICE);
                if(printer!=null) {
                    PrintAttributes attr=new PrintAttributes.Builder()
                      .setMediaSize(PrintAttributes.MediaSize.ISO_A4).build();
                    printer.print("Balance de gas NABEP",web.createPrintDocumentAdapter("GasLift NABEP"),attr);
                }
            },150));
        }
        @JavascriptInterface public void saveFile(String filename,String type,String base64) {
            try{
                byte[] bytes=Base64.decode(base64,Base64.DEFAULT);
                if(bytes.length>12*1024*1024)throw new IllegalArgumentException("Demasiado grande");
                String clean=filename.replaceAll("[^A-Za-z0-9_.\\-]","_");
                runOnUiThread(()->{
                    pendingBytes=bytes;pendingName=clean;
                    Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType((type==null?"application/octet-stream":type).split(";")[0]);
                    i.putExtra(Intent.EXTRA_TITLE,clean);
                    try{startActivityForResult(i,SAVE_FILE);}
                    catch(ActivityNotFoundException ex){toast("No hay gestor de archivos");}
                });
            }catch(Exception e){runOnUiThread(()->toast("No se pudo exportar"));}
        }
        @JavascriptInterface public void shareText(String text) {
            runOnUiThread(()->{
                Intent send=new Intent(Intent.ACTION_SEND);
                send.setType("text/plain");
                send.putExtra(Intent.EXTRA_TEXT,text);
                try{startActivity(Intent.createChooser(send,"Compartir balance"));}
                catch(Exception e){toast("No se encontró aplicación para compartir");}
            });
        }
    }

    @Override @SuppressWarnings("deprecation")
    protected void onActivityResult(int request,int result,Intent data){
        super.onActivityResult(request,result,data);
        if(request==OPEN_FILE){
            if(picker!=null){
                picker.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(result,data));
                picker=null;
            }
        } else if(request==SAVE_FILE) {
            if(result==RESULT_OK&&data!=null&&data.getData()!=null&&pendingBytes!=null){
                try(OutputStream out=getContentResolver().openOutputStream(data.getData())){
                    if(out==null)throw new IOException("Sin salida");
                    out.write(pendingBytes);
                    toast("Archivo guardado: "+pendingName);
                } catch(Exception ex){toast("Error al guardar archivo");}
            }
            pendingBytes=null;pendingName=null;
        }
    }

    private void toast(String msg){Toast.makeText(this,msg,Toast.LENGTH_LONG).show();}
    @Override public void onBackPressed(){
        new AlertDialog.Builder(this).setTitle("Salir de GasLift NABEP")
            .setMessage("Las mediciones se guardan en este dispositivo. ¿Deseas salir?")
            .setPositiveButton("Salir",(d,w)->finish())
            .setNegativeButton("Continuar",(d,w)->d.dismiss()).show();
    }
    @Override public void onDestroy(){
        if(web!=null){web.destroy();web=null;}
        super.onDestroy();
    }
}
