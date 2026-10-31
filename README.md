# Food Delivery Platform

Workspace for a food delivery application. The existing
`SpringBoot_Ecommerce_Project` remains separate and unchanged.

## Technology

- Frontend: React, Vite, JavaScript
- Backend: Java 25, Spring Boot 4.1, Spring Web MVC, Spring Data JPA
- Local database: file-based H2; PostgreSQL driver is included for a later deployment profile

## Run locally

Start the backend from `backend` (the `dev` profile adds local sample restaurants
and menus so the connected frontend has data to display):

```powershell
mvn spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=dev"
```

Start the frontend from `frontend` in another terminal:

```powershell
npm install
npm run dev
```

The frontend runs at `http://localhost:5173` and proxies `/api` requests to
`http://localhost:8080`. Menu prices are displayed in INR. The local H2 databases
are stored under `backend/data/`; sample data is only loaded in the `dev` profile,
and only when its restaurant table is empty.

## Backend API

The first backend milestone provides restaurant discovery and menu management:

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/health` | Check API status |
| GET | `/api/restaurants?search=&city=&cuisine=&page=0&size=20` | Search, filter and page active restaurants |
| GET | `/api/restaurants/{id}` | Get a restaurant |
| POST | `/api/restaurants` | Create a restaurant |
| PUT | `/api/restaurants/{id}` | Update a restaurant |
| DELETE | `/api/restaurants/{id}` | Delete a restaurant and its menu |
| GET | `/api/restaurants/{id}/menu-items` | List available menu items |
| POST | `/api/restaurants/{id}/menu-items` | Add a menu item |
| PUT | `/api/menu-items/{id}` | Update a menu item |
| DELETE | `/api/menu-items/{id}` | Delete a menu item |

Pass `includeUnavailable=true` to the menu endpoint to include unavailable
items. Restaurant writes and menu management are currently unauthenticated;
authentication and role-based access are a later backend milestone and should
be added before exposing write endpoints publicly.

Run backend tests from `backend` with `mvn test`.

## Structure

```text
Food_Delivery_Platform/
|-- backend/
|   `-- src/
|       |-- main/java/com/fooddelivery/platform/
|       |   |-- config/
|       |   |-- features/
|       |   |   |-- auth/
|       |   |   |-- cart/
|       |   |   |-- delivery/
|       |   |   |-- menu/
|       |   |   |-- orders/
|       |   |   |-- payments/
|       |   |   |-- restaurants/
|       |   |   `-- users/
|       |   `-- shared/
|       `-- test/java/com/fooddelivery/platform/
`-- frontend/
    `-- src/
        |-- app/
        |-- components/
        |-- features/
        |   |-- auth/
        |   |-- cart/
        |   |-- delivery/
        |   |-- menu/
        |   |-- orders/
        |   `-- restaurants/
        |-- hooks/
        `-- services/
```

Each feature is kept separate so its API, UI, and business logic can be added
without mixing it into unrelated features. Restaurant and menu endpoints are
implemented; auth, cart, ordering, delivery, and payments remain to be built.
