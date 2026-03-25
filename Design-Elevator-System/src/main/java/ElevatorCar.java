import enums.ElevatorDirection;

/**
 * Represents a physical Elevator Car inside the building.
 * It maintains its state (current floor, direction) and simulates movement via Thread.sleep().
 */
public class ElevatorCar {
    Integer id;
    Integer currentFloor;
    Integer nextFloorStoppage;

    ElevatorDirection movingDirection;
    Door door;

    public ElevatorCar(int id){
        this.id = id;
        currentFloor = 0;
        movingDirection = ElevatorDirection.IDLE;
        door = new Door();
    }

    public void showDisplay(){
        System.out.println("Elevator id: " + id + " Current floor: " + currentFloor + " Moving Direction: " + movingDirection);
    }

    public void setCurrentFloor(Integer currentFloor){
        this.currentFloor = currentFloor;
    }

    public void moveElevator(int destinationFloor){
        this.nextFloorStoppage = destinationFloor;
        if(this.nextFloorStoppage==destinationFloor){
            door.openDoor(id);
            return;
        }

        int startFloor = this.currentFloor;
        door.closeDoor(id);
        if(nextFloorStoppage >= currentFloor){
            movingDirection = ElevatorDirection.UP;
            showDisplay();
            for(int i = startFloor+1;i<=nextFloorStoppage;i++)
            {
                try {
                    Thread.sleep(5);
                }
                catch (Exception e) {

                }
                setCurrentFloor(i);
                showDisplay();
            }
        }
        else {
            movingDirection = ElevatorDirection.DOWN;
            showDisplay();
            for(int i = startFloor-1;i>=nextFloorStoppage;i--)
            {
                try {
                    Thread.sleep(5);
                }
                catch (Exception e){

                }
                setCurrentFloor(i);
                showDisplay();
            }
        }
        door.openDoor(id);
    }
}
