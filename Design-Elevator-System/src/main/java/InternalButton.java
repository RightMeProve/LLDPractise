/**
 * Represents a button panel inside a specific elevator car.
 * Submits the destination floor request to the InternalDispatcher.
 */
public class InternalButton {
    public final ElevatorController elevatorController;
    public InternalButton(ElevatorController elevatorController)
    {
        this.elevatorController = elevatorController;
    }

    public void pressButton(int destinationFloor){
        InternalDispatcher.getInstance()
                .submitInternalRequest(destinationFloor,elevatorController);
    }
}
