import { request } from "./apiClient";

export const useMockAuth = false;

const mockUsers = [
  {
    id: 1,
    nombre: "Leandro",
    apellido: "Gramajo Gargiulo",
    email: "organizador@butaca.local",
    password: "clave123",
    rol: "ORGANIZADOR"
  },
  {
    id: 2,
    nombre: "Mariano",
    apellido: "Piovesan",
    email: "cliente@butaca.local",
    password: "clave123",
    rol: "CLIENTE"
  },
  {
    id: 3,
    nombre: "Santiago",
    apellido: "Gonzalez",
    email: "admin@butaca.local",
    password: "clave123",
    rol: "ADMIN"
  }
];

export async function loginUser(credentials) {
  if (useMockAuth) {
    const user = mockUsers.find(
      (candidate) =>
        candidate.email.toLowerCase() === credentials.email.trim().toLowerCase() &&
        candidate.password === credentials.password
    );

    if (!user) {
      throw new Error("Credenciales invalidas");
    }

    return {
      id: user.id,
      nombre: user.nombre,
      apellido: user.apellido,
      email: user.email,
      rol: user.rol,
      mensaje: "Inicio de sesion correcto"
    };
  }

  const payload = {
    email_usuario: credentials.email_usuario || credentials.email,
    password_usuario: credentials.password_usuario || credentials.password
  };

  const response = await request("/auth/login", {
    method: "POST",
    body: JSON.stringify(payload)
  });
  return normalizeAuthUser(response);
}

export async function registerUser(userData) {
  if (useMockAuth) {
    throw new Error("El registro requiere conexión real al backend");
  }

  const payload = {
    nombre_usuario: userData.nombre_usuario || userData.nombre,
    apellido_usuario: userData.apellido_usuario || userData.apellido,
    email_usuario: userData.email_usuario || userData.email,
    password_usuario: userData.password_usuario || userData.password,
    rol_usuario: normalizeRole(userData.rol_usuario || userData.rol || "CLIENTE"),
    telefono: userData.telefono || ""
  };

  const response = await request("/auth/register", {
    method: "POST",
    body: JSON.stringify(payload)
  });
  return normalizeAuthUser(response);
}

export function normalizeRole(role) {
  const normalized = String(role || "CLIENTE").trim().toUpperCase();
  return normalized === "CLIENT" ? "CLIENTE" : normalized;
}

export function normalizeAuthUser(user) {
  const rol = normalizeRole(user.rol_usuario || user.rolUsuario || user.rol);
  return {
    ...user,
    id: user.id_usuario ?? user.idUsuario ?? user.id,
    id_usuario: user.id_usuario ?? user.idUsuario ?? user.id,
    nombre: user.nombre_usuario ?? user.nombreUsuario ?? user.nombre,
    apellido: user.apellido_usuario ?? user.apellidoUsuario ?? user.apellido,
    email: user.email_usuario ?? user.emailUsuario ?? user.email,
    rol,
    rol_usuario: rol
  };
}
