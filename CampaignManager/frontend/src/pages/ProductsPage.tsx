import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import type { ProductResponse } from "../types/product";
import {
  createProduct,
  deleteProduct,
  getProducts,
  updateProduct,
} from "../api/productsApi";
import type { FormEvent } from "react";
import { getErrorMsg } from "../api/errorsApi";
import { ErrorMessage } from "../components/ErrorMsg";

export function ProductsPage() {
  const navigate = useNavigate();

  const [products, setProducts] = useState<ProductResponse[]>([]);
  const [name, setName] = useState("");
  const [editingProductId, setEditingProductId] = useState<string | null>(null);

  const [error, setError] = useState("");

  useEffect(() => {
    const sellerId = localStorage.getItem("sellerId");

    if (!sellerId) {
      navigate("/login");
      return;
    }

    loadProducts();
  }, [navigate]);

  async function loadProducts() {
    try {
      setError("");
      const data = await getProducts();
      setProducts(data);
    } catch (error) {
      setError(getErrorMsg(error));
    }
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    try {
      setError("");

      if (editingProductId) {
        await updateProduct(editingProductId, { name });
      } else {
        await createProduct({ name });
      }

      setName("");
      setEditingProductId(null);
      await loadProducts();
    } catch (error) {
      setError(getErrorMsg(error));
    }
  }

  function startEditing(product: ProductResponse) {
    setEditingProductId(product.id);
    setName(product.name);
  }

  function cancelEditing() {
    setEditingProductId(null);
    setName("");
  }

  async function handleDelete(productId: string) {
    const confirmed = window.confirm(
      "Are you sure you want to delete this product?",
    );

    if (!confirmed) {
      return;
    }

    try {
      setError("");
      await deleteProduct(productId);
      await loadProducts();
    } catch (error) {
      setError(getErrorMsg(error));
    }
  }

  return (
    <main>
      <h1 style={{ textAlign: "center" }}>Products</h1>

      <ErrorMessage message={error} />

      <section
        style={{
          maxWidth: "500px",
          margin: "0 auto 32px auto",
          padding: "16px",
          border: "1px solid #ddd",
          borderRadius: "8px",
        }}
      >
        <form onSubmit={handleSubmit}>
          <h2>{editingProductId ? "Edit product" : "Add product"}</h2>

          <input
            style={{ width: "100%", padding: "8px", marginBottom: "12px" }}
            value={name}
            onChange={(event) => setName(event.target.value)}
            placeholder="Product name"
          />

          <button type="submit">
            {editingProductId ? "Update product" : "Add product"}
          </button>

          {editingProductId && (
            <button type="button" onClick={cancelEditing}>
              Cancel
            </button>
          )}
        </form>
      </section>

      <section>
        <h2 style={{ textAlign: "center" }}>Product list</h2>

        {products.length === 0 ? (
          <p style={{ textAlign: "center" }}>No products yet.</p>
        ) : (
          <ul style={{ maxWidth: "700px", margin: "0 auto" }}>
            {products.map((product) => (
              <li
                key={product.id}
                style={{
                  marginBottom: "12px",
                  padding: "16px",
                  border: "1px solid #ddd",
                  borderRadius: "8px",
                  listStyle: "none",
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  gap: "16px",
                }}
              >
                <div>
                  <strong style={{ fontSize: "18px" }}>{product.name}</strong>
                </div>

                <div style={{ display: "flex", gap: "8px" }}>
                  <button
                    type="button"
                    onClick={() =>
                      navigate(`/products/${product.id}/campaigns`, {
                        state: { productName: product.name },
                      })
                    }
                    style={{
                      padding: "8px 12px",
                      backgroundColor: "#1f6feb",
                      color: "white",
                      border: "none",
                      borderRadius: "6px",
                      fontWeight: "bold",
                      cursor: "pointer",
                    }}
                  >
                    Manage campaigns
                  </button>

                  <button type="button" onClick={() => startEditing(product)}>
                    Edit
                  </button>

                  <button
                    type="button"
                    onClick={() => handleDelete(product.id)}
                  >
                    Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </section>
    </main>
  );
}
