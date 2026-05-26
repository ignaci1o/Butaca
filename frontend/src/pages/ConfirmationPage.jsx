import { Link } from "react-router-dom";
import Button from "../components/ui/Button";
import { getSelectedOrder } from "../utils/storage";
import { formatPrice } from "../utils/formatters";

function ConfirmationPage() {
  const order = getSelectedOrder();
  const confirmation = order?.confirmation;

  if (!order || !confirmation) {
    return <section className="container page-block">No hay una confirmacion para mostrar.</section>;
  }

  return (
    <section className="container page-block confirmation-page">
      <div className="confirmation-card">
        <p className="section-eyebrow">Compra confirmada</p>
        <h1>Tu pedido ya esta registrado en Butaca.</h1>
        <p>
          Conserva el codigo unico de cada entrada para la validacion de acceso.
        </p>

        <div className="confirmation-code">{confirmation.codigoReserva}</div>

        <div className="confirmation-details">
          <div>
            <span>Evento</span>
            <strong>{order.event.nombre}</strong>
          </div>
          <div>
            <span>Comprador</span>
            <strong>{confirmation.comprador.nombre}</strong>
          </div>
          <div>
            <span>Total</span>
            <strong>{formatPrice(order.total)}</strong>
          </div>
        </div>

        <div className="ticket-code-list">
          {(confirmation.entradas || []).map((entrada) => (
            <span key={entrada.id_entrada}>{entrada.codigo_unico_entrada}</span>
          ))}
        </div>

        <div className="hero-actions">
          <Link to="/">
            <Button>Volver al inicio</Button>
          </Link>
          <Link to="/buscar">
            <Button variant="secondary">Explorar mas eventos</Button>
          </Link>
        </div>
      </div>
    </section>
  );
}

export default ConfirmationPage;
