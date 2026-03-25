import enums.ElevatorDirection;

import java.util.List;

/**
 * Interface for the Strategy Pattern.
 * Defines the contract for selecting an elevator from a list of controllers based on runtime conditions.
 */
public interface ElevatorSelectionStrategy {

    ElevatorController selectElevator(List<ElevatorController> controllers, int floor, ElevatorDirection elevatorDirection);
}
