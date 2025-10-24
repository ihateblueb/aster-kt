package site.remlit.aster.model.ap

class ApValidationException(
	val type: ApValidationExceptionType = ApValidationExceptionType.Unauthorized,
	message: String = "Validation failed."
) : Exception(message)
