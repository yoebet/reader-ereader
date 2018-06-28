package wjy.yo.ereader.util;

import android.os.SystemClock;
import android.support.v4.util.ArrayMap;

import java.util.concurrent.TimeUnit;


public class RateLimiter<K> {
    private ArrayMap<K, Long> timestamps = new ArrayMap<>();
    private final long timeout;

    public static final RateLimiter<String> RequestFailOrNoDataRetryRateLimit = new RateLimiter<>(10, TimeUnit.SECONDS);

    public RateLimiter(int timeout, TimeUnit timeUnit) {
        this.timeout = timeUnit.toMillis(timeout);
    }

    public synchronized void touch(K key) {
        timestamps.put(key, now());
    }

    public synchronized boolean shouldFetch(K key) {
        Long lastFetched = timestamps.get(key);
        long now = now();
        if (lastFetched == null) {
            timestamps.put(key, now);
            return true;
        }
        if (now - lastFetched > timeout) {
            timestamps.put(key, now);
            return true;
        }
        return false;
    }

    private long now() {
        return SystemClock.uptimeMillis();
    }

    public synchronized void reset(K key) {
        timestamps.remove(key);
    }
}
