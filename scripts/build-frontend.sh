./scripts/clean-frontend.sh

cd frontend || exit

pnpm install
pnpm build

cd - || exit
