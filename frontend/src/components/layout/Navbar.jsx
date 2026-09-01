import { Link, NavLink } from "react-router-dom";

function Navbar({ currentUser, onLogout }) {
  const initial = currentUser ? currentUser.nombre?.[0]?.toUpperCase() : null;
  const role = currentUser?.rol || "CLIENTE";
  const roleLabel = role === "ORGANIZADOR" ? "ORGANIZADOR" : "CLIENTE";
  const dashboardLabel = role === "ORGANIZADOR" ? "Panel" : "Mis Pedidos";
  const roleBadgeColor = role === "ORGANIZADOR" ? "badge-info" : "badge-success";

  return (
    <header className="navbar">
      <div className="container navbar-inner">
        <Link to="/" className="brand">
          <span className="brand-mark">B</span>
          <span>
            <strong>Butaca</strong>
            <small>Ticketing & Espectáculos</small>
          </span>
        </Link>

        <nav className="nav-links">
          <NavLink to="/" end>Inicio</NavLink>
          <NavLink to="/buscar">Cartelera</NavLink>
          <Link to="/buscar">Categorías</Link>
        </nav>

        <div className="navbar-actions">
          {currentUser ? (
            <>
              <Link to="/dashboard" className="text-link" style={{ fontWeight: 700 }}>
                {dashboardLabel}
              </Link>
              <div className="account-chip">
                <div
                  style={{
                    width: 30, height: 30, borderRadius: "50%",
                    background: "linear-gradient(135deg, var(--primary), var(--primary-2))",
                    display: "flex", alignItems: "center", justifyContent: "center",
                    fontWeight: 800, fontSize: "0.8rem", color: "#fff", flexShrink: 0
                  }}
                >
                  {initial}
                </div>
                <div>
                  <strong>{currentUser.nombre}</strong>
                  <span className={`badge-status ${roleBadgeColor}`} style={{ fontSize: "0.62rem", padding: "1px 6px" }}>
                    {roleLabel}
                  </span>
                </div>
                <button
                  className="text-link"
                  type="button"
                  onClick={onLogout}
                  style={{ color: "var(--text-3)", fontSize: "0.8rem", padding: "4px 8px" }}
                  title="Cerrar sesión"
                >
                  Salir
                </button>
              </div>
            </>
          ) : (
            <>
              <Link to="/login" className="text-link">Ingresar</Link>
              <Link to="/registro" className="text-link">Registrarse</Link>
            </>
          )}
          <Link to="/buscar" className="pill-link">
            Explorar →
          </Link>
        </div>
      </div>
    </header>
  );
}

export default Navbar;

