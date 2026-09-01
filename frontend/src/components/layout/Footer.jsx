import { Link } from "react-router-dom";

function Footer() {
  return (
    <footer className="footer" id="ayuda">
      <div className="container footer-grid">
        <div>
          <h3>Butaca</h3>
          <p>
            Plataforma académica de gestión y venta simulada de entradas en tiempo real.
          </p>
        </div>
        <div>
          <h4>Navegación</h4>
          <Link to="/">Inicio</Link>
          <Link to="/buscar">Cartelera de espectáculos</Link>
          <Link to="/buscar">Filtrar por categorías</Link>
        </div>
        <div>
          <h4>Acceso</h4>
          <Link to="/login">Iniciar sesión</Link>
          <Link to="/registro">Crear cuenta</Link>
          <Link to="/dashboard">Panel de usuario</Link>
        </div>
        <div>
          <h4>Endpoints REST</h4>
          <p style={{ fontFamily: "monospace", fontSize: "0.78rem", lineHeight: 1.8, color: "var(--text-3)" }}>
            GET /api/eventos<br />
            POST /api/compras<br />
            GET /api/entradas/usuario/:id<br />
            POST /api/auth/login
          </p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;

