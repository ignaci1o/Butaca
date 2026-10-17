import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Button from "../components/ui/Button";
import { loginUser } from "../services/authService";

const initialState = {
  email: "",
  password: ""
};

function LoginPage({ currentUser, onLogin }) {
  const navigate = useNavigate();
  const [formData, setFormData] = useState(initialState);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (currentUser) {
      navigate("/");
    }
  }, [currentUser, navigate]);

  function handleChange(event) {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value
    });
  }

  function fillDemoAccount(email, password) {
    setFormData({ email, password });
    setError("");
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      const user = await loginUser(formData);
      onLogin(user);
      navigate(user.rol === "CLIENTE" ? "/" : "/dashboard");
    } catch (submissionError) {
      setError(submissionError.message || "No se pudo iniciar sesión. Verifique sus credenciales.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="container page-block">
      <div className="auth-layout">
        <div className="auth-panel auth-panel-copy">
          <p className="section-eyebrow">Acceso al Sistema</p>
          <h1>Ingrese a Butaca según el perfil correspondiente.</h1>
          <p className="section-text">
            Acceso a cartelera de espectáculos, compras y gestión operativa.
          </p>

          <div className="auth-role-list">
            <div className="auth-role-card">
              <strong>Cliente</strong>
              <span>Exploración de eventos, compra y descarga de pases de acceso.</span>
            </div>
            <div className="auth-role-card">
              <strong>Organizador</strong>
              <span>Gestión de eventos, publicación y control de disponibilidad.</span>
            </div>
          </div>

          <div className="auth-helper">
            <strong>Cuentas de demostración</strong>
            <span style={{ margin: "4px 0 8px" }}>Seleccione un perfil para autocompletar credenciales:</span>
            <div style={{ display: "flex", flexWrap: "wrap", gap: 8 }}>
              <button
                type="button"
                className="btn-copy"
                onClick={() => fillDemoAccount("cliente@butaca.com", "cliente123")}
              >
                Cliente
              </button>
              <button
                type="button"
                className="btn-copy"
                onClick={() => fillDemoAccount("org@butaca.com", "org123")}
              >
                Organizador
              </button>
              <button
                type="button"
                className="btn-copy"
                onClick={() => fillDemoAccount("admin@butaca.com", "admin123")}
              >
                Administrador
              </button>
            </div>
          </div>
        </div>

        <form className="auth-panel auth-form-panel" onSubmit={handleSubmit}>
          <p className="section-eyebrow">Autenticación</p>
          <h2>Iniciar Sesión</h2>
          <p className="section-text">
            Ingrese su correo electrónico y contraseña.
          </p>

          <div className="form-grid">
            <label>
              Correo electrónico
              <input
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="usuario@dominio.com"
                required
              />
            </label>

            <label>
              Contraseña
              <input
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="••••••••"
                required
              />
            </label>
          </div>

          {error ? <div className="auth-error">{error}</div> : null}

          <div className="auth-form-actions" style={{ display: "grid", gap: 12 }}>
            <Button type="submit" fullWidth disabled={loading}>
              {loading ? "Validando..." : "Ingresar"}
            </Button>
            <div className="auth-switch">
              ¿No tiene una cuenta? <Link to="/registro">Crear cuenta</Link>
            </div>
          </div>
        </form>
      </div>
    </section>
  );
}

export default LoginPage;
