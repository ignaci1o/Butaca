import { Link } from "react-router-dom";
import { formatDate, formatPrice } from "../../utils/formatters";

function EventCard({ event }) {
  const isLowStock = event.disponibilidad > 0 && event.disponibilidad <= 50;

  return (
    <article className="event-card">
      <Link to={`/eventos/${event.id}`} style={{ display: "block", overflow: "hidden" }}>
        <img src={event.imagen} alt={event.nombre} className="event-card-image" loading="lazy" />
      </Link>
      <div className="event-card-body">
        <div className="event-card-top">
          <span className="event-tag">{event.categoria}</span>
          <span
            className="event-availability"
            style={{
              borderColor: isLowStock ? "rgba(245,158,11,0.4)" : "var(--border)",
              color: isLowStock ? "#fbbf24" : "var(--text-2)"
            }}
          >
            {isLowStock ? `Últimos ${event.disponibilidad} cupos` : `${event.disponibilidad} disponibles`}
          </span>
        </div>
        <h3>{event.nombre}</h3>
        <p className="event-artist">{event.artista}</p>
        <p className="event-meta">
          {formatDate(event.fecha)} &nbsp;·&nbsp; {event.recinto || event.ciudad}
        </p>
        <p className="event-price">Desde {formatPrice(event.precioMinimo)}</p>
        <Link to={`/eventos/${event.id}`} className="event-link">
          Ver entradas y comprar
        </Link>
      </div>
    </article>
  );
}

export default EventCard;
