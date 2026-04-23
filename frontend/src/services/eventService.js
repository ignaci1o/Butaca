import { mockEvents } from "../data/mockEvents";
import { request } from "./apiClient";

export const useMockData = false;

function mapSpringEvent(evento) {
  return {
    id: evento.id_evento,
    slug: `evento-${evento.id_evento}`,
    nombre: evento.titulo_evento,
    artista: evento.titulo_evento,
    categoria: "Conciertos",
    ciudad: evento.ubicacion_evento || "Ciudad sin definir",
    recinto: evento.detalles_ubicacion_evento || "Recinto sin definir",
    fecha: evento.fecha_evento,
    hora: evento.hora_evento || "20:00",
    precioMinimo: evento.precio_entrada_evento || 0,
    precioMaximo: evento.precio_entrada_evento || 0,
    destacados: false,
    popular: true,
    disponibilidad: evento.cupos_disponible_evento || 0,
    imagen: "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?auto=format&fit=crop&w=1200&q=80",
    descripcion: evento.descripcion_evento || "Evento disponible en Butaca.",
    etiquetas: ["Disponible"],
    tiposEntrada: [
      { id: "general", nombre: "General", precio: evento.precio_entrada_evento || 0 }
    ]
  };
}

export async function getAllEvents() {
  if (useMockData) {
    return mockEvents;
  }

  const eventos = await request("/eventos");
  return eventos.map(mapSpringEvent);
}

export async function getEventById(id) {
  if (useMockData) {
    return mockEvents.find((event) => String(event.id) === String(id)) || null;
  }

  const evento = await request(`/eventos/${id}`);
  return mapSpringEvent(evento);
}
