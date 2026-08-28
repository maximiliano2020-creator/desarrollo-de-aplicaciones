1) No se pudo conectar con el servidor.
Error: Connection refused: connect
La línea que provoca la excepción es:
Socket socket = new Socket(host, puerto)
2) La línea que bloquea la ejecución del servidor hasta que un cliente se conecta es:
Socket cliente = servidor.accept()
El método accept() queda esperando una conexión entrante. El programa continúa ejecutándose recién cuando un cliente se conecta.
En el cliente también existe una operación bloqueante:
String resultado = entrada.readLine();
Esta espera hasta que el servidor envía el resultado mediante salida.println(resultado).
3) Para ejecutar el Cliente en una notebook y el Servidor en otra conectadas al mismo Wi-Fi, habría que cambiar principalmente la dirección del servidor.
Actualmente el Cliente tiene:
String host = "localhost";
localhost significa la propia computadora, por lo que el cliente buscaría el servidor en la misma notebook.
Habría que reemplazarlo por la dirección IP local de la notebook donde está ejecutándose el Servidor, por ejemplo:
String host = "192.168.1.105";
El puerto 5500 debe mantenerse igual en ambos programas.
Además, el servidor debe permitir conexiones desde otras computadoras de la red. Podría ser necesario permitir el puerto 5500 en el Firewall de Windows. 