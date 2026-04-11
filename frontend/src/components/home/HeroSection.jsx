import { Link } from "react-router-dom";
import Button from "../ui/Button";
import { formatDate, formatPrice } from "../../utils/formatters";

function HeroSection({ featuredEvent }) {
  if (!featuredEvent) {
    return null;
  }

  return (
    <section className="hero-section">
      <div className="container hero-grid">
        <div className="hero-copy">
          <p className="hero-kicker">Plataforma de ticketing</p>
          <h1>Eventos grandes, compra clara y una base lista para tu API.</h1>
          <p className="hero-text">
            Butaca toma la idea de una marketplace de entradas, pero con una estetica propia y una
            estructura de componentes facil de ubicar y modificar.
          </p>

          <div className="hero-badges">
            <span>{featuredEvent.categoria}</span>
            <span>{featuredEvent.ciudad}</span>
            <span>Desde {formatPrice(featuredEvent.precioMinimo)}</span>
          </div>

          <div className="hero-actions">
            <Link to={`/eventos/${featuredEvent.id}`}>
              <Button>Ver evento principal</Button>
            </Link>
            <Link to="/buscar">
              <Button variant="secondary">Buscar mas fechas</Button>
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
              <li>{formatDate(featuredEvent.fecha)}</li>
              <li>{featuredEvent.recinto}</li>
              <li>{featuredEvent.disponibilidad} lugares disponibles</li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  );
}

export default HeroSection;
