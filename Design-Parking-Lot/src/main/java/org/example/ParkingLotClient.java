package org.example;

import org.example.Entity.ParkingSpot;
import org.example.Entity.Vehicle;
import org.example.Enum.VehicleType;
import org.example.LookupStrategy.ParkingSpotLookupStrategy;
import org.example.LookupStrategy.RandomLookupStrategy;
import org.example.ParkingLot.*;
import org.example.Payment.CashPayment;
import org.example.Payment.UpiPayment;
import org.example.Pricing.CostComputation;
import org.example.Pricing.FixedPricingStrategy;
import org.example.SpotManager.FourWheelerSpotManager;
import org.example.SpotManager.ParkingSpotManager;
import org.example.SpotManager.TwoWheelerSpotManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Client class that creates the entire system infrastructure and executes a
 * mock flow.
 * 
 * LLD Note:
 * This class handles heavy initialization and dependency injection.
 * In a real Spring Boot application, most of these instantiations would be
 * managed
 * by the Spring IoC container (@Component, @Service, @Configuration).
 */
public class ParkingLotClient {
        public static void main(String[] args) {
                // 1. Choose the Spot Lookup Strategy
                ParkingSpotLookupStrategy parkingSpotLookupStrategy = new RandomLookupStrategy();

                // 2. Setup Level 1 Managers & Spots
                Map<VehicleType, ParkingSpotManager> levelOneManagers = new HashMap<>();
                levelOneManagers.put(VehicleType.TWO_WHEELER,
                                new TwoWheelerSpotManager(List.of(new ParkingSpot("L1-S1"),
                                                new ParkingSpot("L2-S2")), parkingSpotLookupStrategy));

                levelOneManagers.put(VehicleType.FOUR_WHEELER,
                                new FourWheelerSpotManager(List.of(new ParkingSpot("L1-S3")),
                                                parkingSpotLookupStrategy));

                ParkingLevel level1 = new ParkingLevel(1, levelOneManagers);

                // 3. Setup Level 2 Managers & Spots
                Map<VehicleType, ParkingSpotManager> levelTwoManagers = new HashMap<>();
                levelTwoManagers.put(VehicleType.TWO_WHEELER,
                                new TwoWheelerSpotManager(List.of(new ParkingSpot("L2-S1")),
                                                parkingSpotLookupStrategy));
                levelTwoManagers.put(VehicleType.FOUR_WHEELER,
                                new FourWheelerSpotManager(List.of(new ParkingSpot("L2-S2"), new ParkingSpot("L2-S3")),
                                                parkingSpotLookupStrategy));

                ParkingLevel level2 = new ParkingLevel(2, levelTwoManagers);

                // 4. Create the Building with a global Pricing Strategy
                ParkingBuilding parkingBuilding = new ParkingBuilding(List.of(level1, level2),
                                new CostComputation(new FixedPricingStrategy()));

                // 5. Initialize the Parking Lot Facade
                ParkingLot parkingLot = new ParkingLot(
                                parkingBuilding,
                                new EntranceGate(),
                                new ExitGate(new CostComputation(new FixedPricingStrategy())));

                // 6. Simulate Vehicles arriving
                Vehicle bike = new Vehicle("BIKE-101", VehicleType.TWO_WHEELER);
                Vehicle car = new Vehicle("CAR-101", VehicleType.FOUR_WHEELER);

                System.out.println("--- Entering Parking ---");
                Ticket t1 = parkingLot.vehicleArrives(bike);
                Ticket t2 = parkingLot.vehicleArrives(car);

                System.out.println("\n--- Exiting Parking ---");
                parkingLot.vehicleExits(t1, new CashPayment()); // Using Strategy pattern to vary payment
                parkingLot.vehicleExits(t2, new UpiPayment());

        }
}
