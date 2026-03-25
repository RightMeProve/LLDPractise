import enums.DoorState;

/**
 * Represents the Elevator Door. Includes basic simulation output for open/close actions.
 */
public class Door {
    @SuppressWarnings("unused")
    private DoorState doorState;

    Door(){
        doorState = DoorState.DOOR_CLOSED;
    }

    public void openDoor(int id){
        doorState = DoorState.DOOR_OPEN;
        System.out.println("Opening the Elevator door of elevator: " + id);
    }

    public void closeDoor(int id){
        doorState = DoorState.DOOR_CLOSED;
        System.out.println("Closing the Elevator door od elevator: " + id);
    }
}
