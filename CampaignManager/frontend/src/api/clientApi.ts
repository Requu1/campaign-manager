import axios from "axios";

const apiUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";

export const clientApi = axios.create({
  baseURL: `${apiUrl}/api`,
  withCredentials: true,
});
