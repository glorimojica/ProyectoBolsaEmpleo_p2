import { handleResponse } from "./errorHandler";

const API_URL = "/api/v1/admin/caracteristicas";

function getAuthHeaders() {
    const token = localStorage.getItem("token");

    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
}

export async function listarCaracteristicasAdmin() {
    const response = await fetch(API_URL, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudieron cargar las características");
}

export async function listarCaracteristicasPlanasAdmin() {
    const response = await fetch(`${API_URL}/planas`, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudieron cargar las características");
}

export async function crearCaracteristica(caracteristica) {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(caracteristica),
    });

    return handleResponse(response, "No se pudo crear la característica");
}

export async function actualizarCaracteristica(id, caracteristica) {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify(caracteristica),
    });

    return handleResponse(response, "No se pudo actualizar la característica");
}

export async function eliminarCaracteristica(id) {
    const response = await fetch(`${API_URL}/${id}`, {
        method: "DELETE",
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudo eliminar la característica");
}