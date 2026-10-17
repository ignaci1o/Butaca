import { useState } from "react";
import { formatDate } from "../../utils/formatters";
import QRCodeImage, { downloadTicketAsImage } from "../ui/QRCodeImage";

function TicketPass({ ticket, eventTitle, venue, date, time, buyerName }) {
  const [showModal, setShowModal] = useState(false);
  const qrData = ticket.codigo_unico_entrada || ticket.codigoUnicoEntrada || ticket.id || "BUTACA-TICKET";
  const titularNominal = ticket.nombre_asistente || ticket.nombreAsistente || buyerName || "Titular Nominal";
  const dniNominal = ticket.dni_asistente || ticket.dniAsistente || "";

  function handleDownload() {
    downloadTicketAsImage({
      ticket,
      eventTitle,
      venue,
      date: date ? formatDate(date) : "",
      time,
      buyerName: dniNominal ? `${titularNominal} (DNI ${dniNominal})` : titularNominal
    });
  }

  return (
    <>
      <div className="ticket-pass">
        <div className="ticket-pass-main">
          <div className="ticket-pass-header">
            <h4 className="ticket-pass-title" style={{ fontSize: "1.12rem", fontWeight: 700 }}>
              {eventTitle || "Entrada Butaca"}
            </h4>
          </div>

          <div className="ticket-pass-meta" style={{ marginTop: 4 }}>
            {date ? <span>{formatDate(date)} {time ? `· ${time} hs` : ""}</span> : null}
            {venue ? <span>· {venue}</span> : null}
          </div>

          <div style={{ marginTop: 6, fontSize: "0.84rem", color: "var(--text-2)" }}>
            Asistente nominal: <strong>{titularNominal}</strong>
            {dniNominal ? <> · DNI: <strong>{dniNominal}</strong></> : null}
          </div>

          <div style={{ display: "flex", flexWrap: "wrap", gap: 10, marginTop: 14 }}>
            <button
              type="button"
              className="btn-copy"
              style={{
                background: "linear-gradient(135deg, var(--primary), var(--primary-2))",
                color: "#ffffff",
                borderColor: "transparent",
                fontWeight: 600,
                padding: "8px 16px",
                fontSize: "0.85rem"
              }}
              onClick={handleDownload}
              title="Descargar comprobante en imagen"
            >
              Descargar Entrada
            </button>

            <button
              type="button"
              className="btn-copy"
              onClick={() => setShowModal(true)}
              style={{
                padding: "8px 16px",
                fontSize: "0.85rem",
                color: "var(--text)"
              }}
              title="Visualizar código QR"
            >
              Visualizar Entrada
            </button>
          </div>
        </div>

        <div
          className="ticket-pass-side"
          style={{ cursor: "pointer" }}
          onClick={() => setShowModal(true)}
          title="Clic para visualizar"
        >
          <QRCodeImage value={qrData} size={90} />
        </div>
      </div>

      {/* Modal de visualización */}
      {showModal ? (
        <div
          style={{
            position: "fixed",
            inset: 0,
            background: "rgba(0, 0, 0, 0.8)",
            backdropFilter: "blur(6px)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            zIndex: 9999,
            padding: 20
          }}
          onClick={() => setShowModal(false)}
        >
          <div
            style={{
              background: "var(--surface)",
              border: "1px solid var(--border)",
              borderRadius: "var(--r-lg)",
              padding: "36px 32px",
              maxWidth: 420,
              width: "100%",
              textAlign: "center",
              position: "relative",
              boxShadow: "0 24px 48px rgba(0,0,0,0.6)"
            }}
            onClick={(e) => e.stopPropagation()}
          >
            <button
              type="button"
              onClick={() => setShowModal(false)}
              style={{
                position: "absolute",
                top: 14,
                right: 14,
                background: "rgba(255,255,255,0.06)",
                border: "none",
                color: "var(--text)",
                fontSize: "1.1rem",
                width: 32,
                height: 32,
                borderRadius: "50%",
                cursor: "pointer",
                display: "flex",
                alignItems: "center",
                justifyContent: "center"
              }}
              aria-label="Cerrar"
            >
              ✕
            </button>

            <h3 style={{ margin: "0 0 8px", fontSize: "1.25rem", color: "var(--text)", fontWeight: 700 }}>
              {eventTitle || "Entrada Oficial Nominal"}
            </h3>

            <p style={{ margin: "0 0 8px", color: "var(--text-2)", fontSize: "0.9rem" }}>
              {date ? formatDate(date) : ""} {time ? `· ${time} hs` : ""}
            </p>

            <p style={{ margin: "0 0 20px", color: "var(--text)", fontSize: "0.9rem" }}>
              Titular: <strong>{titularNominal}</strong> {dniNominal ? `(DNI ${dniNominal})` : ""}
            </p>

            <div style={{ display: "inline-block", margin: "0 auto 24px" }}>
              <QRCodeImage value={qrData} size={220} />
            </div>

            <div style={{ display: "flex", justifyContent: "center", gap: 12 }}>
              <button
                type="button"
                className="btn-copy"
                style={{
                  background: "linear-gradient(135deg, var(--primary), var(--primary-2))",
                  color: "#ffffff",
                  borderColor: "transparent",
                  fontWeight: 600,
                  padding: "9px 20px"
                }}
                onClick={handleDownload}
              >
                Descargar
              </button>
              <button
                type="button"
                className="btn-copy"
                style={{ padding: "9px 20px", color: "var(--text)" }}
                onClick={() => setShowModal(false)}
              >
                Cerrar
              </button>
            </div>
          </div>
        </div>
      ) : null}
    </>
  );
}

export default TicketPass;
