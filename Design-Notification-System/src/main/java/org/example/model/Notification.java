package org.example.model;

/**
 * Notification - Represents a single notification to be sent to a user.
 *
 * ✅ GOOD DESIGN:
 * - Simple POJO / Value Object pattern
 * - Contains only domain data, no behaviour
 *
 * ⚠️ FLAW: MUTABLE FIELDS
 * Fields (userId, message) are MUTABLE — there are no 'final' modifiers.
 * A Notification should be immutable once created. Shared across threads
 * (e.g., in AsyncNotificationService), this creates a race condition risk.
 *
 * ✅ FIX: Mark fields as 'final' — data set once in constructor, never changed.
 *
 * ⚠️ MISSING FIELDS (LLD Interview Follow-ups):
 * - notificationId : unique ID for deduplication and tracking
 * - notificationType: e.g., ORDER_UPDATE, ALERT, PROMOTION
 * - timestamp : when it was created
 * - priority : HIGH / MEDIUM / LOW (for retry & queue ordering)
 *
 * 💡 PATTERN NOTE:
 * For richer objects with many optional fields, consider the Builder pattern.
 * e.g., Notification.builder().userId("u1").message("Hello").build()
 */
public class Notification {

    // FIX: fields are now final → immutability guaranteed
    private final String userId;
    private final String message;

    public Notification(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Notification{userId='" + userId + "', message='" + message + "'}";
    }
}
