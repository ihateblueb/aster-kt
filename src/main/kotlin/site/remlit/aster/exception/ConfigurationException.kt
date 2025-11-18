package site.remlit.aster.exception

/**
 * Exception to be thrown when something is missing from the configuration
 * that cannot be, or some other major conflict is found.
 * */
class ConfigurationException(message: String) : Exception(message)
