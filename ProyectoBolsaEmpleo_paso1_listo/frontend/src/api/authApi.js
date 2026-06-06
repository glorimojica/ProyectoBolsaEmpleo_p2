import { handleResponse } from "./errorHandler";

const API_URL = "/api/v1/auth";

export async function loginRequest(usuario, password) {
    const response = await fetch(`${API_URL}/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            usuario,
            password,
        }),
    });

    return handleResponse(response, "No se pudo iniciar sesión");
}

export async function registerEmpresaRequest(empresa) {
    const response = await fetch(`${API_URL}/register/empresa`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(empresa),
    });

    return handleResponse(response, "No se pudo registrar la empresa");
}

export async function registerOferenteRequest(oferente) {
    const response = await fetch(`${API_URL}/register/oferente`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(oferente),
    });

    return handleResponse(response, "No se pudo registrar el oferente");
}

export async function meRequest(token) {
    const response = await fetch(`${API_URL}/me`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    return handleResponse(response, "Sesión inválida");
}