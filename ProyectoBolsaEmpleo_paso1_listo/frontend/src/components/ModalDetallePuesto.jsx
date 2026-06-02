function ModalDetallePuesto({ puesto, onClose }) {
    if (!puesto) return null;

    return (
        <div className="modal-overlay">
            <div className="modal">
                <button className="modal-close" onClick={onClose}>
                    X
                </button>

                <p>{puesto.empresa}</p>
                <h2>{puesto.titulo}</h2>
                <p>₡ {puesto.salario}</p>

                <h4>Descripción</h4>
                <p>{puesto.descripcion}</p>

                <h4>Requisitos</h4>

                {puesto.requisitos && puesto.requisitos.length > 0 ? (
                    <ul>
                        {puesto.requisitos.map((req) => (
                            <li key={req.caracteristicaId}>
                                {req.categoria} - Nivel {req.nivel}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>Este puesto no tiene requisitos registrados.</p>
                )}
            </div>
        </div>
    );
}

export default ModalDetallePuesto;