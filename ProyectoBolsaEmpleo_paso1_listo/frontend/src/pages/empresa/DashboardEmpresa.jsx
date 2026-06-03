import { useAuth } from "../../context/AuthContext";

function DashboardEmpresa() {
    const { usuario, logout } = useAuth();

    return (
        <section>
            <h1>Dashboard Empresa</h1>
            <p>Bienvenido, {usuario?.nombre}</p>
            <p>Rol: {usuario?.rol}</p>

            <button onClick={logout}>Cerrar sesión</button>
        </section>
    );
}

export default DashboardEmpresa;