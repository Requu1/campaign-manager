import { useEffect, useState } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { Navbar } from "./components/Navbar";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { ProductsPage } from "./pages/ProductsPage";
import { CampaignsPage } from "./pages/CampaignsPage";
import { getCurrentSeller, logoutSeller } from "./api/sellersApi";
import type { SellerResponse } from "./types/seller";

export function App() {
  const [seller, setSeller] = useState<SellerResponse | null>(null);

  async function refreshSeller() {
    try {
      const currentSeller = await getCurrentSeller();
      setSeller(currentSeller);
    } catch {
      setSeller(null);
    }
  }

  async function handleLogout() {
    try {
      await logoutSeller();
    } finally {
      setSeller(null);
    }
  }

  useEffect(() => {
    refreshSeller();
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
          <Route path="/" element={<Navigate to="/login" />} />

          <Route
            path="/login"
            element={<LoginPage onAuthSuccess={refreshSeller} />}
          />

          <Route
            path="/register"
            element={<RegisterPage onAuthSuccess={refreshSeller} />}
          />

          <Route
            path="/products"
            element={<ProductsPage onSellerBalanceChanged={refreshSeller} />}
          />

          <Route
            path="/products/:productId/campaigns"
            element={<CampaignsPage onSellerBalanceChanged={refreshSeller} />}
          />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
