function Footer() {
  return (
    <footer className="footer" id="ayuda">
      <div className="container footer-grid">
        <div>
          <h3>Butaca</h3>
          <p>
            Plataforma de venta de entradas conectada a Spring Boot. 
            Diseñada para crecer por módulos.
          </p>
        </div>
        <div>
          <h4>Plataforma</h4>
          <a href="/">Inicio</a>
          <a href="/buscar">Buscar eventos</a>
          <a href="#colecciones">Colecciones</a>
        </div>
        <div>
          <h4>Cuenta</h4>
          <a href="/login">Ingresar</a>
          <a href="/registro">Registrarse</a>
          <a href="/dashboard">Panel organizador</a>
        </div>
        <div>
          <h4>API</h4>
          <p style={{ fontFamily: "monospace", fontSize: "0.78rem", lineHeight: 1.8 }}>
            GET /api/eventos<br />
            POST /api/compras<br />
            POST /api/auth/login
          </p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
