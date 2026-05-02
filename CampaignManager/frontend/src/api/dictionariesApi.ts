import { clientApi } from "./clientApi";

export async function getKeywords(): Promise<string[]> {
  const response = await clientApi.get<string[]>("/dictionaries/keywords");
  return response.data;
}

export async function getTowns(): Promise<string[]> {
  const response = await clientApi.get<string[]>("/dictionaries/towns");
  return response.data;
}
