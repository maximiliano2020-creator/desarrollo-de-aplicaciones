const URL = "http://localhost:3000/productos";

// GET: consultar productos
async function consultarProductos() {
    try {
        const respuesta = await fetch(URL);

        const datos = await respuesta.json();

        if (!respuesta.ok) {
            throw new Error(datos.mensaje);
        }

        console.log("\n--- LISTA DE PRODUCTOS ---");
        console.log(datos);

    } catch (error) {
        console.error("Error al consultar productos:", error.message);
    }
}

// POST: registrar un producto
async function registrarProducto() {
    try {
        const respuesta = await fetch(URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                nombre: "Teclado",
                precio: 25000,
                stock: 10
            })
        });

        const datos = await respuesta.json();

        if (!respuesta.ok) {
            throw new Error(datos.mensaje);
        }

        console.log("\n--- PRODUCTO REGISTRADO ---");
        console.log(datos);

    } catch (error) {
        console.error("Error al registrar producto:", error.message);
    }
}

// Ejecutar las operaciones
async function iniciarCliente() {
    await registrarProducto();
    await consultarProductos();
}

iniciarCliente();