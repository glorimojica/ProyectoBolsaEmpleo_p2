import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

function DashboardAdmin() {
    const { usuario, logout } = useAuth();

    return (
        <section>
            <h1>Dashboard Administrador</h1>
            <p>Bienvenido, {usuario?.nombre}</p>
            <p>Rol: {usuario?.rol}</p>

            <div className="dashboard-grid">
                <Link className="dashboard-card" to="/admin/empresas">
                    Aprobar empresas
                </Link>

                <Link className="dashboard-card" to="/admin/oferentes">
                    Aprobar oferentes
                </Link>

                <Link className="dashboard-card" to="/admin/caracteristicas">
                    Gestionar características
                </Link>
            </div>

            <button onClick={logout}>Cerrar sesión</button>
        </section>
    );
}

export default DashboardAdmin;