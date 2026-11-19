const URL = "https://localhost:3000";

// Almacenar el JWT recibido
let token = null;

// POST: iniciar sesión
async function iniciarSesion() {
    try {
        const respuesta = await fetch(`${URL}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: "admin",
                password: "admin123"
            })
        });

        const datos = await respuesta.json();

        if (!respuesta.ok) {
            throw new Error(datos.mensaje);
        }

        token = datos.token;

        console.log("\n--- LOGIN EXITOSO ---");
        console.log("JWT recibido correctamente");

    } catch (error) {
        console.error("Error al iniciar sesión:", error.message);
        throw error;
    }
}

// GET: consultar productos
async function consultarProductos() {
    try {
        const respuesta = await fetch(`${URL}/productos`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        
        console.log("Solicitud:", `${URL}/productos`);
        console.log("Autorización: Bearer JWT");
        console.log("Estado HTTP:", respuesta.status);

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
        const respuesta = await fetch(`${URL}/productos`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
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
    try {
        await iniciarSesion();
        await registrarProducto();
        await consultarProductos();

    } catch (error) {
        console.error("No se pudo completar la ejecución del cliente.");
    }
}

iniciarCliente();