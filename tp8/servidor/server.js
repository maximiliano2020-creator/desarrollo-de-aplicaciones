const express = require("express");
const { Pool } = require("pg");

const app = express();

app.use(express.json());

// Conexión con el nodo primario
const dbPrimary = new Pool({
    host: "127.0.0.1",
    port: 5434,
    database: "inventario",
    user: "app_user",
    password: "app_pass"
});

// Conexión con el nodo secundario
const dbReplica = new Pool({
    host: "127.0.0.1",
    port: 5433,
    database: "inventario",
    user: "app_user",
    password: "app_pass"
});

// GET: consultar todos los productos desde la réplica
app.get("/productos", async (req, res) => {
    try {
        const resultado = await dbReplica.query(
            "SELECT * FROM productos ORDER BY id"
        );

        res.status(200).json(resultado.rows);

    } catch (error) {
        console.error("Error al consultar la réplica:", error.message);

        res.status(500).json({
            mensaje: "Error al consultar los productos"
        });
    }
});

// POST: registrar un producto en el primario
app.post("/productos", async (req, res) => {
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

    try {
        const resultado = await dbPrimary.query(
            `INSERT INTO productos (nombre, precio, stock)
             VALUES ($1, $2, $3)
             RETURNING *`,
            [nombre.trim(), precio, stock]
        );

        res.status(201).json({
            mensaje: "Producto registrado correctamente",
            producto: resultado.rows[0]
        });

    } catch (error) {
        console.error("Error al registrar producto:", error.message);

        res.status(500).json({
            mensaje: "Error al registrar el producto"
        });
    }
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