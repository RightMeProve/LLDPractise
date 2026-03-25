import enums.ElevatorDirection;

/**
 * Represents a button located outside the elevator on a specific floor.
 * Submits the directional request to the ExternalDispatcher.
 */
public class ExternalButton {
    private final ExternalDispatcher externalDispatcher;
    public ExternalButton(ExternalDispatcher externalDispatcher){
        this.externalDispatcher = externalDispatcher;
    }

    public void pressButton(int floor, ElevatorDirection elevatorDirection){
        externalDispatcher.submitExternalRequest(floor,elevatorDirection);
    }
}
