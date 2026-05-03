import { clientApi } from "./clientApi";
import type {
  SellerLoginRequest,
  SellerRegisterRequest,
  SellerResponse,
} from "../types/seller";

export async function registerSeller(
  data: SellerRegisterRequest,
): Promise<SellerResponse> {
  const response = await clientApi.post<SellerResponse>(
    "/sellers/register",
    data,
  );
  return response.data;
}

export async function loginSeller(
  data: SellerLoginRequest,
): Promise<SellerResponse> {
  const response = await clientApi.post<SellerResponse>("/sellers/login", data);
  return response.data;
}

export async function getCurrentSeller(): Promise<SellerResponse> {
  const response = await clientApi.get<SellerResponse>("/sellers/me");
  return response.data;
}

export async function logoutSeller(): Promise<void> {
  await clientApi.post<void>("/sellers/logout");
}
