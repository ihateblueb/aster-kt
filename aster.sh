if [[ $1 == "build" ]]; then
	./scripts/build.sh
elif [[ $1 == "build-frontend" ]]; then
    ./scripts/build-frontend.sh
elif [[ $1 == "clean-frontend" ]]; then
	./scripts/clean-frontend.sh
elif [[ $1 == "dev-frontend" ]]; then
    ./scripts/dev-frontend.sh
elif [[ $1 == "build-backend" ]]; then
    ./scripts/build-backend.sh
else
	echo "Unknown command"
fi
