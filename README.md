# GasLift NABEP — Aplicación Android

Repositorio público dedicado a GasLift NABEP, separado de la aplicación de gestión de pozos.

## Edición pública sin datos operacionales

El APK se compila a partir de un proyecto **sin la base real de pozos**, por protección de la información operacional. El usuario puede añadir pozos manualmente o **importar un Excel/CSV autorizado** en su teléfono. El registro, cálculo, jornada e informe funcionan de forma local y sin internet.

## Cómo generar el APK

1. Descarga `GasLift_NABEP_Proyecto_Android_PUBLICO.zip` en el chat.
2. En este repositorio, haz clic en **Add file → Upload files**.
3. Sube el ZIP a la raíz **sin descomprimir** y con el mismo nombre.
4. Confirma el cambio en la rama `main`. La compilación se inicia automáticamente.
5. Abre [Actions](https://github.com/miguelansam2002-png/Gas-lift/actions) y consulta **Compilar GasLift NABEP APK**.
6. Cuando finalice correctamente, descarga **GasLift-NABEP-APK** desde la ejecución.
7. Descomprime ese artefacto e instala `app-debug.apk` en Android para pruebas.

**Importante:** no subas los archivos originales con pozos, presiones ni ajustes a este repositorio público. El APK para pruebas aún requiere una compilación exitosa. Los cálculos basados en AGA 3 se consideran estimaciones de ingeniería hasta su validación técnica.
