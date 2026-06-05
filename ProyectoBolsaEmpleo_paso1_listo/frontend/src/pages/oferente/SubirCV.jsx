import { useEffect, useState } from "react";
import {
    obtenerPerfilOferente,
    obtenerUrlMiCv,
    subirCv,
} from "../../api/oferenteApi";
import BackToDashboard from "../../components/BackToDashboard";

function SubirCV() {
    const [perfil, setPerfil] = useState(null);
    const [archivo, setArchivo] = useState(null);
    const [mensaje, setMensaje] = useState("");
    const [error, setError] = useState("");
    const [cargando, setCargando] = useState(false);

    async function cargarPerfil() {
        try {
            setError("");
            const data = await obtenerPerfilOferente();
            setPerfil(data);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        cargarPerfil();
    }, []);

    function handleChange(e) {
        setArchivo(e.target.files[0]);
    }

    async function handleSubmit(e) {
        e.preventDefault();

        setMensaje("");
        setError("");

        if (!archivo) {
            setError("Debe seleccionar un archivo PDF.");
            return;
        }

        if (!archivo.name.toLowerCase().endsWith(".pdf")) {
            setError("El archivo debe ser PDF.");
            return;
        }

        setCargando(true);

        try {
            const data = await subirCv(archivo);
            setPerfil(data);
            setArchivo(null);
            setMensaje("CV subido correctamente.");

            const input = document.getElementById("archivoCv");
            if (input) {
                input.value = "";
            }
        } catch (err) {
            setError(err.message);
        } finally {
            setCargando(false);
        }
    }

    function abrirCv() {
        const token = localStorage.getItem("token");

        fetch(obtenerUrlMiCv(), {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("No se pudo abrir el CV");
                }

                return response.blob();
            })
            .then((blob) => {
                const url = window.URL.createObjectURL(blob);
                window.open(url, "_blank");
            })
            .catch((err) => setError(err.message));
    }

    return (
        <section className="form-page">
            <h1>Currículo del oferente</h1>

            {perfil && (
                <div className="info-card">
                    <p>
                        <strong>Estado:</strong>{" "}
                        {perfil.cvDisponible ? "CV disponible" : "Sin CV registrado"}
                    </p>

                    {perfil.cvDisponible && (
                        <p>
                            <strong>Archivo:</strong> {perfil.cvNombreArchivo}
                        </p>
                    )}
                </div>
            )}

            <form className="form-card" onSubmit={handleSubmit}>
                {mensaje && <p className="success">{mensaje}</p>}
                {error && <p className="error">{error}</p>}

                <label>Seleccionar CV en PDF</label>
                <input
                    id="archivoCv"
                    type="file"
                    accept="application/pdf,.pdf"
                    onChange={handleChange}
                />

                <button type="submit" disabled={cargando}>
                    {cargando ? "Subiendo..." : "Subir CV"}
                </button>

                {perfil?.cvDisponible && (
                    <button type="button" onClick={abrirCv}>
                        Ver mi CV
                    </button>
                )}
            </form>
            <BackToDashboard tipo="oferente" />
        </section>
    );
}

export default SubirCV;