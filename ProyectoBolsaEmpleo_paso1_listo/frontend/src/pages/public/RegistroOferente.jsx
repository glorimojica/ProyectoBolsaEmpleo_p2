import { useState } from "react";
import { registerOferenteRequest } from "../../api/authApi";

function RegistroOferente() {
    const [form, setForm] = useState({
        identificacion: "",
        nombre: "",
        apellido: "",
        nacionalidad: "",
        telefono: "",
        correo: "",
        residencia: "",
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
            await registerOferenteRequest(form);

            setMensaje(
                "Oferente registrado correctamente. Debe esperar aprobación del administrador."
            );

            setForm({
                identificacion: "",
                nombre: "",
                apellido: "",
                nacionalidad: "",
                telefono: "",
                correo: "",
                residencia: "",
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
            <h1>Registro de Oferente</h1>

            <form className="form-card" onSubmit={handleSubmit}>
                {mensaje && <p className="success">{mensaje}</p>}
                {error && <p className="error">{error}</p>}

                <label>Identificación</label>
                <input
                    name="identificacion"
                    type="text"
                    value={form.identificacion}
                    onChange={handleChange}
                    required
                />

                <label>Nombre</label>
                <input
                    name="nombre"
                    type="text"
                    value={form.nombre}
                    onChange={handleChange}
                    required
                />

                <label>Primer apellido</label>
                <input
                    name="apellido"
                    type="text"
                    value={form.apellido}
                    onChange={handleChange}
                    required
                />

                <label>Nacionalidad</label>
                <input
                    name="nacionalidad"
                    type="text"
                    value={form.nacionalidad}
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

                <label>Correo electrónico</label>
                <input
                    name="correo"
                    type="email"
                    value={form.correo}
                    onChange={handleChange}
                    required
                />

                <label>Lugar de residencia</label>
                <input
                    name="residencia"
                    type="text"
                    value={form.residencia}
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

export default RegistroOferente;