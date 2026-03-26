package payment;

import bill.Bill;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager class responsible for processing payments.
 * Uses the Strategy Pattern via PaymentStrategy to process payments
 * through various modes (UPI, Credit Card, Cash, etc.) without altering core logic.
 */
public class PaymentManager {
    private PaymentStrategy paymentStrategy;
    
    // Thread-safe map to store historical payments
    private final Map<Integer, Payment> payments = new ConcurrentHashMap<>();

    public PaymentManager(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public Payment makePayment(Bill bill, double paymentAmount) {

        Payment payment = paymentStrategy.processPayment(bill, paymentAmount);
        payments.put(payment.getPaymentId(), payment);
        return payment;
    }

    public List<Payment> getPaymentsForBill(int billId) {
        return payments.values().stream()
                .filter(p -> p.getBillId() == billId)
                .toList();
    }

    public Optional<Payment> getPayment(int paymentId) {
        return Optional.ofNullable(payments.get(paymentId));
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
}
