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
  const [filterStatus, setFilterStatus] = useState("TODOS");
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);
  const [error, setError] = useState("");

  const organizerId = currentUser?.id;

  useEffect(() => {
    loadDashboard();
  }, [organizerId]);

  const totals = useMemo(() => {
    const capacity = events.reduce((sum, event) => sum + (event.cuposTotales || 0), 0);
    const available = events.reduce((sum, event) => sum + (event.disponibilidad || 0), 0);
    const sold = Math.max(0, capacity - available);
    return {
      events: events.length,
      sold,
      revenue: orders.reduce((sum, order) => sum + Number(order.total || 0), 0)
    };
  }, [events, orders]);

  const filteredEvents = useMemo(() => {
    if (filterStatus === "TODOS") return events;
    return events.filter((e) => e.estado === filterStatus);
  }, [events, filterStatus]);

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
      setEvents(eventData || []);
      setOrders(orderData || []);
      setCategories(categoryData || []);
      setLocations(locationData || []);
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
    setActionLoading(true);

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
    } finally {
      setActionLoading(false);
    }
  }

  async function handlePublish(eventId) {
    try {
      setActionLoading(true);
      await publishEvent(eventId);
      await loadDashboard();
    } catch (err) {
      setError(err.message);
    } finally {
      setActionLoading(false);
    }
  }

  async function handleCancel(eventId) {
    if (!window.confirm("¿Seguro que deseas cancelar este evento? Las entradas confirmadas se anularán.")) {
      return;
    }
    try {
      setActionLoading(true);
      await cancelEvent(eventId);
      await loadDashboard();
    } catch (err) {
      setError(err.message);
    } finally {
      setActionLoading(false);
    }
  }

  if (!currentUser || !["ORGANIZADOR", "ADMIN"].includes(currentUser.rol)) {
    return (
      <section className="container page-block">
        <div className="empty-state">No cuenta con permisos para ver este panel.</div>
      </section>
    );
  }

  return (
    <section className="container page-block">
      <div className="dashboard-header">
        <div>
          <p className="section-eyebrow">Panel de Gestión</p>
          <h1>Eventos y Ventas</h1>
        </div>
        <button type="button" className="pill-link" onClick={() => setShowForm((value) => !value)}>
          {showForm ? "Cerrar formulario" : "+ Nuevo evento"}
        </button>
      </div>

      {error ? <div className="auth-error">{error}</div> : null}

      <div className="stats-grid">
        <div className="stat-card">
          <span>Eventos Creados</span>
          <strong>{totals.events}</strong>
        </div>
        <div className="stat-card">
          <span>Entradas Vendidas</span>
          <strong>{totals.sold}</strong>
        </div>
        <div className="stat-card">
          <span>Facturación Registrada</span>
          <strong>{formatPrice(totals.revenue)}</strong>
        </div>
      </div>

      {showForm ? (
        <form className="dashboard-panel event-form" onSubmit={handleSubmit} style={{ animation: "fadeIn 0.3s" }}>
          <h2>Crear Nuevo Evento</h2>
          <p style={{ color: "var(--text-2)", fontSize: "0.85rem", margin: "0 0 18px" }}>
            El evento se registrará en estado <strong>BORRADOR</strong>. Podrá publicarlo una vez verificados los datos.
          </p>

          <div className="form-grid two-columns">
            <label>
              Título del evento
              <input name="titulo_evento" value={formData.titulo_evento} onChange={handleChange} placeholder="Ej: Recital Acústico" required />
            </label>
            <label>
              Categoría
              <select name="categoria_id" value={formData.categoria_id} onChange={handleChange} required>
                {categories.map((category) => (
                  <option key={category.id} value={category.id}>
                    {category.nombre}
                  </option>
                ))}
              </select>
            </label>
            <label className="span-2">
              Descripción
              <textarea name="descripcion_evento" value={formData.descripcion_evento} onChange={handleChange} rows="3" placeholder="Detalles de la función, bandas o temario..." />
            </label>
            <label>
              Ubicación física
              <select name="ubicacion_id" value={formData.ubicacion_id} onChange={handleChange} required>
                {locations.map((location) => (
                  <option key={location.id} value={location.id}>
                    {location.nombre} · Capacidad: {location.capacidadMaxima}
                  </option>
                ))}
              </select>
            </label>
            <label>
              URL de imagen de portada
              <input name="imagen_url" value={formData.imagen_url} onChange={handleChange} placeholder="https://images.unsplash.com/..." />
            </label>
            <label>
              Fecha del evento
              <input type="date" name="fecha_evento" value={formData.fecha_evento} onChange={handleChange} required />
            </label>
            <label>
              Hora
              <input type="time" name="hora_evento" value={formData.hora_evento} onChange={handleChange} required />
            </label>
            <label>
              Precio por entrada ($)
              <input type="number" min="0" step="0.01" name="precio_entrada_evento" value={formData.precio_entrada_evento} onChange={handleChange} placeholder="Ej: 12000" required />
            </label>
            <label>
              Cupos totales disponibles
              <input type="number" min="1" name="cupos_totales_evento" value={formData.cupos_totales_evento} onChange={handleChange} placeholder="Ej: 200" required />
            </label>
          </div>

          <div className="form-actions-row" style={{ gap: 10 }}>
            <button type="button" className="button button-ghost" onClick={() => setShowForm(false)}>
              Cancelar
            </button>
            <button type="submit" className="button button-primary" disabled={actionLoading}>
              {actionLoading ? "Guardando..." : "Guardar borrador"}
            </button>
          </div>
        </form>
      ) : null}

      {loading ? (
        <div className="dashboard-grid">
          <div className="skeleton skeleton-card" />
          <div className="skeleton skeleton-card" />
        </div>
      ) : (
        <div className="dashboard-grid">
          <section className="dashboard-panel">
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 16 }}>
              <h2>Mis Eventos ({filteredEvents.length})</h2>
              <div style={{ display: "flex", gap: 6 }}>
                {["TODOS", "PUBLICADO", "BORRADOR", "CANCELADO"].map((status) => (
                  <button
                    key={status}
                    type="button"
                    className={`btn-copy ${filterStatus === status ? "copied" : ""}`}
                    onClick={() => setFilterStatus(status)}
                  >
                    {status}
                  </button>
                ))}
              </div>
            </div>

            <div className="table-list">
              {filteredEvents.length ? (
                filteredEvents.map((event) => {
                  const total = event.cuposTotales || 1;
                  const avail = event.disponibilidad ?? 0;
                  const sold = Math.max(0, total - avail);
                  const occupancy = Math.round((sold / total) * 100);

                  const badgeClass =
                    event.estado === "PUBLICADO"
                      ? "badge-success"
                      : event.estado === "BORRADOR"
                      ? "badge-warning"
                      : "badge-danger";

                  return (
                    <div key={event.id}>
                      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                        <strong style={{ fontSize: "1.05rem" }}>{event.nombre}</strong>
                        <span className={`badge-status ${badgeClass}`}>
                          {event.estado}
                        </span>
                      </div>

                      <span>
                        {formatDate(event.fecha)} · {event.hora}hs · {event.recinto || event.ciudad} · {formatPrice(event.precioMinimo)}
                      </span>

                      {/* Barra de ocupación */}
                      <div className="capacity-tracker" style={{ margin: "6px 0" }}>
                        <div className="capacity-info" style={{ fontSize: "0.75rem" }}>
                          <span>Ocupación</span>
                          <strong>{sold} de {total} ({occupancy}%)</strong>
                        </div>
                        <div className="progress-track" style={{ height: 6 }}>
                          <div
                            className="progress-fill"
                            style={{ width: `${occupancy}%` }}
                          />
                        </div>
                      </div>

                      <div className="row-actions">
                        {event.estado === "BORRADOR" ? (
                          <button
                            type="button"
                            className="btn-copy"
                            onClick={() => handlePublish(event.id)}
                            disabled={actionLoading}
                          >
                            Publicar evento
                          </button>
                        ) : null}
                        {event.estado !== "CANCELADO" ? (
                          <button
                            type="button"
                            className="btn-copy"
                            style={{ color: "#f87171" }}
                            onClick={() => handleCancel(event.id)}
                            disabled={actionLoading}
                          >
                            Cancelar evento
                          </button>
                        ) : null}
                      </div>
                    </div>
                  );
                })
              ) : (
                <p className="empty-inline">No hay eventos registrados para este filtro.</p>
              )}
            </div>
          </section>

          <section className="dashboard-panel">
            <h2>Últimas Ventas Registradas</h2>
            <div className="table-list">
              {orders.length ? (
                orders.slice(0, 10).map((order) => (
                  <div key={order.id} style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <div>
                      <strong>{order.evento_titulo}</strong>
                      <span>
                        {order.cliente_email} · {order.cantidad} entrada(s)
                      </span>
                      <small style={{ color: "var(--text-3)", display: "block" }}>
                        {formatDate(order.fecha_compra)}
                      </small>
                    </div>
                    <strong style={{ color: "var(--primary-2)", fontSize: "1.05rem" }}>
                      {formatPrice(order.total)}
                    </strong>
                  </div>
                ))
              ) : (
                <p className="empty-inline">Aún no se registran compras para sus eventos.</p>
              )}
            </div>
          </section>
        </div>
      )}
    </section>
  );
}

export default OrganizerDashboard;
