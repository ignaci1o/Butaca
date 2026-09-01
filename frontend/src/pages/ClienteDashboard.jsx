import { useEffect, useState } from "react";
import { getUserOrders } from "../services/orderService";
import { formatDate, formatPrice } from "../utils/formatters";
import TicketPass from "../components/events/TicketPass";

function ClienteDashboard({ currentUser }) {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!currentUser?.id) return;
    setLoading(true);
    getUserOrders(currentUser.id)
      .then((ordersData) => {
        setOrders(ordersData || []);
      })
      .catch((loadError) => setError(loadError.message))
      .finally(() => setLoading(false));
  }, [currentUser?.id]);

  return (
    <section className="container page-block">
      <div className="dashboard-header" style={{ marginBottom: 28 }}>
        <div>
          <p className="section-eyebrow">Mi Cuenta</p>
          <h1>Mis Pedidos</h1>
        </div>
      </div>

      {error ? <div className="auth-error">{error}</div> : null}

      {loading ? (
        <div style={{ display: "grid", gap: 16 }}>
          <div className="skeleton skeleton-card" />
          <div className="skeleton skeleton-card" />
        </div>
      ) : orders.length ? (
        <div style={{ display: "grid", gap: 24 }}>
          {orders.map((order) => (
            <div
              key={order.id}
              style={{
                background: "var(--surface)",
                border: "1px solid var(--border)",
                borderRadius: "var(--r-md)",
                padding: "24px"
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  flexWrap: "wrap",
                  gap: 12,
                  marginBottom: 18,
                  borderBottom: "1px solid var(--border)",
                  paddingBottom: 14
                }}
              >
                <div>
                  <h3 style={{ fontSize: "1.2rem", fontWeight: 700, margin: 0, color: "var(--text)" }}>
                    {order.evento_titulo}
                  </h3>
                  <div style={{ fontSize: "0.85rem", color: "var(--text-2)", marginTop: 4 }}>
                    Pedido #{order.id} · {formatDate(order.fecha_compra)}
                  </div>
                </div>

                <div style={{ textAlign: "right" }}>
                  <div style={{ fontSize: "0.8rem", color: "var(--text-2)" }}>Total pagado</div>
                  <strong style={{ fontSize: "1.15rem", color: "var(--primary-2)", fontWeight: 800 }}>
                    {formatPrice(order.total)}
                  </strong>
                </div>
              </div>

              <div style={{ display: "grid", gap: 12 }}>
                {(order.entradas && order.entradas.length > 0) ? (
                  order.entradas.map((entrada, idx) => (
                    <TicketPass
                      key={entrada.id_entrada || idx}
                      ticket={entrada}
                      eventTitle={order.evento_titulo}
                      date={order.fecha_compra}
                      buyerName={currentUser?.nombre ? `${currentUser.nombre} ${currentUser.apellido || ""}`.trim() : "Cliente"}
                    />
                  ))
                ) : (
                  <p className="empty-inline">No se encontraron tickets individuales para este pedido.</p>
                )}
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="empty-state">
          <p>No tienes compras registradas todavía.</p>
        </div>
      )}
    </section>
  );
}

export default ClienteDashboard;
