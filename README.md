# Campaign Manager

Campaign Manager is a simple full-stack web application for managing product advertising campaigns.  
The application allows sellers to create, view, edit and delete separate campaigns for the products they want to sell.

## Live Demo

Live version: https://campaign-manager-amber.vercel.app

## Main Features

- Create a new campaign
- View existing campaigns
- Edit campaign details
- Delete campaigns
- Manage products and their related campaigns
- Register and log in as a seller
- Display and update seller Emerald account balance
- Validate required campaign fields
- Use predefined values for keywords and towns

## Technologies Used

### Backend

- Java
- Spring Boot
- Spring Web / REST API
- Spring Data JPA
- Spring Validation
- H2 in-memory database
- Gradle

### Frontend

- React
- TypeScript
- Vite
- React Router
- Axios

## Project Structure

```text
CampaignManager/
├── backend/      # Spring Boot REST API
└── frontend/     # React frontend application

```

## Running the Application

The project consists of two separate parts:

- `backend` — Spring Boot REST API
- `frontend` — React application

Both parts should be started separately.

### Backend

Go to the backend directory:

```bash
cd CampaignManager/backend
```

Run the Spring Boot application:

```bash
./gradlew bootRun
```

On Windows:

```powershell
gradlew.bat bootRun
```

The backend should be available at:

http://localhost:8080

### Frontend

Go to the frontend directory:

```bash
cd CampaignManager/frontend
```

Install dependencies:

```bash
npm install
```

Run the frontend development server:

```bash
npm run dev
```

The frontend should be available at:

http://localhost:5173

### Running Tests

Go to the backend directory:

```bash
cd CampaignManager/backend
```

Run backend tests:

```
./gradlew clean test
```

On Windows:

```powershell
gradlew.bat clean test
```

## Author

**Szymon Potępa** / Requu1
