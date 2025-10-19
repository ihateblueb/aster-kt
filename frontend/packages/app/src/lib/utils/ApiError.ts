export default class ApiError extends Error {
    constructor(
        public status: number,
        public message: string,
        public serverStackTrace: string
    ) {
        super();
        Object.setPrototypeOf(this, ApiError.prototype);
        this.name = 'ApiError';
        this.status = status ?? 0;
        this.serverStackTrace = serverStackTrace ?? "";
    }
}
