import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { crearPuesto } from "../../api/empresaApi";
import BackToDashboard from "../../components/BackToDashboard";

function CrearPuesto() {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        titulo: "",
        descripcion: "",
        salario: "",
        publico: true,
    });

    const [error, setError] = useState("");
    const [cargando, setCargando] = useState(false);

    function handleChange(e) {
        const { name, value, type, checked } = e.target;

        setForm({
            ...form,
            [name]: type === "checkbox" ? checked : value,
        });
    }

    async function handleSubmit(e) {
        e.preventDefault();

        setError("");
        setCargando(true);

        try {
            const payload = {
                titulo: form.titulo,
                descripcion: form.descripcion,
                salario: Number(form.salario),
                publico: Boolean(form.publico),
            };

            const puestoCreado = await crearPuesto(payload);

            navigate(`/empresa/puestos/${puestoCreado.id}/requisitos`);
        } catch (err) {
            setError(err.message);
        } finally {
            setCargando(false);
        }
    }

    return (
        <section className="form-page">
            <h1>Publicar nuevo puesto</h1>

            <form className="form-card" onSubmit={handleSubmit}>
                {error && <p className="error">{error}</p>}

                <label>Título del puesto</label>
                <input
                    name="titulo"
                    type="text"
                    value={form.titulo}
                    onChange={handleChange}
                    required
                />

                <label>Descripción general</label>
                <textarea
                    name="descripcion"
                    value={form.descripcion}
                    onChange={handleChange}
                    required
                />

                <label>Salario ofrecido</label>
                <input
                    name="salario"
                    type="number"
                    min="0"
                    value={form.salario}
                    onChange={handleChange}
                    required
                />

                <label className="checkbox-row">
                    <input
                        name="publico"
                        type="checkbox"
                        checked={form.publico}
                        onChange={handleChange}
                    />
                    Publicar como puesto público
                </label>

                <button type="submit" disabled={cargando}>
                    {cargando ? "Guardando..." : "Crear puesto"}
                </button>
            </form>
            <BackToDashboard tipo="empresa" />
        </section>
    );
}

export default CrearPuesto;