/**
 * Singleton dispatcher handling requests originating from inside the elevators.
 * Directly routes the request to the elevator's controller since the elevator is already known.
 */
public class InternalDispatcher {
    private static InternalDispatcher INSTANCE = new InternalDispatcher();
    private InternalDispatcher(){};
    public static InternalDispatcher getInstance() {return INSTANCE;}


    public void submitInternalRequest(int destinationFloor, ElevatorController elevatorController) {
        elevatorController.submitRequest(destinationFloor);
    }
}
