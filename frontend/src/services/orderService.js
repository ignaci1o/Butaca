import { request } from "./apiClient";

export const useMockOrders = false;

const AUTH_STORAGE_KEY = "butaca-auth-user";

function getStoredUser() {
  const raw = localStorage.getItem(AUTH_STORAGE_KEY);
  return raw ? JSON.parse(raw) : null;
}

export async function createOrder(orderData) {
  if (useMockOrders) {
    return {
      codigoReserva: `BTC-${Math.random().toString(36).slice(2, 8).toUpperCase()}`,
      total: orderData.total,
      comprador: orderData.comprador,
      entradas: []
    };
  }

  const storedUser = getStoredUser();
  const usuarioId = orderData.usuarioId || storedUser?.id_usuario || storedUser?.id;
  const payload = {
    evento_id: orderData.eventoId,
    usuario_id: usuarioId,
    cantidad: orderData.cantidad || 1
  };

  const response = await request("/compras", {
    method: "POST",
    body: JSON.stringify(payload)
  });

  const entradas = response.entradas || [];
  return {
    idCompra: response.id,
    codigoReserva: entradas[0]?.codigo_unico_entrada || `COMPRA-${response.id}`,
    total: Number(response.total ?? orderData.total),
    comprador: orderData.comprador,
    entradas,
    raw: response
  };
}

export async function getUserOrders(userId) {
  return request(`/compras/usuario/${userId}`);
}

export async function getOrganizerOrders(organizerId) {
  return request(`/compras/organizador/${organizerId}`);
}

export async function getUserNotifications(userId) {
  return request(`/notificaciones/usuario/${userId}`);
}

export async function markNotificationRead(notificationId) {
  return request(`/notificaciones/${notificationId}/leida`, { method: "PUT" });
}
