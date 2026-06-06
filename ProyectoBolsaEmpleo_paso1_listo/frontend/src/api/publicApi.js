import { handleResponse } from "./errorHandler";

const API_URL = "/api/v1/public";

export async function obtenerPuestosRecientes() {
    const response = await fetch(`${API_URL}/puestos/recientes`);

    return handleResponse(response, "No se pudieron cargar los puestos recientes");
}

export async function buscarPuestosPublicos(caracteristicaId = "", nivel = "") {
    const params = new URLSearchParams();

    if (caracteristicaId) {
        params.append("caracteristicaId", caracteristicaId);
    }

    if (nivel) {
        params.append("nivel", nivel);
    }

    const response = await fetch(`${API_URL}/puestos?${params.toString()}`);

    return handleResponse(response, "No se pudieron buscar los puestos");
}

export async function obtenerCaracteristicas() {
    const response = await fetch(`${API_URL}/caracteristicas`);

    return handleResponse(response, "No se pudieron cargar las características");
}

export async function obtenerDetallePuesto(id) {
    const response = await fetch(`${API_URL}/puestos/${id}`);

    return handleResponse(response, "No se pudo cargar el detalle del puesto");
}