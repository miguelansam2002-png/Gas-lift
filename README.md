# GasLift NABEP — Android

Repositorio dedicado a la aplicación móvil de registro de ajustes y balance de consumo de gas de levantamiento.

## Estado
- **Repositorio exclusivo:** GasLift NABEP, separado del sistema de gestión de pozos.
- **Código Android:** pendiente de incorporar.
- **APK:** pendiente de compilación.

## Antes de cargar el proyecto
**Convertir el repositorio en privado** en Settings → General → Danger Zone → Change repository visibility → Private.

El proyecto original incluye registros operacionales de pozos; **no subir el ZIP mientras este repositorio sea público**.

## Generar el APK
Después de configurar el repositorio como privado:

1. Descarga `GasLift_NABEP_Proyecto_Android_Offline.zip` del chat.
2. En GitHub, selecciona **Add file → Upload files**.
3. Sube el ZIP **sin descomprimir**, con exactamente ese nombre, en la raíz de este repositorio.
4. En **Actions**, selecciona **Compilar GasLift NABEP APK** → **Run workflow**.
5. Tras una compilación correcta, descarga el artefacto `GasLift-NABEP-APK` desde la ejecución.
6. Dentro encontrarás `app-debug.apk`, destinado a pruebas en dispositivos Android.

La aplicación operará sin internet después de su instalación. El APK de pruebas no sustituye una versión de publicación firmada. El informe AGA 3 sigue siendo estimativo y sujeto a validación técnica.
