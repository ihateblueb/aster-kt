./scripts/clean-frontend.sh

cd frontend || exit

pnpm install
pnpm dev

cd - || exit
