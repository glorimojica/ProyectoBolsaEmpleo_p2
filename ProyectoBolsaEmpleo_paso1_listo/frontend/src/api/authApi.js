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

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(data?.message || "No se pudo iniciar sesión");
    }

    return data;
}

export async function registerEmpresaRequest(empresa) {
    const response = await fetch(`${API_URL}/register/empresa`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(empresa),
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(data?.message || "No se pudo registrar la empresa");
    }

    return data;
}

export async function registerOferenteRequest(oferente) {
    const response = await fetch(`${API_URL}/register/oferente`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(oferente),
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(data?.message || "No se pudo registrar el oferente");
    }

    return data;
}

export async function meRequest(token) {
    const response = await fetch(`${API_URL}/me`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error("Sesión inválida");
    }

    return response.json();
}