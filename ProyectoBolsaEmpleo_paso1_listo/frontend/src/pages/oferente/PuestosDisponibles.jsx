import { useEffect, useState } from "react";
import { listarPuestosDisponiblesOferente } from "../../api/oferenteApi";
import ModalDetallePuesto from "../../components/ModalDetallePuesto";
import BackToDashboard from "../../components/BackToDashboard";

function PuestosDisponibles() {
    const [puestos, setPuestos] = useState([]);
    const [puestoSeleccionado, setPuestoSeleccionado] = useState(null);
    const [error, setError] = useState("");

    async function cargarPuestos() {
        try {
            setError("");
            const data = await listarPuestosDisponiblesOferente();
            setPuestos(data);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        cargarPuestos();
    }, []);

    return (
        <section>
            <h1>Puestos disponibles</h1>

            <p>
                Aquí se muestran los puestos activos disponibles para oferentes registrados,
                incluyendo puestos públicos y privados.
            </p>

            {error && <p className="error">{error}</p>}

            {puestos.length === 0 ? (
                <p>No hay puestos disponibles.</p>
            ) : (
                <div className="jobs-grid">
                    {puestos.map((puesto) => (
                        <article key={puesto.id} className="job-card">
                            <p className="empresa">{puesto.empresa}</p>
                            <h3>{puesto.titulo}</h3>
                            <p>₡ {puesto.salario}</p>
                            <p>{puesto.publico ? "Público" : "Privado"}</p>

                            <button
                                type="button"
                                onClick={() => setPuestoSeleccionado(puesto)}
                            >
                                Ver detalle
                            </button>
                        </article>
                    ))}
                </div>
            )}

            <ModalDetallePuesto
                puesto={puestoSeleccionado}
                onClose={() => setPuestoSeleccionado(null)}
            />
            <BackToDashboard tipo="oferente" />
        </section>
    );
}

export default PuestosDisponibles;