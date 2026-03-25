import enums.ElevatorDirection;

/**
 * Represents a single floor in the building.
 * Contains UP/DOWN external buttons that communicate with the ExternalDispatcher.
 */
public class Floor {
    int floorNumber;
    ExternalButton upButton;
    ExternalButton downButton;

    public Floor(int floorNumber,ExternalDispatcher externalDispatcher){
        this.floorNumber = floorNumber;
        this.upButton = new ExternalButton(externalDispatcher);
        this.downButton = new ExternalButton(externalDispatcher);

    }
    public void pressUpButton(){
        upButton.pressButton(floorNumber, ElevatorDirection.UP);
    }
    public void pressDownButton(){
        downButton.pressButton(floorNumber,ElevatorDirection.DOWN);
    }
}
