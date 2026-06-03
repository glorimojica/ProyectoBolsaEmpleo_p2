import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function ProtectedRoute({ children, rolesPermitidos }) {
    const { usuario, cargando, estaAutenticado } = useAuth();

    if (cargando) {
        return <p>Cargando sesión...</p>;
    }

    if (!estaAutenticado) {
        return <Navigate to="/login" replace />;
    }

    if (rolesPermitidos && !rolesPermitidos.includes(usuario.rol)) {
        return <Navigate to="/" replace />;
    }

    return children;
}

export default ProtectedRoute;