package com.es.phoneshop.security;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class DefaultDosProtectionService implements DosProtectionService{
    private static volatile DefaultDosProtectionService instance;
    private static Long THRESHOLD = 20L;

    public static DefaultDosProtectionService getInstance() {
        if (instance == null) {
            synchronized (DefaultDosProtectionService.class) {
                if (instance == null) {
                    instance = new DefaultDosProtectionService();
                }
            }
        }

        return instance;
    }

    private final Map<String, Deque<String>> filterCountMap = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private DefaultDosProtectionService() {

    }

    @Override
    public boolean isAllowed(String ip) {
        rwLock.writeLock().lock();
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            Deque<String> ipCounter = filterCountMap.get(ip);

            if (ipCounter == null) {
                ipCounter = new ArrayDeque<>();
                filterCountMap.put(ip, ipCounter);
                return true;
            }

            boolean isAllowedToEnter = true;
            recalculateTime(dateTime, ipCounter);
            if (ipCounter.size() > THRESHOLD) {
                isAllowedToEnter = false;
            }

            ipCounter.addLast(dateTime.toString());
            return isAllowedToEnter;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void recalculateTime(LocalDateTime dateTime, Deque<String> ipCounter) {
        ipCounter.removeIf(item -> Duration.between(LocalDateTime.parse(item), dateTime).getSeconds() > 60);
    }
}
