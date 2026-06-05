import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { desactivarPuesto, listarMisPuestos } from "../../api/empresaApi";
import BackToDashboard from "../../components/BackToDashboard";

function MisPuestos() {
    const [puestos, setPuestos] = useState([]);
    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState("");

    async function cargarPuestos() {
        try {
            setError("");
            const data = await listarMisPuestos();
            setPuestos(data);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        cargarPuestos();
    }, []);

    async function handleDesactivar(id) {
        const confirmar = window.confirm("¿Seguro que deseas desactivar este puesto?");

        if (!confirmar) return;

        try {
            setMensaje("");
            setError("");

            await desactivarPuesto(id);

            setMensaje("Puesto desactivado correctamente.");
            await cargarPuestos();
        } catch (err) {
            setError(err.message);
        }
    }

    return (
        <section>
            <h1>Mis puestos</h1>

            <Link className="button-link" to="/empresa/puestos/nuevo">
                Crear nuevo puesto
            </Link>

            {mensaje && <p className="success">{mensaje}</p>}
            {error && <p className="error">{error}</p>}

            {puestos.length === 0 ? (
                <p>No tienes puestos registrados.</p>
            ) : (
                <table className="data-table">
                    <thead>
                    <tr>
                        <th>Título</th>
                        <th>Salario</th>
                        <th>Tipo</th>
                        <th>Estado</th>
                        <th>Requisitos</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>

                    <tbody>
                    {puestos.map((puesto) => (
                        <tr key={puesto.id}>
                            <td>{puesto.titulo}</td>
                            <td>₡ {puesto.salario}</td>
                            <td>{puesto.publico ? "Público" : "Privado"}</td>
                            <td>{puesto.activo ? "Activo" : "Inactivo"}</td>
                            <td>{puesto.requisitos?.length || 0}</td>
                            <td>
                                <Link to={`/empresa/puestos/${puesto.id}/requisitos`}>
                                    Requisitos
                                </Link>

                                {" | "}

                                <Link to={`/empresa/puestos/${puesto.id}/candidatos`}>
                                    Candidatos
                                </Link>

                                {puesto.activo && (
                                    <>
                                        {" | "}
                                        <button onClick={() => handleDesactivar(puesto.id)}>
                                            Desactivar
                                        </button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            <BackToDashboard tipo="empresa" />
        </section>

    );
}

export default MisPuestos;