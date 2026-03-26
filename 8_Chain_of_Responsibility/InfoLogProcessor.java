/**
 * Concrete handler for INFO level logs.
 */
public class InfoLogProcessor extends LogProcessor {

    /**
     * Constructor passes the next processor to the superclass.
     */
    public InfoLogProcessor(LogProcessor nextLogProcessor) {
        super(nextLogProcessor);
    }

    /**
     * Overrides the log method. Only processes the log if it is an INFO level.
     * Otherwise, lets the superclass pass it down the chain.
     */
    @Override
    public void log(int logLevel, String message) {
        // Handle request if log level is INFO
        if (logLevel == INFO) {
            System.out.println("INFO: " + message);
        } else {
            // Unhandled request; pass to next item in chain
            super.log(logLevel, message);
        }
    }
}
