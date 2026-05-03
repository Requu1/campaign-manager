import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginSeller } from "../api/sellersApi";
import { getErrorMsg } from "../api/errorsApi";
import { ErrorMessage } from "../components/ErrorMsg";
import type { FormEvent } from "react";

type LoginPageProps = {
  onAuthSuccess: () => Promise<void>;
};

export function LoginPage({ onAuthSuccess }: LoginPageProps) {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [error, setError] = useState("");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    try {
      setError("");

      const seller = await loginSeller({
        email,
        password,
      });

      localStorage.setItem("sellerId", seller.id);

      await onAuthSuccess();

      navigate("/products");
    } catch (error) {
      setError(getErrorMsg(error));
    }
  }

  return (
    <main style={{ maxWidth: "420px", margin: "0 auto" }}>
      <h1 style={{ textAlign: "center" }}>Login</h1>

      <ErrorMessage message={error} />

      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: "12px" }}>
          <label>Email</label>
          <br />
          <input
            style={{ width: "100%", padding: "8px" }}
            type="text"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
          />
        </div>

        <div style={{ marginBottom: "12px" }}>
          <label>Password</label>
          <br />
          <input
            style={{ width: "100%", padding: "8px" }}
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />
        </div>

        <button type="submit">Login</button>
      </form>

      <p>
        No account? <Link to="/register">Register</Link>
      </p>
    </main>
  );
}
