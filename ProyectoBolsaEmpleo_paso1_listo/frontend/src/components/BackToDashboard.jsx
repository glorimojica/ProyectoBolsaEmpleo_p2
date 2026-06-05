import { Link } from "react-router-dom";

function BackToDashboard({ tipo }) {
    const rutas = {
        admin: "/admin/dashboard",
        empresa: "/empresa/dashboard",
        oferente: "/oferente/dashboard",
    };

    const textos = {
        admin: "Volver al dashboard admin",
        empresa: "Volver al dashboard empresa",
        oferente: "Volver al dashboard oferente",
    };

    return (
        <div className="back-dashboard">
            <Link to={rutas[tipo]} className="button-link">
                {textos[tipo]}
            </Link>
        </div>
    );
}

export default BackToDashboard;