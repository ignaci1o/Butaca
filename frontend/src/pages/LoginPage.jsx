import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Button from "../components/ui/Button";
import { loginUser, useMockAuth } from "../services/authService";

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

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      const user = await loginUser(formData);
      onLogin(user);
      navigate(user.rol === "CLIENTE" ? "/" : "/dashboard");
    } catch (submissionError) {
      setError(submissionError.message || "No se pudo iniciar sesion");
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="container page-block">
      <div className="auth-layout">
        <div className="auth-panel auth-panel-copy">
          <p className="section-eyebrow">Acceso a cuentas</p>
          <h1>Ingresa a Butaca con el perfil correcto para probar cada flujo.</h1>
          <p className="section-text">
            Accede al catalogo, a tus entradas o a la gestion de eventos segun el rol de tu cuenta.
          </p>

          <div className="auth-role-list">
            <div className="auth-role-card">
              <strong>Administrador</strong>
              <span>Control general del sistema y supervision operativa.</span>
            </div>
            <div className="auth-role-card">
              <strong>Organizador</strong>
              <span>Creacion de eventos, tipos de entrada y seguimiento basico.</span>
            </div>
            <div className="auth-role-card">
              <strong>Cliente</strong>
              <span>Exploracion de eventos, reserva y compra de entradas.</span>
            </div>
          </div>

          {useMockAuth ? (
            <div className="auth-helper">
              <strong>Modo demo activo</strong>
              <span>`organizador@butaca.local`, `cliente@butaca.local` o `admin@butaca.local`</span>
              <span>Clave: `clave123`</span>
            </div>
          ) : (
            <div className="auth-helper">
              <strong>Cuentas demo</strong>
              <span>admin@butaca.com, org@butaca.com, cliente@butaca.com.</span>
            </div>
          )}
        </div>

        <form className="auth-panel auth-form-panel" onSubmit={handleSubmit}>
          <p className="section-eyebrow">Inicio de sesion</p>
          <h2>Cuenta Butaca</h2>
          <p className="section-text">
            Usa tu correo y contrasena para continuar.
          </p>

          <div className="form-grid">
            <label>
              Correo electronico
              <input
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="tu@email.com"
                required
              />
            </label>

            <label>
              Contrasena
              <input
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Ingresa tu clave"
                required
              />
            </label>
          </div>

          {error ? <div className="auth-error">{error}</div> : null}

          <div className="auth-form-actions">
            <Button type="submit" fullWidth disabled={loading}>
              {loading ? "Validando..." : "Ingresar"}
            </Button>
          </div>
        </form>
      </div>
    </section>
  );
}

export default LoginPage;
