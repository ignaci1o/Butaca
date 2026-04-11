import { Link, NavLink } from "react-router-dom";

function Navbar() {
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
          <button className="text-link" type="button">
            Ingresar
          </button>
          <Link to="/buscar" className="pill-link">
            Explorar eventos
          </Link>
        </div>
      </div>
    </header>
  );
}

export default Navbar;
