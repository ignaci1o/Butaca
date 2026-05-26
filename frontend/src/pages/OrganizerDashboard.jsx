import { useEffect, useMemo, useState } from "react";
import {
  cancelEvent,
  createOrganizerEvent,
  getCategories,
  getLocations,
  getOrganizerEvents,
  publishEvent
} from "../services/eventService";
import { getOrganizerOrders } from "../services/orderService";
import { formatDate, formatPrice } from "../utils/formatters";

const initialForm = {
  titulo_evento: "",
  descripcion_evento: "",
  fecha_evento: "",
  hora_evento: "",
  precio_entrada_evento: "",
  cupos_totales_evento: "",
  categoria_id: "",
  ubicacion_id: "",
  imagen_url: ""
};

function OrganizerDashboard({ currentUser }) {
  const [events, setEvents] = useState([]);
  const [orders, setOrders] = useState([]);
  const [categories, setCategories] = useState([]);
  const [locations, setLocations] = useState([]);
  const [formData, setFormData] = useState(initialForm);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const organizerId = currentUser?.id;

  useEffect(() => {
    loadDashboard();
  }, [organizerId]);

  const totals = useMemo(() => {
    const capacity = events.reduce((sum, event) => sum + event.cuposTotales, 0);
    const available = events.reduce((sum, event) => sum + event.disponibilidad, 0);
    return {
      events: events.length,
      sold: capacity - available,
      revenue: orders.reduce((sum, order) => sum + Number(order.total || 0), 0)
    };
  }, [events, orders]);

  async function loadDashboard() {
    if (!organizerId) {
      return;
    }

    setLoading(true);
    setError("");
    try {
      const [eventData, orderData, categoryData, locationData] = await Promise.all([
        getOrganizerEvents(organizerId),
        getOrganizerOrders(organizerId),
        getCategories(),
        getLocations()
      ]);
      setEvents(eventData);
      setOrders(orderData);
      setCategories(categoryData);
      setLocations(locationData);
      setFormData((current) => ({
        ...current,
        categoria_id: current.categoria_id || categoryData[0]?.id || "",
        ubicacion_id: current.ubicacion_id || locationData[0]?.id || ""
      }));
    } catch (loadError) {
      setError(loadError.message);
    } finally {
      setLoading(false);
    }
  }

  function handleChange(event) {
    const { name, value } = event.target;
    setFormData((current) => ({ ...current, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");

    try {
      await createOrganizerEvent({
        ...formData,
        categoria_id: Number(formData.categoria_id),
        ubicacion_id: Number(formData.ubicacion_id),
        precio_entrada_evento: Number(formData.precio_entrada_evento),
        cupos_totales_evento: Number(formData.cupos_totales_evento),
        organizador_id_evento: organizerId,
        estado_publicacion_evento: "BORRADOR"
      });
      setFormData({
        ...initialForm,
        categoria_id: categories[0]?.id || "",
        ubicacion_id: locations[0]?.id || ""
      });
      setShowForm(false);
      await loadDashboard();
    } catch (submitError) {
      setError(submitError.message);
    }
  }

  async function handlePublish(eventId) {
    await publishEvent(eventId);
    await loadDashboard();
  }

  async function handleCancel(eventId) {
    await cancelEvent(eventId);
    await loadDashboard();
  }

  if (!currentUser || !["ORGANIZADOR", "ADMIN"].includes(currentUser.rol)) {
    return (
      <section className="container page-block">
        <div className="empty-state">No tenes permisos para ver este panel.</div>
      </section>
    );
  }

  return (
    <section className="container page-block">
      <div className="dashboard-header">
        <div>
          <p className="section-eyebrow">Organizador</p>
          <h1>Eventos y ventas</h1>
        </div>
        <button type="button" className="pill-link" onClick={() => setShowForm((value) => !value)}>
          {showForm ? "Cerrar" : "Nuevo evento"}
        </button>
      </div>

      {error ? <div className="auth-error">{error}</div> : null}

      <div className="stats-grid">
        <div className="stat-card">
          <span>Eventos</span>
          <strong>{totals.events}</strong>
        </div>
        <div className="stat-card">
          <span>Entradas vendidas</span>
          <strong>{totals.sold}</strong>
        </div>
        <div className="stat-card">
          <span>Facturacion registrada</span>
          <strong>{formatPrice(totals.revenue)}</strong>
        </div>
      </div>

      {showForm ? (
        <form className="dashboard-panel event-form" onSubmit={handleSubmit}>
          <h2>Nuevo evento</h2>
          <div className="form-grid two-columns">
            <label>
              Titulo
              <input name="titulo_evento" value={formData.titulo_evento} onChange={handleChange} required />
            </label>
            <label>
              Categoria
              <select name="categoria_id" value={formData.categoria_id} onChange={handleChange} required>
                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.nombre}
                  </option>
                ))}
              </select>
            </label>
            <label className="span-2">
              Descripcion
              <textarea name="descripcion_evento" value={formData.descripcion_evento} onChange={handleChange} rows="3" />
            </label>
            <label>
              Ubicacion
              <select name="ubicacion_id" value={formData.ubicacion_id} onChange={handleChange} required>
                {locations.map((location) => (
                  <option key={location.id} value={location.id}>
                    {location.nombre} · {location.capacidadMaxima}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Imagen URL
              <input name="imagen_url" value={formData.imagen_url} onChange={handleChange} />
            </label>
            <label>
              Fecha
              <input type="date" name="fecha_evento" value={formData.fecha_evento} onChange={handleChange} required />
            </label>
            <label>
              Hora
              <input type="time" name="hora_evento" value={formData.hora_evento} onChange={handleChange} required />
            </label>
            <label>
              Precio
              <input type="number" min="0" step="0.01" name="precio_entrada_evento" value={formData.precio_entrada_evento} onChange={handleChange} required />
            </label>
            <label>
              Cupos
              <input type="number" min="1" name="cupos_totales_evento" value={formData.cupos_totales_evento} onChange={handleChange} required />
            </label>
          </div>
          <div className="form-actions-row">
            <button type="submit" className="button button-primary">
              Guardar borrador
            </button>
          </div>
        </form>
      ) : null}

      {loading ? (
        <p className="empty-state">Cargando panel...</p>
      ) : (
        <div className="dashboard-grid">
          <section className="dashboard-panel">
            <h2>Eventos</h2>
            <div className="table-list">
              {events.length ? events.map((event) => (
                <div key={event.id}>
                  <strong>{event.nombre}</strong>
                  <span>{formatDate(event.fecha)} · {event.hora} · {event.disponibilidad}/{event.cuposTotales} cupos · {event.estado}</span>
                  <div className="row-actions">
                    {event.estado === "BORRADOR" ? (
                      <button type="button" className="text-link" onClick={() => handlePublish(event.id)}>
                        Publicar
                      </button>
                    ) : null}
                    {event.estado !== "CANCELADO" ? (
                      <button type="button" className="text-link" onClick={() => handleCancel(event.id)}>
                        Cancelar
                      </button>
                    ) : null}
                  </div>
                </div>
              )) : <p className="empty-inline">No hay eventos cargados.</p>}
            </div>
          </section>

          <section className="dashboard-panel">
            <h2>Compras recibidas</h2>
            <div className="table-list">
              {orders.length ? orders.slice(0, 8).map((order) => (
                <div key={order.id}>
                  <strong>{order.evento_titulo}</strong>
                  <span>{order.cliente_email || order.usuario_email} · {order.cantidad} entrada(s) · {formatPrice(order.total)}</span>
                </div>
              )) : <p className="empty-inline">No hay compras para tus eventos.</p>}
            </div>
          </section>
        </div>
      )}
    </section>
  );
}

export default OrganizerDashboard;
