import { Link } from "react-router-dom";
import { formatDate, formatPrice } from "../../utils/formatters";

function EventCard({ event }) {
  return (
    <article className="event-card">
      <div style={{ overflow: "hidden" }}>
        <img src={event.imagen} alt={event.nombre} className="event-card-image" />
      </div>
      <div className="event-card-body">
        <div className="event-card-top">
          <span className="event-tag">{event.categoria}</span>
          <span className="event-availability">{event.disponibilidad} disponibles</span>
        </div>
        <h3>{event.nombre}</h3>
        <p className="event-artist">{event.artista}</p>
        <p className="event-meta">
          📅 {formatDate(event.fecha)} &nbsp;·&nbsp; 📍 {event.ciudad}
        </p>
        <p className="event-price">Desde {formatPrice(event.precioMinimo)}</p>
        <Link to={`/eventos/${event.id}`} className="event-link">
          Ver entradas
        </Link>
      </div>
    </article>
  );
}

export default EventCard;
