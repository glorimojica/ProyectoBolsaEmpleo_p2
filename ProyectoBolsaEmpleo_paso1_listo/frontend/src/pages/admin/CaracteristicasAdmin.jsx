import { useEffect, useState } from "react";
import {
    actualizarCaracteristica,
    crearCaracteristica,
    eliminarCaracteristica,
    listarCaracteristicasAdmin,
    listarCaracteristicasPlanasAdmin,
} from "../../api/caracteristicaApi";
import BackToDashboard from "../../components/BackToDashboard";

function CaracteristicasAdmin() {
    const [arbol, setArbol] = useState([]);
    const [planas, setPlanas] = useState([]);

    const [form, setForm] = useState({
        nombre: "",
        padreId: "",
    });

    const [editandoId, setEditandoId] = useState(null);
    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState("");
    const [cargando, setCargando] = useState(false);

    async function cargarDatos() {
        try {
            setError("");

            const [arbolData, planasData] = await Promise.all([
                listarCaracteristicasAdmin(),
                listarCaracteristicasPlanasAdmin(),
            ]);

            setArbol(arbolData);
            setPlanas(planasData);
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

    function limpiarFormulario() {
        setForm({
            nombre: "",
            padreId: "",
        });

        setEditandoId(null);
    }

    async function handleSubmit(e) {
        e.preventDefault();

        setMensaje("");
        setError("");
        setCargando(true);

        const payload = {
            nombre: form.nombre,
            padreId: form.padreId === "" ? null : Number(form.padreId),
        };

        try {
            if (editandoId) {
                await actualizarCaracteristica(editandoId, payload);
                setMensaje("Característica actualizada correctamente.");
            } else {
                await crearCaracteristica(payload);
                setMensaje("Característica creada correctamente.");
            }

            limpiarFormulario();
            await cargarDatos();
        } catch (err) {
            setError(err.message);
        } finally {
            setCargando(false);
        }
    }

    function handleEditar(caracteristica) {
        setEditandoId(caracteristica.id);

        setForm({
            nombre: caracteristica.nombre,
            padreId: caracteristica.padreId ? String(caracteristica.padreId) : "",
        });

        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    async function handleEliminar(id) {
        const confirmar = window.confirm(
            "¿Seguro que deseas eliminar esta característica?"
        );

        if (!confirmar) {
            return;
        }

        try {
            setMensaje("");
            setError("");

            await eliminarCaracteristica(id);

            setMensaje("Característica eliminada correctamente.");
            await cargarDatos();
        } catch (err) {
            setError(err.message);
        }
    }

    function renderArbol(lista, nivel = 0) {
        if (!lista || lista.length === 0) {
            return null;
        }

        return (
            <ul className={nivel === 0 ? "tree-root" : "tree-child"}>
                {lista.map((caracteristica) => (
                    <li key={caracteristica.id} className="tree-item">
                        <div className="tree-row">
                            <span>
                                {caracteristica.nombre}
                                {caracteristica.hoja ? (
                                    <small className="tag-hoja"> hoja</small>
                                ) : (
                                    <small className="tag-padre"> categoría</small>
                                )}
                            </span>

                            <div className="tree-actions">
                                <button
                                    type="button"
                                    onClick={() => handleEditar(caracteristica)}
                                >
                                    Editar
                                </button>

                                <button
                                    type="button"
                                    onClick={() => handleEliminar(caracteristica.id)}
                                >
                                    Eliminar
                                </button>
                            </div>
                        </div>

                        {renderArbol(caracteristica.hijas, nivel + 1)}
                    </li>
                ))}
            </ul>
        );
    }

    return (
        <section>
            <h1>Gestión de características</h1>

            <p>
                Aquí el administrador puede crear categorías y habilidades jerárquicas
                que luego usarán las empresas en los puestos y los oferentes en sus
                habilidades.
            </p>

            {mensaje && <p className="success">{mensaje}</p>}
            {error && <p className="error">{error}</p>}

            <form className="form-card" onSubmit={handleSubmit}>
                <h3>{editandoId ? "Editar característica" : "Nueva característica"}</h3>

                <label>Nombre</label>
                <input
                    name="nombre"
                    type="text"
                    value={form.nombre}
                    onChange={handleChange}
                    required
                />

                <label>Característica padre</label>
                <select
                    name="padreId"
                    value={form.padreId}
                    onChange={handleChange}
                >
                    <option value="">Sin padre / categoría principal</option>

                    {planas
                        .filter((caracteristica) => caracteristica.id !== editandoId)
                        .map((caracteristica) => (
                            <option key={caracteristica.id} value={caracteristica.id}>
                                {caracteristica.nombreCompleto}
                            </option>
                        ))}
                </select>

                <div className="form-actions">
                    <button type="submit" disabled={cargando}>
                        {cargando
                            ? "Guardando..."
                            : editandoId
                                ? "Actualizar"
                                : "Crear"}
                    </button>

                    {editandoId && (
                        <button type="button" onClick={limpiarFormulario}>
                            Cancelar edición
                        </button>
                    )}
                </div>
            </form>

            <h2>Árbol de características</h2>

            {arbol.length === 0 ? (
                <p>No hay características registradas.</p>
            ) : (
                <div className="tree-container">{renderArbol(arbol)}</div>
            )}
            <BackToDashboard tipo="admin" />
        </section>
    );
}

export default CaracteristicasAdmin;