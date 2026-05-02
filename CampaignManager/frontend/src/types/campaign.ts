export type CampaignStatus = "ON" | "OF";

export interface CampaignCreateRequest {
  name: string;
  keywords: string[];
  bidAmount: number;
  campaignFund: number;
  status: CampaignStatus;
  town: string;
  radius: number;
}

export interface CampaignResponse {
  id: number;
  productId: number;
  name: string;
  keywords: string;
  bidAmount: number;
  campaignFund: number;
  status: CampaignStatus;
  town: string;
  radius: number;
  newEmeraldBalance: number;
}
