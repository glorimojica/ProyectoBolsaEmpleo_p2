import { handleBlobResponse, handleResponse } from "./errorHandler";

const API_URL = "/api/v1/empresa";

function getAuthHeaders() {
    const token = localStorage.getItem("token");

    return {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
    };
}

function getAuthHeadersSinContentType() {
    const token = localStorage.getItem("token");

    return {
        Authorization: `Bearer ${token}`,
    };
}

export async function listarMisPuestos() {
    const response = await fetch(`${API_URL}/puestos`, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudieron cargar los puestos");
}

export async function obtenerPuesto(id) {
    const response = await fetch(`${API_URL}/puestos/${id}`, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudo cargar el puesto");
}

export async function crearPuesto(puesto) {
    const response = await fetch(`${API_URL}/puestos`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(puesto),
    });

    return handleResponse(response, "No se pudo crear el puesto");
}

export async function desactivarPuesto(id) {
    const response = await fetch(`${API_URL}/puestos/${id}/desactivar`, {
        method: "PUT",
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudo desactivar el puesto");
}

export async function agregarRequisito(puestoId, requisito) {
    const response = await fetch(`${API_URL}/puestos/${puestoId}/requisitos`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(requisito),
    });

    return handleResponse(response, "No se pudo agregar el requisito");
}

export async function buscarCandidatos(puestoId) {
    const response = await fetch(`${API_URL}/puestos/${puestoId}/candidatos`, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudieron cargar los candidatos");
}

export async function obtenerCvCandidato(oferenteId) {
    const response = await fetch(`${API_URL}/candidatos/${oferenteId}/cv`, {
        headers: getAuthHeadersSinContentType(),
    });

    return handleBlobResponse(response, "No se pudo abrir el CV del candidato");
}