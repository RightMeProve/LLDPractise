package org.example;

import org.example.api.AsyncNotificationService;
import org.example.api.NotificationService;
import org.example.model.ChannelType;
import org.example.model.Notification;
import org.example.model.UserPreference;
import org.example.service.NotificationDispatcherService;
import org.example.service.UserPreferenceService;

import java.util.Set;

/**
 * Main - Entry point demonstrating the Notification System.
 *
 * ✅ SHOWCASES:
 * 1. Synchronous notification via NotificationService
 * 2. Asynchronous notification via AsyncNotificationService
 * 3. User preference-based channel routing
 *
 * ⚠️ ORIGINAL BUG IN MAIN:
 * Notification was created with userId "user1" but preferences were saved for
 * "user".
 * → "user1" would fall back to EMAIL-only default, so EMAIL + PUSH preference
 * was ignored.
 * ✅ FIX: Use the same userId ("user1") for both preference save and
 * notification.
 *
 * ⚠️ ORIGINAL BUG IN MAIN:
 * dispatch() was called TWICE on the same notification —
 * once via service.sendNotification() AND once via dispatcher.dispatch()
 * directly.
 * This would result in double-sending to the same user.
 * ✅ FIX: Use only one of the two calling styles per notification.
 *
 * 💡 IN A SPRING BOOT APP:
 * Main is replaced by @SpringBootApplication + dependency injection.
 * UserPreferenceService & NotificationDispatcherService are @Service beans.
 * NotificationService is a @RestController input handler.
 * AsyncNotificationService uses @Async on its method.
 */
public class Main {

        public static void main(String[] args) throws InterruptedException {

                // ──────────────────────────────────────────────────
                // 1. Set up user preferences
                // ──────────────────────────────────────────────────
                UserPreferenceService userPreferenceService = new UserPreferenceService();

                // Save preference for "user1" — prefers EMAIL and PUSH channels
                userPreferenceService.savePreference(
                                new UserPreference("user1", Set.of(ChannelType.EMAIL, ChannelType.PUSH)));

                // ──────────────────────────────────────────────────
                // 2. Wire up the dispatcher and services
                // ──────────────────────────────────────────────────
                NotificationDispatcherService notificationDispatcherService = new NotificationDispatcherService(
                                userPreferenceService);

                NotificationService syncService = new NotificationService(notificationDispatcherService);
                AsyncNotificationService asyncService = new AsyncNotificationService(notificationDispatcherService);

                // ──────────────────────────────────────────────────
                // 3. Create notification matching the registered userId
                // ──────────────────────────────────────────────────
                Notification notification = new Notification(
                                "user1", // FIX: was "user1" while prefs were for "user"
                                "Hi, Your order has been delivered!");

                // ──────────────────────────────────────────────────
                // 4a. Synchronous send (blocks until all channels complete)
                // ──────────────────────────────────────────────────
                System.out.println("=== Synchronous Send ===");
                syncService.sendNotification(notification);

                // ──────────────────────────────────────────────────
                // 4b. Asynchronous send (returns immediately, dispatches on worker thread)
                // ──────────────────────────────────────────────────
                System.out.println("\n=== Asynchronous Send ===");
                asyncService.sendNotification(notification);

                // Give async task a moment to complete before JVM exits in this demo
                // In production, the JVM shutdown hook in AsyncNotificationService handles this
                Thread.sleep(500);

                System.out.println("\n=== Unknown User (falls back to EMAIL default) ===");
                syncService.sendNotification(new Notification("unknownUser", "You have a new message!"));

                // asyncService.shutdown() is triggered automatically via JVM shutdown hook
        }
}