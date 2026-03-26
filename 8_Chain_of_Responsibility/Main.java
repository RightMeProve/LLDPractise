/**
 * Main application class to demonstrate the Chain of Responsibility pattern.
 */
public class Main {
    public static void main(String[] args) {
        // 1. Build the Chain: Info -> Debug -> Error
        // The chain specifies the order in which the request will be processed.
        // We start with InfoLogProcessor, which points to DebugLogProcessor, which points to ErrorLogProcessor.
        LogProcessor logObject = new InfoLogProcessor(
            new DebugLogProcessor(
                new ErrorLogProcessor(null)
            )
        );

        // 2. Generate Requests
        // The sender does not need to know WHICH object will handle the request.
        System.out.println("Sending ERROR request...");
        logObject.log(LogProcessor.ERROR, "System out of memory exception.");

        System.out.println("\nSending DEBUG request...");
        logObject.log(LogProcessor.DEBUG, "Tracing SQL query execution.");

        System.out.println("\nSending INFO request...");
        logObject.log(LogProcessor.INFO, "Application started successfully.");
    }
}
