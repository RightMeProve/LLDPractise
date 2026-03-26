package system;
import store.Store;
import user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The core orchestrator for the Car Rental System.
 * This class acts as the main entry point to register users and stores,
 * functioning similarly to a central database or service registry.
 */
public class VehicleRentalSystem {
    // List of stores operating under this rental system
    List<Store> storeList;
    
    // List of registered users who can rent vehicles
    List<User> userList;

    public VehicleRentalSystem(){
        storeList = new ArrayList<>();
        userList = new ArrayList<>();
    }

    /**
     * Retrieves a specific store by its unique ID.
     * In a real-world scenario, this might query a database.
     * 
     * @param storeId The unique identifier of the store
     * @return The Store object matching the ID
     */
    public Store getStore(int storeId) {
        return storeList.stream().filter(store -> store.getStoreId() == storeId).findFirst().get();
    }

    /**
     * Retrieves a user by their unique ID.
     * Note: Currently uses list index instead of matching ID.
     * 
     * @param userId The index of the user in the list
     * @return The User object
     */
    public User getUser(int userId) {
        return userList.get(userId);
    }

    /**
     * Registers a new store in the system.
     */
    public void addStore(Store store) {
        storeList.add(store);
    }

    /**
     * Registers a new user in the system.
     */
    public void addUser(User user) {
        userList.add(user);
    }

    /**
     * Removes a store from the system.
     */
    public void removeStore(int storeId) {
        storeList.remove(storeId);
    }

    /**
     * Removes a user from the system.
     */
    public void removeUser(int userId) {
        userList.remove(userId);
    }
}
