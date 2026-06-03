import { useAuth } from "../../context/AuthContext";

function DashboardOferente() {
    const { usuario, logout } = useAuth();

    return (
        <section>
            <h1>Dashboard Oferente</h1>
            <p>Bienvenido, {usuario?.nombre}</p>
            <p>Rol: {usuario?.rol}</p>

            <button onClick={logout}>Cerrar sesión</button>
        </section>
    );
}

export default DashboardOferente;