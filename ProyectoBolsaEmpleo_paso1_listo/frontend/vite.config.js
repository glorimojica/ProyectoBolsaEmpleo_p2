import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // Preparado para el siguiente paso: cuando el backend exponga endpoints REST,
      // React podrá llamar a /api/... y Vite lo enviará a Spring Boot en el puerto 8080.
      '/api': 'http://localhost:8080'
    }
  }
})
