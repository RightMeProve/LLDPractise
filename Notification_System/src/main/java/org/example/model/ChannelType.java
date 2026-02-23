package org.example.model;

/**
 * ChannelType - Enum representing supported notification delivery channels.
 *
 * ✅ WHY AN ENUM?
 * Using an enum gives us a closed, type-safe set of channel names.
 * It prevents magic strings (like "email", "Email", "EMAIL") from
 * leaking into switch-cases and comparisons.
 *
 * ⚠️ SCALABILITY CONCERN (Open/Closed Principle - SOLID):
 * Every time you add a NEW channel (e.g., IN_APP, WHATSAPP), you must:
 * 1. Add the value here
 * 2. Update the switch-case in NotificationChannelFactory
 * This violates OCP (Open for extension, Closed for modification).
 *
 * 💡 BETTER APPROACH (for large-scale systems):
 * Instead of a switch-case factory, use a Map<ChannelType, NotificationChannel>
 * that can be populated at startup (e.g., via a registry/config).
 * New channels can then be registered WITHOUT touching the factory.
 */
public enum ChannelType {
    SMS,
    EMAIL,
    PUSH
    // To add WHATSAPP or IN_APP: add here + register in factory/registry
}
