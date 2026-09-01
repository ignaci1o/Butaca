import { formatPrice } from "../../utils/formatters";

function OrderSummary({ event, ticketType, quantity }) {
  if (!event || !ticketType) {
    return null;
  }

  const total = ticketType.precio * quantity;

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
      <div className="summary-line total">
        <span>Total</span>
        <span>{formatPrice(total)}</span>
      </div>
    </aside>
  );
}

export default OrderSummary;
