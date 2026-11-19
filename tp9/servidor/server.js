const https = require("https");
const fs = require("fs");
const path = require("path");
const jwt = require("jsonwebtoken");
const express = require("express");
const { Pool } = require("pg");

const app = express();

app.use(express.json());

// Usuarios de prueba
const usuarios = [
    {
        id: 1,
        username: "admin",
        password: "admin123",
        rol: "administrador"
    },
    {
        id: 2,
        username: "usuario",
        password: "usuario123",
        rol: "usuario"
    }
];

// POST: iniciar sesión y emitir JWT
app.post("/login", (req, res) => {
    if (!process.env.JWT_SECRET) {
    return res.status(500).json({
        mensaje: "Error de configuración del servidor"
    });
    }
    const { username, password } = req.body;

    const usuario = usuarios.find(
        u => u.username === username && u.password === password
    );

    if (!usuario) {
        return res.status(401).json({
            mensaje: "Credenciales incorrectas"
        });
    }

    const token = jwt.sign(
        {
            rol: usuario.rol
        },
        process.env.JWT_SECRET,
        {
            subject: String(usuario.id),
            expiresIn: "1h"
        }
    );

    res.status(200).json({
        mensaje: "Autenticación exitosa",
        token
    });
});

// Middleware: verificar autenticación mediante JWT
function autenticarToken(req, res, next) {
    const authHeader = req.headers.authorization;

    // Comprobar que exista el encabezado Bearer
    const token = authHeader && authHeader.split(" ")[1];

    if (!token) {
        return res.status(401).json({
            mensaje: "Token de acceso requerido"
        });
    }

    // Verificar firma y expiración del token
    jwt.verify(token, process.env.JWT_SECRET, (error, usuario) => {
        if (error) {
            return res.status(401).json({
                mensaje: "Token inválido o expirado"
            });
        }

        req.usuario = usuario;
        next();
    });
}

// Middleware: comprobar permisos según el rol
function autorizarRol(rolRequerido) {
    return (req, res, next) => {
        if (req.usuario.rol !== rolRequerido) {
            return res.status(403).json({
                mensaje: "No tienes permisos para realizar esta operación"
            });
        }

        next();
    };
}

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
app.get("/productos", autenticarToken, async (req, res) => {
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
app.post("/productos", autenticarToken, autorizarRol("administrador"), async (req, res) => {
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

// Cargar certificados TLS
const opcionesTLS = {
    key: fs.readFileSync(
        path.join(__dirname, "certs", "localhost+2-key.pem")
    ),
    cert: fs.readFileSync(
        path.join(__dirname, "certs", "localhost+2.pem")
    )
};

// Iniciar servidor HTTPS
https.createServer(opcionesTLS, app).listen(3000, () => {
    console.log("Servidor HTTPS iniciado en https://localhost:3000");
});