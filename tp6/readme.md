TP6 - Middleware y Objetos Remotos (Java RMI)

Materia: Desarrollo de Aplicaciones para Ambientes Distribuidos
Unidad: 3 - Middleware y Objetos Remotos (RMI / RPC)
Tema: Invocación Remota de Métodos y Abstracción de Comunicación

1. Descripción del proyecto

Este trabajo práctico consiste en desarrollar un sistema distribuido utilizando Java RMI (Remote Method Invocation).

El sistema permite que un cliente solicite operaciones de procesamiento a un servidor remoto mediante invocaciones de métodos, sin tener que gestionar directamente la comunicación mediante sockets.

El proyecto está dividido en tres componentes principales:

- Cliente: permite al usuario seleccionar operaciones mediante un menú interactivo por consola.
- Servidor: recibe las solicitudes, ejecuta las operaciones y devuelve los resultados.
- Middleware: contiene la interfaz remota y la clase de datos compartida entre el cliente y el servidor.

2. Funcionalidades

El servidor ofrece las siguientes operaciones:

2.1. Procesamiento matemático

Recibe un arreglo de números y calcula:

- Promedio.
- Valor máximo.
- Valor mínimo.
- Desviación estándar poblacional.

2.2. Validación de datos

Comprueba si una cadena cumple con el formato de un DNI argentino de 7 u 8 dígitos.

La validación comprueba únicamente el formato, no la existencia ni la validez registral del documento.

2.3. Filtro de contenido

Recibe un arreglo de cadenas de texto y un patrón de búsqueda. Devuelve únicamente los textos que contienen dicho patrón.

La búsqueda no distingue entre mayúsculas y minúsculas.

3. Tecnologías utilizadas

- Java JDK 21 o superior.
- Java RMI.
- Visual Studio Code.
- Extension Pack for Java.

No se utilizan dependencias externas: Java RMI forma parte de la plataforma Java.

4. Estructura del proyecto

TP6/
├── README.md
└── tp6/
    ├── cliente/
    │   └── Cliente.java
    ├── middleware/
    │   ├── Estadisticas.java
    │   └── InterfazRemota.java
    └── servidor/
        ├── ProcesadorImpl.java
        └── Servidor.java

Responsabilidad de los componentes

Middleware

- "InterfazRemota.java": declara los métodos remotos disponibles.
- "Estadisticas.java": representa los resultados matemáticos y permite su serialización mediante "Serializable".

Servidor

- "Servidor.java": inicia el registro RMI y publica el servicio con el identificador "Procesador".
- "ProcesadorImpl.java": implementa las operaciones remotas y registra las conexiones y solicitudes recibidas.

Cliente

- "Cliente.java": busca el servicio en el registro RMI, obtiene la referencia remota y presenta el menú interactivo.

5. Configuración y ejecución

Requisitos previos

Tener instalado el JDK 21 o superior y comprobar que Java esté disponible desde la terminal:

java -version
javac -version

5.1. Compilación

Abrir una terminal en la carpeta principal del repositorio, donde se encuentra la carpeta "tp6".

Crear una carpeta para los archivos compilados:

mkdir out

Compilar los archivos Java:

