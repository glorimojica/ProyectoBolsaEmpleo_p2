function JobCard({ puesto, onVerDetalle }) {
    return (
        <article className="job-card">
            <p className="empresa">{puesto.empresa}</p>
            <h3>{puesto.titulo}</h3>
            <p className="salario">₡ {puesto.salario}</p>

            <button onClick={() => onVerDetalle(puesto)}>
                Ver detalle
            </button>
        </article>
    );
}

export default JobCard;