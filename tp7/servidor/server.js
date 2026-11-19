const express = require("express");

const app = express();

app.use(express.json());

const productos = [];

// GET: consultar todos los productos
app.get("/productos", (req, res) => {
    res.status(200).json(productos);
});

// POST: registrar un producto
app.post("/productos", (req, res) => {
    const { nombre, precio, stock } = req.body;
    console.log("Datos recibidos:", req.body);

    // Validar los datos
    if (
        typeof nombre !== "string" ||
        nombre.trim() === "" ||
        typeof precio !== "number" ||
        !Number.isFinite(precio) ||
        precio < 0 ||
        !Number.isInteger(stock) ||
        stock < 0
    ) {
        return res.status(400).json({
            mensaje: "Los datos del producto son inválidos"
        });
    }

    const nuevoProducto = {
        id: productos.length + 1,
        nombre: nombre.trim(),
        precio,
        stock
    };

    productos.push(nuevoProducto);

    res.status(201).json({
        mensaje: "Producto registrado correctamente",
        producto: nuevoProducto
    });
});

// Ruta inexistente
app.use((req, res) => {
    res.status(404).json({
        mensaje: "La ruta solicitada no existe"
    });
});

// Manejo de errores generales
app.use((error, req, res, next) => {
    if (error instanceof SyntaxError && error.status === 400 && "body" in error) {
        return res.status(400).json({
            mensaje: "El JSON enviado no tiene un formato válido"
        });
    }

    console.error("Error interno:", error);

    res.status(500).json({
        mensaje: "Error interno del servidor"
    });
});

// Iniciar servidor
app.listen(3000, () => {
    console.log("Servidor iniciado en el puerto 3000");
});