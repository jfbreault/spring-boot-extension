package com.ticketmaster.boot.logging.support;

import static org.springframework.util.Assert.state;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import org.springframework.util.Assert;

public class EvictingQueue<E> implements Collection<E> {

    final Queue<E> delegate;
    final int maxSize;

    public static <E> EvictingQueue<E> create(int maxSize) {
        return new EvictingQueue<E>(maxSize);
    }

    private EvictingQueue(int maxSize) {
        state(maxSize >= 0, "maxSize (" + maxSize + ") must >= 0");
        this.delegate = new ArrayDeque<E>(maxSize);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        Assert.notNull(e); // check before removing
        if (maxSize == 0) {
            return true;
        }
        if (size() == maxSize) {
            delegate.remove();
        }
        delegate.add(e);
        return true;
    }

    protected Queue<E> delegate() {
        return delegate;
    }

    @Override
    public Iterator<E> iterator() {
        return delegate().iterator();
    }

    @Override
    public int size() {
        return delegate().size();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return delegate().removeAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    @Override
    public boolean contains(Object object) {
        return delegate().contains(object);
    }

    @Override
    public boolean remove(Object object) {
        return delegate().remove(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return delegate().containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return delegate().addAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return delegate().retainAll(collection);
    }

    @Override
    public void clear() {
        delegate().clear();
    }

    @Override
    public Object[] toArray() {
        return delegate().toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return delegate().toArray(array);
    }

}
