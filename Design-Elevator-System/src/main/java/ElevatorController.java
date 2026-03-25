import enums.ElevatorDirection;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Controls an individual ElevatorCar. 
 * Runs continuously in its own thread to process requests from upMinPQ and downMaxPQ.
 * Uses wait() and notify() pattern on 'monitor' to safely pause when idle.
 */
public class ElevatorController implements Runnable{
    PriorityBlockingQueue<Integer> upMinPQ;
    PriorityBlockingQueue<Integer> downMaxPQ;

    ElevatorCar elevatorCar;

    private final Object monitor = new Object();

    ElevatorController(ElevatorCar elevatorCar){
        this.elevatorCar = elevatorCar;
        // Min-Priority Queue for UP movements.
        // Reason: When traveling UP, the elevator must process the lowest destination floors first.
        // For example, if current pending floors are 7 and 4, the MinPQ sorts them to serve 4 then 7.
        upMinPQ = new PriorityBlockingQueue<>();

        // Max-Priority Queue for DOWN movements.
        // Reason: When traveling DOWN, the elevator must process the highest destination floors first.
        // For example, if current pending floors are 2 and 5, the MaxPQ (using the b-a comparator) 
        // sorts them to serve 5 then 2.
        downMaxPQ = new PriorityBlockingQueue<>(10,(a,b)->b-a);
    }

    public void submitRequest(int destinationFloor){
        enqueueRequest(destinationFloor);
    }

    private void enqueueRequest(int destinationFloor) {
        System.out.println("Request details-> destinationFloor: " + destinationFloor+ " accepted by elevator: " +elevatorCar.id);

        // Edge case: Ignore request if we're already at the intended floor
        if(destinationFloor == elevatorCar.currentFloor) {
            return;
        }

        // Dynamic Routing Algorithm: Decide which Queue receives the request
        // The algorithm checks the relative position of the requested floor against the car's general trajectory.
        // If the destination lies ahead of our next stoppage in an upward trajectory, queue it in the MIN PQ.
        // Otherwise, consider it a downward journey or a skipped floor and queue it in the MAX PQ.
        if(destinationFloor>=elevatorCar.nextFloorStoppage) {
            if(!upMinPQ.contains(destinationFloor)) {
                upMinPQ.offer(destinationFloor);
            }
        }
        else {
            if(!downMaxPQ.contains(destinationFloor)) {
                downMaxPQ.offer(destinationFloor);
            }
        }

        // Concurrency Control Point:
        // We synchronize on the 'monitor' object and notify the sleeping controller thread 
        // that a new request has arrived in the queue. This prevents the thread from being permanently IDLE.
        synchronized (monitor){
            monitor.notify();
        }
    }

    @Override
    public void run(){controlElevator();}

    private void controlElevator() {

        while (true) {
            // Thread Sleep Mechanics: 
            // We lock the monitor object and loop to check for empty queues (loop prevents spurious wakeups).
            // If there's no path to travel, execute monitor.wait() to yield CPU resources entirely.
            // The thread will stay paused here rather than busy-waiting.
            synchronized (monitor) {
                while (upMinPQ.isEmpty() && downMaxPQ.isEmpty()) {
                    try {
                        System.out.println("Elevator: " + elevatorCar.id + " is IDLE");
                        elevatorCar.movingDirection = ElevatorDirection.IDLE;
                        monitor.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            while (!upMinPQ.isEmpty()) {
                int floor = upMinPQ.poll();
                System.out.println("Serving Floor: " +floor + " by elevator: " + elevatorCar.id + " currentFloor: " + elevatorCar.currentFloor);
                elevatorCar.moveElevator(floor);
            }

            while (!downMaxPQ.isEmpty()){
                int floor = downMaxPQ.poll();
                System.out.println("Serving Floor: " + floor + " by elevator: " + elevatorCar.id +" currentFloor: " +elevatorCar.currentFloor);
                elevatorCar.moveElevator(floor);
            }
        }
    }
}
