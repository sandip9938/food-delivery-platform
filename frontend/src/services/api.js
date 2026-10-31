async function request(path, options = {}) {
  let response;
  const { headers, ...fetchOptions } = options;

  try {
    response = await fetch(path, {
      headers: {
        Accept: "application/json",
        ...(fetchOptions.body ? { "Content-Type": "application/json" } : {}),
        ...headers,
      },
      ...fetchOptions,
    });
  } catch {
    throw new Error(
      "Can't reach the food service. Make sure the backend is running, then try again.",
    );
  }

  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    try {
      const body = await response.json();
      if (body.message) message = body.message;
    } catch {
      // Keep the HTTP status message when the response has no JSON body.
    }
    throw new Error(message);
  }

  if (response.status === 204) return null;
  return response.json();
}

export function getRestaurants(
  { search = "", city = "", cuisine = "", page = 0, size = 9 } = {},
  signal,
) {
  const params = new URLSearchParams({ page, size, sort: "name,asc" });
  if (search.trim()) params.set("search", search.trim());
  if (city.trim()) params.set("city", city.trim());
  if (cuisine) params.set("cuisine", cuisine);
  return request(`/api/restaurants?${params}`, { signal });
}

export function getRestaurantMenu(restaurantId, signal) {
  return request(`/api/restaurants/${restaurantId}/menu-items`, { signal });
}

export function getHealth() {
  return request("/api/health");
}
