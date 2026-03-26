package store;
import location.Location;
import user.User;
import product.Vehicle;
import product.VehicleType;
import bill.Bill;
import bill.BillManager;
import bill.BillingStrategy;
import bill.DailyBillingStrategy;
import payment.Payment;
import payment.PaymentManager;
import payment.PaymentStrategy;
import payment.UPIPaymentStrategy;
import product.VehicleInventoryManager;
import reservation.Reservation;
import reservation.ReservationManager;
import reservation.ReservationType;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a physical rental store location in the Car Rental System.
 * It manages its own inventory of vehicles, reservations, billing, and payments.
 * This class acts as a Facade, providing a unified interface for the client (User)
 * to interact with various sub-systems (Inventory, Reservation, Billing, Payment).
 */
public class Store {
    private final int storeId;
    private final Location storeLocation;
    
    // Sub-systems managed by this store
    private final VehicleInventoryManager inventory;
    private final ReservationManager reservationManager;
    private final BillManager billManager;
    private final PaymentManager paymentManager;

    public Store(int storeId, Location location) {
        this.storeId = storeId;
        this.storeLocation = location;
        
        // Initialize sub-systems for this specific store
        this.inventory = new VehicleInventoryManager();
        this.reservationManager = new ReservationManager(inventory);
        
        // Default strategies
        this.billManager = new BillManager(new DailyBillingStrategy(inventory)); 
        this.paymentManager = new PaymentManager(new UPIPaymentStrategy()); 
    }

    /**
     * Retrieves available vehicles of a specific type within a given date range.
     * Delegates the check to VehicleInventoryManager.
     */
    public List<Vehicle> getVehicles(VehicleType type, LocalDate from, LocalDate to) {
        return inventory.getAvailableVehicles(type, from, to);
    }

    // ----------------- Create Reservation -----------------
    /**
     * Creates a new reservation for a vehicle if available.
     * @return the newly created Reservation object
     */
    public Reservation createReservation(int vehicleId, User user, LocalDate from, LocalDate to,
                                         ReservationType type) throws Exception {
        return reservationManager.createReservation(vehicleId, user, from, to, type);
    }

    // ----------------- Update Reservation -----------------

    /**
     * Cancels an existing reservation and frees up the vehicle.
     */
    public void cancelReservation(int reservationId) {
        reservationManager.cancelReservation(reservationId);
    }

    /**
     * Marks the beginning of a rental trip. Updates the reservation status.
     */
    public void startTrip(int reservationId) {
        reservationManager.startTrip(reservationId);
    }

    /**
     * Marks the end of a rental trip when the user returns the vehicle.
     */
    public void submitVehicle(int reservationId) {
        reservationManager.submitVehicle(reservationId);
    }

    // ----------------- Billing & Payment ------------------

    /**
     * Generates a final bill for a completed reservation using the specified strategy.
     * Supports Open-Closed Principle by allowing dynamic insertion of new billing strategies.
     */
    public Bill generateBill(int reservationId, BillingStrategy billingStrategy) {
        Reservation r = reservationManager.findByID(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        billManager.setBillingStrategy(billingStrategy);
        return billManager.generateBill(r);
    }

    /**
     * Processes payment for a generated bill. 
     * Uses Strategy pattern for resolving payment modes (UPI, Card, etc.).
     */
    public Payment makePayment(Bill bill, PaymentStrategy paymentStrategy, double paymentAmount) {
        paymentManager.setPaymentStrategy(paymentStrategy);
        Payment payment = paymentManager.makePayment(bill, paymentAmount);

        if (!bill.isBillPaid()) {
            throw new RuntimeException("Payment failed");
        }

        // NOW we can safely remove the reservation from the repo to free up space
        // Alternatively, it could be moved to an 'Archive' repository for history.
        reservationManager.remove(bill.getReservationId());
        return payment;
    }

    public VehicleInventoryManager getInventory() {
        return inventory;
    }

    public int getStoreId() {
        return storeId;
    }
}
