export interface SellerRegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface SellerLoginRequest {
  email: string;
  password: string;
}

export interface SellerResponse {
  id: string;
  username: string;
  email: string;
  emeraldBalance: number;
}
