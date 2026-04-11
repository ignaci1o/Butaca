import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import Button from "../components/ui/Button";
import OrderSummary from "../components/checkout/OrderSummary";
import { getEventById } from "../services/eventService";
import { formatDate, formatPrice } from "../utils/formatters";
import { saveSelectedOrder } from "../utils/storage";

function EventDetailPage() {
  const { eventId } = useParams();
  const navigate = useNavigate();
  const [event, setEvent] = useState(null);
  const [selectedTicketId, setSelectedTicketId] = useState("");
  const [quantity, setQuantity] = useState(2);

  useEffect(() => {
    getEventById(eventId).then((result) => {
      setEvent(result);
      if (result?.tiposEntrada?.length) {
        setSelectedTicketId(result.tiposEntrada[0].id);
      }
    });
  }, [eventId]);

  const selectedTicket = useMemo(() => {
    return event?.tiposEntrada.find((ticket) => ticket.id === selectedTicketId) || null;
  }, [event, selectedTicketId]);

  const lowAvailability = event && event.disponibilidad <= 120;

  function handleContinue() {
    if (!event || !selectedTicket) {
      return;
    }

    const subtotal = selectedTicket.precio * quantity;
    const serviceFee = Math.round(subtotal * 0.12);

    saveSelectedOrder({
      event,
      ticketType: selectedTicket,
      quantity,
      total: subtotal + serviceFee
    });

    navigate("/checkout");
  }

  if (!event) {
    return <section className="container page-block">Cargando detalle del evento...</section>;
  }

  return (
    <section className="page-block">
      <div className="container event-detail-grid">
        <div className="event-hero-panel">
          <img src={event.imagen} alt={event.nombre} className="event-banner" />
          <div className="event-info-card">
            <p className="section-eyebrow">{event.categoria}</p>
            <h1>{event.nombre}</h1>
            <p className="event-detail-artist">{event.artista}</p>

            <div className="event-detail-meta">
              <span>{formatDate(event.fecha)}</span>
              <span>{event.hora}</span>
              <span>{event.recinto}</span>
              <span>{event.ciudad}</span>
            </div>

            <p className="event-description">{event.descripcion}</p>

            <div className="tag-row">
              {event.etiquetas.map((tag) => (
                <span key={tag} className="event-tag">
                  {tag}
                </span>
              ))}
            </div>

            {lowAvailability ? (
              <div className="availability-alert">
                Quedan pocas entradas para este evento. Conviene usar este bloque para urgencia de
                compra.
              </div>
            ) : null}
          </div>
        </div>

        <div className="ticket-panel">
          <div className="ticket-selector">
            <h2>Selecciona tus entradas</h2>
            <p>Bloque ideal para luego cargar sectores reales desde tu backend.</p>

            <div className="ticket-options">
              {event.tiposEntrada.map((ticket) => (
                <button
                  key={ticket.id}
                  type="button"
                  className={selectedTicketId === ticket.id ? "ticket-option active" : "ticket-option"}
                  onClick={() => setSelectedTicketId(ticket.id)}
                >
                  <span>{ticket.nombre}</span>
                  <strong>{formatPrice(ticket.precio)}</strong>
                </button>
              ))}
            </div>

            <label className="quantity-field">
              Cantidad
              <select value={quantity} onChange={(event) => setQuantity(Number(event.target.value))}>
                <option value={1}>1</option>
                <option value={2}>2</option>
                <option value={3}>3</option>
                <option value={4}>4</option>
              </select>
            </label>

            <div className="detail-actions">
              <Button onClick={handleContinue} fullWidth>
                Continuar al checkout
              </Button>
              <Link to="/buscar" className="text-link">
                Seguir explorando
              </Link>
            </div>
          </div>

          <OrderSummary event={event} ticketType={selectedTicket} quantity={quantity} />
        </div>
      </div>
    </section>
  );
}

export default EventDetailPage;
