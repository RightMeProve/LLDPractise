import java.util.Arrays;

/**
 * Demo class to run and test the Elevator System simulation.
 * This class acts as the client that initializes all components and triggers external/internal requests.
 */
public class Demo {
    public static void main(String[] args){
        try{
            ElevatorCar car1 = new ElevatorCar(1);
            ElevatorController controller1 = new ElevatorController(car1);

            ElevatorCar car2 = new ElevatorCar(2);
            ElevatorController controller2 = new ElevatorController(car2);

            InternalButton internalButton_for_elevator1 = new InternalButton(controller1);
            InternalButton internalButton_for_elevator2 = new InternalButton(controller2);

            // Pass both controllers to the scheduler instead of duplicates of controller1
            ElevatorScheduler elevatorScheduler = new ElevatorScheduler(
                    Arrays.asList(controller1,controller2),
                    new NearestElevatorStrategy()
            );

            ExternalDispatcher externalDispatcher = new ExternalDispatcher(elevatorScheduler);

            Building building = new Building(5,externalDispatcher);

            new Thread(controller1,"Elevator-1").start();
            new Thread(controller2,"Elevator-2").start();

            building.getFloor(3).pressUpButton();
            Thread.sleep(5);

            building.getFloor(5).pressDownButton();
            Thread.sleep(5);

            internalButton_for_elevator1.pressButton(4); // user inside elevator 1 presses floor 4
            Thread.sleep(5);

            internalButton_for_elevator1.pressButton(5); // user inside elevator 1 presses floor 5
            Thread.sleep(5);

            building.getFloor(1).pressDownButton();
            Thread.sleep(5);

            building.getFloor(2).pressUpButton();
            Thread.sleep(5);

            internalButton_for_elevator1.pressButton(2);
        }
        catch (Exception e){

        }
    }
}
