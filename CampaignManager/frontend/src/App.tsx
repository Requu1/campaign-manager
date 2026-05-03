import { useEffect, useState } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { Navbar } from "./components/Navbar";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { ProductsPage } from "./pages/ProductsPage";
import { CampaignsPage } from "./pages/CampaignsPage";
import { getSeller } from "./api/sellersApi";
import type { SellerResponse } from "./types/seller";

export function App() {
  const [seller, setSeller] = useState<SellerResponse | null>(null);

  async function refreshSeller() {
    const sellerId = localStorage.getItem("sellerId");

    if (!sellerId) {
      setSeller(null);
      return;
    }

    const currentSeller = await getSeller(sellerId);
    setSeller(currentSeller);
  }

  function handleLogout() {
    localStorage.removeItem("sellerId");
    setSeller(null);
  }

  useEffect(() => {
    refreshSeller().catch(() => {
      localStorage.removeItem("sellerId");
      setSeller(null);
    });
  }, []);

  return (
    <BrowserRouter>
      <div
        style={{
          maxWidth: "1000px",
          margin: "0 auto",
          padding: "24px",
        }}
      >
        <Navbar seller={seller} onLogout={handleLogout} />

        <Routes>
          <Route path="/" element={<Navigate to="/products" />} />

          <Route
            path="/login"
            element={<LoginPage onAuthSuccess={refreshSeller} />}
          />

          <Route
            path="/register"
            element={<RegisterPage onAuthSuccess={refreshSeller} />}
          />

          <Route path="/products" element={<ProductsPage />} />

          <Route
            path="/products/:productId/campaigns"
            element={<CampaignsPage onSellerBalanceChanged={refreshSeller} />}
          />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
