import { request } from "./apiClient";

export const useMockOrders = true;

export async function createOrder(orderData) {
  if (useMockOrders) {
    return {
      codigoReserva: `BTC-${Math.random().toString(36).slice(2, 8).toUpperCase()}`,
      total: orderData.total,
      comprador: orderData.comprador
    };
  }

  return request("/pedidos", {
    method: "POST",
    body: JSON.stringify(orderData)
  });
}
