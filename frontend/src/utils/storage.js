export function saveSelectedOrder(order) {
  localStorage.setItem("butaca-order", JSON.stringify(order));
}

export function getSelectedOrder() {
  const raw = localStorage.getItem("butaca-order");
  return raw ? JSON.parse(raw) : null;
}

export function clearSelectedOrder() {
  localStorage.removeItem("butaca-order");
}
