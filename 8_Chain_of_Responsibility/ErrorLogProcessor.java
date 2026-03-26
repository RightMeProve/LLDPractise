/**
 * Concrete handler for ERROR level logs.
 */
public class ErrorLogProcessor extends LogProcessor {

    public ErrorLogProcessor(LogProcessor nextLogProcessor) {
        super(nextLogProcessor);
    }

    /**
     * Overrides the log method. Only processes the log if it is an ERROR level.
     * Otherwise, lets the superclass pass it down the chain.
     */
    @Override
    public void log(int logLevel, String message) {
        // Handle request if log level is ERROR
        if (logLevel == ERROR) {
            System.out.println("ERROR: " + message);
        } else {
            // Note: Since this will be the last node in our chain, `super.log`
            // will just check `if (nextLogProcessor != null)` and essentially do nothing.
            super.log(logLevel, message);
        }
    }
}
