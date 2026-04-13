const [major] = process.versions.node.split(".").map(Number);

if (major !== 20) {
  console.error("Node.js 20.x es obligatorio para este proyecto.");
  console.error(`Version detectada: ${process.version}`);
  console.error("Instala o activa Node 20 antes de ejecutar npm install o npm run dev.");
  process.exit(1);
}

console.log(`Node.js ${process.version} detectado. Entorno compatible.`);
