package org.example.channel;

import org.example.model.Notification;

/**
 * EmailNotificationChannel - Concrete implementation of NotificationChannel for
 * email delivery.
 *
 * ✅ SOLID: Single Responsibility Principle (SRP)
 * This class has ONE job — send notifications via Email.
 * No routing logic, no user lookup, no formatting of other channels.
 *
 * ✅ SOLID: Open/Closed Principle (OCP)
 * To change email behaviour (e.g., call a real SMTP client),
 * we only modify THIS file. Nothing else changes.
 *
 * ⚠️ PRODUCTION GAP:
 * Currently uses System.out.println — this simulates delivery.
 * In production: inject an EmailClient / JavaMailSender dependency
 * rather than printing. This also makes it testable (mockable dependency).
 *
 * 💡 LLD INTERVIEW FOLLOW-UP:
 * Q: "How would you add retry logic for failed email sends?"
 * A: Wrap this in a RetryableNotificationChannel decorator, or use
 * a resilience library (e.g., Resilience4j) at the factory/registry level.
 */
public class EmailNotificationChannel implements NotificationChannel {

    @Override
    public void send(Notification notification) {
        // TODO (Production): Replace with actual EmailClient.send(notification)
        System.out.println(
                "[EMAIL] → User: " + notification.getUserId()
                        + " | Message: " + notification.getMessage());
    }
}
