import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { obtenerCaracteristicas } from "../../api/publicApi";
import { agregarRequisito, obtenerPuesto } from "../../api/empresaApi";

function RequisitosPuesto() {
    const { id } = useParams();

    const [puesto, setPuesto] = useState(null);
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

            const [puestoData, caracteristicasData] = await Promise.all([
                obtenerPuesto(id),
                obtenerCaracteristicas(),
            ]);

            setPuesto(puestoData);

            const hojas = convertirArbolAListaHojas(caracteristicasData);
            setCaracteristicas(hojas);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        cargarDatos();
    }, [id]);

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
            await agregarRequisito(id, {
                caracteristicaId: Number(form.caracteristicaId),
                nivel: Number(form.nivel),
            });

            setMensaje("Requisito guardado correctamente.");

            setForm({
                caracteristicaId: "",
                nivel: "1",
            });

            await cargarDatos();
        } catch (err) {
            setError(err.message);
        }
    }

    return (
        <section>
            <h1>Requisitos del puesto</h1>

            {puesto && (
                <>
                    <h2>{puesto.titulo}</h2>
                    <p>{puesto.descripcion}</p>
                    <p>Salario: ₡ {puesto.salario}</p>
                    <p>Tipo: {puesto.publico ? "Público" : "Privado"}</p>
                </>
            )}

            {mensaje && <p className="success">{mensaje}</p>}
            {error && <p className="error">{error}</p>}

            <form className="form-card" onSubmit={handleSubmit}>
                <h3>Agregar requisito</h3>

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

                <label>Nivel requerido</label>
                <select name="nivel" value={form.nivel} onChange={handleChange}>
                    <option value="1">Nivel 1</option>
                    <option value="2">Nivel 2</option>
                    <option value="3">Nivel 3</option>
                    <option value="4">Nivel 4</option>
                    <option value="5">Nivel 5</option>
                </select>

                <button type="submit">Guardar requisito</button>
            </form>

            <h3>Requisitos actuales</h3>

            {!puesto || puesto.requisitos.length === 0 ? (
                <p>Este puesto todavía no tiene requisitos.</p>
            ) : (
                <table className="data-table">
                    <thead>
                    <tr>
                        <th>Característica</th>
                        <th>Nivel</th>
                    </tr>
                    </thead>

                    <tbody>
                    {puesto.requisitos.map((req) => (
                        <tr key={req.caracteristicaId}>
                            <td>{req.nombreCompleto}</td>
                            <td>{req.nivel}</td>
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

export default RequisitosPuesto;