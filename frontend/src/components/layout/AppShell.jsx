import Navbar from "./Navbar";
import Footer from "./Footer";

function AppShell({ children }) {
  return (
    <div className="app-shell">
      <Navbar />
      <main>{children}</main>
      <Footer />
    </div>
  );
}

export default AppShell;
