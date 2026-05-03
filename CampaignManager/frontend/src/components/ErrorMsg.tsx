type ErrorMessageProps = {
  message: string;
};

export function ErrorMessage({ message }: ErrorMessageProps) {
  if (!message) {
    return null;
  }

  return (
    <div
      style={{
        color: "darkred",
        backgroundColor: "#ffe5e5",
        border: "1px solid darkred",
        padding: "10px",
        borderRadius: "6px",
        margin: "16px 0",
        textAlign: "center",
      }}
    >
      {message}
    </div>
  );
}
