const API_URL = "/api/v1/public";

export async function obtenerPuestosRecientes() {
    const response = await fetch(`${API_URL}/puestos/recientes`);

    if (!response.ok) {
        throw new Error("No se pudieron cargar los puestos recientes");
    }

    return response.json();
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

    if (!response.ok) {
        throw new Error("No se pudieron buscar los puestos");
    }

    return response.json();
}

export async function obtenerCaracteristicas() {
    const response = await fetch(`${API_URL}/caracteristicas`);

    if (!response.ok) {
        throw new Error("No se pudieron cargar las características");
    }

    return response.json();
}

export async function obtenerDetallePuesto(id) {
    const response = await fetch(`${API_URL}/puestos/${id}`);

    if (!response.ok) {
        throw new Error("No se pudo cargar el detalle del puesto");
    }

    return response.json();
}