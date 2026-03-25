import java.util.ArrayList;
import java.util.List;

/**
 * Represents the physical building containing multiple floors.
 * It manages the initialization of floors and delegates the external dispatcher to each floor.
 */
public class Building {
    List<Floor> floors = new ArrayList<>();

    public Building(int totalFloors,ExternalDispatcher externalDispatcher){
        for(int i = 1;i<=totalFloors;i++)
        {
            floors.add(new Floor(i,externalDispatcher));
        }
    }

    public Floor getFloor(int floor){
        return floors.get(floor-1);
    }


}
