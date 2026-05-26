import { useEffect, useState } from "react";
import { getUserNotifications, getUserOrders, markNotificationRead } from "../services/orderService";
import { formatDate, formatPrice } from "../utils/formatters";

function ClienteDashboard({ currentUser }) {
  const [orders, setOrders] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getUserOrders(currentUser.id), getUserNotifications(currentUser.id)])
      .then(([ordersData, notificationsData]) => {
        setOrders(ordersData);
        setNotifications(notificationsData);
      })
      .catch((loadError) => setError(loadError.message));
  }, [currentUser.id]);

  async function handleRead(notificationId) {
    const updated = await markNotificationRead(notificationId);
    setNotifications((items) =>
      items.map((notification) =>
        notification.id_notificacion === notificationId ? updated : notification
      )
    );
  }

  return (
    <section className="container page-block">
      <div className="dashboard-header">
        <div>
          <p className="section-eyebrow">Cliente</p>
          <h1>Mis entradas</h1>
        </div>
      </div>

      {error ? <div className="auth-error">{error}</div> : null}

      <div className="dashboard-grid">
        <section className="dashboard-panel">
          <h2>Compras</h2>
          <div className="table-list">
            {orders.length ? orders.map((order) => (
              <div key={order.id}>
                <strong>{order.evento_titulo}</strong>
                <span>{formatDate(order.fecha_compra)} · {order.cantidad} entrada(s) · {formatPrice(order.total)}</span>
              </div>
            )) : <p className="empty-inline">Todavia no hay compras registradas.</p>}
          </div>
        </section>

        <section className="dashboard-panel">
          <h2>Notificaciones</h2>
          <div className="table-list">
            {notifications.length ? notifications.map((notification) => (
              <div key={notification.id_notificacion}>
                <strong>{notification.tipo || "INFO"}</strong>
                <span>{notification.mensaje}</span>
                {!notification.leida ? (
                  <button type="button" className="text-link" onClick={() => handleRead(notification.id_notificacion)}>
                    Marcar leida
                  </button>
                ) : null}
              </div>
            )) : <p className="empty-inline">No hay notificaciones.</p>}
          </div>
        </section>
      </div>
    </section>
  );
}

export default ClienteDashboard;
