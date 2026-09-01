import { useEffect, useState } from "react";
import { getAdminStats, getCategories, getLocations } from "../services/eventService";

function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [categories, setCategories] = useState([]);
  const [locations, setLocations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    setLoading(true);
    Promise.all([getAdminStats(), getCategories(), getLocations()])
      .then(([statsData, categoryData, locationData]) => {
        setStats(statsData || {});
        setCategories(categoryData || []);
        setLocations(locationData || []);
      })
      .catch((loadError) => setError(loadError.message))
      .finally(() => setLoading(false));
  }, []);

  const statLabels = {
    total_usuarios: "Usuarios Registrados",
    total_eventos: "Eventos Creados",
    total_compras: "Compras Procesadas",
    total_entradas: "Tickets Emitidos",
    total_recaudacion: "Recaudación Total"
  };

  return (
    <section className="container page-block">
      <div className="dashboard-header">
        <div>
          <p className="section-eyebrow">Administración del Sistema</p>
          <h1>Panel de Control Global</h1>
        </div>
      </div>

      {error ? <div className="auth-error">{error}</div> : null}

      {loading ? (
        <div className="dashboard-grid">
          <div className="skeleton skeleton-card" />
          <div className="skeleton skeleton-card" />
        </div>
      ) : (
        <>
          <div className="stats-grid">
            {Object.entries(stats || {}).map(([key, value]) => {
              const label = statLabels[key] || key.replaceAll("_", " ");
              return (
                <div className="stat-card" key={key}>
                  <span>{label}</span>
                  <strong>{typeof value === "number" && key.includes("recaudacion") ? `$${value.toLocaleString("es-AR")}` : value}</strong>
                </div>
              );
            })}
          </div>

          <div className="dashboard-grid">
            <section className="dashboard-panel">
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 14 }}>
                <h2>Categorías del Catálogo ({categories.length})</h2>
              </div>
              <div className="table-list">
                {categories.map((category) => (
                  <div key={category.id} style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <div>
                      <strong>{category.nombre}</strong>
                      <span>{category.descripcion || "Sin descripción"}</span>
                    </div>
                    <span className="badge-status badge-info">ID #{category.id}</span>
                  </div>
                ))}
              </div>
            </section>

            <section className="dashboard-panel">
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 14 }}>
                <h2>Recintos y Ubicaciones ({locations.length})</h2>
              </div>
              <div className="table-list">
                {locations.map((location) => (
                  <div key={location.id} style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <div>
                      <strong>{location.nombre}</strong>
                      <span>{location.direccion}</span>
                      <small style={{ color: "var(--text-3)", display: "block" }}>
                        Capacidad máxima: {location.capacidadMaxima} espectadores
                      </small>
                    </div>
                    <span className={`badge-status ${location.activo ? "badge-success" : "badge-danger"}`}>
                      {location.activo ? "Activa" : "Inactiva"}
                    </span>
                  </div>
                ))}
              </div>
            </section>
          </div>
        </>
      )}
    </section>
  );
}

export default AdminDashboard;
