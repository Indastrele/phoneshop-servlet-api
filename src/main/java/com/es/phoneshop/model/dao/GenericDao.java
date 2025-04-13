package com.es.phoneshop.model.dao;

import com.es.phoneshop.model.exceptions.product.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class GenericDao<T extends DaoItem, E extends RuntimeException> {
    protected final ReentrantReadWriteLock rwLock =  new ReentrantReadWriteLock();
    protected List<T> items = new ArrayList<>();
    protected Long identity = 1L;

    final protected T getItem(Long id, E exception) {
        rwLock.readLock().lock();
        try {
            return  items.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findFirst()
                    .orElseThrow(() -> exception);
        }
        finally {
            rwLock.readLock().unlock();
        }
    }

    public void save(T item, E exception) {
        rwLock.writeLock().lock();
        try {
            var id = item.getId();
            if (id == null) {
                item.setId(identity++);
                items.add(item);
                return;
            }

            var oldItem = getItem(id, exception);

            var index = items.indexOf(oldItem);
            item.setId(oldItem.getId());
            items.set(index, item);
        }
        finally {
            rwLock.writeLock().unlock();
        }
    }
}
