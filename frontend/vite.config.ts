import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import { TanStackRouterVite } from '@tanstack/router-plugin/vite'

// https://vite.dev/config/
export default defineConfig({
	plugins: [
		TanStackRouterVite({ target: 'react', autoCodeSplitting: true }),
		react()
	],
	resolve: {
		alias: {
			'@tabler/icons-react': '@tabler/icons-react/dist/esm/icons/index.mjs',
		}
	},
	server: {
		proxy: {
			'/api': {
				target: 'http://localhost:9978',
				changeOrigin: false,
			},
			'/swagger': {
				target: 'http://localhost:9978',
				changeOrigin: false,
			},
			'/upload': {
				target: 'http://localhost:9978',
				changeOrigin: false,
			},
			'/uploads': {
				target: 'http://localhost:9978',
				changeOrigin: false,
			}
		}
	}
})
