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
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        marginBottom: "32px",
        padding: "12px 0",
        borderBottom: "1px solid #ddd",
      }}
    >
      <div style={{ display: "flex", gap: "16px" }}>
        {seller && <Link to="/products">Products</Link>}

        {!seller && <Link to="/login">Login</Link>}
        {!seller && <Link to="/register">Register</Link>}

        {seller && (
          <button type="button" onClick={handleLogout}>
            Logout
          </button>
        )}
      </div>

      {seller && (
        <div style={{ fontWeight: "bold" }}>
          Emerald balance: {seller.emeraldBalance}
        </div>
      )}
    </nav>
  );
}
