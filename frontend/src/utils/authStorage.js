const AUTH_STORAGE_KEY = "butaca-auth-user";

export function saveAuthenticatedUser(user) {
  localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(user));
}

export function getAuthenticatedUser() {
  const raw = localStorage.getItem(AUTH_STORAGE_KEY);
  return raw ? JSON.parse(raw) : null;
}

export function clearAuthenticatedUser() {
  localStorage.removeItem(AUTH_STORAGE_KEY);
}

export const saveAuthUser = saveAuthenticatedUser;
export const getAuthUser = getAuthenticatedUser;
export const clearAuthUser = clearAuthenticatedUser;

