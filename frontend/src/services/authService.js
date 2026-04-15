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

  return request("/auth/login", {
    method: "POST",
    body: JSON.stringify(credentials)
  });
}
