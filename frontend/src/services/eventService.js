import { mockEvents } from "../data/mockEvents";
import { request } from "./apiClient";

export const useMockData = true;

function mapSpringEvent(evento) {
  return {
    id: evento.id,
    slug: `evento-${evento.id}`,
    nombre: evento.nombre,
    artista: evento.artista || evento.nombre,
    categoria: evento.categoria || "Conciertos",
    ciudad: evento.ciudad || "Ciudad sin definir",
    recinto: evento.recinto || "Recinto sin definir",
    fecha: evento.fecha,
    hora: evento.hora || "20:00",
    precioMinimo: evento.precioMinimo || 0,
    precioMaximo: evento.precioMaximo || evento.precioMinimo || 0,
    destacados: Boolean(evento.destacado),
    popular: true,
    disponibilidad: evento.entradasDisponibles || 0,
    imagen:
      evento.imagen ||
      "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?auto=format&fit=crop&w=1200&q=80",
    descripcion: evento.descripcion || "Evento disponible en Butaca.",
    etiquetas: evento.etiquetas || ["Disponible"],
    tiposEntrada:
      evento.tiposEntrada || [{ id: "general", nombre: "General", precio: evento.precioMinimo || 0 }]
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
