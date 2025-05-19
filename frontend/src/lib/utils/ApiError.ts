export default class ApiError extends Error {
	constructor(
		public status: number,
		public message: string
	) {
		super();
		Object.setPrototypeOf(this, ApiError.prototype);
		this.name = 'ApiError';
		this.status = status ?? 0;
	}
}
