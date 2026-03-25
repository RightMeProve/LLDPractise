package org.example.api;

import org.example.model.Notification;
import org.example.service.NotificationDispatcherService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * AsyncNotificationService - Asynchronous API that submits notifications to a
 * thread pool.
 *
 * ✅ DESIGN CHOICE: Thread Pool via ExecutorService
 * Notifications are dispatched on worker threads, freeing the caller
 * immediately.
 * Fixed pool of 10 threads handles bursts without spawning unlimited threads.
 *
 * ────────────────────────────────────────────────────────────────────
 * ⚠️ ORIGINAL CRITICAL FLAW: NO SHUTDOWN HOOK
 * ────────────────────────────────────────────────────────────────────
 * ORIGINAL CODE:
 * Executors.newFixedThreadPool(10) — no shutdown() anywhere
 *
 * PROBLEM:
 * The ExecutorService runs a pool of non-daemon threads.
 * When main() finishes, the JVM WILL NOT EXIT because these threads are still
 * alive.
 * This is a thread/resource LEAK — a critical production bug.
 *
 * ✅ FIX: shutdown() method + JVM shutdown hook registered via
 * Runtime.addShutdownHook()
 * Ensures graceful draining of queued tasks before the JVM exits.
 *
 * ────────────────────────────────────────────────────────────────────
 * ⚠️ OTHER CONCERNS
 * ────────────────────────────────────────────────────────────────────
 *
 * ❌ Hardcoded thread pool size (10):
 * In production, make this configurable (e.g., from properties or environment
 * config)
 * based on traffic load and hardware specs.
 *
 * ❌ No Back-pressure / Queue Bounding:
 * newFixedThreadPool uses an UNBOUNDED queue. Under heavy load,
 * millions of tasks can pile up → OutOfMemoryError.
 * → Use ThreadPoolExecutor with a bounded queue + RejectedExecutionHandler.
 *
 * 💡 PRODUCTION PATTERN:
 * For true async at scale, decouple via a message broker:
 * Producer → Kafka/RabbitMQ → Consumer → Dispatcher
 * This provides durability, replay, and horizontal scaling.
 *
 * 💡 TIP: Share the SAME dispatcher and pool between sync & async services
 * to avoid redundant thread pools in a single JVM.
 */
public class AsyncNotificationService {

    private final NotificationDispatcherService dispatcherService;
    private final ExecutorService executorService;

    // Default thread pool size — make this configurable in production
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;

    public AsyncNotificationService(NotificationDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
        this.executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);

        // ✅ FIX: Register JVM shutdown hook to gracefully drain the thread pool
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "async-notif-shutdown"));
    }

    /**
     * Submits the notification dispatch as a non-blocking task on the thread pool.
     * Returns immediately to the caller — dispatch happens on a worker thread.
     *
     * @param notification the notification to send asynchronously
     */
    public void sendNotification(Notification notification) {
        if (notification == null) {
            System.err.println("[AsyncNotificationService] Skipped null notification.");
            return;
        }
        // Lambda captures notification — safe because Notification is now immutable
        // (final fields)
        executorService.submit(() -> dispatcherService.dispatch(notification));
    }

    /**
     * Gracefully shuts down the thread pool.
     * Waits up to 30 seconds for queued tasks to complete, then forces shutdown.
     *
     * Call this when the application is stopping (e.g., @PreDestroy in Spring).
     */
    public void shutdown() {
        System.out.println("[AsyncNotificationService] Initiating graceful shutdown...");
        executorService.shutdown(); // stop accepting new tasks
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("[AsyncNotificationService] Timeout! Forcing shutdown.");
                executorService.shutdownNow(); // interrupt running tasks
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt(); // restore interrupt flag
        }
        System.out.println("[AsyncNotificationService] Shutdown complete.");
    }
}
