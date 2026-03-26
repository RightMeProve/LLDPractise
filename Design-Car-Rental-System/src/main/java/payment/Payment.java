package payment;

import java.util.Date;

/**
 * Data model representing a completed payment transaction.
 * Links a transaction to a specific Bill and records the payment mode and date.
 */
public class Payment {
    private final int paymentId;
    private final int billId;
    private final double amountPaid;
    private final PaymentMode paymentMode;
    private final Date paymentDate;

    public Payment(int paymentId,
                   int billId,
                   double amountPaid,
                   PaymentMode paymentMode,
                   Date paymentDate) {

        this.paymentId = paymentId;
        this.billId = billId;
        this.amountPaid = amountPaid;
        this.paymentMode = paymentMode;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() { return paymentId; }
    public int getBillId() { return billId; }
    public double getAmountPaid() { return amountPaid; }
    public PaymentMode getPaymentMode() { return paymentMode; }
    public Date getPaymentDate() { return paymentDate; }
}
