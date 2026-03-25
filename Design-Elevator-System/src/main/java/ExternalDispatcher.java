import enums.ElevatorDirection;

/**
 * Handles requests coming from outside (Floors).
 * Delegates the request to the ElevatorScheduler to find the best elevator.
 */
public class ExternalDispatcher {
    ElevatorScheduler elevatorScheduler;
    public ExternalDispatcher(ElevatorScheduler elevatorScheduler){
        this.elevatorScheduler = elevatorScheduler;
    }

    public void submitExternalRequest(int floor, ElevatorDirection elevatorDirection) {
        ElevatorController controller = elevatorScheduler.assignElevator(floor,elevatorDirection);
        controller.submitRequest(floor);
    }
}
