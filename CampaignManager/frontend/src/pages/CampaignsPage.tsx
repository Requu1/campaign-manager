import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import type {
  CampaignCreateRequest,
  CampaignResponse,
  CampaignStatus,
} from "../types/campaign";
import type { FormEvent } from "react";
import {
  createCampaign,
  deleteCampaign,
  getCampaigns,
  updateCampaign,
} from "../api/campaignsApi";
import { getKeywords, getTowns } from "../api/dictionariesApi";
import { getErrorMsg } from "../api/errorsApi";
import { ErrorMessage } from "../components/ErrorMsg";

type CampaignsPageProps = {
  onSellerBalanceChanged: () => Promise<void>;
};

type LocationState = {
  productName?: string;
};

export function CampaignsPage({ onSellerBalanceChanged }: CampaignsPageProps) {
  const navigate = useNavigate();
  const location = useLocation();
  const { productId } = useParams<{ productId: string }>();

  const state = location.state as LocationState | null;
  const productName = state?.productName;

  const [campaigns, setCampaigns] = useState<CampaignResponse[]>([]);
  const [towns, setTowns] = useState<string[]>([]);
  const [keywordSuggestions, setKeywordSuggestions] = useState<string[]>([]);

  const [editingCampaignId, setEditingCampaignId] = useState<string | null>(
    null,
  );
  const [isFormOpen, setIsFormOpen] = useState(false);

  const [name, setName] = useState("");
  const [keywords, setKeywords] = useState("");
  const [bidAmount, setBidAmount] = useState("");
  const [campaignFund, setCampaignFund] = useState("");
  const [status, setStatus] = useState<CampaignStatus>("ON");
  const [town, setTown] = useState("");
  const [radius, setRadius] = useState("");

  const [error, setError] = useState("");

  useEffect(() => {
    const sellerId = localStorage.getItem("sellerId");

    if (!sellerId) {
      navigate("/login");
      return;
    }

    if (!productId) {
      navigate("/products");
      return;
    }

    loadInitialData(productId);
  }, [navigate, productId]);

  async function loadInitialData(currentProductId: string) {
    try {
      setError("");

      const [campaignsData, townsData, keywordsData] = await Promise.all([
        getCampaigns(currentProductId),
        getTowns(),
        getKeywords(),
      ]);

      setCampaigns(campaignsData);
      setTowns(townsData);
      setKeywordSuggestions(keywordsData);

      if (townsData.length > 0) {
        setTown(townsData[0]);
      }
    } catch (error) {
      setError(getErrorMsg(error));
    }
  }

  async function loadCampaigns() {
    if (!productId) {
      return;
    }

    try {
      setError("");
      const data = await getCampaigns(productId);
      setCampaigns(data);
    } catch (error) {
      setError(getErrorMsg(error));
    }
  }

  function resetForm() {
    setEditingCampaignId(null);
    setName("");
    setKeywords("");
    setBidAmount("");
    setCampaignFund("");
    setStatus("ON");
    setTown(towns.length > 0 ? towns[0] : "");
    setRadius("");
    setIsFormOpen(false);
  }

  function buildCampaignRequest(): CampaignCreateRequest {
    return {
      name,
      keywords: keywords
        .split(",")
        .map((keyword) => keyword.trim())
        .filter((keyword) => keyword.length > 0),
      bidAmount: Number(bidAmount),
      campaignFund: Number(campaignFund),
      status,
      town,
      radius: Number(radius),
    };
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    if (!productId) {
      return;
    }

    try {
      setError("");

      const campaignRequest = buildCampaignRequest();

      if (editingCampaignId) {
        await updateCampaign(productId, editingCampaignId, campaignRequest);
      } else {
        await createCampaign(productId, campaignRequest);
      }

      resetForm();
      await loadCampaigns();
      await onSellerBalanceChanged();
    } catch (error) {
      setError(getErrorMsg(error));
    }
  }

  function openCreateForm() {
    setEditingCampaignId(null);
    setName("");
    setKeywords("");
    setBidAmount("");
    setCampaignFund("");
    setStatus("ON");
    setTown(towns.length > 0 ? towns[0] : "");
    setRadius("");
    setIsFormOpen((current) => !current);
  }

  function startEditing(campaign: CampaignResponse) {
    setEditingCampaignId(campaign.id);
    setName(campaign.name);
    setKeywords(campaign.keywords.join(", "));
    setBidAmount(String(campaign.bidAmount));
    setCampaignFund(String(campaign.campaignFund));
    setStatus(campaign.status);
    setTown(campaign.town);
    setRadius(String(campaign.radius));
    setIsFormOpen(true);
  }

  async function handleDelete(campaignId: string) {
    if (!productId) {
      return;
    }

    const confirmed = window.confirm(
      "Are you sure you want to delete this campaign?",
    );

    if (!confirmed) {
      return;
    }

    try {
      setError("");
      await deleteCampaign(productId, campaignId);
      await loadCampaigns();
      await onSellerBalanceChanged();
    } catch (error) {
      setError(getErrorMsg(error));
    }
  }

  return (
    <main>
      <Link to="/products">Back to products</Link>

      <h1 style={{ textAlign: "center" }}>Campaigns</h1>

      <p style={{ textAlign: "center", fontSize: "18px" }}>
        Product: <strong>{productName ?? productId}</strong>
      </p>

      <ErrorMessage message={error} />

      <div
        style={{
          display: "flex",
          justifyContent: "center",
          marginBottom: "24px",
        }}
      >
        <button
          type="button"
          onClick={openCreateForm}
          style={{
            padding: "10px 16px",
            fontWeight: "bold",
            cursor: "pointer",
          }}
        >
          {isFormOpen && !editingCampaignId ? "Hide form" : "Add campaign"}
        </button>
      </div>

      {isFormOpen && (
        <section
          style={{
            maxWidth: "600px",
            margin: "0 auto 32px auto",
            padding: "16px",
            border: "1px solid #ddd",
            borderRadius: "8px",
            backgroundColor: "#fafafa",
          }}
        >
          <form onSubmit={handleSubmit}>
            <h2>{editingCampaignId ? "Edit campaign" : "Add campaign"}</h2>

            {editingCampaignId && (
              <p>
                Editing campaign for product:{" "}
                <strong>{productName ?? productId}</strong>
              </p>
            )}

            <div style={{ marginBottom: "12px" }}>
              <label>Campaign name</label>
              <br />
              <input
                style={{ width: "100%", padding: "8px" }}
                value={name}
                onChange={(event) => setName(event.target.value)}
              />
            </div>

            <div style={{ marginBottom: "12px" }}>
              <label>Keywords separated by commas</label>
              <br />
              <input
                style={{ width: "100%", padding: "8px" }}
                value={keywords}
                onChange={(event) => setKeywords(event.target.value)}
                placeholder="gaming, laptop, sale"
              />

              {keywordSuggestions.length > 0 && (
                <p>Suggestions: {keywordSuggestions.join(", ")}</p>
              )}
            </div>

            <div style={{ marginBottom: "12px" }}>
              <label>Bid amount</label>
              <br />
              <input
                style={{ width: "100%", padding: "8px" }}
                type="number"
                value={bidAmount}
                onChange={(event) => setBidAmount(event.target.value)}
              />
            </div>

            <div style={{ marginBottom: "12px" }}>
              <label>Campaign fund</label>
              <br />
              <input
                style={{ width: "100%", padding: "8px" }}
                type="number"
                value={campaignFund}
                onChange={(event) => setCampaignFund(event.target.value)}
              />
            </div>

            <div style={{ marginBottom: "12px" }}>
              <label>Status</label>
              <br />
              <select
                style={{ width: "100%", padding: "8px" }}
                value={status}
                onChange={(event) =>
                  setStatus(event.target.value as CampaignStatus)
                }
              >
                <option value="ON">ON</option>
                <option value="OFF">OFF</option>
              </select>
            </div>

            <div style={{ marginBottom: "12px" }}>
              <label>Town</label>
              <br />
              <select
                style={{ width: "100%", padding: "8px" }}
                value={town}
                onChange={(event) => setTown(event.target.value)}
              >
                {towns.map((townOption) => (
                  <option key={townOption} value={townOption}>
                    {townOption}
                  </option>
                ))}
              </select>
            </div>

            <div style={{ marginBottom: "12px" }}>
              <label>Radius in kilometres</label>
              <br />
              <input
                style={{ width: "100%", padding: "8px" }}
                type="number"
                value={radius}
                onChange={(event) => setRadius(event.target.value)}
              />
            </div>

            <button type="submit">
              {editingCampaignId ? "Update campaign" : "Add campaign"}
            </button>

            <button type="button" onClick={resetForm}>
              Cancel
            </button>
          </form>
        </section>
      )}

      <section>
        <h2 style={{ textAlign: "center" }}>Campaign list</h2>

        {campaigns.length === 0 ? (
          <p style={{ textAlign: "center" }}>No campaigns yet.</p>
        ) : (
          <div
            style={{
              maxWidth: "900px",
              margin: "0 auto",
              display: "grid",
              gap: "16px",
            }}
          >
            {campaigns.map((campaign) => (
              <article
                key={campaign.id}
                style={{
                  padding: "16px",
                  border: "1px solid #ddd",
                  borderRadius: "10px",
                  backgroundColor: "white",
                  boxShadow: "0 2px 6px rgba(0, 0, 0, 0.06)",
                }}
              >
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "flex-start",
                    gap: "16px",
                    marginBottom: "12px",
                  }}
                >
                  <div>
                    <h3 style={{ margin: "0 0 6px 0" }}>{campaign.name}</h3>

                    <span
                      style={{
                        display: "inline-block",
                        padding: "4px 8px",
                        borderRadius: "999px",
                        backgroundColor:
                          campaign.status === "ON" ? "#e6ffed" : "#f1f1f1",
                        border: "1px solid #ddd",
                        fontSize: "14px",
                      }}
                    >
                      Status: {campaign.status}
                    </span>
                  </div>

                  <div style={{ display: "flex", gap: "8px" }}>
                    <button
                      type="button"
                      onClick={() => startEditing(campaign)}
                    >
                      Edit
                    </button>

                    <button
                      type="button"
                      onClick={() => handleDelete(campaign.id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>

                <div
                  style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fit, minmax(160px, 1fr))",
                    gap: "12px",
                  }}
                >
                  <div>
                    <strong>Campaign fund</strong>
                    <p>{campaign.campaignFund}</p>
                  </div>

                  <div>
                    <strong>Bid amount</strong>
                    <p>{campaign.bidAmount}</p>
                  </div>

                  <div>
                    <strong>Town</strong>
                    <p>{campaign.town}</p>
                  </div>

                  <div>
                    <strong>Radius</strong>
                    <p>{campaign.radius} km</p>
                  </div>
                </div>

                <div>
                  <strong>Keywords</strong>

                  <div
                    style={{
                      display: "flex",
                      flexWrap: "wrap",
                      gap: "6px",
                      marginTop: "8px",
                    }}
                  >
                    {campaign.keywords.map((keyword) => (
                      <span
                        key={keyword}
                        style={{
                          padding: "4px 8px",
                          borderRadius: "999px",
                          backgroundColor: "#f3f4f6",
                          border: "1px solid #ddd",
                          fontSize: "14px",
                        }}
                      >
                        {keyword}
                      </span>
                    ))}
                  </div>
                </div>
              </article>
            ))}
          </div>
        )}
      </section>
    </main>
  );
}
