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
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    getEventById(eventId)
      .then((result) => {
        setEvent(result);
        if (result?.tiposEntrada?.length) {
          setSelectedTicketId(result.tiposEntrada[0].id);
        }
      })
      .finally(() => setLoading(false));
  }, [eventId]);

  const selectedTicket = useMemo(() => {
    return event?.tiposEntrada.find((ticket) => ticket.id === selectedTicketId) || null;
  }, [event, selectedTicketId]);

  const totalCapacity = event?.cuposTotales || 100;
  const availableCount = event?.disponibilidad ?? 0;
  const soldCount = Math.max(0, totalCapacity - availableCount);
  const occupancyPercentage = Math.round((soldCount / (totalCapacity || 1)) * 100);
  const lowAvailability = event && availableCount > 0 && availableCount <= 50;

  function handleContinue() {
    if (!event || !selectedTicket) {
      return;
    }

    const safeQuantity = Math.min(quantity, event.disponibilidad);

    saveSelectedOrder({
      event,
      ticketType: selectedTicket,
      quantity: safeQuantity,
      total: selectedTicket.precio * safeQuantity
    });

    navigate("/checkout");
  }

  function handleQuantityChange(delta) {
    const max = Math.min(event?.disponibilidad || 6, 6);
    setQuantity((prev) => Math.max(1, Math.min(max, prev + delta)));
  }

  if (loading) {
    return (
      <section className="container page-block">
        <div className="skeleton skeleton-card" style={{ height: 450, margin: "40px 0" }} />
      </section>
    );
  }

  if (!event) {
    return (
      <section className="container page-block">
        <div className="empty-state">
          <p>No se encontró el evento solicitado.</p>
          <Link to="/buscar" className="pill-link" style={{ marginTop: 12 }}>
            Explorar otros eventos
          </Link>
        </div>
      </section>
    );
  }

  return (
    <section className="page-block">
      <div className="container event-detail-grid">
        <div className="event-hero-panel">
          <img src={event.imagen} alt={event.nombre} className="event-banner" />
          <div className="event-info-card">
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <p className="section-eyebrow">{event.categoria}</p>
              <span className="badge-status badge-success">
                ● {event.estado || "PUBLICADO"}
              </span>
            </div>

            <h1>{event.nombre}</h1>
            <p className="event-detail-artist">{event.artista}</p>

            <div className="event-detail-meta">
              <span>{formatDate(event.fecha)} · {event.hora}hs</span>
              <span>· {event.recinto}</span>
              <span>· {event.ciudad}</span>
            </div>

            <p className="event-description" style={{ lineHeight: 1.65, color: "var(--text-2)" }}>
              {event.descripcion}
            </p>

            <div className="tag-row">
              {event.etiquetas.map((tag) => (
                <span key={tag} className="event-tag">
                  {tag}
                </span>
              ))}
            </div>

            {/* Disponibilidad y Barra de Progreso */}
            <div className="capacity-tracker" style={{ marginTop: 24 }}>
              <div className="capacity-info">
                <span>Disponibilidad</span>
                <strong>{availableCount} de {totalCapacity} cupos restantes ({occupancyPercentage}% ocupado)</strong>
              </div>
              <div className="progress-track">
                <div
                  className={`progress-fill ${lowAvailability ? "low" : ""}`}
                  style={{ width: `${occupancyPercentage}%` }}
                />
              </div>
            </div>

            {lowAvailability ? (
              <div className="availability-alert">
                Últimos {availableCount} cupos disponibles para este espectáculo.
              </div>
            ) : null}
          </div>
        </div>

        <div className="ticket-panel">
          <div className="ticket-selector">
            <h2>Selecciona tus entradas</h2>
            <p style={{ color: "var(--text-2)", fontSize: "0.9rem", margin: "0 0 16px" }}>
              {availableCount > 0
                ? `${availableCount} entradas disponibles para venta inmediata.`
                : "Entradas agotadas para este espectáculo."}
            </p>

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

            <div style={{ margin: "20px 0" }}>
              <label style={{ display: "block", fontSize: "0.85rem", fontWeight: 700, marginBottom: 8, color: "var(--text-2)" }}>
                Cantidad de entradas
              </label>
              <div className="stepper-control">
                <button
                  type="button"
                  className="stepper-btn"
                  onClick={() => handleQuantityChange(-1)}
                  disabled={quantity <= 1}
                >
                  −
                </button>
                <span className="stepper-value">{quantity}</span>
                <button
                  type="button"
                  className="stepper-btn"
                  onClick={() => handleQuantityChange(1)}
                  disabled={quantity >= Math.min(availableCount, 6)}
                >
                  +
                </button>
              </div>
            </div>

            <div className="detail-actions" style={{ display: "grid", gap: 10, marginTop: 24 }}>
              <Button onClick={handleContinue} fullWidth disabled={availableCount <= 0}>
                {availableCount > 0 ? "Continuar al checkout →" : "Entradas Agotadas"}
              </Button>
              <Link to="/buscar" className="text-link" style={{ textAlign: "center" }}>
                ← Explorar otros eventos
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

