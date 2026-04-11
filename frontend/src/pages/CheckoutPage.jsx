import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Button from "../components/ui/Button";
import OrderSummary from "../components/checkout/OrderSummary";
import { createOrder } from "../services/orderService";
import { clearSelectedOrder, getSelectedOrder, saveSelectedOrder } from "../utils/storage";

function CheckoutPage() {
  const navigate = useNavigate();
  const order = getSelectedOrder();
  const [step, setStep] = useState(1);
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    nombre: "",
    email: "",
    documento: "",
    tarjeta: "",
    vencimiento: "",
    seguridad: ""
  });

  if (!order) {
    return <section className="container page-block">No hay una compra seleccionada todavia.</section>;
  }

  function handleChange(event) {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value
    });
  }

  async function handleSubmit(event) {
    event.preventDefault();

    if (step === 1) {
      setStep(2);
      return;
    }

    setLoading(true);

    try {
      const result = await createOrder({
        comprador: {
          nombre: formData.nombre,
          email: formData.email,
          documento: formData.documento
        },
        eventoId: order.event.id,
        tipoEntrada: order.ticketType.id,
        cantidad: order.quantity,
        total: order.total
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
          <h1>{step === 1 ? "Tus datos" : "Pago"}</h1>
          <p>
            Flujo simple en dos pasos. Mas adelante podes separarlo en componentes o conectarlo con
            validaciones reales.
          </p>

          {step === 1 ? (
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
          ) : (
            <div className="form-grid">
              <label>
                Numero de tarjeta
                <input name="tarjeta" value={formData.tarjeta} onChange={handleChange} required />
              </label>
              <label>
                Vencimiento
                <input name="vencimiento" placeholder="MM/AA" value={formData.vencimiento} onChange={handleChange} required />
              </label>
              <label>
                Codigo de seguridad
                <input name="seguridad" value={formData.seguridad} onChange={handleChange} required />
              </label>
            </div>
          )}

          <div className="checkout-actions">
            {step === 2 ? (
              <Button type="button" variant="ghost" onClick={() => setStep(1)}>
                Volver
              </Button>
            ) : (
              <Button type="button" variant="ghost" onClick={handleCancel}>
                Cancelar
              </Button>
            )}

            <Button type="submit" disabled={loading}>
              {loading ? "Procesando..." : step === 1 ? "Continuar" : "Confirmar compra"}
            </Button>
          </div>
        </form>

        <OrderSummary event={order.event} ticketType={order.ticketType} quantity={order.quantity} />
      </div>
    </section>
  );
}

export default CheckoutPage;
