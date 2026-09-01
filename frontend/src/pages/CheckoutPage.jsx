import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Button from "../components/ui/Button";
import OrderSummary from "../components/checkout/OrderSummary";
import { createOrder } from "../services/orderService";
import { clearSelectedOrder, getSelectedOrder, saveSelectedOrder } from "../utils/storage";

function CheckoutPage({ currentUser }) {
  const navigate = useNavigate();
  const order = getSelectedOrder();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    nombre: currentUser ? `${currentUser.nombre} ${currentUser.apellido || ""}`.trim() : "",
    email: currentUser?.email || "",
    documento: ""
  });

  if (!order) {
    return <section className="container page-block">No hay una compra seleccionada todavia.</section>;
  }

  if (!currentUser) {
    return (
      <section className="container page-block">
        <div className="empty-state">
          <p>Necesitas iniciar sesion para confirmar la compra.</p>
          <Link to="/login" className="pill-link">Ingresar</Link>
        </div>
      </section>
    );
  }

  function handleChange(event) {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value
    });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);

    try {
      const result = await createOrder({
        comprador: {
          nombre: formData.nombre,
          email: formData.email,
          documento: formData.documento
        },
        eventoId: order.event.id,
        usuarioId: currentUser.id,
        tipoEntrada: order.ticketType.id,
        cantidad: order.quantity
      });

      saveSelectedOrder({
        ...order,
        confirmation: result
      });
      navigate("/confirmacion");
    } catch (error) {
      window.alert(error.message);
    } finally {
      setLoading(false);
    }
  }

  function handleCancel() {
    clearSelectedOrder();
    navigate("/");
  }

  return (
    <section className="container page-block">
      <div className="checkout-layout">
        <form className="checkout-form" onSubmit={handleSubmit}>
          <p className="section-eyebrow">Checkout</p>
          <h1>Confirmar compra</h1>
          <p>El pago es simulado para la demostracion académica. No se solicitan datos de tarjeta.</p>

          <div className="form-grid">
            <label>
              Nombre completo
              <input name="nombre" value={formData.nombre} onChange={handleChange} required />
            </label>
            <label>
              Correo electronico
              <input name="email" type="email" value={formData.email} onChange={handleChange} required />
            </label>
            <label>
              Documento
              <input name="documento" value={formData.documento} onChange={handleChange} required />
            </label>
          </div>

          <div className="checkout-actions">
            <Button type="button" variant="ghost" onClick={handleCancel}>
              Cancelar
            </Button>

            <Button type="submit" disabled={loading}>
              {loading ? "Procesando..." : "Confirmar compra"}
            </Button>
          </div>
        </form>

        <OrderSummary event={order.event} ticketType={order.ticketType} quantity={order.quantity} />
      </div>
    </section>
  );
}

export default CheckoutPage;
