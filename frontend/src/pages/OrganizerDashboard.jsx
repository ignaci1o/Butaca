import { useState, useEffect } from "react";
import { request } from "../services/apiClient";

function OrganizerDashboard({ currentUser }) {
  const [eventos, setEventos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [formData, setFormData] = useState({
    titulo_evento: "",
    descripcion_evento: "",
    ubicacion_evento: "",
    detalles_ubicacion_evento: "",
    fecha_evento: "",
    hora_evento: "",
    precio_entrada_evento: 0,
    cupos_totales_evento: 100
  });

  useEffect(() => {
    cargarEventos();
  }, []);

  const cargarEventos = async () => {
    try {
      setLoading(true);
      const data = await request(`/eventos/organizador/${currentUser.id}`);
      setEventos(data);
    } catch (error) {
      console.error("Error al cargar eventos", error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        ...formData,
        organizador_id_evento: currentUser.id
      };
      await request("/eventos", {
        method: "POST",
        body: JSON.stringify(payload)
      });
      setMostrarForm(false);
      cargarEventos();
      // Limpiar form
      setFormData({
        titulo_evento: "",
        descripcion_evento: "",
        ubicacion_evento: "",
        detalles_ubicacion_evento: "",
        fecha_evento: "",
        hora_evento: "",
        precio_entrada_evento: 0,
        cupos_totales_evento: 100
      });
    } catch (error) {
      alert("Error al crear el evento: " + error.message);
    }
  };

  const handlePublicar = async (idEvento) => {
    try {
      await request(`/eventos/${idEvento}/publicar`, {
        method: "PUT"
      });
      cargarEventos();
    } catch (error) {
      alert("Error al publicar: " + error.message);
    }
  };

  if (!currentUser || (currentUser.rol !== 'ORGANIZADOR' && currentUser.rol !== 'ADMIN')) {
    return (
      <div className="container" style={{ padding: "4rem 2rem", textAlign: "center" }}>
        <h2>Acceso denegado</h2>
        <p>Esta página es exclusiva para organizadores.</p>
      </div>
    );
  }

  return (
    <div className="container" style={{ padding: "2rem 0" }}>
      <header style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "2rem" }}>
        <h1>Panel de Organizador</h1>
        <button 
          className="pill-link" 
          onClick={() => setMostrarForm(!mostrarForm)}
          style={{ cursor: "pointer", border: "none" }}
        >
          {mostrarForm ? "Cancelar" : "+ Crear Evento"}
        </button>
      </header>

      {mostrarForm && (
        <div style={{ background: "#f9fafb", padding: "2rem", borderRadius: "8px", marginBottom: "2rem", border: "1px solid #e5e7eb" }}>
          <h2 style={{ marginTop: 0, marginBottom: "1.5rem" }}>Nuevo Evento</h2>
          <form onSubmit={handleSubmit} style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "1rem" }}>
            <div style={{ gridColumn: "span 2" }}>
              <label className="block text-sm font-medium text-gray-700 mb-1">Título</label>
              <input type="text" name="titulo_evento" value={formData.titulo_evento} onChange={handleChange} required className="w-full border p-2 rounded" />
            </div>
            <div style={{ gridColumn: "span 2" }}>
              <label className="block text-sm font-medium text-gray-700 mb-1">Descripción</label>
              <textarea name="descripcion_evento" value={formData.descripcion_evento} onChange={handleChange} className="w-full border p-2 rounded" rows="3"></textarea>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Ubicación (Ciudad)</label>
              <input type="text" name="ubicacion_evento" value={formData.ubicacion_evento} onChange={handleChange} required className="w-full border p-2 rounded" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Detalles de Ubicación (Recinto)</label>
              <input type="text" name="detalles_ubicacion_evento" value={formData.detalles_ubicacion_evento} onChange={handleChange} className="w-full border p-2 rounded" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Fecha</label>
              <input type="date" name="fecha_evento" value={formData.fecha_evento} onChange={handleChange} required className="w-full border p-2 rounded" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Hora</label>
              <input type="time" name="hora_evento" value={formData.hora_evento} onChange={handleChange} required className="w-full border p-2 rounded" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Precio Entrada</label>
              <input type="number" name="precio_entrada_evento" value={formData.precio_entrada_evento} onChange={handleChange} required min="0" step="0.01" className="w-full border p-2 rounded" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Cupos Totales</label>
              <input type="number" name="cupos_totales_evento" value={formData.cupos_totales_evento} onChange={handleChange} required min="1" className="w-full border p-2 rounded" />
            </div>
            <div style={{ gridColumn: "span 2", textAlign: "right", marginTop: "1rem" }}>
              <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded font-medium hover:bg-blue-700 transition">
                Guardar Borrador
              </button>
            </div>
          </form>
        </div>
      )}

      {loading ? (
        <p>Cargando tus eventos...</p>
      ) : eventos.length === 0 ? (
        <div style={{ background: "#f3f4f6", padding: "3rem", textAlign: "center", borderRadius: "8px" }}>
          <p>No tienes eventos creados todavía.</p>
        </div>
      ) : (
        <div style={{ display: "grid", gap: "1rem" }}>
          {eventos.map(evento => (
            <div key={evento.id_evento} style={{ display: "flex", justifyContent: "space-between", alignItems: "center", background: "white", padding: "1.5rem", borderRadius: "8px", border: "1px solid #e5e7eb", boxShadow: "0 1px 3px rgba(0,0,0,0.05)" }}>
              <div>
                <h3 style={{ margin: "0 0 0.5rem 0", fontSize: "1.25rem" }}>{evento.titulo_evento}</h3>
                <p style={{ margin: 0, color: "#4b5563", fontSize: "0.875rem" }}>
                  {evento.fecha_evento} a las {evento.hora_evento} | {evento.cupos_disponible_evento} / {evento.cupos_totales_evento} cupos disponibles
                </p>
                <div style={{ marginTop: "0.5rem" }}>
                  <span style={{ display: "inline-block", padding: "0.25rem 0.75rem", borderRadius: "999px", fontSize: "0.75rem", fontWeight: "bold", background: evento.estado_publicacion_evento === 'PUBLICADO' ? '#dcfce7' : '#fef9c3', color: evento.estado_publicacion_evento === 'PUBLICADO' ? '#166534' : '#854d0e' }}>
                    {evento.estado_publicacion_evento}
                  </span>
                </div>
              </div>
              <div>
                {evento.estado_publicacion_evento === 'BORRADOR' && (
                  <button onClick={() => handlePublicar(evento.id_evento)} style={{ background: "#10b981", color: "white", border: "none", padding: "0.5rem 1rem", borderRadius: "4px", cursor: "pointer", fontWeight: "500" }}>
                    Publicar
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default OrganizerDashboard;
