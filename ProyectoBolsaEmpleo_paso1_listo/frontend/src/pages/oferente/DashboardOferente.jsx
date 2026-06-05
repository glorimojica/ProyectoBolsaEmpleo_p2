import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

function DashboardOferente() {
    const { usuario, logout } = useAuth();

    return (
        <section>
            <h1>Dashboard Oferente</h1>

            <p>Bienvenido, {usuario?.nombre || "Oferente"}</p>
            <p>Rol: {usuario?.rol}</p>

            <div className="dashboard-grid">
                <Link className="dashboard-card" to="/oferente/perfil">
                    Editar perfil
                </Link>

                <Link className="dashboard-card" to="/oferente/habilidades">
                    Gestionar habilidades
                </Link>

                <Link className="dashboard-card" to="/oferente/cv">
                    Subir currículo
                </Link>

                <Link className="dashboard-card" to="/oferente/puestos">
                    Ver puestos disponibles
                </Link>
            </div>

            <button type="button" onClick={logout}>
                Cerrar sesión
            </button>
        </section>
    );
}

export default DashboardOferente;