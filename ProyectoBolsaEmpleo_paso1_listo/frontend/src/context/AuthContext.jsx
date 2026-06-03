import { createContext, useContext, useEffect, useState } from "react";
import { loginRequest, meRequest } from "../api/authApi";

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [usuario, setUsuario] = useState(null);
    const [token, setToken] = useState(localStorage.getItem("token"));
    const [cargando, setCargando] = useState(true);

    useEffect(() => {
        async function cargarSesion() {
            if (!token) {
                setCargando(false);
                return;
            }

            try {
                const data = await meRequest(token);
                setUsuario(data);
            } catch (error) {
                localStorage.removeItem("token");
                setToken(null);
                setUsuario(null);
            } finally {
                setCargando(false);
            }
        }

        cargarSesion();
    }, [token]);

    async function login(usuarioInput, password) {
        const data = await loginRequest(usuarioInput, password);

        localStorage.setItem("token", data.token);
        setToken(data.token);

        const usuarioLogueado = {
            id: data.id,
            nombre: data.nombre,
            usuario: data.usuario,
            rol: data.rol,
            estado: data.estado,
        };

        setUsuario(usuarioLogueado);

        return usuarioLogueado;
    }

    function logout() {
        localStorage.removeItem("token");
        setToken(null);
        setUsuario(null);
    }

    const value = {
        usuario,
        token,
        cargando,
        login,
        logout,
        estaAutenticado: !!token && !!usuario,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
    return useContext(AuthContext);
}