javac -d out tp6/middleware/*.java tp6/servidor/*.java tp6/cliente/*.java

La opción "-d out" indica que los archivos ".class" se generarán dentro de la carpeta "out", respetando la estructura de paquetes.

5.2. Iniciar el servidor

Ejecutar:

java -cp out tp6.servidor.Servidor

Si se inicia correctamente, aparecerá un mensaje similar a:

Servidor RMI iniciado correctamente.
IP del servidor: localhost
Puerto del registro: 1099
Servicio publicado: Procesador

El servidor debe permanecer en ejecución mientras se utiliza el cliente.

5.3. Iniciar el cliente

Abrir una segunda terminal, también ubicada en la carpeta principal del repositorio.

Ejecutar:

java -cp out tp6.cliente.Cliente

El cliente se conectará al servidor local y mostrará el menú de operaciones.

6. Uso del cliente

El menú presenta las siguientes opciones:

===== PROCESADOR REMOTO =====
1. Calcular estadísticas
2. Validar DNI
3. Filtrar contenido
4. Salir

Opción 1 - Calcular estadísticas

Permite ingresar números separados por espacios.

Ejemplo:

10 20 30 40

El servidor devuelve el promedio, máximo, mínimo y desviación estándar.

Opción 2 - Validar DNI

Permite ingresar una cadena para comprobar si cumple el formato establecido.

Opción 3 - Filtrar contenido

Permite ingresar varios textos y un patrón de búsqueda. El servidor devuelve los textos que coinciden.

Opción 4 - Salir

Finaliza la ejecución del cliente.

7. Despliegue en computadoras diferentes

El sistema está preparado para utilizarse en equipos diferentes dentro de una red.

Para realizar el despliegue:

1. Colocar el proyecto en ambas computadoras.
2. Compilar los archivos en ambas computadoras.
3. Identificar la dirección IP local de la computadora que ejecutará el servidor.
4. Permitir las conexiones entrantes en los puertos TCP 1099 y 1100 del equipo servidor.
5. Iniciar el servidor indicando su dirección IP.
6. Iniciar el cliente indicando la dirección IP del servidor.

En la computadora del servidor:

java -cp out tp6.servidor.Servidor 192.168.1.100

Reemplazar "192.168.1.100" por la dirección IP real del equipo servidor.

En la computadora del cliente:

java -cp out tp6.cliente.Cliente 192.168.1.100

Utilizar la misma dirección IP del servidor.

Puertos utilizados

Puerto| Función
1099| Registro de nombres RMI (Registry).
1100| Comunicación con el objeto remoto.

Ambos puertos deben estar disponibles y permitidos por el firewall del equipo servidor.

8. Trazabilidad

El servidor registra en consola las conexiones anunciadas por los clientes y las operaciones solicitadas.

La información registrada incluye:

- Fecha y hora.
- Dirección IP del cliente.
- Tipo de operación ejecutada.

Ejemplo:

[CONEXIÓN] Cliente conectado desde: 127.0.0.1

[CLIENTE: 127.0.0.1]
[OPERACIÓN] Cálculo de estadísticas solicitado.

[CLIENTE: 127.0.0.1]
[OPERACIÓN] Validación de DNI solicitada.

[CLIENTE: 127.0.0.1]
[OPERACIÓN] Filtrado de contenido solicitado.

9. Manejo de errores

El cliente contempla excepciones relacionadas con la comunicación remota mediante "RemoteException".

Si el servidor deja de estar disponible durante una solicitud, el cliente informa que no pudo establecer comunicación.

También se contemplan errores de entrada, como ingresar valores numéricos con un formato incorrecto.

10. Pruebas realizadas

Se realizaron pruebas locales con el servidor y el cliente ejecutándose como procesos independientes.

Las verificaciones incluyeron:

- Cálculo de estadísticas con diferentes números.
- Validación de formatos de DNI.
- Filtrado de cadenas mediante patrones de búsqueda.
- Registro de conexiones y operaciones en la consola del servidor.
- Interrupción del servidor durante la ejecución del cliente y comprobación del error de comunicación.

La comunicación entre dos computadoras físicas queda pendiente de una prueba práctica de despliegue.

11. Objetivo del trabajo

El objetivo principal es comprender y aplicar Java RMI para desarrollar aplicaciones distribuidas, utilizando middleware para abstraer los detalles de comunicación entre cliente y servidor.

También se busca comprender el funcionamiento de las interfaces remotas, el registro de nombres, las referencias remotas, la serialización de objetos y el manejo de excepciones durante las invocaciones remotas.

12. Ejercicio 2: Cuestionario teórico y análisis de arquitectura

12.1. Abstracción del Middleware

Pregunta: Explica la diferencia entre realizar una llamada remota mediante sockets puros versus hacerlo mediante Java RMI / gRPC. ¿Qué tareas complejas abstrae el middleware al desarrollador?

Respuesta:

Cuando se utilizan sockets puros, el desarrollador debe gestionar manualmente gran parte de la comunicación entre el cliente y el servidor. Esto incluye establecer conexiones, enviar y recibir datos, definir protocolos de comunicación, convertir los datos a formatos transmisibles y controlar posibles errores de conexión.

En cambio, Java RMI proporciona una capa de middleware que permite invocar métodos de objetos ubicados en otros procesos o computadoras de una manera similar a una llamada local.

El middleware abstrae tareas como:

- Localización de objetos remotos mediante un registro.
- Establecimiento de la comunicación.
- Serialización y deserialización de parámetros y resultados.
- Envío de solicitudes y recepción de respuestas.
- Gestión de excepciones relacionadas con la comunicación.

En nuestro proyecto, el cliente utiliza la referencia obtenida mediante el RMI Registry para solicitar operaciones como "calcularEstadisticas()" sin tener que implementar manualmente el envío y recepción de datos mediante sockets.

Sin embargo, esta abstracción no elimina los posibles fallos de red ni garantiza que el servidor esté siempre disponible.

12.2. Ciclo de vida y Stub/Skeleton

Pregunta: Describe qué es un Stub (o Proxy) en el lado del cliente y qué función cumple el Skeleton / Dispatcher en el lado del servidor durante el marshalling y unmarshalling de datos.

Respuesta:

El Stub o Proxy es el representante del objeto remoto que utiliza el cliente para realizar invocaciones.

Cuando el cliente llama a un método remoto, el Stub se encarga de preparar los argumentos mediante el proceso de marshalling, transmitir la solicitud y recibir la respuesta. Luego realiza el unmarshalling para reconstruir los datos recibidos y entregarlos al cliente.

Del lado del servidor, el Skeleton tradicional cumplía la función de recibir las solicitudes remotas, reconstruir los argumentos y dirigir la ejecución hacia el método correspondiente del objeto servidor.

En Java RMI moderno, como el utilizado con JDK 21, no es necesario generar manualmente una clase Skeleton. El mecanismo de despacho está integrado en la infraestructura de RMI.

En nuestro proyecto, el cliente obtiene la referencia remota mediante:

registro.lookup("Procesador");

A partir de esa referencia puede invocar los métodos declarados en "InterfazRemota", mientras que Java RMI se encarga de los mecanismos internos de comunicación.

12.3. Manejo de fallos parciales

Pregunta: ¿Qué ocurre con el cliente si el servidor remoto se interrumpe abruptamente durante la ejecución de un método? Identifica las excepciones específicas que se generan e indica cómo deben gestionarse en un entorno productivo.

Respuesta:

En un sistema distribuido pueden producirse fallos parciales. Esto significa que una parte del sistema puede dejar de funcionar mientras otra continúa ejecutándose.

Si el servidor se interrumpe durante una invocación remota, el cliente puede quedar esperando una respuesta que nunca llegará o recibir una excepción relacionada con la comunicación.

En Java RMI, la excepción general para estos problemas es "RemoteException". Entre sus posibles subclases se encuentran:

- "ConnectException": indica que no se pudo establecer una conexión.
- "ConnectIOException": indica un problema de entrada/salida durante la conexión.
- "UnmarshalException": indica un problema al reconstruir los datos recibidos.

En nuestro proyecto comprobamos este comportamiento deteniendo el servidor mientras el cliente seguía ejecutándose. Al intentar solicitar una operación, el cliente recibió un error de conexión rechazada y mostró un mensaje indicando que no podía comunicarse con el servidor.

En un entorno productivo, estos errores deben gestionarse adecuadamente mediante:

- Captura y tratamiento de excepciones remotas.
- Mensajes claros para informar al usuario.
- Registro de errores para facilitar el diagnóstico.
- Configuración de tiempos de espera.
- Reintentos controlados cuando la operación lo permita.
- Verificación de la disponibilidad del servicio.

También es importante considerar que, si se pierde la respuesta, no siempre es posible saber si el servidor llegó a ejecutar la operación. Por eso, los reintentos deben diseñarse cuidadosamente para evitar ejecutar dos veces una operación que no debería repetirse.