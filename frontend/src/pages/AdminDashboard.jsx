import { useEffect, useState } from "react";
import { getAdminStats, getCategories, getLocations } from "../services/eventService";

function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [categories, setCategories] = useState([]);
  const [locations, setLocations] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getAdminStats(), getCategories(), getLocations()])
      .then(([statsData, categoryData, locationData]) => {
        setStats(statsData);
        setCategories(categoryData);
        setLocations(locationData);
      })
      .catch((loadError) => setError(loadError.message));
  }, []);

  return (
    <section className="container page-block">
      <div className="dashboard-header">
        <div>
          <p className="section-eyebrow">Administracion</p>
          <h1>Panel general</h1>
        </div>
      </div>

      {error ? <div className="auth-error">{error}</div> : null}

      <div className="stats-grid">
        {Object.entries(stats || {}).map(([key, value]) => (
          <div className="stat-card" key={key}>
            <span>{key.replaceAll("_", " ")}</span>
            <strong>{value}</strong>
          </div>
        ))}
      </div>

      <div className="dashboard-grid">
        <section className="dashboard-panel">
          <h2>Categorias</h2>
          <div className="table-list">
            {categories.map((category) => (
              <div key={category.id}>
                <strong>{category.nombre}</strong>
                <span>{category.descripcion || "Sin descripcion"}</span>
              </div>
            ))}
          </div>
        </section>

        <section className="dashboard-panel">
          <h2>Ubicaciones activas</h2>
          <div className="table-list">
            {locations.map((location) => (
              <div key={location.id}>
                <strong>{location.nombre}</strong>
                <span>{location.direccion} · {location.capacidadMaxima} personas</span>
              </div>
            ))}
          </div>
        </section>
      </div>
    </section>
  );
}

export default AdminDashboard;
