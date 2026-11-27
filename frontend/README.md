# TournamentIQ Web

The React workspace provides tournament selection, team and field registration, manual match entry and schedule views.

## Local development

Use Node.js 22.12+ and npm. Run `npm ci` and `npm run dev` here, then open `http://localhost:5173`. Vite proxies API requests to the backend on port 8080. See the [root README](../README.md) for the backend startup command. Run `npm run build` for the type check and production build.

## Architecture

- [Web container](<../docs/Target Architecture (HLD)/v1/Containers/Web.md>)
- [Tournament Workspace](<../docs/System Architecture (LLD)/Web/Tournament Workspace.md>)
