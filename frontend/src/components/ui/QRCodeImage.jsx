import { useMemo, useState } from "react";
import { generateQRMatrix } from "../../utils/qrCode";

function QRCodeImage({ value, size = 150, className = "" }) {
  const [imgError, setImgError] = useState(false);
  const textValue = value || "BUTACA-ACCESO-DIGITAL";

  // Fallback a matriz SVG local si no hay conexión externa
  const matrix = useMemo(() => {
    try {
      return generateQRMatrix(textValue);
    } catch {
      return null;
    }
  }, [textValue]);

  // URL del código QR nítido
  const qrApiUrl = `https://api.qrserver.com/v1/create-qr-code/?size=${size * 2}x${size * 2}&margin=8&data=${encodeURIComponent(textValue)}`;

  if (!imgError) {
    return (
      <img
        src={qrApiUrl}
        alt="Código QR de Acceso"
        width={size}
        height={size}
        className={className}
        onError={() => setImgError(true)}
        style={{
          borderRadius: "12px",
          background: "#ffffff",
          padding: "6px",
          boxShadow: "0 6px 20px rgba(0,0,0,0.25)",
          display: "block",
          objectFit: "contain"
        }}
      />
    );
  }

  // Fallback SVG local
  if (!matrix) {
    return <div style={{ width: size, height: size, background: "#ffffff", borderRadius: "12px" }} />;
  }

  const moduleCount = matrix.length;
  return (
    <svg
      width={size}
      height={size}
      viewBox={`0 0 ${moduleCount} ${moduleCount}`}
      className={className}
      style={{
        background: "#ffffff",
        borderRadius: "12px",
        padding: "8px",
        boxShadow: "0 6px 20px rgba(0,0,0,0.25)",
        display: "block"
      }}
      shapeRendering="crispEdges"
    >
      <rect width={moduleCount} height={moduleCount} fill="#ffffff" />
      {matrix.map((row, r) =>
        row.map((isDark, c) =>
          isDark ? (
            <rect
              key={`${r}-${c}`}
              x={c}
              y={r}
              width={1}
              height={1}
              fill="#0d1117"
            />
          ) : null
        )
      )}
    </svg>
  );
}

export function downloadTicketAsImage({ ticket, eventTitle, venue, date, time, buyerName }) {
  const canvas = document.createElement("canvas");
  const width = 680;
  const height = 360;
  canvas.width = width;
  canvas.height = height;
  const ctx = canvas.getContext("2d");

  const qrData = ticket.codigo_unico_entrada || ticket.codigoUnicoEntrada || ticket.id || "BUTACA-TICKET";
  const matrix = generateQRMatrix(qrData);
  const moduleCount = matrix.length;

  // Background con gradiente elegante
  const gradient = ctx.createLinearGradient(0, 0, width, height);
  gradient.addColorStop(0, "#0c131f");
  gradient.addColorStop(0.5, "#141c2c");
  gradient.addColorStop(1, "#1a253a");
  ctx.fillStyle = gradient;
  ctx.fillRect(0, 0, width, height);

  // Marco exterior
  ctx.strokeStyle = "rgba(232, 109, 63, 0.6)";
  ctx.lineWidth = 3;
  ctx.strokeRect(12, 12, width - 24, height - 24);

  // Cabecera
  ctx.fillStyle = "#e86d3f";
  ctx.font = "bold 13px sans-serif";
  ctx.fillText("BUTACA TICKETS · PASE DIGITAL OFICIAL", 36, 44);

  // Título del evento
  ctx.fillStyle = "#ffffff";
  ctx.font = "bold 22px sans-serif";
  const truncatedTitle = (eventTitle || "Entrada de Espectáculo").length > 30
    ? (eventTitle || "Entrada de Espectáculo").substring(0, 30) + "..."
    : (eventTitle || "Entrada de Espectáculo");
  ctx.fillText(truncatedTitle, 36, 88);

  // Datos principales
  ctx.fillStyle = "#94a3b8";
  ctx.font = "14px sans-serif";
  ctx.fillText(`Lugar:`, 36, 130);
  ctx.fillStyle = "#f8fafc";
  ctx.fillText(venue || "Ubicación confirmada", 90, 130);

  ctx.fillStyle = "#94a3b8";
  ctx.fillText(`Fecha:`, 36, 162);
  ctx.fillStyle = "#f8fafc";
  ctx.fillText(`${date || "Próximamente"} ${time ? `· ${time} hs` : ""}`, 90, 162);

  ctx.fillStyle = "#94a3b8";
  ctx.fillText(`Titular:`, 36, 194);
  ctx.fillStyle = "#f8fafc";
  ctx.fillText(buyerName || "Espectador Butaca", 90, 194);

  // Línea divisoria
  ctx.strokeStyle = "rgba(255, 255, 255, 0.12)";
  ctx.lineWidth = 1;
  ctx.beginPath();
  ctx.moveTo(36, 230);
  ctx.lineTo(width - 36, 230);
  ctx.stroke();

  // Pie de tarjeta
  ctx.fillStyle = "#e86d3f";
  ctx.font = "bold 13px sans-serif";
  ctx.fillText("ENTRADA VÁLIDA PARA INGRESO DIRECTO", 36, 270);

  ctx.fillStyle = "#64748b";
  ctx.font = "12px sans-serif";
  ctx.fillText("Presente el código QR en el lector al ingresar al recinto.", 36, 298);
  ctx.fillText("Válido para un único acceso.", 36, 318);

  // Código QR (Lado derecho)
  const qrSize = 160;
  const qrX = width - qrSize - 36;
  const qrY = 46;

  // Fondo blanco del QR
  ctx.fillStyle = "#ffffff";
  ctx.beginPath();
  ctx.roundRect ? ctx.roundRect(qrX - 10, qrY - 10, qrSize + 20, qrSize + 20, 10) : ctx.fillRect(qrX - 10, qrY - 10, qrSize + 20, qrSize + 20);
  ctx.fill();

  // Dibujar matriz del QR
  const cellSize = qrSize / moduleCount;
  ctx.fillStyle = "#0c131f";
  for (let r = 0; r < moduleCount; r++) {
    for (let c = 0; c < moduleCount; c++) {
      if (matrix[r][c]) {
        ctx.fillRect(qrX + c * cellSize, qrY + r * cellSize, cellSize + 0.35, cellSize + 0.35);
      }
    }
  }

  // Leyenda bajo QR
  ctx.fillStyle = "#94a3b8";
  ctx.font = "bold 10px sans-serif";
  ctx.textAlign = "center";
  ctx.fillText("ESCANEAR EN PUERTA", qrX + qrSize / 2, qrY + qrSize + 28);
  ctx.textAlign = "left";

  // Descargar archivo
  const link = document.createElement("a");
  const cleanTitle = (eventTitle || "entrada").toLowerCase().replace(/[^a-z0-9]/g, "-");
  link.download = `entrada-butaca-${cleanTitle}.png`;
  link.href = canvas.toDataURL("image/png");
  link.click();
}

export default QRCodeImage;
