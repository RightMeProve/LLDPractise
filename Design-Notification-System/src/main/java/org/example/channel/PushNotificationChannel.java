package org.example.channel;

import org.example.model.Notification;

/**
 * PushNotificationChannel - Concrete implementation for mobile push
 * notifications.
 *
 * ✅ SOLID: Single Responsibility Principle (SRP)
 * Exclusively responsible for mobile push delivery.
 *
 * ⚠️ PRODUCTION CONSIDERATIONS:
 * - Real push notifications require a push provider:
 * • Firebase Cloud Messaging (FCM) for Android
 * • Apple Push Notification Service (APNs) for iOS
 * - The user's device token must be resolved before sending.
 * (This design omits device-token lookup — a production gap.)
 *
 * 💡 INTERVIEW TIP — Failure Handling:
 * Push notifications can silently fail (device offline, app uninstalled).
 * A production system should:
 * 1. Queue failed pushes in a Dead Letter Queue
 * 2. Retry with exponential backoff
 * 3. Fall back to SMS or Email if all retries exhaust
 */
public class PushNotificationChannel implements NotificationChannel {

    @Override
    public void send(Notification notification) {
        // TODO (Production): Replace with FCM/APNs client call using stored device
        // token
        System.out.println(
                "[PUSH]  → User: " + notification.getUserId()
                        + " | Message: " + notification.getMessage());
    }
}
