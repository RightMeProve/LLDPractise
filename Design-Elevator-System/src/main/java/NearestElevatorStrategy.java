import enums.ElevatorDirection;

import java.util.List;

/**
 * Strategy implementation that selects an elevator closest to the user's floor, 
 * provided it is moving in the requested direction or is currently idle.
 * Falls back to the first available elevator if no optimal one is found.
 */
public class NearestElevatorStrategy implements ElevatorSelectionStrategy{
    @Override
    public ElevatorController selectElevator(List<ElevatorController> controllers, int requestfloor, ElevatorDirection elevatorDirection) {
        ElevatorController best  = null;
        int minDistance = Integer.MAX_VALUE;

        for(ElevatorController controller: controllers){
            int nextFloorStoppage = controller.elevatorCar.nextFloorStoppage;

            // Trajectory Matcher Algorithm: finding the 'Sweet Spot'
            // To be considered an optimal candidate, the elevator must:
            // 1. Move in the exact SAME direction requested by the user.
            // 2. Not have passed the user's floor yet.
            //    - If moving UP: Ensure the car hasn't gone past the requested floor (nextFloorStoppage <= requestfloor)
            //    - If moving DOWN: Ensure the car hasn't dropped past the requested floor (nextFloorStoppage >= requestfloor)
            boolean isSameDirectionCandidate = controller.elevatorCar.movingDirection == elevatorDirection
                    && ((elevatorDirection == ElevatorDirection.UP && nextFloorStoppage <= requestfloor) ||
                    (elevatorDirection == ElevatorDirection.DOWN && nextFloorStoppage >= requestfloor));

            // Using pure distance as the deciding metric
            int dist = Math.abs(nextFloorStoppage - requestfloor);

            if(isSameDirectionCandidate && dist < minDistance){
                minDistance = dist;
                best = controller;
            }
        }

        // Fallback Algorithm mechanism:
        // If NO elevator perfectly matched the trajectory constraints above (all were going in the wrong
        // direction, or had already passed the floor), immediately try to find an IDLE elevator.
        if(best == null){
            for(ElevatorController controller : controllers){
                if(controller.elevatorCar.movingDirection == ElevatorDirection.IDLE){
                    best = controller;
                    break;
                }
            }

            if(best == null){
                best = controllers.get(0);
            }
        }

        return best;
    }
}
