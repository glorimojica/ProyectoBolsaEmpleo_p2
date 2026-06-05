import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import {
    buscarCandidatos,
    obtenerCvCandidato,
    obtenerPuesto,
} from "../../api/empresaApi";
import BackToDashboard from "../../components/BackToDashboard";

function CandidatosPuesto() {
    const { id } = useParams();

    const [puesto, setPuesto] = useState(null);
    const [candidatos, setCandidatos] = useState([]);
    const [error, setError] = useState("");

    async function cargarDatos() {
        try {
            setError("");

            const [puestoData, candidatosData] = await Promise.all([
                obtenerPuesto(id),
                buscarCandidatos(id),
            ]);

            setPuesto(puestoData);
            setCandidatos(candidatosData);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        cargarDatos();
    }, [id]);

    async function handleVerCv(oferenteId) {
        try {
            setError("");

            const blob = await obtenerCvCandidato(oferenteId);
            const url = window.URL.createObjectURL(blob);

            window.open(url, "_blank");
        } catch (err) {
            setError(err.message);
        }
    }

    return (
        <section>
            <h1>Candidatos compatibles</h1>

            {puesto && (
                <>
                    <h2>{puesto.titulo}</h2>
                    <p>{puesto.descripcion}</p>
                </>
            )}

            {error && <p className="error">{error}</p>}

            {candidatos.length === 0 ? (
                <p>No hay candidatos compatibles o el puesto no tiene requisitos suficientes.</p>
            ) : (
                <table className="data-table">
                    <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Correo</th>
                        <th>Teléfono</th>
                        <th>Residencia</th>
                        <th>Coincidencia</th>
                        <th>CV</th>
                    </tr>
                    </thead>

                    <tbody>
                    {candidatos.map((candidato) => (
                        <tr key={candidato.oferenteId}>
                            <td>{candidato.nombre}</td>
                            <td>{candidato.correo}</td>
                            <td>{candidato.telefono}</td>
                            <td>{candidato.residencia}</td>
                            <td>
                                {candidato.requisitosCumplidos}/{candidato.totalRequisitos}
                                {" - "}
                                {Number(candidato.porcentajeCoincidencia).toFixed(1)}%
                            </td>
                            <td>
                                <button
                                    type="button"
                                    onClick={() => handleVerCv(candidato.oferenteId)}
                                >
                                    Ver CV
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}

            <p>
                <Link to="/empresa/puestos">Volver a mis puestos</Link>
            </p>
        </section>
    );
}

export default CandidatosPuesto;