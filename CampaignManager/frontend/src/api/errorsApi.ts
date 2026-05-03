import axios from "axios";

type ValidationErrors = Record<string, string>;

export function getErrorMsg(error: unknown): string {
  if (axios.isAxiosError(error)) {
    const responseData = error.response?.data;

    if (typeof responseData === "string") {
      return responseData;
    }

    if (responseData && typeof responseData === "object") {
      const validationErrors = responseData as ValidationErrors;
      return Object.values(validationErrors).join(", ");
    }
  }

  return "Unexpected error occurred";
}
