import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../services/authService";

function RegisterPage() {
  const [formData, setFormData] = useState({
    nombre_usuario: "",
    apellido_usuario: "",
    email_usuario: "",
    password_usuario: "",
    rol_usuario: "CLIENTE"
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await registerUser(formData);
      navigate("/login", { state: { message: "Registro exitoso. Ahora puedes iniciar sesión." } });
    } catch (err) {
      setError(err.message || "Error al registrar la cuenta");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-12 bg-white p-8 border border-gray-200 rounded shadow-sm">
      <h1 className="text-2xl font-bold mb-6 text-center">Crear cuenta en Butaca</h1>

      {error && (
        <div className="bg-red-50 text-red-600 p-3 rounded mb-4 text-sm border border-red-200">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
          <input
            type="text"
            name="nombre_usuario"
            value={formData.nombre_usuario}
            onChange={handleChange}
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Apellido</label>
          <input
            type="text"
            name="apellido_usuario"
            value={formData.apellido_usuario}
            onChange={handleChange}
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Correo Electrónico</label>
          <input
            type="email"
            name="email_usuario"
            value={formData.email_usuario}
            onChange={handleChange}
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Contraseña</label>
          <input
            type="password"
            name="password_usuario"
            value={formData.password_usuario}
            onChange={handleChange}
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Tipo de cuenta</label>
          <select
            name="rol_usuario"
            value={formData.rol_usuario}
            onChange={handleChange}
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="CLIENTE">Comprador (Cliente)</option>
            <option value="ORGANIZADOR">Productora (Organizador)</option>
          </select>
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white font-medium py-2 px-4 rounded hover:bg-blue-700 transition disabled:opacity-50"
        >
          {loading ? "Registrando..." : "Crear cuenta"}
        </button>
      </form>

      <div className="mt-6 text-center text-sm text-gray-600">
        ¿Ya tienes cuenta?{" "}
        <Link to="/login" className="text-blue-600 hover:underline">
          Inicia sesión aquí
        </Link>
      </div>
    </div>
  );
}

export default RegisterPage;
