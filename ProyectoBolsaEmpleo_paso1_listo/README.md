# Proyecto Bolsa de Empleo - Paso 1

Este paquete deja el proyecto separado en dos carpetas principales:

- `backend/`: proyecto Spring Boot original.
- `frontend/`: proyecto React creado con estructura base de Vite.
- `database/`: carpeta preparada para el script SQL final.

## Ejecutar backend

Desde la carpeta raíz:

```bash
cd backend
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

El backend levanta en:

```text
http://localhost:8080
```

## Ejecutar frontend

En otra terminal, desde la carpeta raíz:

```bash
cd frontend
npm install
npm run dev
```

El frontend levanta en:

```text
http://localhost:5173
```

## Nota

En este paso no se migraron pantallas de Thymeleaf a React ni se cambió la seguridad a JWT. Solo se separó la estructura para preparar el proyecto para la modalidad SPA.
