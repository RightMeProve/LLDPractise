package org.example.factory;

import org.example.channel.EmailNotificationChannel;
import org.example.channel.NotificationChannel;
import org.example.channel.PushNotificationChannel;
import org.example.channel.SMSNotificationChannel;
import org.example.model.ChannelType;

import java.util.EnumMap;
import java.util.Map;

/**
 * NotificationChannelFactory - Factory Pattern for creating/retrieving
 * NotificationChannels.
 *
 * ✅ DESIGN PATTERN: Factory Pattern (Static Factory variant)
 * The caller (Dispatcher) asks for a channel by type and gets back a
 * NotificationChannel without knowing which concrete class is returned.
 * This decouples the creation from the usage.
 *
 * ────────────────────────────────────────────────────────────────────
 * ⚠️ ORIGINAL FLAW: NEW OBJECT PER CALL (Object Thrashing)
 * ────────────────────────────────────────────────────────────────────
 * ORIGINAL CODE:
 * case EMAIL -> new EmailNotificationChannel();
 * Each call to `getChannel()` created a BRAND NEW channel object.
 * Channel objects are STATELESS — they have zero instance state.
 * Creating thousands of them per second is wasteful (GC pressure).
 *
 * ✅ FIX: Pre-instantiate channels in a static EnumMap (Flyweight Pattern).
 * All calls share the SAME channel instance — safe because channels are
 * stateless.
 * This is also O(1) lookup instead of a switch evaluation each time.
 *
 * ────────────────────────────────────────────────────────────────────
 * ⚠️ SOLID VIOLATION: Open/Closed Principle (OCP)
 * ────────────────────────────────────────────────────────────────────
 * To add a new channel (e.g., WhatsApp), you MUST modify this class.
 * This violates OCP (classes should be open for extension, closed for
 * modification).
 *
 * 💡 HOW TO FIX OCP:
 * Replace switch-case with a registry pattern:
 * public static void register(ChannelType type, NotificationChannel channel) {
 * CHANNEL_MAP.put(type, channel);
 * }
 * Each channel module self-registers at startup (e.g., via @PostConstruct or
 * static init).
 * The factory itself never needs to change when new channels are added.
 *
 * ────────────────────────────────────────────────────────────────────
 * ⚠️ MISSING: Error Handling for Unknown Channel
 * ────────────────────────────────────────────────────────────────────
 * If `channelType` has no mapping, we throw a descriptive exception
 * rather than returning null (which would cause a NullPointerException
 * downstream).
 */
public class NotificationChannelFactory {

    /**
     * FLYWEIGHT: Pre-instantiated, shared, stateless channel instances.
     * EnumMap is faster and more memory-efficient than HashMap for enum keys.
     */
    private static final Map<ChannelType, NotificationChannel> CHANNEL_MAP;

    static {
        // Initialize once at class-load time; channels are reused across all dispatch
        // calls
        CHANNEL_MAP = new EnumMap<>(ChannelType.class);
        CHANNEL_MAP.put(ChannelType.EMAIL, new EmailNotificationChannel());
        CHANNEL_MAP.put(ChannelType.SMS, new SMSNotificationChannel());
        CHANNEL_MAP.put(ChannelType.PUSH, new PushNotificationChannel());
        // 💡 To add WHATSAPP: CHANNEL_MAP.put(ChannelType.WHATSAPP, new
        // WhatsAppNotificationChannel());
    }

    /**
     * Returns the shared NotificationChannel for the given type.
     *
     * @param channelType the delivery channel requested
     * @return the corresponding NotificationChannel (never null)
     * @throws IllegalArgumentException if the channel type is not registered
     */
    public static NotificationChannel getChannel(ChannelType channelType) {
        NotificationChannel channel = CHANNEL_MAP.get(channelType);
        if (channel == null) {
            // Fail fast with a descriptive message rather than a silent NPE downstream
            throw new IllegalArgumentException(
                    "No channel registered for type: " + channelType);
        }
        return channel;
    }

    // Private constructor — utility class, not meant to be instantiated
    private NotificationChannelFactory() {
    }
}
