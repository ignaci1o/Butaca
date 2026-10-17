import { useEffect, useRef, useState } from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { getCategories } from "../../services/eventService";

const defaultCategories = [
  { nombre: "Conciertos", icon: "🎸", desc: "Recitales y música en vivo" },
  { nombre: "Teatro", icon: "🎭", desc: "Obras dramáticas y comedia" },
  { nombre: "Deportes", icon: "🏀", desc: "Torneos y finales en vivo" },
  { nombre: "Festivales", icon: "🎪", desc: "Experiencias y aire libre" }
];

function getCategoryIcon(nombre = "") {
  const key = nombre.toLowerCase();
  if (key.includes("concierto") || key.includes("musica")) return "🎸";
  if (key.includes("teatro") || key.includes("comedia")) return "🎭";
  if (key.includes("deporte") || key.includes("basquet")) return "🏀";
  if (key.includes("festival") || key.includes("expo")) return "🎪";
  return "🎟️";
}

function Navbar({ currentUser, onLogout }) {
  const navigate = useNavigate();
  const [catOpen, setCatOpen] = useState(false);
  const [categories, setCategories] = useState(defaultCategories);
  const dropdownRef = useRef(null);

  useEffect(() => {
    getCategories()
      .then((data) => {
        if (data && data.length > 0) {
          setCategories(
            data.map((c) => ({
              nombre: c.nombre,
              icon: getCategoryIcon(c.nombre),
              desc: c.descripcion || "Espectáculos disponibles"
            }))
          );
        }
      })
      .catch(() => {});
  }, []);

  useEffect(() => {
    function handleClickOutside(e) {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
        setCatOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const initial = currentUser ? currentUser.nombre?.[0]?.toUpperCase() : null;
  const role = currentUser?.rol || "CLIENTE";
  const roleLabel = role === "ADMIN" ? "ADMIN" : role === "ORGANIZADOR" ? "ORGANIZADOR" : "CLIENTE";
  const dashboardLabel = role === "ADMIN" ? "Panel Admin" : role === "ORGANIZADOR" ? "Panel Productora" : "Mis Entradas";
  const roleBadgeColor =
    role === "ADMIN" ? "badge-danger" : role === "ORGANIZADOR" ? "badge-info" : "badge-success";

  function handleSelectCategory(catName) {
    setCatOpen(false);
    if (!catName || catName === "Todos") {
      navigate("/buscar");
    } else {
      navigate(`/buscar?categoria=${encodeURIComponent(catName)}`);
    }
  }

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

        <nav className="nav-links" style={{ alignItems: "center" }}>
          <NavLink to="/" end>Inicio</NavLink>
          <NavLink to="/buscar">Cartelera</NavLink>

          <div ref={dropdownRef} style={{ position: "relative" }}>
            <button
              type="button"
              onClick={() => setCatOpen((prev) => !prev)}
              style={{
                background: catOpen ? "rgba(255,255,255,0.08)" : "transparent",
                border: "1px solid transparent",
                borderColor: catOpen ? "var(--border)" : "transparent",
                color: "var(--text-2)",
                fontSize: "0.92rem",
                fontWeight: 500,
                padding: "6px 12px",
                borderRadius: "999px",
                cursor: "pointer",
                display: "inline-flex",
                alignItems: "center",
                gap: 6,
                transition: "all 0.2s"
              }}
            >
              Categorías
              <span style={{ fontSize: "0.7rem", transform: catOpen ? "rotate(180deg)" : "none", transition: "transform 0.2s" }}>
                ▼
              </span>
            </button>

            {catOpen ? (
              <div
                style={{
                  position: "absolute",
                  top: "calc(100% + 10px)",
                  left: 0,
                  minWidth: 270,
                  background: "rgba(15, 20, 32, 0.98)",
                  backdropFilter: "blur(16px)",
                  border: "1px solid var(--border)",
                  borderRadius: "var(--r-md)",
                  padding: "10px",
                  boxShadow: "0 20px 44px rgba(0, 0, 0, 0.65)",
                  zIndex: 999,
                  display: "grid",
                  gap: 4
                }}
              >
                <button
                  type="button"
                  onClick={() => handleSelectCategory("Todos")}
                  style={{
                    display: "flex",
                    alignItems: "center",
                    gap: 10,
                    padding: "9px 12px",
                    borderRadius: "var(--r-sm)",
                    background: "rgba(255,255,255,0.03)",
                    border: "none",
                    color: "var(--text)",
                    cursor: "pointer",
                    textAlign: "left",
                    width: "100%"
                  }}
                >
                  <span style={{ fontSize: "1.1rem" }}>✨</span>
                  <div>
                    <strong style={{ display: "block", fontSize: "0.86rem" }}>Todas las categorías</strong>
                    <span style={{ fontSize: "0.74rem", color: "var(--text-3)" }}>Ver cartelera completa</span>
                  </div>
                </button>

                {categories.map((cat) => (
                  <button
                    key={cat.nombre}
                    type="button"
                    onClick={() => handleSelectCategory(cat.nombre)}
                    style={{
                      display: "flex",
                      alignItems: "center",
                      gap: 10,
                      padding: "9px 12px",
                      borderRadius: "var(--r-sm)",
                      background: "transparent",
                      border: "none",
                      color: "var(--text)",
                      cursor: "pointer",
                      textAlign: "left",
                      width: "100%"
                    }}
                    onMouseEnter={(e) => {
                      e.currentTarget.style.background = "rgba(255,255,255,0.06)";
                    }}
                    onMouseLeave={(e) => {
                      e.currentTarget.style.background = "transparent";
                    }}
                  >
                    <span style={{ fontSize: "1.15rem" }}>{cat.icon}</span>
                    <div>
                      <strong style={{ display: "block", fontSize: "0.86rem" }}>{cat.nombre}</strong>
                      <span style={{ fontSize: "0.74rem", color: "var(--text-3)" }}>{cat.desc}</span>
                    </div>
                  </button>
                ))}
              </div>
            ) : null}
          </div>
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
                    width: 30,
                    height: 30,
                    borderRadius: "50%",
                    background: "linear-gradient(135deg, var(--primary), var(--primary-2))",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    fontWeight: 800,
                    fontSize: "0.8rem",
                    color: "#fff",
                    flexShrink: 0
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


