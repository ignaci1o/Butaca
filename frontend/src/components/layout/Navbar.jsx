import { Link, NavLink } from "react-router-dom";

function Navbar({ currentUser, onLogout }) {
  const initial = currentUser ? currentUser.nombre?.[0]?.toUpperCase() : null;
  const roleLabel = currentUser ? currentUser.rol.replace("_", " ") : null;

  return (
    <header className="navbar">
      <div className="container navbar-inner">
        <Link to="/" className="brand">
          <span className="brand-mark">B</span>
          <span>
            <strong>Butaca</strong>
            <small>Entradas en tiempo real</small>
          </span>
        </Link>

        <nav className="nav-links">
          <NavLink to="/">Inicio</NavLink>
          <NavLink to="/buscar">Buscar</NavLink>
          <a href="#colecciones">Colecciones</a>
          <a href="#ayuda">Ayuda</a>
        </nav>

        <div className="navbar-actions">
          {currentUser ? (
            <>
              <Link to="/dashboard" className="text-link">
                Mi Panel
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
                  <small>{roleLabel}</small>
                </div>
                <button className="text-link" type="button" onClick={onLogout}>
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
