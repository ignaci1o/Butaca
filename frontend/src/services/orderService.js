import { request } from "./apiClient";

// Si el frontend tiene un objeto currentUser, asumo que mandaremos usuario_id desde orderData.
// Adaptamos orderData a lo que espera CompraRequestDTO

export const useMockOrders = false;

export async function createOrder(orderData) {
  if (useMockOrders) {
    return {
      codigoReserva: `BTC-${Math.random().toString(36).slice(2, 8).toUpperCase()}`,
      total: orderData.total,
      comprador: orderData.comprador
    };
  }

  // Se asume que orderData provee eventoId y la app de React envia el usuario_id del estado global
  const payload = {
    evento_id: orderData.eventoId || orderData.items[0]?.id,
    usuario_id: orderData.usuarioId || JSON.parse(localStorage.getItem("usuario"))?.id_usuario
  };

  const response = await request("/compras", {
    method: "POST",
    body: JSON.stringify(payload)
  });

  // La API devuelve EntradaResponseDTO con codigo_unico_entrada
  return {
    codigoReserva: response.codigo_unico_entrada,
    total: orderData.total,
    comprador: orderData.comprador
  };
}
