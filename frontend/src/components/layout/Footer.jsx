import { Link } from "react-router-dom";

function Footer() {
  return (
    <footer className="footer" id="ayuda">
      <div className="container footer-grid">
        <div>
          <h3>Butaca</h3>
          <p>
            Tu plataforma de entradas digitales para recitales, teatro y eventos en vivo en toda la Argentina.
          </p>
        </div>
        <div>
          <h4>Navegación</h4>
          <Link to="/">Inicio</Link>
          <Link to="/buscar">Cartelera de espectáculos</Link>
          <Link to="/dashboard">Mis entradas</Link>
        </div>
        <div>
          <h4>Sobre nosotros</h4>
          <a href="#ayuda">Quiénes somos</a>
          <a href="#ayuda">Términos y condiciones</a>
          <a href="#ayuda">Política de privacidad</a>
          <Link
            to="/registro?modo=organizacion"
            style={{ color: "var(--text-3)", fontSize: "0.82rem", marginTop: 4 }}
          >
            Sumá tu organización
          </Link>
        </div>
        <div>
          <h4>Ayuda</h4>
          <a href="#ayuda">Centro de ayuda</a>
          <a href="#ayuda">Entradas nominales y acceso</a>
          <a href="#ayuda">Medios de pago aceptados</a>
          <a href="#ayuda">Botón de arrepentimiento</a>
        </div>
      </div>
    </footer>
  );
}

export default Footer;


