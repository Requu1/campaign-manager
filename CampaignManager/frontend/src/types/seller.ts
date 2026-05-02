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
  username: string;
  email: string;
  emeraldBalance: number;
}
