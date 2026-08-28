# ejercicio 3

## 1) Thundering Herd Problem o Efecto Estampida

El Thundering Herd Problem ocurre cuando muchos clientes intentan realizar una petición al mismo tiempo, especialmente después de que un servidor tuvo un fallo.

Si todos los clientes utilizan reintentos con intervalos fijos y sin Jitter, volverán a realizar sus peticiones aproximadamente al mismo tiempo. Esto puede generar un nuevo pico de solicitudes y provocar que el servidor vuelva a saturarse.

El Jitter ayuda a evitar este problema agregando un tiempo aleatorio a la espera de cada cliente. De esta manera, los reintentos se distribuyen en diferentes momentos y se reduce la posibilidad de que todos los clientes soliciten el servicio simultáneamente.

## 2) Fallo transitorio y fallo permanente

Un fallo transitorio es un error temporal que puede desaparecer después de un determinado período de tiempo. Por ejemplo, un servidor puede estar temporalmente saturado y no responder a una petición. Después de unos segundos, el servidor puede volver a funcionar correctamente, por lo que un reintento puede tener éxito.

Un fallo permanente es un error que no se solucionará simplemente esperando y reintentando. Por ejemplo, si un cliente solicita un recurso que fue eliminado permanentemente del servidor, volver a realizar la misma petición no solucionará el problema.

La principal diferencia es que un fallo transitorio puede solucionarse con el paso del tiempo, mientras que un fallo permanente requiere solucionar la causa del problema.
