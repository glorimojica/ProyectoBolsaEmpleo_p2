import { useState } from "react";
import { registerEmpresaRequest } from "../../api/authApi";

function RegistroEmpresa() {
    const [form, setForm] = useState({
        nombre: "",
        localizacion: "",
        correo: "",
        telefono: "",
        descripcion: "",
        password: "",
    });

    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState("");
    const [cargando, setCargando] = useState(false);

    function handleChange(e) {
        const { name, value } = e.target;

        setForm({
            ...form,
            [name]: value,
        });
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setMensaje("");
        setError("");
        setCargando(true);

        try {
            await registerEmpresaRequest(form);

            setMensaje(
                "Empresa registrada correctamente. Debe esperar aprobación del administrador."
            );

            setForm({
                nombre: "",
                localizacion: "",
                correo: "",
                telefono: "",
                descripcion: "",
                password: "",
            });
        } catch (err) {
            setError(err.message);
        } finally {
            setCargando(false);
        }
    }

    return (
        <section className="form-page">
            <h1>Registro de Empresa</h1>

            <form className="form-card" onSubmit={handleSubmit}>
                {mensaje && <p className="success">{mensaje}</p>}
                {error && <p className="error">{error}</p>}

                <label>Nombre</label>
                <input
                    name="nombre"
                    type="text"
                    value={form.nombre}
                    onChange={handleChange}
                    required
                />

                <label>Localización</label>
                <input
                    name="localizacion"
                    type="text"
                    value={form.localizacion}
                    onChange={handleChange}
                    required
                />

                <label>Correo electrónico</label>
                <input
                    name="correo"
                    type="email"
                    value={form.correo}
                    onChange={handleChange}
                    required
                />

                <label>Teléfono</label>
                <input
                    name="telefono"
                    type="text"
                    value={form.telefono}
                    onChange={handleChange}
                    required
                />

                <label>Descripción</label>
                <textarea
                    name="descripcion"
                    value={form.descripcion}
                    onChange={handleChange}
                    required
                />

                <label>Clave</label>
                <input
                    name="password"
                    type="password"
                    value={form.password}
                    onChange={handleChange}
                    required
                />

                <button type="submit" disabled={cargando}>
                    {cargando ? "Registrando..." : "Registrarse"}
                </button>
            </form>
        </section>
    );
}

export default RegistroEmpresa;