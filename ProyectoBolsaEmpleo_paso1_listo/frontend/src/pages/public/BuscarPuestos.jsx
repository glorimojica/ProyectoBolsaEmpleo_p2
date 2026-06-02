import { useEffect, useState } from "react";
import {
    buscarPuestosPublicos,
    obtenerCaracteristicas,
} from "../../api/publicApi";
import JobCard from "../../components/JobCard";
import ModalDetallePuesto from "../../components/ModalDetallePuesto";

function BuscarPuestos() {
    const [caracteristicas, setCaracteristicas] = useState([]);
    const [puestos, setPuestos] = useState([]);
    const [caracteristicaId, setCaracteristicaId] = useState("");
    const [nivel, setNivel] = useState("");
    const [puestoSeleccionado, setPuestoSeleccionado] = useState(null);
    const [error, setError] = useState("");

    useEffect(() => {
        obtenerCaracteristicas()
            .then(setCaracteristicas)
            .catch((err) => setError(err.message));

        buscarPuestosPublicos()
            .then(setPuestos)
            .catch((err) => setError(err.message));
    }, []);

    function obtenerOpcionesCaracteristicas(lista, nivelVisual = 0) {
        let opciones = [];

        lista.forEach((caracteristica) => {
            opciones.push({
                id: caracteristica.id,
                nombre: `${"— ".repeat(nivelVisual)}${caracteristica.nombre}`,
                hoja: caracteristica.hoja,
            });

            if (caracteristica.hijas && caracteristica.hijas.length > 0) {
                opciones = opciones.concat(
                    obtenerOpcionesCaracteristicas(caracteristica.hijas, nivelVisual + 1)
                );
            }
        });

        return opciones;
    }

    function handleBuscar(e) {
        e.preventDefault();
        setError("");

        buscarPuestosPublicos(caracteristicaId, nivel)
            .then(setPuestos)
            .catch((err) => setError(err.message));
    }

    const opcionesCaracteristicas = obtenerOpcionesCaracteristicas(caracteristicas);

    return (
        <section>
            <h1>Buscar Puestos</h1>

            {error && <p className="error">{error}</p>}

            <div className="search-layout">
                <form className="search-panel" onSubmit={handleBuscar}>
                    <h3>Filtros</h3>

                    <label>Característica</label>
                    <select
                        value={caracteristicaId}
                        onChange={(e) => setCaracteristicaId(e.target.value)}
                    >
                        <option value="">Todas</option>
                        {opcionesCaracteristicas.map((caracteristica) => (
                            <option key={caracteristica.id} value={caracteristica.id}>
                                {caracteristica.nombre}
                            </option>
                        ))}
                    </select>

                    <label>Nivel mínimo</label>
                    <select value={nivel} onChange={(e) => setNivel(e.target.value)}>
                        <option value="">Cualquiera</option>
                        <option value="1">Nivel 1</option>
                        <option value="2">Nivel 2</option>
                        <option value="3">Nivel 3</option>
                        <option value="4">Nivel 4</option>
                        <option value="5">Nivel 5</option>
                    </select>

                    <button type="submit">Buscar</button>
                </form>

                <div className="results-panel">
                    <h3>Resultados</h3>

                    {puestos.length === 0 ? (
                        <p>No se encontraron puestos.</p>
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
                </div>
            </div>

            <ModalDetallePuesto
                puesto={puestoSeleccionado}
                onClose={() => setPuestoSeleccionado(null)}
            />
        </section>
    );
}

export default BuscarPuestos;