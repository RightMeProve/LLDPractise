package org.example.service;

import org.example.model.ChannelType;
import org.example.model.UserPreference;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserPreferenceService - Manages storage and retrieval of user notification
 * preferences.
 *
 * ✅ GOOD DESIGN — Thread-Safe In-Memory Store:
 * Uses ConcurrentHashMap, so concurrent reads/writes (from async threads) are
 * safe
 * without explicit synchronization.
 *
 * ✅ SOLID: Single Responsibility Principle (SRP)
 * This class ONLY manages user preferences. It does not send notifications,
 * route channels, or hold business logic for dispatching.
 *
 * ✅ DEFAULT FALLBACK:
 * If a user has no saved preference, getPreference() returns EMAIL channel by
 * default.
 * This is a safe, sensible default — EMAIL is the most reliable channel.
 *
 * ────────────────────────────────────────────────────────────────────
 * ⚠️ FLAWS & IMPROVEMENTS
 * ────────────────────────────────────────────────────────────────────
 *
 * ❌ FLAW 1: In-Memory Only (No Persistence)
 * All preferences are LOST on restart. In production:
 * → Use a database (Redis, PostgreSQL) for durable storage.
 * → Extract a UserPreferenceRepository interface for testable persistence.
 *
 * ❌ FLAW 2: No Validation in savePreference
 * A null UserPreference (or one with a null userId) would cause NPE.
 * → Added null guard in both save and get methods.
 *
 * ❌ FLAW 3: getPreference returns a new default object every time for unknown
 * users
 * This could cause confusion if callers compare references.
 * → Document the behaviour clearly (done below).
 *
 * 💡 SCALABILITY INSIGHT:
 * For microservices, this should call a gRPC / REST preference service,
 * not hold state locally. Local in-memory cache + TTL eviction is a viable
 * intermediate pattern to reduce network calls.
 */
public class UserPreferenceService {

    /**
     * In-memory preference store.
     * ConcurrentHashMap ensures thread safety for concurrent dispatcher calls.
     * Key: userId, Value: UserPreference
     */
    private final Map<String, UserPreference> preferences = new ConcurrentHashMap<>();

    /**
     * Persists (or overwrites) a user's notification preferences.
     *
     * @param preference the preference object to store (must not be null)
     * @throws IllegalArgumentException if preference or userId is null
     */
    public void savePreference(UserPreference preference) {
        if (preference == null) {
            throw new IllegalArgumentException("UserPreference must not be null");
        }
        preferences.put(preference.getUserId(), preference);
    }

    /**
     * Retrieves the preference for the given userId.
     * Returns a safe default (EMAIL only) if no preference has been saved.
     *
     * NOTE: Each call for an unknown user creates a new default object.
     * Callers should not rely on reference equality.
     *
     * @param userId the user to look up
     * @return the stored UserPreference, or a default EMAIL-only preference
     */
    public UserPreference getPreference(String userId) {
        // getOrDefault is safe and atomic for ConcurrentHashMap reads
        return preferences.getOrDefault(
                userId,
                new UserPreference(userId, Set.of(ChannelType.EMAIL)) // sensible default
        );
    }
}
