import { clientApi } from "./clientApi";
import type {
  CampaignCreateRequest,
  CampaignResponse,
} from "../types/campaign";

export async function getCampaigns(
  productId: string,
): Promise<CampaignResponse[]> {
  const response = await clientApi.get<CampaignResponse[]>(
    `/products/${productId}/campaigns`,
  );
  return response.data;
}

export async function createCampaign(
  productId: string,
  campaign: CampaignCreateRequest,
): Promise<CampaignResponse> {
  const response = await clientApi.post<CampaignResponse>(
    `/products/${productId}/campaigns`,
    campaign,
  );
  return response.data;
}

export async function updateCampaign(
  productId: string,
  campaignId: string,
  campaign: CampaignCreateRequest,
): Promise<CampaignResponse> {
  const response = await clientApi.put<CampaignResponse>(
    `/products/${productId}/campaigns/${campaignId}`,
    campaign,
  );
  return response.data;
}

export async function deleteCampaign(
  productId: string,
  campaignId: string,
): Promise<void> {
  await clientApi.delete<void>(
    `/products/${productId}/campaigns/${campaignId}`,
  );
}
