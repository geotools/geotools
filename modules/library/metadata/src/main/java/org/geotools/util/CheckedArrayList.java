/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.util;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.opengis.util.Cloneable;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A {@linkplain Collections#checkedList checked} and {@linkplain Collections#synchronizedList
 * synchronized} {@link java.util.List}. Type checks are performed at run-time in addition of
 * compile-time checks. The synchronization lock can be modified at runtime by overriding the
 * {@link #getLock} method.
 * <p>
 * This class is similar to using the wrappers provided in {@link Collections}, minus the cost
 * of indirection levels and with the addition of overrideable methods.
 *
 * @param <E> The type of elements in the list.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett (Refractions Research)
 * @author Martin Desruisseaux (IRD)
 *
 * @see Collections#checkedList
 * @see Collections#synchronizedList
 */
public class CheckedArrayList<E> extends ArrayList<E> implements CheckedCollection<E>, Cloneable {
    /**
     * Serial version UID for compatibility with different versions.
     */
    private static final long serialVersionUID = -587331971085094268L;

    /**
     * The element type.
     */
    private final Class<E> type;

    /**
     * Constructs a list of the specified type.
     *
     * @param type The element type (should not be null).
     */
    public CheckedArrayList(final Class<E> type) {
        super();
        this.type = type;
        ensureNonNull();
    }

    /**
     * Constructs a list of the specified type and initial capacity.
     *
     * @param type The element type (should not be null).
     * @param capacity The initial capacity.
     *
     * @since 2.4
     */
    public CheckedArrayList(final Class<E> type, final int capacity) {
        super(capacity);
        this.type = type;
        ensureNonNull();
    }

    /**
     * Returns the element type given at construction time.
     *
     * @since 2.4
     */
    public Class<E> getElementType() {
        return type;
    }

    /**
     * Make sure that {@link #type} is non-null.
     */
    private void ensureNonNull() {
        if (type == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "type"));
        }
    }

    /**
     * Checks the type of the specified object. The default implementation ensure
     * that the object is assignable to the type specified at construction time.
     *
     * @param  element the object to check, or {@code null}.
     * @throws IllegalArgumentException if the specified element is not of the expected type.
     */
    protected void ensureValidType(final E element) throws IllegalArgumentException {
        if (element!=null && !type.isInstance(element)) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.ILLEGAL_CLASS_$2, element.getClass(), type));
        }
    }

    /**
     * Checks the type of all elements in the specified collection.
     *
     * @param  collection the collection to check, or {@code null}.
     * @throws IllegalArgumentException if at least one element is not of the expected type.
     */
    private void ensureValid(final Collection<? extends E> collection) throws IllegalArgumentException {
        if (collection != null) {
            for (final E element : collection) {
                ensureValidType(element);
            }
        }
    }

    /**
     * Checks if changes in this collection are allowed. This method is automatically invoked
     * after this collection got the {@linkplain #getLock lock} and before any operation that
     * may change the content. The default implementation does nothing (i.e. this collection
     * is modifiable). Subclasses should override this method if they want to control write
     * access.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     *
     * @since 2.5
     */
    protected void checkWritePermission() throws UnsupportedOperationException {
    }

    /**
     * Returns the synchronization lock. The default implementation returns {@code this}.
     * Subclasses that override this method should be careful to update the lock reference
     * when this list is {@linkplain #clone cloned}.
     *
     * @return The synchronization lock.
     *
     * @since 2.5
     */
    protected Object getLock() {
        return this;
    }

    /**
     * Returns an iterator over the elements in this list.
     */
    @Override
    public Iterator<E> iterator() {
        final Object lock = getLock();
        synchronized (lock) {
            return new SynchronizedIterator<E>(super.iterator(), lock);
        }
    }

    // Note: providing a synchronized iterator is a little bit of paranoia because the ArrayList
    // implementation inherits the default AbstractList implementation, which delegates its work
    // to the public List methods. And the later are synchronized. We do not override ListIterator
    // for this reason and because it is less used.

    /**
     * Returns the number of elements in this list.
     */
    @Override
    public int size() {
	synchronized (getLock()) {
            return super.size();
        }
    }

    /**
     * Returns {@code true} if this list contains no elements.
     */
    @Override
    public boolean isEmpty() {
	synchronized (getLock()) {
            return super.isEmpty();
        }
    }

    /**
     * Returns {@code true} if this list contains the specified element.
     */
    @Override
    public boolean contains(final Object o) {
	synchronized (getLock()) {
            return super.contains(o);
        }
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list,
     * or -1 if none.
     */
    @Override
    public int indexOf(Object o) {
	synchronized (getLock()) {
            return super.indexOf(o);
        }
    }

    /**
     * Returns the index of the last occurrence of the specified element in this list,
     * or -1 if none.
     */
    @Override
    public int lastIndexOf(Object o) {
	synchronized (getLock()) {
            return super.lastIndexOf(o);
        }
    }

    /**
     * Returns the element at the specified position in this list.
     */
    @Override
    public E get(int index) {
        synchronized (getLock()) {
            return super.get(index);
        }
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param  index   index of element to replace.
     * @param  element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws IndexOutOfBoundsException if index out of range.
     * @throws IllegalArgumentException if the specified element is not of the expected type.
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public E set(final int index, final E element)
            throws IllegalArgumentException, UnsupportedOperationException
    {
        ensureValidType(element);
        synchronized (getLock()) {
            checkWritePermission();
            return super.set(index, element);
        }
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param  element element to be appended to this list.
     * @return always {@code true}.
     * @throws IllegalArgumentException if the specified element is not of the expected type.
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public boolean add(final E element)
            throws IllegalArgumentException, UnsupportedOperationException
    {
        ensureValidType(element);
        synchronized (getLock()) {
            checkWritePermission();
            return super.add(element);
        }
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param  index index at which the specified element is to be inserted.
     * @param  element element to be inserted.
     * @throws IndexOutOfBoundsException if index out of range.
     * @throws IllegalArgumentException if the specified element is not of the expected type.
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public void add(final int index, final E element)
            throws IllegalArgumentException, UnsupportedOperationException
    {
        ensureValidType(element);
        synchronized (getLock()) {
            checkWritePermission();
            super.add(index, element);
        }
    }

    /**
     * Appends all of the elements in the specified collection to the end of this list,
     * in the order that they are returned by the specified Collection's Iterator.
     *
     * @param collection the elements to be inserted into this list.
     * @return {@code true} if this list changed as a result of the call.
     * @throws IllegalArgumentException if at least one element is not of the expected type.
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public boolean addAll(final Collection<? extends E> collection)
            throws IllegalArgumentException, UnsupportedOperationException
    {
        ensureValid(collection);
        synchronized (getLock()) {
            checkWritePermission();
            return super.addAll(collection);
        }
    }

    /**
     * Inserts all of the elements in the specified collection into this list,
     * starting at the specified position.
     *
     * @param index index at which to insert first element fromm the specified collection.
     * @param collection elements to be inserted into this list.
     * @return {@code true} if this list changed as a result of the call.
     * @throws IllegalArgumentException if at least one element is not of the expected type.
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> collection)
            throws IllegalArgumentException, UnsupportedOperationException
    {
        ensureValid(collection);
        synchronized (getLock()) {
            checkWritePermission();
            return super.addAll(index, collection);
        }
    }

    /**
     * Removes the element at the specified position in this list.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public E remove(int index) throws UnsupportedOperationException {
        synchronized (getLock()) {
            checkWritePermission();
            return super.remove(index);
        }
    }

    /**
     * Removes the first occurrence of the specified element from this list.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public boolean remove(Object o) throws UnsupportedOperationException {
        synchronized (getLock()) {
            checkWritePermission();
            return super.remove(o);
        }
    }

    /**
     * Removes all of this list's elements that are also contained in the specified collection.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public boolean removeAll(Collection<?> c) throws UnsupportedOperationException {
        synchronized (getLock()) {
            checkWritePermission();
            return super.removeAll(c);
        }
    }

    /**
     * Retains only the elements in this list that are contained in the specified collection.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public boolean retainAll(Collection<?> c) throws UnsupportedOperationException {
        synchronized (getLock()) {
            checkWritePermission();
            return super.retainAll(c);
        }
    }

    /**
     * Trims the capacity to the list's current size.
     */
    @Override
    public void trimToSize() {
        synchronized (getLock()) {
            super.trimToSize();
        }
    }

    /**
     * Increases the capacity, if necessary, to ensure that it can hold the given number
     * of elements.
     */
    @Override
    public void ensureCapacity(final int minCapacity) {
        synchronized (getLock()) {
            super.ensureCapacity(minCapacity);
        }
    }

    /**
     * Removes all of the elements from this list.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public void clear() throws UnsupportedOperationException {
        synchronized (getLock()) {
            checkWritePermission();
            super.clear();
        }
    }

    /**
     * Returns an array containing all of the elements in this list.
     */
    @Override
    public Object[] toArray() {
        synchronized (getLock()) {
            return super.toArray();
        }
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence.
     *
     * @param <T> The type of array elements.
     */
    @Override
    public <T> T[] toArray(T[] a) {
        synchronized (getLock()) {
            return super.toArray(a);
        }
    }

    /**
     * Returns a string representation of this list.
     */
    @Override
    public String toString() {
        synchronized (getLock()) {
            return super.toString();
        }
    }

    /**
     * Compares the specified object with this list for equality.
     */
    @Override
    public boolean equals(Object o) {
        synchronized (getLock()) {
            return super.equals(o);
        }
    }

    /**
     * Returns the hash code value for this list.
     */
    @Override
    public int hashCode() {
        synchronized (getLock()) {
            return super.hashCode();
        }
    }

    /**
     * Returns a shallow copy of this list.
     *
     * @return A shallow copy of this list.
     */
    @Override
    @SuppressWarnings("unchecked")
    public CheckedArrayList<E> clone() {
        synchronized (getLock()) {
            return (CheckedArrayList) super.clone();
        }
    }
}
