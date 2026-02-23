package org.example.api;

import org.example.model.Notification;
import org.example.service.NotificationDispatcherService;

/**
 * NotificationService - Synchronous API facade over the
 * NotificationDispatcherService.
 *
 * ✅ WHY A FACADE?
 * The caller (e.g., a REST controller) should not directly depend on
 * NotificationDispatcherService — that's an internal service class.
 * This facade provides a stable, clean public API for the notification system.
 *
 * ⚠️ CURRENT FLAW: Near-Zero Value Wrapper
 * As-is, this class is a thin delegator — sendNotification() calls dispatch()
 * and nothing else.
 * This is fine for a learning exercise, but in production, a facade should add:
 * - Input validation (null checks on notification)
 * - Logging / Audit trail ("Notification sent by user X at time T")
 * - Rate limiting (throttle per user to prevent spam)
 * - Metrics (track notification success/failure rates)
 *
 * 💡 LLD INTERVIEW TIP:
 * Q: "Should NotificationService and AsyncNotificationService share an
 * interface?"
 * A: YES — define a NotificationPort interface with
 * sendNotification(Notification n).
 * Both sync and async implementations implement it.
 * Callers depend on the interface, not the concrete class.
 * This respects the Dependency Inversion Principle.
 *
 * 💡 ALTERNATIVE DESIGN (if only one service needed):
 * Expose sendAsync(boolean isAsync) flag rather than two separate classes.
 * Or use a @Async annotation in Spring Boot.
 */
public class NotificationService {

    private final NotificationDispatcherService dispatcherService;

    public NotificationService(NotificationDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    /**
     * Synchronously sends a notification through the dispatcher.
     * Blocks the caller thread until all channels have been attempted.
     *
     * @param notification the notification to send
     */
    public void sendNotification(Notification notification) {
        if (notification == null) {
            System.err.println("[NotificationService] Cannot send null notification.");
            return;
        }
        dispatcherService.dispatch(notification);
    }
}
