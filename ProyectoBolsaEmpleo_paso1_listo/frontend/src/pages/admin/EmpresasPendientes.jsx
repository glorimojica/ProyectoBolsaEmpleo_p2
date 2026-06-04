import { useEffect, useState } from "react";
import {
    aprobarEmpresa,
    listarEmpresasPendientes,
    rechazarEmpresa,
} from "../../api/adminApi";

function EmpresasPendientes() {
    const [empresas, setEmpresas] = useState([]);
    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState("");

    async function cargarEmpresas() {
        try {
            setError("");
            const data = await listarEmpresasPendientes();
            setEmpresas(data);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        cargarEmpresas();
    }, []);

    async function handleAprobar(id) {
        try {
            setMensaje("");
            setError("");
            await aprobarEmpresa(id);
            setMensaje("Empresa aprobada correctamente.");
            cargarEmpresas();
        } catch (err) {
            setError(err.message);
        }
    }

    async function handleRechazar(id) {
        try {
            setMensaje("");
            setError("");
            await rechazarEmpresa(id);
            setMensaje("Empresa rechazada correctamente.");
            cargarEmpresas();
        } catch (err) {
            setError(err.message);
        }
    }

    return (
        <section>
            <h1>Empresas pendientes</h1>

            {mensaje && <p className="success">{mensaje}</p>}
            {error && <p className="error">{error}</p>}

            {empresas.length === 0 ? (
                <p>No hay empresas pendientes.</p>
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
                    {empresas.map((empresa) => (
                        <tr key={empresa.id}>
                            <td>{empresa.nombre}</td>
                            <td>{empresa.correo}</td>
                            <td>{empresa.telefono}</td>
                            <td>{empresa.estado}</td>
                            <td>
                                <button onClick={() => handleAprobar(empresa.id)}>
                                    Aprobar
                                </button>

                                <button onClick={() => handleRechazar(empresa.id)}>
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

export default EmpresasPendientes;