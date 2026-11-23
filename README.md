# GasLift NABEP — Android (prueba pública)

Repositorio exclusivo para probar la aplicación móvil offline de **balance y ajustes del gas de levantamiento**.

La app contiene el código HTML con su base de pozos, las fichas individuales, jornadas de múltiples, carga rápida mediante WhatsApp, cálculo de consumo estimado y exportación de informes. El teléfono no necesita internet una vez instalada.

## Obtener APK
1. Abre [Actions](https://github.com/miguelansam2002-png/Gas-lift/actions/workflows/gaslift-apk.yml).
2. Abre la ejecución más reciente de **Compilar GasLift NABEP APK**.
3. Comprueba que esté marcada como **Success**.
4. Descarga el artefacto **GasLift-NABEP-APK** al final de la ejecución.
5. Extrae `app-debug.apk` y cópialo al dispositivo Android.
6. Instálalo aceptando la instalación desde esa fuente si el teléfono lo solicita.

Esta es una **compilación de prueba** (debug), no una versión comercial firmada. Se recomienda verificar en un dispositivo las pantallas, importación Excel/CSV, cálculos, impresión a PDF y respaldo de datos antes de distribuirla.

## Funcionamiento sin conexión
El APK incorpora HTML, cálculos, plantillas y base de pozos. La información que cada usuario registra se conserva localmente en el dispositivo y los archivos pueden importarse/exportarse desde el almacenamiento del teléfono.

## Advertencia sobre privacidad
**El repositorio está público por decisión del usuario para realizar pruebas. Contiene una base operacional de pozos.** Cualquier tercero con el enlace puede copiar ese contenido. Cambiar luego el repositorio a privado **no elimina las copias o clones obtenidos durante la publicación**. Antes del uso empresarial regular, solicitar autorización de la entidad propietaria de los datos y restringir la distribución.

## Cálculo técnico
El modelo de placa de orificio es una estimación operativa pendiente de validación de parámetros físicos; no constituye medición fiscal certificada AGA 3.
