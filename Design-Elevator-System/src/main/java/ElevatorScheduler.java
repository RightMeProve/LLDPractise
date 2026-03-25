import enums.ElevatorDirection;

import java.util.List;

/**
 * Central scheduler that routes external requests to the optimal elevator controller.
 * Utilizes the Strategy Pattern (ElevatorSelectionStrategy) to allow flexible routing algorithms.
 */
public class ElevatorScheduler {
    private List<ElevatorController > controllers;
    private ElevatorSelectionStrategy strategy;

    public ElevatorScheduler(List<ElevatorController> controllers,ElevatorSelectionStrategy strategy){
        this.controllers = controllers;
        this.strategy = strategy;
    }

    public void setStrategy(ElevatorSelectionStrategy strategy){
        this.strategy = strategy;
    }

    public ElevatorController assignElevator(int floor, ElevatorDirection elevatorDirection){
        return strategy.selectElevator(controllers,floor,elevatorDirection);
    }
}
