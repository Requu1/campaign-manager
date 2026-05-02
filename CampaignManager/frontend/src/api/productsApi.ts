import { clientApi } from "./clientApi";
import type { ProductCreateRequest, ProductResponse } from "../types/product";

export async function getProducts(): Promise<ProductResponse[]> {
  const response = await clientApi.get<ProductResponse[]>("/products");
  return response.data;
}

export async function createProduct(
  product: ProductCreateRequest,
): Promise<ProductResponse> {
  const response = await clientApi.post<ProductResponse>("/products", product);
  return response.data;
}

export async function updateProduct(
  productId: string,
  product: ProductCreateRequest,
): Promise<ProductResponse> {
  const response = await clientApi.put<ProductResponse>(
    `/products/${productId}`,
    product,
  );
  return response.data;
}

export async function deleteProduct(productId: string): Promise<void> {
  await clientApi.delete<void>(`/products/${productId}`);
}
