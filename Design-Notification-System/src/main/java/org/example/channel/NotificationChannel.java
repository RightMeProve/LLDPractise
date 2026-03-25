package org.example.channel;

import org.example.model.Notification;

/**
 * NotificationChannel - Core Strategy Interface for all notification delivery
 * channels.
 *
 * ✅ DESIGN PATTERN: Strategy Pattern
 * Each concrete channel (Email, SMS, Push) IS-A NotificationChannel.
 * The dispatcher uses this interface without caring about the implementation.
 * Swapping or adding channels requires ZERO change in the dispatcher.
 *
 * ✅ SOLID: Interface Segregation Principle (ISP)
 * The interface is minimal — only one method `send()`.
 * No channel is forced to implement methods it doesn't need.
 *
 * ✅ SOLID: Dependency Inversion Principle (DIP)
 * High-level modules (Dispatcher) depend on THIS abstraction,
 * NOT on concrete EmailChannel / SMSChannel classes.
 *
 * 💡 LLD INTERVIEW TIP:
 * If a channel needs retries, rate limiting, or fallback logic,
 * inject a wrapper/decorator around this interface instead of
 * modifying concrete implementations.
 */
public interface NotificationChannel {

    /**
     * Sends the given notification via this channel.
     *
     * @param notification the notification to deliver (must not be null)
     */
    void send(Notification notification);
}
