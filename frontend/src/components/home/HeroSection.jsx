import { useState } from "react";
import { Link } from "react-router-dom";
import Button from "../ui/Button";
import { formatDate, formatPrice } from "../../utils/formatters";

function HeroSection({ featuredEvent, events = [] }) {
  const showcaseList = events.length > 0 ? events.slice(0, 4) : featuredEvent ? [featuredEvent] : [];
  const [selectedIndex, setSelectedIndex] = useState(0);
  const current = showcaseList[selectedIndex] || featuredEvent;

  if (!current) {
    return (
      <section className="hero-section">
        <div className="container" style={{ textAlign: "center", padding: "80px 0" }}>
          <p className="hero-kicker">Experiencias en vivo</p>
          <h1
            style={{
              fontSize: "clamp(2.4rem, 5vw, 5rem)",
              fontWeight: 900,
              letterSpacing: "-0.04em",
              lineHeight: 1,
              margin: "0 0 20px",
              background: "linear-gradient(135deg, #f0f4f8 40%, rgba(240,244,248,0.4))",
              WebkitBackgroundClip: "text",
              WebkitTextFillColor: "transparent",
              backgroundClip: "text"
            }}
          >
            Tu próximo evento<br />empieza aquí.
          </h1>
          <p style={{ color: "var(--text-2)", fontSize: "1.1rem", marginBottom: 32 }}>
            Explorá conciertos, deportes, teatro y festivales en un solo lugar.
          </p>
          <div style={{ display: "flex", gap: 12, justifyContent: "center" }}>
            <Link to="/buscar"><Button>Explorar cartelera</Button></Link>
          </div>
        </div>
      </section>
    );
  }

  return (
    <section className="hero-section">
      <div className="container hero-grid">
        <div className="hero-copy">
          <p className="hero-kicker">Cartelera Oficial · Temporada en Vivo</p>
          <h1>Eventos que no podés perderte.</h1>
          <p className="hero-text">
            Asegurá tu lugar en los mejores recitales, obras de teatro, torneos y festivales con acceso digital inmediato.
          </p>

          <div className="hero-badges">
            <span>{current.categoria}</span>
            <span>{current.recinto || current.ciudad}</span>
            <span>Desde {formatPrice(current.precioMinimo)}</span>
          </div>

          <div className="hero-actions">
            <Link to={`/eventos/${current.id}`}>
              <Button>Comprar entradas</Button>
            </Link>
            <Link to="/buscar">
              <Button variant="ghost">Explorar cartelera →</Button>
            </Link>
          </div>

          {showcaseList.length > 1 ? (
            <div style={{ display: "flex", gap: 8, marginTop: 22, flexWrap: "wrap", alignItems: "center" }}>
              <span style={{ fontSize: "0.75rem", color: "var(--text-3)", marginRight: 4 }}>En cartelera:</span>
              {showcaseList.map((item, idx) => (
                <button
                  key={item.id}
                  type="button"
                  onClick={() => setSelectedIndex(idx)}
                  style={{
                    padding: "5px 12px",
                    borderRadius: "999px",
                    fontSize: "0.76rem",
                    fontWeight: 600,
                    cursor: "pointer",
                    border: idx === selectedIndex ? "1px solid var(--primary)" : "1px solid var(--border)",
                    background: idx === selectedIndex ? "rgba(225, 29, 72, 0.18)" : "rgba(255,255,255,0.03)",
                    color: idx === selectedIndex ? "#fff" : "var(--text-2)",
                    transition: "all 0.2s"
                  }}
                >
                  {item.nombre.split(" ").slice(0, 2).join(" ")}
                </button>
              ))}
            </div>
          ) : null}
        </div>

        <div className="hero-card">
          <img
            src={current.imagen}
            alt={current.nombre}
            style={{ height: 260, width: "100%", objectFit: "cover", transition: "opacity 0.25s" }}
          />
          <div className="hero-card-body">
            <p className="hero-card-label">Show destacado · {current.categoria}</p>
            <h2>{current.nombre}</h2>
            <p style={{ color: "var(--text-2)", fontSize: "0.88rem" }}>{current.descripcion}</p>
            <ul>
              <li>📅 {formatDate(current.fecha)} — {current.hora} hs</li>
              <li>📍 {current.recinto}</li>
              <li>🎟️ {current.disponibilidad} entradas disponibles</li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  );
}

export default HeroSection;

