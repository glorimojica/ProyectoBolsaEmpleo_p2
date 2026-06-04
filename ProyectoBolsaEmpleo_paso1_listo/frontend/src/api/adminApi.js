const API_URL = "/api/v1/admin";

function getAuthHeaders() {
    const token = localStorage.getItem("token");

    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
}

async function handleResponse(response, defaultMessage) {
    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(data?.message || data?.error || defaultMessage);
    }

    return data;
}

export async function listarEmpresasPendientes() {
    const response = await fetch(`${API_URL}/empresas/pendientes`, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudieron cargar las empresas pendientes");
}

export async function aprobarEmpresa(id) {
    const response = await fetch(`${API_URL}/empresas/${id}/aprobar`, {
        method: "PUT",
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudo aprobar la empresa");
}

export async function rechazarEmpresa(id) {
    const response = await fetch(`${API_URL}/empresas/${id}/rechazar`, {
        method: "PUT",
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudo rechazar la empresa");
}

export async function listarOferentesPendientes() {
    const response = await fetch(`${API_URL}/oferentes/pendientes`, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudieron cargar los oferentes pendientes");
}

export async function aprobarOferente(id) {
    const response = await fetch(`${API_URL}/oferentes/${id}/aprobar`, {
        method: "PUT",
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudo aprobar el oferente");
}

export async function rechazarOferente(id) {
    const response = await fetch(`${API_URL}/oferentes/${id}/rechazar`, {
        method: "PUT",
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudo rechazar el oferente");
}