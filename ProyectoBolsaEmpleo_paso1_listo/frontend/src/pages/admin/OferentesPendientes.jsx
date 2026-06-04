import { useEffect, useState } from "react";
import {
    aprobarOferente,
    listarOferentesPendientes,
    rechazarOferente,
} from "../../api/adminApi.js";

function OferentesPendientes() {
    const [oferentes, setOferentes] = useState([]);
    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState("");

    async function cargarOferentes() {
        try {
            setError("");
            const data = await listarOferentesPendientes();
            setOferentes(data);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        cargarOferentes();
    }, []);

    async function handleAprobar(id) {
        try {
            setMensaje("");
            setError("");
            await aprobarOferente(id);
            setMensaje("Oferente aprobado correctamente.");
            cargarOferentes();
        } catch (err) {
            setError(err.message);
        }
    }

    async function handleRechazar(id) {
        try {
            setMensaje("");
            setError("");
            await rechazarOferente(id);
            setMensaje("Oferente rechazado correctamente.");
            cargarOferentes();
        } catch (err) {
            setError(err.message);
        }
    }

    return (
        <section>
            <h1>Oferentes pendientes</h1>

            {mensaje && <p className="success">{mensaje}</p>}
            {error && <p className="error">{error}</p>}

            {oferentes.length === 0 ? (
                <p>No hay oferentes pendientes.</p>
            ) : (
                <table className="data-table">
                    <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Correo</th>
                        <th>Teléfono</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>

                    <tbody>
                    {oferentes.map((oferente) => (
                        <tr key={oferente.id}>
                            <td>{oferente.nombre}</td>
                            <td>{oferente.correo}</td>
                            <td>{oferente.telefono}</td>
                            <td>{oferente.estado}</td>
                            <td>
                                <button onClick={() => handleAprobar(oferente.id)}>
                                    Aprobar
                                </button>

                                <button onClick={() => handleRechazar(oferente.id)}>
                                    Rechazar
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </section>
    );
}

export default OferentesPendientes;