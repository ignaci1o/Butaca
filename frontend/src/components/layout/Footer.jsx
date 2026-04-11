function Footer() {
  return (
    <footer className="footer" id="ayuda">
      <div className="container footer-grid">
        <div>
          <h3>Butaca</h3>
          <p>
            Frontend React listo para conectarse con un backend Java Spring Boot y crecer por
            modulos.
          </p>
        </div>
        <div>
          <h4>Secciones</h4>
          <a href="/">Inicio</a>
          <a href="/buscar">Buscar</a>
          <a href="#colecciones">Colecciones</a>
        </div>
        <div>
          <h4>Operaciones</h4>
          <a href="/">Compra de entradas</a>
          <a href="/">Validacion</a>
          <a href="/">Panel futuro</a>
        </div>
        <div>
          <h4>Integracion</h4>
          <p>API base esperada: `/api/eventos` y `/api/pedidos`</p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
