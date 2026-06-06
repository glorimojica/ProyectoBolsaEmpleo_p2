export function getErrorMessage(data, defaultMessage) {
    if (!data) {
        return defaultMessage;
    }

    if (data.errors) {
        if (Array.isArray(data.errors)) {
            return data.errors.join("\n");
        }

        return Object.values(data.errors).join("\n");
    }

    return data.message || data.error || defaultMessage;
}

export async function handleResponse(response, defaultMessage) {
    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(getErrorMessage(data, defaultMessage));
    }

    return data;
}