import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

function DashboardEmpresa() {
    const { usuario, logout } = useAuth();

    return (
        <section>
            <h1>Dashboard Empresa</h1>

            <p>Bienvenido, {usuario?.nombre || "Empresa"}</p>
            <p>Rol: {usuario?.rol}</p>

            <div className="dashboard-grid">
                <Link className="dashboard-card" to="/empresa/puestos">
                    Mis puestos
                </Link>

                <Link className="dashboard-card" to="/empresa/puestos/nuevo">
                    Publicar nuevo puesto
                </Link>
            </div>

            <button type="button" onClick={logout}>
                Cerrar sesión
            </button>
        </section>
    );
}

export default DashboardEmpresa;