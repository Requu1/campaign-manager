export type CampaignStatus = "ON" | "OFF";

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
  id: string;
  productId: string;
  name: string;
  keywords: string[];
  bidAmount: number;
  campaignFund: number;
  status: CampaignStatus;
  town: string;
  radius: number;
  newEmeraldBalance: number;
}
