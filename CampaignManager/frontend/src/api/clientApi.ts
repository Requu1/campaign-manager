import axios from "axios";

export const clientApi = axios.create({ baseURL: "http://localhost:8080/api" });

clientApi.interceptors.request.use((config) => {
  const sellerId = localStorage.getItem("sellerId");

  if (sellerId) {
    config.headers.SellerId = sellerId;
  }

  return config;
});
