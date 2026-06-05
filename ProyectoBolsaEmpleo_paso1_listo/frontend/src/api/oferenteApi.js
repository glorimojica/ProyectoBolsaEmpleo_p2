const API_URL = "/api/v1/oferente";

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

async function handleResponse(response, defaultMessage) {
    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(data?.message || data?.error || defaultMessage);
    }

    return data;
}

export async function obtenerPerfilOferente() {
    const response = await fetch(`${API_URL}/perfil`, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudo cargar el perfil del oferente");
}

export async function actualizarPerfilOferente(perfil) {
    const response = await fetch(`${API_URL}/perfil`, {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify(perfil),
    });

    return handleResponse(response, "No se pudo actualizar el perfil");
}

export async function listarHabilidades() {
    const response = await fetch(`${API_URL}/habilidades`, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudieron cargar las habilidades");
}

export async function guardarHabilidad(habilidad) {
    const response = await fetch(`${API_URL}/habilidades`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(habilidad),
    });

    return handleResponse(response, "No se pudo guardar la habilidad");
}

export async function eliminarHabilidad(id) {
    const response = await fetch(`${API_URL}/habilidades/${id}`, {
        method: "DELETE",
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudo eliminar la habilidad");
}

export async function subirCv(archivo) {
    const formData = new FormData();
    formData.append("archivo", archivo);

    const response = await fetch(`${API_URL}/cv`, {
        method: "POST",
        headers: getAuthHeadersSinContentType(),
        body: formData,
    });

    return handleResponse(response, "No se pudo subir el CV");
}

export function obtenerUrlMiCv() {
    return `${API_URL}/cv/archivo`;
}

export async function listarPuestosDisponiblesOferente() {
    const response = await fetch(`${API_URL}/puestos`, {
        headers: getAuthHeaders(),
    });

    return handleResponse(response, "No se pudieron cargar los puestos disponibles");
}