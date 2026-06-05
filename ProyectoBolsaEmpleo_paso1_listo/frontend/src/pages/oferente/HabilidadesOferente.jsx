import { useEffect, useState } from "react";
import { obtenerCaracteristicas } from "../../api/publicApi";
import {
    eliminarHabilidad,
    guardarHabilidad,
    listarHabilidades,
} from "../../api/oferenteApi";
import BackToDashboard from "../../components/BackToDashboard";

function HabilidadesOferente() {
    const [habilidades, setHabilidades] = useState([]);
    const [caracteristicas, setCaracteristicas] = useState([]);

    const [form, setForm] = useState({
        caracteristicaId: "",
        nivel: "1",
    });

    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState("");

    function convertirArbolAListaHojas(lista) {
        let resultado = [];

        lista.forEach((caracteristica) => {
            if (caracteristica.hoja) {
                resultado.push(caracteristica);
            }

            if (caracteristica.hijas && caracteristica.hijas.length > 0) {
                resultado = resultado.concat(
                    convertirArbolAListaHojas(caracteristica.hijas)
                );
            }
        });

        return resultado;
    }

    async function cargarDatos() {
        try {
            setError("");

            const [habilidadesData, caracteristicasData] = await Promise.all([
                listarHabilidades(),
                obtenerCaracteristicas(),
            ]);

            setHabilidades(habilidadesData);
            setCaracteristicas(convertirArbolAListaHojas(caracteristicasData));
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        cargarDatos();
    }, []);

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

        try {
            await guardarHabilidad({
                caracteristicaId: Number(form.caracteristicaId),
                nivel: Number(form.nivel),
            });

            setMensaje("Habilidad guardada correctamente.");

            setForm({
                caracteristicaId: "",
                nivel: "1",
            });

            await cargarDatos();
        } catch (err) {
            setError(err.message);
        }
    }

    async function handleEliminar(id) {
        const confirmar = window.confirm("¿Seguro que deseas eliminar esta habilidad?");

        if (!confirmar) {
            return;
        }

        try {
            setMensaje("");
            setError("");

            await eliminarHabilidad(id);

            setMensaje("Habilidad eliminada correctamente.");
            await cargarDatos();
        } catch (err) {
            setError(err.message);
        }
    }

    return (
        <section>
            <h1>Mis habilidades</h1>

            {mensaje && <p className="success">{mensaje}</p>}
            {error && <p className="error">{error}</p>}

            <form className="form-card" onSubmit={handleSubmit}>
                <h3>Agregar o actualizar habilidad</h3>

                <label>Característica</label>
                <select
                    name="caracteristicaId"
                    value={form.caracteristicaId}
                    onChange={handleChange}
                    required
                >
                    <option value="">Seleccione una característica</option>

                    {caracteristicas.map((c) => (
                        <option key={c.id} value={c.id}>
                            {c.nombreCompleto}
                        </option>
                    ))}
                </select>

                <label>Nivel</label>
                <select name="nivel" value={form.nivel} onChange={handleChange}>
                    <option value="1">Nivel 1</option>
                    <option value="2">Nivel 2</option>
                    <option value="3">Nivel 3</option>
                    <option value="4">Nivel 4</option>
                    <option value="5">Nivel 5</option>
                </select>

                <button type="submit">Guardar habilidad</button>
            </form>

            <h2>Habilidades registradas</h2>

            {habilidades.length === 0 ? (
                <p>No tienes habilidades registradas.</p>
            ) : (
                <table className="data-table">
                    <thead>
                    <tr>
                        <th>Característica</th>
                        <th>Nivel</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>

                    <tbody>
                    {habilidades.map((habilidad) => (
                        <tr key={habilidad.id}>
                            <td>{habilidad.nombreCompleto}</td>
                            <td>{habilidad.nivel}</td>
                            <td>
                                <button
                                    type="button"
                                    onClick={() => handleEliminar(habilidad.id)}
                                >
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            <BackToDashboard tipo="oferente" />
        </section>

    );
}

export default HabilidadesOferente;