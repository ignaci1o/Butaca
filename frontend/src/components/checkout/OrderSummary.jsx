import { formatPrice } from "../../utils/formatters";

function OrderSummary({ event, ticketType, quantity }) {
  if (!event || !ticketType) {
    return null;
  }

  const subtotal = ticketType.precio * quantity;
  const serviceFee = Math.round(subtotal * 0.12);
  const total = subtotal + serviceFee;

  return (
    <aside className="order-summary">
      <p className="summary-eyebrow">Resumen del pedido</p>
      <h3>{event.nombre}</h3>
      <p>{ticketType.nombre}</p>
      <div className="summary-line">
        <span>Entradas</span>
        <span>
          {quantity} x {formatPrice(ticketType.precio)}
        </span>
      </div>
      <div className="summary-line">
        <span>Cargo de servicio</span>
        <span>{formatPrice(serviceFee)}</span>
      </div>
      <div className="summary-line total">
        <span>Total</span>
        <span>{formatPrice(total)}</span>
      </div>
    </aside>
  );
}

export default OrderSummary;
