import axios from "axios";

const apiUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";
axios.get(`${apiUrl}/api`);

export const clientApi = axios.create({
  baseURL: apiUrl,
  withCredentials: true,
});
