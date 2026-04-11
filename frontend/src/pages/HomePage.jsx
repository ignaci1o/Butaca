import { useEffect, useMemo, useState } from "react";
import HeroSection from "../components/home/HeroSection";
import CategoryTabs from "../components/home/CategoryTabs";
import EventGrid from "../components/events/EventGrid";
import SectionTitle from "../components/ui/SectionTitle";
import { categoryOptions } from "../data/mockEvents";
import { getAllEvents } from "../services/eventService";

function HomePage() {
  const [events, setEvents] = useState([]);
  const [activeCategory, setActiveCategory] = useState("Todos");

  useEffect(() => {
    getAllEvents().then(setEvents).catch(() => setEvents([]));
  }, []);

  const featuredEvent = events.find((event) => event.destacados);
  const popularEvents = events.filter((event) => event.popular).slice(0, 3);

  const visibleEvents = useMemo(() => {
    if (activeCategory === "Todos") {
      return events;
    }

    return events.filter((event) => event.categoria === activeCategory);
  }, [activeCategory, events]);

  return (
    <>
      <HeroSection featuredEvent={featuredEvent} />

      <section className="container home-section" id="colecciones">
        <SectionTitle
          eyebrow="Explora"
          title="Eventos listos para venderse"
          text="Esta grilla te sirve como base para conectar luego el listado real desde Spring Boot."
        />
        <CategoryTabs
          categories={categoryOptions}
          activeCategory={activeCategory}
          onChange={setActiveCategory}
        />
        <EventGrid events={visibleEvents} />
      </section>

      <section className="container highlight-strip">
        <div>
          <p className="section-eyebrow">Operacion simple</p>
          <h3>Una interfaz pensada para crecer sin mezclar logica de negocio con presentacion.</h3>
        </div>
        <div className="highlight-stats">
          <span>React + Vite</span>
          <span>Listo para Spring Boot</span>
          <span>Componentes claros</span>
        </div>
      </section>

      <section className="container home-section">
        <SectionTitle
          eyebrow="Cerca tuyo"
          title="Populares ahora"
          text="Bloque reutilizable para ranking, tendencias o recomendaciones futuras."
        />
        <EventGrid events={popularEvents} />
      </section>
    </>
  );
}

export default HomePage;
