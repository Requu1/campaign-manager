import { Link, useNavigate } from "react-router-dom";
import type { SellerResponse } from "../types/seller";

type NavbarProps = {
  seller: SellerResponse | null;
  onLogout: () => void;
};

export function Navbar({ seller, onLogout }: NavbarProps) {
  const navigate = useNavigate();

  function handleLogout() {
    onLogout();
    navigate("/login");
  }

  return (
    <nav
      style={{
        display: "grid",
        gridTemplateColumns: "1fr auto 1fr",
        alignItems: "center",
        marginBottom: "32px",
        padding: "12px 0",
        borderBottom: "1px solid #ddd",
      }}
    >
      <div
        style={{ display: "flex", gap: "16px", justifyContent: "flex-start" }}
      >
        {!seller && <Link to="/login">Login</Link>}
        {!seller && <Link to="/register">Register</Link>}

        {seller && (
          <div style={{ fontWeight: "bold" }}>Hi {seller.username}!</div>
        )}

        {seller && (
          <button type="button" onClick={handleLogout}>
            Logout
          </button>
        )}
      </div>

      <div
        style={{
          fontWeight: "bold",
          fontSize: "25px",
          textAlign: "center",
        }}
      >
        {!seller && <div style={{ fontWeight: "bold" }}>Campaign Manager</div>}
      </div>

      <div style={{ display: "flex", justifyContent: "flex-end" }}>
        {seller && (
          <div style={{ fontWeight: "bold" }}>
            Emerald balance: {seller.emeraldBalance}
          </div>
        )}
      </div>
    </nav>
  );
}
