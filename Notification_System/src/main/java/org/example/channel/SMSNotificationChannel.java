package org.example.channel;

import org.example.model.Notification;

/**
 * SMSNotificationChannel - Concrete implementation of NotificationChannel for
 * SMS delivery.
 *
 * ✅ SOLID: Single Responsibility Principle (SRP)
 * Only responsible for sending SMS notifications.
 * No business logic, no preference lookup, no orchestration.
 *
 * ⚠️ PRODUCTION CONSIDERATIONS:
 * - Real-world SMS requires an external provider: Twilio, AWS SNS, etc.
 * - Inject the SMS client via constructor (Dependency Injection) for
 * testability.
 * - SMS has strict character limits (160 chars for GSM-7 encoding).
 * Consider validating message length before sending.
 *
 * 💡 DESIGN IMPROVEMENT — Constructor Injection:
 * Instead of hardcoding System.out, inject an SmsGateway interface:
 * class SMSNotificationChannel implements NotificationChannel {
 * private final SmsGateway gateway;
 * SMSNotificationChannel(SmsGateway gateway) { this.gateway = gateway; }
 * }
 * This enables unit testing without real network calls.
 */
public class SMSNotificationChannel implements NotificationChannel {

    @Override
    public void send(Notification notification) {
        // TODO (Production): Replace with SmsGateway.send(notification.getUserId(),
        // notification.getMessage())
        System.out.println(
                "[SMS]   → User: " + notification.getUserId()
                        + " | Message: " + notification.getMessage());
    }
}
