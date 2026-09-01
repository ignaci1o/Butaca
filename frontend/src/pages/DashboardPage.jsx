import { Link } from "react-router-dom";
import ClienteDashboard from "./ClienteDashboard";
import OrganizerDashboard from "./OrganizerDashboard";

function DashboardPage({ currentUser }) {
  if (!currentUser) {
    return (
      <section className="container page-block">
        <div className="empty-state">
          <p>Inicie sesión para acceder a su cuenta.</p>
          <Link to="/login" className="pill-link">
            Ingresar
          </Link>
        </div>
      </section>
    );
  }

  if (currentUser.rol === "ORGANIZADOR") {
    return <OrganizerDashboard currentUser={currentUser} />;
  }

  return <ClienteDashboard currentUser={currentUser} />;
}

export default DashboardPage;
