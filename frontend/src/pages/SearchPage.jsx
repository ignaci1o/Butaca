import { useEffect, useMemo, useState } from "react";
import EventGrid from "../components/events/EventGrid";
import SectionTitle from "../components/ui/SectionTitle";
import { categoryOptions } from "../data/mockEvents";
import { getAllEvents } from "../services/eventService";

function SearchPage() {
  const [events, setEvents] = useState([]);
  const [searchText, setSearchText] = useState("");
  const [category, setCategory] = useState("Todos");
  const [maxPrice, setMaxPrice] = useState(300);
  const [sortBy, setSortBy] = useState("fecha");

  useEffect(() => {
    getAllEvents().then(setEvents).catch(() => setEvents([]));
  }, []);

  const filteredEvents = useMemo(() => {
    let result = [...events];

    if (searchText.trim()) {
      const term = searchText.toLowerCase();
      result = result.filter(
        (event) =>
          event.nombre.toLowerCase().includes(term) ||
          event.artista.toLowerCase().includes(term) ||
          event.ciudad.toLowerCase().includes(term)
      );
    }

    if (category !== "Todos") {
      result = result.filter((event) => event.categoria === category);
    }

    result = result.filter((event) => event.precioMinimo <= maxPrice);

    if (sortBy === "precio") {
      result.sort((a, b) => a.precioMinimo - b.precioMinimo);
    } else if (sortBy === "popularidad") {
      result.sort((a, b) => b.disponibilidad - a.disponibilidad);
    } else {
      result.sort((a, b) => new Date(a.fecha) - new Date(b.fecha));
    }

    return result;
  }, [category, events, maxPrice, searchText, sortBy]);

  return (
    <section className="container page-block">
      <SectionTitle
        eyebrow="Busqueda"
        title="Encontra eventos sin depender todavia del backend"
        text="Cuando conectes la API, esta pagina puede pasar a consumir filtros reales o mantenerse del lado del cliente."
      />

      <div className="search-layout">
        <aside className="search-panel">
          <label>
            Buscar por nombre, artista o ciudad
            <input
              type="text"
              value={searchText}
              onChange={(event) => setSearchText(event.target.value)}
              placeholder="Ej: My Chemical Romance"
            />
          </label>

          <label>
            Categoria
            <select value={category} onChange={(event) => setCategory(event.target.value)}>
              {categoryOptions.map((option) => (
                <option key={option} value={option}>
                  {option}
                </option>
              ))}
            </select>
          </label>

          <label>
            Ordenar por
            <select value={sortBy} onChange={(event) => setSortBy(event.target.value)}>
              <option value="fecha">Fecha</option>
              <option value="precio">Precio</option>
              <option value="popularidad">Disponibilidad</option>
            </select>
          </label>

          <label>
            Precio maximo: ${maxPrice}
            <input
              type="range"
              min="20"
              max="300"
              step="10"
              value={maxPrice}
              onChange={(event) => setMaxPrice(Number(event.target.value))}
            />
          </label>
        </aside>

        <div className="search-results">
          <p className="results-count">{filteredEvents.length} resultados encontrados</p>
          <EventGrid events={filteredEvents} />
        </div>
      </div>
    </section>
  );
}

export default SearchPage;
