import { useEffect, useState } from "react";
import {
  getAdminAuditLogs,
  getAdminPurchases,
  getAdminStats,
  getAdminUsers,
  getCategories,
  getLocations,
  updateUserRole
} from "../services/eventService";
import { formatDate, formatPrice } from "../utils/formatters";

function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [users, setUsers] = useState([]);
  const [purchases, setPurchases] = useState([]);
  const [categories, setCategories] = useState([]);
  const [locations, setLocations] = useState([]);
  const [auditLogs, setAuditLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [roleUpdatingId, setRoleUpdatingId] = useState(null);
  const [error, setError] = useState("");

  async function loadAll() {
    setLoading(true);
    try {
      const [statsData, usersData, purchasesData, categoryData, locationData, auditData] = await Promise.all([
        getAdminStats(),
        getAdminUsers(),
        getAdminPurchases(),
        getCategories(),
        getLocations(),
        getAdminAuditLogs()
      ]);
      setStats(statsData || {});
      setUsers(usersData || []);
      setPurchases(purchasesData || []);
      setCategories(categoryData || []);
      setLocations(locationData || []);
      setAuditLogs(auditData || []);
    } catch (loadError) {
      setError(loadError.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadAll();
  }, []);

  async function handleToggleOrganizer(user) {
    const currentRole = user.rol_usuario || "CLIENTE";
    const nextRole = currentRole === "ORGANIZADOR" ? "CLIENTE" : "ORGANIZADOR";
    setRoleUpdatingId(user.id_usuario);
    setError("");
    try {
      await updateUserRole(user.id_usuario, nextRole);
      await loadAll();
    } catch (err) {
      setError(err.message || "No se pudo actualizar el rol del usuario.");
    } finally {
      setRoleUpdatingId(null);
    }
  }

  const statLabels = {
    usuarios: "Usuarios Registrados",
    verificaciones_kyc: "Identidades KYC Verificadas",
    eventos: "Eventos Creados",
    eventos_publicados: "Eventos Publicados",
    compras: "Compras Procesadas",
    pagos_aprobados: "Pagos Pasarela Aprobados",
    comprobantes_emitidos: "Comprobantes SHA-256",
    entradas: "Tickets Nominales Emitidos",
    eventos_auditoria: "Eventos de Tracking"
  };

  return (
    <section className="container page-block">
      <div className="dashboard-header">
        <div>
          <p className="section-eyebrow">Administración y Seguridad Informática</p>
          <h1>Panel de Control Global, Roles y Auditoría</h1>
        </div>
      </div>

      {error ? <div className="auth-error">{error}</div> : null}

      {loading ? (
        <div className="dashboard-grid">
          <div className="skeleton skeleton-card" />
          <div className="skeleton skeleton-card" />
        </div>
      ) : (
        <>
          <div className="stats-grid">
            {Object.entries(stats || {}).map(([key, value]) => {
              const label = statLabels[key] || key.replaceAll("_", " ");
              return (
                <div className="stat-card" key={key}>
                  <span>{label}</span>
                  <strong>{typeof value === "number" && key.includes("recaudacion") ? `$${value.toLocaleString("es-AR")}` : value}</strong>
                </div>
              );
            })}
          </div>

          {/* 1. Gestión de Usuarios y Asignación de Organizadores */}
          <section className="dashboard-panel" style={{ marginBottom: 24 }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", flexWrap: "wrap", gap: 10, marginBottom: 14 }}>
              <div>
                <h2 style={{ margin: 0 }}>Gestión de Usuarios y Permisos de Organizador ({users.length})</h2>
                <p style={{ color: "var(--text-2)", fontSize: "0.85rem", margin: "4px 0 0" }}>
                  Todas las cuentas nuevas ingresan como Cliente. Desde aquí podés habilitar o revocar el rol de Organizador.
                </p>
              </div>
            </div>

            <div className="table-list">
              {users.map((u) => {
                const role = u.rol_usuario || "CLIENTE";
                const isVerified = u.identidad_verificada || u.estado_verificacion === "VERIFICADA";
                return (
                  <div
                    key={u.id_usuario}
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                      flexWrap: "wrap",
                      gap: 12
                    }}
                  >
                    <div>
                      <strong>
                        {u.nombre_usuario} {u.apellido_usuario}
                      </strong>
                      <span>
                        {u.email_usuario} · DNI: <strong>{u.dni_usuario || "No informado"}</strong>
                      </span>
                      <small style={{ color: "var(--text-3)", display: "block" }}>
                        Estado KYC: {isVerified ? "✓ VERIFICADA (RENAPER)" : "PENDIENTE"} · Cuenta: {u.estado_usuario}
                      </small>
                    </div>

                    <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                      <span
                        className={`badge-status ${
                          role === "ADMIN" ? "badge-danger" : role === "ORGANIZADOR" ? "badge-info" : "badge-success"
                        }`}
                      >
                        {role}
                      </span>

                      {role !== "ADMIN" ? (
                        <button
                          type="button"
                          className="btn-copy"
                          onClick={() => handleToggleOrganizer(u)}
                          disabled={roleUpdatingId === u.id_usuario}
                        >
                          {roleUpdatingId === u.id_usuario
                            ? "Actualizando..."
                            : role === "ORGANIZADOR"
                            ? "Pasar a Cliente"
                            : "Habilitar como Organizador"}
                        </button>
                      ) : null}
                    </div>
                  </div>
                );
              })}
            </div>
          </section>

          {/* 2. Auditoría Técnica de Cruce de Compras, Pasarela (PCI-DSS) y Firma SHA-256 */}
          <section className="dashboard-panel" style={{ marginBottom: 24 }}>
            <div style={{ marginBottom: 14 }}>
              <h2 style={{ margin: 0 }}>Auditoría de Transacciones, Pasarela PCI-DSS y Cruce SHA-256 ({purchases.length})</h2>
              <p style={{ color: "var(--text-2)", fontSize: "0.85rem", margin: "4px 0 0" }}>
                Información técnica reservada al Administrador: referencias externas de pasarela, últimos 4 dígitos tokenizados, envío de comprobantes y hash criptográfico de cruce.
              </p>
            </div>

            <div className="table-list">
              {purchases.map((compra) => (
                <div key={compra.id} style={{ display: "grid", gap: 6 }}>
                  <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", flexWrap: "wrap", gap: 8 }}>
                    <div>
                      <strong>
                        Compra #{compra.id} · {compra.evento_titulo}
                      </strong>
                      <span>
                        Cliente: {compra.cliente_email} (DNI {compra.cliente_dni || "-"}) · {compra.cantidad} entrada(s) · {formatDate(compra.fecha_compra)}
                      </span>
                    </div>
                    <strong style={{ color: "var(--primary-2)" }}>{formatPrice(compra.total)}</strong>
                  </div>

                  {compra.pago ? (
                    <div style={{ fontSize: "0.82rem", color: "var(--text-2)" }}>
                      <strong>Pasarela:</strong> {compra.pago.pasarela_pago} · <strong>Medio:</strong> {compra.pago.medio_pago} (PCI-DSS:{" "}
                      <code>****{compra.pago.ultimos_cuatro_digitos}</code>) · <strong>Ref. Externa:</strong>{" "}
                      <code>{compra.pago.referencia_externa_pago}</code> · <strong>Estado:</strong> {compra.pago.estado_pago}
                    </div>
                  ) : null}

                  {compra.comprobante ? (
                    <div style={{ fontSize: "0.78rem", color: "var(--text-3)", wordBreak: "break-all" }}>
                      <strong>Comprobante:</strong> {compra.comprobante.numero_comprobante} ({compra.comprobante.tipo_comprobante} ·{" "}
                      {compra.comprobante.estado_envio_comprobante}) · <strong>Hash SHA-256 de Cruce:</strong>{" "}
                      <code>{compra.comprobante.hash_cruce_seguridad}</code>
                    </div>
                  ) : null}
                </div>
              ))}
            </div>
          </section>

          <div className="dashboard-grid">
            <section className="dashboard-panel">
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 14 }}>
                <h2>Categorías del Catálogo ({categories.length})</h2>
              </div>
              <div className="table-list">
                {categories.map((category) => (
                  <div key={category.id} style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <div>
                      <strong>{category.nombre}</strong>
                      <span>{category.descripcion || "Sin descripción"}</span>
                    </div>
                    <span className="badge-status badge-info">ID #{category.id}</span>
                  </div>
                ))}
              </div>
            </section>

            <section className="dashboard-panel">
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 14 }}>
                <h2>Recintos y Ubicaciones ({locations.length})</h2>
              </div>
              <div className="table-list">
                {locations.map((location) => (
                  <div key={location.id} style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <div>
                      <strong>{location.nombre}</strong>
                      <span>{location.direccion}</span>
                      <small style={{ color: "var(--text-3)", display: "block" }}>
                        Capacidad máxima: {location.capacidadMaxima} espectadores
                      </small>
                    </div>
                    <span className={`badge-status ${location.activo ? "badge-success" : "badge-danger"}`}>
                      {location.activo ? "Activa" : "Inactiva"}
                    </span>
                  </div>
                ))}
              </div>
            </section>
          </div>

          <section className="dashboard-panel" style={{ marginTop: 24 }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 14 }}>
              <h2>Bitácora de Tracking y Seguridad Informática ({auditLogs.length})</h2>
            </div>
            <div className="table-list">
              {auditLogs.map((log) => (
                <div key={log.id_auditoria} style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: 12 }}>
                  <div>
                    <strong>{log.accion_auditoria} · {log.entidad_afectada} #{log.id_registro_afectado ?? "-"}</strong>
                    <span>{log.detalle_tracking}</span>
                    <small style={{ color: "var(--text-3)", display: "block" }}>
                      Usuario: {log.email_usuario || "Anónimo"} · IP: {log.ip_origen || "127.0.0.1"} · {formatDate(log.fecha_auditoria)}
                    </small>
                  </div>
                  <span className={`badge-status ${log.resultado_auditoria === "EXITOSO" ? "badge-success" : "badge-danger"}`}>
                    {log.resultado_auditoria}
                  </span>
                </div>
              ))}
            </div>
          </section>
        </>
      )}
    </section>
  );
}

export default AdminDashboard;

