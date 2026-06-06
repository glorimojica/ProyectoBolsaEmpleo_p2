import { useEffect, useState } from "react";
import {
    actualizarPerfilOferente,
    obtenerPerfilOferente,
} from "../../api/oferenteApi";
import BackToDashboard from "../../components/BackToDashboard";

function PerfilOferente() {
    const [form, setForm] = useState({
        nombre: "",
        apellido: "",
        nacionalidad: "",
        telefono: "",
        residencia: "",
    });

    const [perfil, setPerfil] = useState(null);
    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState("");
    const [cargando, setCargando] = useState(false);

    async function cargarPerfil() {
        try {
            setError("");

            const data = await obtenerPerfilOferente();

            setPerfil(data);

            setForm({
                nombre: data.nombre || "",
                apellido: data.apellido || "",
                nacionalidad: data.nacionalidad || "",
                telefono: data.telefono || "",
                residencia: data.residencia || "",
            });
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        cargarPerfil();
    }, []);

    function handleChange(e) {
        let { name, value } = e.target;

        if (name === "telefono") {
            value = value.replace(/\D/g, "").slice(0, 8);
        }

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
            const data = await actualizarPerfilOferente(form);
            setPerfil(data);
            setMensaje("Perfil actualizado correctamente.");
        } catch (err) {
            setError(err.message);
        } finally {
            setCargando(false);
        }
    }

    return (
        <section className="form-page">
            <h1>Perfil del oferente</h1>

            {perfil && (
                <div className="info-card">
                    <p><strong>Identificación:</strong> {perfil.identificacion}</p>
                    <p><strong>Correo:</strong> {perfil.correo}</p>
                    <p>
                        <strong>CV:</strong>{" "}
                        {perfil.cvDisponible ? perfil.cvNombreArchivo : "No disponible"}
                    </p>
                </div>
            )}

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

                <label>Apellido</label>
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
                    type="tel"
                    inputMode="numeric"
                    pattern="[0-9]{8}"
                    maxLength="8"
                    title="El teléfono debe contener exactamente 8 números"
                    value={form.telefono}
                    onChange={handleChange}
                    required
                />

                <label>Residencia</label>
                <input
                    name="residencia"
                    type="text"
                    value={form.residencia}
                    onChange={handleChange}
                    required
                />

                <button type="submit" disabled={cargando}>
                    {cargando ? "Guardando..." : "Guardar cambios"}
                </button>
            </form>
            <BackToDashboard tipo="oferente" />
        </section>
    );
}

export default PerfilOferente;