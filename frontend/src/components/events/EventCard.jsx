import { Link } from "react-router-dom";
import { formatDate, formatPrice } from "../../utils/formatters";

function EventCard({ event }) {
  return (
    <article className="event-card">
      <img src={event.imagen} alt={event.nombre} className="event-card-image" />
      <div className="event-card-body">
        <div className="event-card-top">
          <span className="event-tag">{event.categoria}</span>
          <span className="event-availability">{event.disponibilidad} disponibles</span>
        </div>
        <h3>{event.nombre}</h3>
        <p className="event-artist">{event.artista}</p>
        <p className="event-meta">
          {formatDate(event.fecha)} · {event.ciudad}
        </p>
        <p className="event-price">Desde {formatPrice(event.precioMinimo)}</p>
        <Link to={`/eventos/${event.id}`} className="event-link">
          Ver detalle
        </Link>
      </div>
    </article>
  );
}

export default EventCard;
