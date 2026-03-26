/**
 * Abstract handler in the Chain of Responsibility pattern.
 * It contains a reference to the next handler in the chain.
 * If this handler cannot process the request, it delegates it to the next one.
 */
public abstract class LogProcessor {
    // Log Levels
    public static int INFO = 1;
    public static int DEBUG = 2;
    public static int ERROR = 3;

    // Reference to the next component in the chain
    LogProcessor nextLogProcessor;

    /**
     * Constructor to initialize the next processor in the chain.
     * @param logProcessor The next log processor to delegate to
     */
    public LogProcessor(LogProcessor logProcessor) {
        this.nextLogProcessor = logProcessor;
    }

    /**
     * The method that will be implemented by concrete classes.
     * By default, it passes the request to the next processor in the chain.
     * @param logLevel The level of the log (INFO, DEBUG, ERROR)
     * @param message The log message to display
     */
    public void log(int logLevel, String message) {
        if (nextLogProcessor != null) {
            nextLogProcessor.log(logLevel, message);
        }
    }
}
