import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Button from "../components/ui/Button";
import { registerUser } from "../services/authService";

function RegisterPage() {
  const [formData, setFormData] = useState({
    nombre_usuario: "",
    apellido_usuario: "",
    email_usuario: "",
    password_usuario: "",
    telefono: "",
    rol_usuario: "CLIENTE"
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  function handleChange(event) {
    const { name, value } = event.target;
    setFormData((current) => ({ ...current, [name]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      await registerUser(formData);
      navigate("/login", { state: { message: "Registro exitoso. Ahora podes iniciar sesion." } });
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
          <p className="section-eyebrow">Registro</p>
          <h2>Crear cuenta</h2>

          <div className="form-grid two-columns">
            <label>
              Nombre
              <input name="nombre_usuario" value={formData.nombre_usuario} onChange={handleChange} required />
            </label>
            <label>
              Apellido
              <input name="apellido_usuario" value={formData.apellido_usuario} onChange={handleChange} required />
            </label>
            <label>
              Correo electronico
              <input name="email_usuario" type="email" value={formData.email_usuario} onChange={handleChange} required />
            </label>
            <label>
              Telefono
              <input name="telefono" value={formData.telefono} onChange={handleChange} />
            </label>
            <label>
              Contrasena
              <input name="password_usuario" type="password" value={formData.password_usuario} onChange={handleChange} required minLength={6} />
            </label>
            <label>
              Tipo de cuenta
              <select name="rol_usuario" value={formData.rol_usuario} onChange={handleChange}>
                <option value="CLIENTE">Cliente</option>
                <option value="ORGANIZADOR">Organizador</option>
              </select>
            </label>
          </div>

          {error ? <div className="auth-error">{error}</div> : null}

          <div className="auth-form-actions">
            <Button type="submit" fullWidth disabled={loading}>
              {loading ? "Registrando..." : "Crear cuenta"}
            </Button>
          </div>

          <p className="auth-switch">
            Ya tenes cuenta? <Link to="/login">Iniciar sesion</Link>
          </p>
        </form>
      </div>
    </section>
  );
}

export default RegisterPage;
