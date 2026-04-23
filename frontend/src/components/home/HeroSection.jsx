import { Link } from "react-router-dom";
import Button from "../ui/Button";
import { formatDate, formatPrice } from "../../utils/formatters";

function HeroSection({ featuredEvent }) {
  if (!featuredEvent) {
    return (
      <section className="hero-section">
        <div className="container" style={{ textAlign: "center", padding: "80px 0" }}>
          <p className="hero-kicker">Plataforma de ticketing</p>
          <h1 style={{
            fontSize: "clamp(2.4rem, 5vw, 5rem)", fontWeight: 900,
            letterSpacing: "-0.04em", lineHeight: 1, margin: "0 0 20px",
            background: "linear-gradient(135deg, #f0f4f8 40%, rgba(240,244,248,0.4))",
            WebkitBackgroundClip: "text", WebkitTextFillColor: "transparent", backgroundClip: "text"
          }}>
            Tu próximo evento<br />empieza aquí.
          </h1>
          <p style={{ color: "var(--text-2)", fontSize: "1.1rem", marginBottom: 32 }}>
            Explorá conciertos, deportes, teatro y más.
          </p>
          <div style={{ display: "flex", gap: 12, justifyContent: "center" }}>
            <Link to="/buscar"><Button>Explorar eventos</Button></Link>
          </div>
        </div>
      </section>
    );
  }

  return (
    <section className="hero-section">
      <div className="container hero-grid">
        <div className="hero-copy">
          <p className="hero-kicker">Plataforma de ticketing</p>
          <h1>Eventos que no podés perderte.</h1>
          <p className="hero-text">
            Encontrá las mejores entradas para conciertos, deportes y teatro. 
            Comprá fácil, recibí rápido.
          </p>

          <div className="hero-badges">
            <span>🎵 {featuredEvent.categoria}</span>
            <span>📍 {featuredEvent.ciudad}</span>
            <span>💰 Desde {formatPrice(featuredEvent.precioMinimo)}</span>
          </div>

          <div className="hero-actions">
            <Link to={`/eventos/${featuredEvent.id}`}>
              <Button>Ver evento destacado</Button>
            </Link>
            <Link to="/buscar">
              <Button variant="ghost">Ver todos →</Button>
            </Link>
          </div>
        </div>

        <div className="hero-card">
          <img src={featuredEvent.imagen} alt={featuredEvent.nombre} />
          <div className="hero-card-body">
            <p className="hero-card-label">Evento destacado</p>
            <h2>{featuredEvent.nombre}</h2>
            <p>{featuredEvent.artista}</p>
            <ul>
              <li>📅 {formatDate(featuredEvent.fecha)} — {featuredEvent.hora}hs</li>
              <li>🏟 {featuredEvent.recinto}</li>
              <li>🎟 {featuredEvent.disponibilidad} lugares disponibles</li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  );
}

export default HeroSection;
