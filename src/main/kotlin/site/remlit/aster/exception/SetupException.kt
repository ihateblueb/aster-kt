package site.remlit.aster.exception

/**
 * Exception to be thrown when the setup service has failed to
 * prepare the application for running.
 * */
class SetupException(message: String) : Exception(message)
