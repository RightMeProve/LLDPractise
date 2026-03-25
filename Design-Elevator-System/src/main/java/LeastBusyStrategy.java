import enums.ElevatorDirection;

import java.util.List;

/**
 * Strategy implementation that selects the elevator with the fewest pending requests 
 * across both its UP and DOWN queues.
 */
public class LeastBusyStrategy implements ElevatorSelectionStrategy{
    @Override
    public ElevatorController selectElevator(List<ElevatorController> controllers, int floor, ElevatorDirection elevatorDirection) {
        ElevatorController best = null;
        int minLoad = Integer.MAX_VALUE;

        for(ElevatorController controller: controllers){
            // Algorithmic Metric: Total Pending Workload
            // Instead of evaluating spatial proximity, this strategy adds up the exact backlog sizing 
            // from both internal control queues (UP requests + DOWN requests) to determine latency. 
            // In a heavily congested commercial building, a closer elevator with a massive queue length 
            // takes longer than a far away elevator with no queue.
            int load = controller.upMinPQ.size() + controller.downMaxPQ.size();
            if(load < minLoad){
                minLoad = load;
                best = controller;
            }

        }

        return best;
    }
}
