/**
 * Concrete handler for DEBUG level logs.
 */
public class DebugLogProcessor extends LogProcessor {

    public DebugLogProcessor(LogProcessor nextLogProcessor) {
        super(nextLogProcessor);
    }

    /**
     * Overrides the log method. Only processes the log if it is a DEBUG level.
     * Otherwise, lets the superclass pass it down the chain.
     */
    @Override
    public void log(int logLevel, String message) {
        // Handle request if log level is DEBUG
        if (logLevel == DEBUG) {
            System.out.println("DEBUG: " + message);
        } else {
            // Pass to the next processor if we cannot handle it
            super.log(logLevel, message);
        }
    }
}
