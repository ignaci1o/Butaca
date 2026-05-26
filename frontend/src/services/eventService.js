import { mockEvents } from "../data/mockEvents";
import { request } from "./apiClient";

export const useMockData = false;

const fallbackImage =
  "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?auto=format&fit=crop&w=1200&q=80";

function toNumber(value, fallback = 0) {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : fallback;
}

export function mapSpringEvent(evento) {
  const id = evento.id_evento ?? evento.idEvento ?? evento.id;
  const precio = toNumber(evento.precio_entrada_evento ?? evento.precioEntradaEvento);
  const cupos = toNumber(evento.cupos_disponibles ?? evento.cupos_disponible_evento ?? evento.cuposDisponibleEvento);
  const categoria = evento.categoria_nombre || evento.categoriaNombre || "General";
  const ubicacionNombre = evento.ubicacion_nombre || evento.ubicacion_evento || evento.ubicacionEvento || "Lugar sin definir";
  const ubicacionDireccion =
    evento.ubicacion_direccion || evento.detalles_ubicacion_evento || evento.detallesUbicacionEvento || "";

  return {
    raw: evento,
    id,
    slug: `evento-${id}`,
    nombre: evento.titulo_evento || evento.tituloEvento,
    artista: evento.titulo_evento || evento.tituloEvento,
    categoria,
    categoriaId: evento.categoria_id ?? evento.categoriaId,
    ciudad: ubicacionDireccion || ubicacionNombre,
    recinto: ubicacionNombre,
    ubicacionId: evento.ubicacion_id ?? evento.ubicacionId,
    ubicacionDireccion,
    fecha: evento.fecha_evento || evento.fechaEvento,
    hora: String(evento.hora_evento || evento.horaEvento || "20:00").slice(0, 5),
    precioMinimo: precio,
    precioMaximo: precio,
    destacados: true,
    popular: true,
    disponibilidad: cupos,
    cuposTotales: toNumber(evento.cupos_totales_evento ?? evento.cuposTotalesEvento),
    estado: evento.estado_publicacion_evento || evento.estadoPublicacionEvento,
    imagen: evento.imagen_url || evento.imagenUrl || fallbackImage,
    descripcion: evento.descripcion_evento || evento.descripcionEvento || "Evento disponible en Butaca.",
    etiquetas: [categoria, evento.estado_publicacion_evento || evento.estadoPublicacionEvento || "Disponible"],
    tiposEntrada: [{ id: "general", nombre: "Entrada general", precio }]
  };
}

export async function getAllEvents(params = {}) {
  if (useMockData) {
    return mockEvents;
  }

  const query = params.categoriaId ? `?categoriaId=${params.categoriaId}` : "";
  const eventos = await request(`/eventos${query}`);
  return eventos.map(mapSpringEvent);
}

export async function getEventById(id) {
  if (useMockData) {
    return mockEvents.find((event) => String(event.id) === String(id)) || null;
  }

  const evento = await request(`/eventos/${id}`);
  return mapSpringEvent(evento);
}

export async function getCategories() {
  const categorias = await request("/admin/categorias");
  return categorias.map((categoria) => ({
    id: categoria.id_categoria,
    nombre: categoria.nombre,
    descripcion: categoria.descripcion
  }));
}

export async function getLocations() {
  const ubicaciones = await request("/admin/ubicaciones");
  return ubicaciones.map((ubicacion) => ({
    id: ubicacion.id_ubicacion,
    nombre: ubicacion.nombre_lugar,
    direccion: ubicacion.direccion,
    capacidadMaxima: ubicacion.capacidad_maxima,
    activo: ubicacion.activo
  }));
}

export async function getOrganizerEvents(organizerId) {
  const eventos = await request(`/eventos/organizador/${organizerId}`);
  return eventos.map(mapSpringEvent);
}

export async function createOrganizerEvent(eventData) {
  return request("/eventos", {
    method: "POST",
    body: JSON.stringify(eventData)
  });
}

export async function publishEvent(eventId) {
  return request(`/eventos/${eventId}/publicar`, { method: "PUT" });
}

export async function cancelEvent(eventId) {
  return request(`/eventos/${eventId}/cancelar`, { method: "PUT" });
}

export async function getAdminStats() {
  return request("/admin/dashboard");
}
