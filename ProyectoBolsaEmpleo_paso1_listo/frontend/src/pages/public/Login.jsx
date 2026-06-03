import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

function Login() {
    const [usuarioInput, setUsuarioInput] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [cargando, setCargando] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    function obtenerRutaPorRol(rol) {
        if (rol === "ADMIN") return "/admin/dashboard";
        if (rol === "EMPRESA") return "/empresa/dashboard";
        if (rol === "OFERENTE") return "/oferente/dashboard";
        return "/";
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setCargando(true);

        try {
            const usuarioLogueado = await login(usuarioInput, password);
            navigate(obtenerRutaPorRol(usuarioLogueado.rol));
        } catch (err) {
            setError(err.message);
        } finally {
            setCargando(false);
        }
    }

    return (
        <section className="form-page">
            <h1>Login</h1>

            <form className="form-card" onSubmit={handleSubmit}>
                {error && <p className="error">{error}</p>}

                <label>Usuario o correo</label>
                <input
                    type="text"
                    value={usuarioInput}
                    onChange={(e) => setUsuarioInput(e.target.value)}
                    placeholder="admin o empresa@empresa.com"
                    required
                />

                <label>Clave</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Ingrese su clave"
                    required
                />

                <button type="submit" disabled={cargando}>
                    {cargando ? "Ingresando..." : "Ingresar"}
                </button>
            </form>
        </section>
    );
}

export default Login;