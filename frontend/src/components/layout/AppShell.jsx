import Navbar from "./Navbar";
import Footer from "./Footer";

function AppShell({ children, currentUser, onLogout }) {
  return (
    <div className="app-shell">
      <Navbar currentUser={currentUser} onLogout={onLogout} />
      <main>{children}</main>
      <Footer />
    </div>
  );
}

export default AppShell;
