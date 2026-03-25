package org.example.service;

import org.example.channel.NotificationChannel;
import org.example.factory.NotificationChannelFactory;
import org.example.model.ChannelType;
import org.example.model.Notification;
import org.example.model.UserPreference;

import java.util.Set;

/**
 * NotificationDispatcherService - Orchestrates the routing of a Notification to
 * all of the user's preferred NotificationChannels.
 *
 * ✅ DESIGN PATTERN: Strategy + Factory combination
 * - Looks up the user's preferred channels (preference-based routing)
 * - Delegates to the Factory to obtain the correct NotificationChannel
 * (Strategy)
 * - Invokes send() on each channel
 *
 * ✅ SOLID: Single Responsibility Principle (SRP)
 * Solely responsible for DISPATCH logic:
 * look up preference → get channel → send
 * It does NOT manage threads, handle persistence, or implement channel logic.
 *
 * ✅ SOLID: Dependency Inversion Principle (DIP)
 * Depends on UserPreferenceService and NotificationChannel (abstractions),
 * NOT on concrete ConcurrentHashMap or EmailChannel directly.
 *
 * ────────────────────────────────────────────────────────────────────
 * ⚠️ ORIGINAL FLAWS FIXED
 * ────────────────────────────────────────────────────────────────────
 *
 * ❌ FLAW 1: No Null Guard on Preference
 * If preferenceService.getPreference() returns null (e.g., future code change),
 * the original code would throw a NullPointerException with no useful context.
 * → Added explicit null check with a descriptive log.
 *
 * ❌ FLAW 2: No Exception Handling Per Channel
 * ORIGINAL: if Email send() throws (e.g., SMTP timeout), the loop breaks and
 * SMS / Push are NEVER attempted — silent data loss.
 * → FIX: Wrap each channel.send() in try-catch so one failure doesn't block
 * others.
 *
 * ❌ FLAW 3: Factory method name was `getChannelType` — misleading.
 * → Updated to call `NotificationChannelFactory.getChannel()` (clearer name).
 *
 * 💡 PRODUCTION IMPROVEMENTS (not implemented here):
 * - Add structured LOGGING (SLF4J) for observability
 * - Add metrics (success/failure count per channel)
 * - Add retry mechanism for transient failures
 * - Publish failure events to a Dead Letter Queue
 */
public class NotificationDispatcherService {

    private final UserPreferenceService preferenceService;

    public NotificationDispatcherService(UserPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    /**
     * Dispatches the notification to all channels preferred by the notification's
     * user.
     *
     * @param notification the notification to dispatch (must not be null)
     */
    public void dispatch(Notification notification) {
        if (notification == null) {
            System.err.println("[DISPATCHER] Received null notification — skipping.");
            return;
        }

        // Step 1: Resolve user preferences (falls back to EMAIL if no preference saved)
        UserPreference preference = preferenceService.getPreference(notification.getUserId());

        if (preference == null) {
            System.err.println("[DISPATCHER] No preference found for user: " + notification.getUserId());
            return;
        }

        Set<ChannelType> channels = preference.getPreferredChannels();

        // Step 2: Iterate over each preferred channel and attempt delivery
        for (ChannelType channelType : channels) {
            try {
                // Factory returns a shared, stateless channel instance (Flyweight)
                NotificationChannel channel = NotificationChannelFactory.getChannel(channelType);
                channel.send(notification);
            } catch (Exception e) {
                // ✅ FIX: One failing channel does NOT block others
                // In production: log to metrics + push to retry queue
                System.err.println(
                        "[DISPATCHER] Failed to send via " + channelType
                                + " for user " + notification.getUserId()
                                + " → Reason: " + e.getMessage());
            }
        }
    }
}
