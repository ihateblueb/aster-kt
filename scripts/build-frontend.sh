cd frontend || exit

pnpm install
pnpm link-shared
pnpm build

cd - || exit
