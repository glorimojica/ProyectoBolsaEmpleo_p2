import { useEffect, useState } from "react";
import { obtenerPuestosRecientes } from "../../api/publicApi";
import JobCard from "../../components/JobCard";
import ModalDetallePuesto from "../../components/ModalDetallePuesto";

function Home() {
    const [puestos, setPuestos] = useState([]);
    const [puestoSeleccionado, setPuestoSeleccionado] = useState(null);
    const [error, setError] = useState("");

    useEffect(() => {
        obtenerPuestosRecientes()
            .then(setPuestos)
            .catch((err) => setError(err.message));
    }, []);

    return (
        <section>
            <h1>Bolsa de Empleo</h1>
            <h2>Puestos públicos recientes</h2>

            {error && <p className="error">{error}</p>}

            {puestos.length === 0 ? (
                <p>No hay puestos públicos recientes.</p>
            ) : (
                <div className="jobs-grid">
                    {puestos.map((puesto) => (
                        <JobCard
                            key={puesto.id}
                            puesto={puesto}
                            onVerDetalle={setPuestoSeleccionado}
                        />
                    ))}
                </div>
            )}

            <ModalDetallePuesto
                puesto={puestoSeleccionado}
                onClose={() => setPuestoSeleccionado(null)}
            />
        </section>
    );
}

export default Home;