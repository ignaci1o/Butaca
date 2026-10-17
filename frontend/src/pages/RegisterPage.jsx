import { useEffect, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import Button from "../components/ui/Button";
import { registerUser } from "../services/authService";

function RegisterPage() {
  const [searchParams] = useSearchParams();
  const isOrganizationMode = searchParams.get("modo") === "organizacion";

  const [formData, setFormData] = useState({
    nombre_usuario: "",
    apellido_usuario: "",
    dni_usuario: "",
    email_usuario: "",
    password_usuario: "",
    telefono: "",
    rol_usuario: isOrganizationMode ? "ORGANIZADOR" : "CLIENTE"
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    setFormData((current) => ({
      ...current,
      rol_usuario: isOrganizationMode ? "ORGANIZADOR" : "CLIENTE"
    }));
  }, [isOrganizationMode]);

  function handleChange(event) {
    const { name, value } = event.target;
    setFormData((current) => ({ ...current, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      await registerUser({
        ...formData,
        rol_usuario: isOrganizationMode ? "ORGANIZADOR" : "CLIENTE"
      });
      navigate("/login", {
        state: {
          message: isOrganizationMode
            ? "Tu organización fue registrada con éxito. Iniciá sesión para gestionar tus eventos."
            : "Cuenta creada con éxito. Ahora podés iniciar sesión."
        }
      });
    } catch (submissionError) {
      setError(submissionError.message || "Error al registrar la cuenta");
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="container page-block">
      <div className="auth-layout single-auth">
        <form className="auth-panel auth-form-panel" onSubmit={handleSubmit}>
          <p className="section-eyebrow">
            {isOrganizationMode ? "Productoras y Teatros" : "Registro"}
          </p>
          <h2>{isOrganizationMode ? "Sumá tu organización" : "Crear cuenta"}</h2>
          {isOrganizationMode ? (
            <p style={{ color: "var(--text-2)", fontSize: "0.88rem", margin: "-6px 0 18px" }}>
              Completá los datos del responsable de la productora o sala para publicar y gestionar espectáculos en Butaca.
            </p>
          ) : null}

          <div className="form-grid two-columns">
            <label>
              {isOrganizationMode ? "Nombre del responsable / Productora" : "Nombre"}
              <input name="nombre_usuario" value={formData.nombre_usuario} onChange={handleChange} required />
            </label>
            <label>
              {isOrganizationMode ? "Apellido / Razón social" : "Apellido"}
              <input name="apellido_usuario" value={formData.apellido_usuario} onChange={handleChange} required />
            </label>
            <label>
              DNI / Documento
              <input name="dni_usuario" value={formData.dni_usuario} onChange={handleChange} placeholder="Ej: 42850123" required />
            </label>
            <label>
              Correo electrónico
              <input name="email_usuario" type="email" value={formData.email_usuario} onChange={handleChange} required />
            </label>
            <label>
              Teléfono de contacto
              <input name="telefono" value={formData.telefono} onChange={handleChange} />
            </label>
            <label>
              Contraseña
              <input name="password_usuario" type="password" value={formData.password_usuario} onChange={handleChange} required minLength={6} />
            </label>
          </div>

          {error ? <div className="auth-error">{error}</div> : null}

          <div className="auth-form-actions">
            <Button type="submit" fullWidth disabled={loading}>
              {loading
                ? "Registrando..."
                : isOrganizationMode
                ? "Registrar organización"
                : "Crear cuenta"}
            </Button>
          </div>

          <p className="auth-switch">
            ¿Ya tenés cuenta? <Link to="/login">Iniciar sesión</Link>
          </p>
        </form>
      </div>
    </section>
  );
}

export default RegisterPage;

