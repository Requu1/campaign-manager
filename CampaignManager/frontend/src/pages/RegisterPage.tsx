import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerSeller } from "../api/sellersApi";
import { getErrorMsg } from "../api/errorsApi";
import { ErrorMessage } from "../components/ErrorMsg";
import type { FormEvent } from "react";

type RegisterPageProps = {
  onAuthSuccess: () => Promise<void>;
};

export function RegisterPage({ onAuthSuccess }: RegisterPageProps) {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [repeatedPassword, setRepeatedPassword] = useState("");

  const [error, setError] = useState("");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    if (password !== repeatedPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
      setError("");

      const seller = await registerSeller({
        username,
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
      <h1 style={{ textAlign: "center" }}>Register</h1>

      <ErrorMessage message={error} />

      <form onSubmit={handleSubmit} noValidate>
        <div style={{ marginBottom: "12px" }}>
          <label>Username</label>
          <br />
          <input
            style={{ width: "100%", padding: "8px" }}
            value={username}
            onChange={(event) => setUsername(event.target.value)}
          />
        </div>

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

        <div style={{ marginBottom: "12px" }}>
          <label>Repeat password</label>
          <br />
          <input
            style={{ width: "100%", padding: "8px" }}
            type="password"
            value={repeatedPassword}
            onChange={(event) => setRepeatedPassword(event.target.value)}
          />
        </div>

        <button type="submit">Register</button>
      </form>

      <p>
        Already have an account? <Link to="/login">Login</Link>
      </p>
    </main>
  );
}
