package org.example.model;

import java.util.Collections;
import java.util.Set;

/**
 * UserPreference - Stores a user's preferred notification delivery channels.
 *
 * ✅ GOOD DESIGN:
 * - Encapsulates all user-specific notification config in one place.
 * - Uses a Set<ChannelType> so duplicates are automatically prevented.
 *
 * ⚠️ FLAW 1: MUTABLE SET RETURNED DIRECTLY
 * getPreferredChannels() returns the internal Set reference.
 * A caller could mutate it: preference.getPreferredChannels().clear()
 * This breaks ENCAPSULATION (one of OOP's core pillars).
 *
 * ✅ FIX: Return Collections.unmodifiableSet(preferredChannels)
 *
 * ⚠️ FLAW 2: NO NULL / EMPTY VALIDATION
 * If someone passes null or empty set as preferredChannels,
 * notifications will silently fail. Add a guard in the constructor.
 *
 * 💡 MISSING FIELDS (LLD Follow-ups):
 * - quietHours : time range when no notifications should be sent
 * - maxPerDay : rate-limiting the user to avoid notification spam
 * - locale / timezone: for proper time-zone aware delivery
 */
public class UserPreference {

    private final String userId;

    // Store as an unmodifiable snapshot so external callers cannot mutate it
    private final Set<ChannelType> preferredChannels;

    public UserPreference(String userId, Set<ChannelType> preferredChannels) {
        // Guard: ensure we never store null or empty preferences
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be null or blank");
        }
        if (preferredChannels == null || preferredChannels.isEmpty()) {
            throw new IllegalArgumentException("At least one preferred channel must be specified");
        }
        this.userId = userId;
        // FIX: wrap in unmodifiable to prevent external mutation
        this.preferredChannels = Collections.unmodifiableSet(preferredChannels);
    }

    public String getUserId() {
        return userId;
    }

    /**
     * Returns an unmodifiable view of the user's preferred channels.
     * Callers can iterate but cannot mutate this set.
     */
    public Set<ChannelType> getPreferredChannels() {
        return preferredChannels;
    }

    @Override
    public String toString() {
        return "UserPreference{userId='" + userId + "', channels=" + preferredChannels + "}";
    }
}
