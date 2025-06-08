/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.referencing.AuthorityFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.NoSuchIdentifierException;
import org.geotools.api.referencing.operation.CoordinateOperationAuthorityFactory; // For javadoc
import org.geotools.util.Utilities;

/**
 * A lazy set of {@linkplain IdentifiedObject identified objects}. This set creates {@link IdentifiedObject}s from
 * authority codes only when first needed. This class is typically used as the set returned by implementations of the
 * {@link CoordinateOperationAuthorityFactory#createFromCoordinateReferenceSystemCodes
 * createFromCoordinateReferenceSystemCodes} method. Deferred creation in this case may have great performance impact
 * since a set may contains about 40 entries (e.g. transformations from "ED50" (EPSG:4230) to "WGS 84" (EPSG:4326))
 * while some users only want to look for the first entry (e.g. the default
 * {@link org.geotools.referencing.operation.AuthorityBackedFactory} implementation).
 *
 * <p>
 *
 * <h3>Exception handling</h3>
 *
 * If the underlying factory failed to creates an object because of an unsupported operation method
 * ({@link NoSuchIdentifierException}), the exception is logged with the {@link Level#FINE FINE} level (because this is
 * a recoverable failure) and the iteration continue. If the operation creation failed for any other kind of reason
 * ({@link FactoryException}), then the exception is rethrown as an unchecked {@link BackingStoreException}. This
 * default behavior can be changed if a subclass overrides the {@link #isRecoverableFailure isRecoverableFailure}
 * method.
 *
 * <p>
 *
 * <h3>Serialization</h3>
 *
 * Serialization of this class forces the immediate creation of all {@linkplain IdentifiedObject identified objects} not
 * yet created. The serialized set is disconnected from the {@linkplain #factory underlying factory}.
 *
 * <p>
 *
 * <h3>Thread safety</h3>
 *
 * This class is not thread-safe.
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class IdentifiedObjectSet extends AbstractSet implements Serializable {
    /** For cross-version compatibility during serialisation. */
    private static final long serialVersionUID = -4221260663706882719L;

    /**
     * The map of object codes (keys), and the actual identified objects (values) when it has been created. Each entry
     * has a null value until the corresponding object is created.
     */
    private final Map<String, IdentifiedObject> objects = new LinkedHashMap<>();

    /** The factory to use for creating {@linkplain IdentifiedObject identified objects} when first needed. */
    protected final AuthorityFactory factory;

    /**
     * Creates an initially empty set. The {@linkplain IdentifiedObject}s will be created when first needed using the
     * specified factory.
     *
     * @param factory The factory to use for deferred {@link IdentifiedObject}s creations.
     */
    public IdentifiedObjectSet(final AuthorityFactory factory) {
        this.factory = factory;
    }

    /** Removes all of the elements from this collection. */
    @Override
    public void clear() {
        objects.clear();
    }

    /**
     * Returns the number of objects available in this set. Note that this number may decrease during the iteration
     * process if the creation of some {@linkplain IdentifiedObject identified objects} failed.
     */
    @Override
    public int size() {
        return objects.size();
    }

    /**
     * Ensures that this collection contains an object for the specified authority code. The
     * {@linkplain IdentifiedObject identified object} will be created from the specified code only when first needed.
     * This method returns {@code true} if this set changed as a result of this call.
     */
    public boolean addAuthorityCode(final String code) {
        final boolean already = objects.containsKey(code);
        final IdentifiedObject old = objects.put(code, null);
        if (old != null) {
            // A fully created object was already there. Keep it.
            objects.put(code, old);
            return false;
        }
        return !already;
    }

    /**
     * Ensures that this collection contains the specified object. This set do not allows multiple objects for the same
     * {@linkplain #getAuthorityCode authority code}. If this set already contains an object using the same
     * {@linkplain #getAuthorityCode authority code} than the specified one, then the old object is replaced by the new
     * one even if the objects are not otherwise identical.
     */
    @Override
    public boolean add(final Object object) {
        final String code = getAuthorityCode((IdentifiedObject) object);
        return !Utilities.equals(objects.put(code, (IdentifiedObject) object), object);
    }

    /**
     * Returns the identified object for the specified value, {@linkplain #createObject creating} it if needed.
     *
     * @throws BackingStoreException if the object creation failed.
     */
    private IdentifiedObject get(final String code) throws BackingStoreException {
        IdentifiedObject object = objects.get(code);
        if (object == null && objects.containsKey(code)) {
            try {
                object = createObject(code);
                objects.put(code, object);
            } catch (FactoryException exception) {
                if (!isRecoverableFailure(exception)) {
                    throw new BackingStoreException(exception);
                }
                log(exception, code);
                objects.remove(code);
            }
        }
        return object;
    }

    /** Returns {@code true} if this collection contains the specified object. */
    @Override
    public boolean contains(final Object object) {
        final String code = getAuthorityCode((IdentifiedObject) object);
        final IdentifiedObject current = get(code);
        return object.equals(current);
    }

    /** Removes a single instance of the specified element from this collection, if it is present. */
    @Override
    public boolean remove(final Object object) {
        final String code = getAuthorityCode((IdentifiedObject) object);
        final IdentifiedObject current = get(code);
        if (object.equals(current)) {
            objects.remove(code);
            return true;
        }
        return false;
    }

    /** Removes from this collection all of its elements that are contained in the specified collection. */
    @Override
    public boolean removeAll(final Collection collection) {
        boolean modified = false;
        for (Object o : collection) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Returns an iterator over the objects in this set. If the iteration encounter any kind of {@link FactoryException}
     * other than {@link NoSuchIdentifierException}, then the exception will be rethrown as an unchecked
     * {@link BackingStoreException}.
     */
    @Override
    public Iterator iterator() {
        return new Iter(objects.entrySet().iterator());
    }

    /**
     * Ensures that the <var>n</var> first objects in this set are created. This method is typically invoked after some
     * calls to {@link #addAuthorityCode} in order to make sure that the {@linkplain #factory underlying factory} is
     * really capable to create at least one object. {@link FactoryException} (except the ones accepted as
     * {@linkplain #isRecoverableFailure recoverable failures}) are thrown as if they were never wrapped into
     * {@link BackingStoreException}.
     *
     * @param n The number of object to resolve. If this number is equals or greater than the {@linkplain #size set's
     *     size}, then the creation of all objects is garantee successful.
     * @throws FactoryException if an {@linkplain #createObject object creation} failed.
     */
    @SuppressWarnings("PMD.UnusedLocalVariable")
    public void resolve(int n) throws FactoryException {
        if (n > 0)
            try {
                for (Object unused : this) {
                    if (--n == 0) {
                        break;
                    }
                }
            } catch (BackingStoreException exception) {
                final Throwable cause = exception.getCause();
                if (cause instanceof FactoryException) {
                    throw (FactoryException) cause;
                }
                throw exception;
            }
    }

    /**
     * Returns the {@linkplain #getAuthorityCode authority code} of all objects in this set. The returned array contains
     * the codes in iteration order. This method do not trig the {@linkplain #createObject creation} of any new object.
     *
     * <p>This method is typically used together with {@link #setAuthorityCodes} for altering the iteration order on the
     * basis of authority codes.
     */
    public String[] getAuthorityCodes() {
        final Set<String> codes = objects.keySet();
        return codes.toArray(new String[codes.size()]);
    }

    /**
     * Set the content of this set as an array of authority codes. For any code in the given list, this method will
     * preserve the corresponding {@linkplain IdentifiedObject identified object} if it was already created. Other
     * objects will be {@linkplain #createObject created} only when first needed, as usual in this
     * {@code IdentifiedObjectSet} implementation.
     *
     * <p>This method is typically used together with {@link #getAuthorityCodes} for altering the iteration order on the
     * basis of authority codes. If the specified {@code codes} array contains the same elements than
     * {@link #getAuthorityCodes} in a different order, then this method just set the new ordering.
     *
     * @see #addAuthorityCode
     */
    public void setAuthorityCodes(final String... codes) {
        final Map<String, IdentifiedObject> copy = new HashMap<>(objects);
        objects.clear();
        for (final String code : codes) {
            objects.put(code, copy.get(code));
        }
    }

    /**
     * Returns the code to uses as a key for the specified object. The default implementation returns the code of the
     * first {@linkplain IdentifiedObject#getIdentifiers identifier}, if any, or the code of
     * the{@linkplain IdentifiedObject#getName primary name} otherwise. Subclasses may overrides this method if they
     * want to use a different key for this set.
     */
    protected String getAuthorityCode(final IdentifiedObject object) {
        final Identifier id;
        final Set identifiers = object.getIdentifiers();
        if (identifiers != null && !identifiers.isEmpty()) {
            id = (Identifier) identifiers.iterator().next();
        } else {
            id = object.getName();
        }
        return id.getCode();
    }

    /**
     * Creates an object for the specified authority code. This method is invoked during the iteration process if an
     * object was not already created. The default implementation invokes <code>
     * {@linkplain #factory}.{@link AuthorityFactory#createObject createObject}(code)</code>. Subclasses may override
     * this method if they want to invoke a more specific method.
     */
    protected IdentifiedObject createObject(final String code) throws FactoryException {
        return factory.createObject(code);
    }

    /**
     * Returns {@code true} if the specified exception should be handled as a recoverable failure. This method is
     * invoked during the iteration process if the factory failed to create some object. If this method returns
     * {@code true} for the given exception, then the exception will be logged in the
     * {@linkplain AbstractAuthorityFactory#LOGGER Geotools factory logger} with the {@link Level#FINE FINE} level. If
     * this method returns {@code false}, then the exception will be retrown as a {@link BackingStoreException}. The
     * default implementation returns {@code true} only for {@link NoSuchIdentifierException} (not to be confused with
     * {@link NoSuchAuthorityCodeException}).
     */
    protected boolean isRecoverableFailure(final FactoryException exception) {
        return exception instanceof NoSuchIdentifierException
                || exception.getMessage().startsWith("Could not locate grid file");
    }

    /**
     * Log an message for the specified exception.
     *
     * @todo Localize.
     */
    static void log(final FactoryException exception, final String code) {
        final LogRecord record = new LogRecord(Level.FINE, "Failed to create an object for code \"" + code + "\".");
        record.setSourceClassName(IdentifiedObjectSet.class.getName());
        record.setSourceMethodName("createObject");
        record.setThrown(exception);
        final Logger logger = AbstractAuthorityFactory.LOGGER;
        record.setLoggerName(logger.getName());
        logger.log(record);
    }

    /**
     * Returns a serializable copy of this set. This method is invoked automatically during serialization. The
     * serialised set of identified objects is disconnected from the {@linkplain #factory underlying factory}.
     */
    @SuppressWarnings("unchecked")
    protected Object writeReplace() throws ObjectStreamException {
        return new LinkedHashSet<>(this);
    }

    /**
     * The iterator over the entries in the enclosing set. This iterator will creates the {@linkplain IdentifiedObject
     * identified objects} when first needed.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    private final class Iter implements Iterator {
        /** The iterator over the entries from the underlying map. */
        private final Iterator iterator;

        /** The next object to returns, or {@code null} if the iteration is over. */
        private IdentifiedObject element;

        /** Creates a new instance of this iterator. */
        public Iter(final Iterator iterator) {
            this.iterator = iterator;
            toNext();
        }

        /**
         * Moves to the next element.
         *
         * @throws BackingStoreException if the underlying factory failed to creates the coordinate operation.
         */
        @SuppressWarnings("unchecked")
        private void toNext() throws BackingStoreException {
            while (iterator.hasNext()) {
                final Map.Entry entry = (Map.Entry) iterator.next();
                element = (IdentifiedObject) entry.getValue();
                if (element == null) {
                    final String code = (String) entry.getKey();
                    try {
                        element = createObject(code);
                    } catch (FactoryException exception) {
                        if (!isRecoverableFailure(exception)) {
                            throw new BackingStoreException(exception);
                        }
                        log(exception, code);
                        iterator.remove();
                        continue;
                    }
                    entry.setValue(element);
                }
                return; // Element found.
            }
            element = null; // No more element found.
        }

        /** Returns {@code true} if there is more elements. */
        @Override
        public boolean hasNext() {
            return element != null;
        }

        /**
         * Returns the next element.
         *
         * @throws NoSuchElementException if there is no more operations in the set.
         */
        @Override
        public Object next() throws NoSuchElementException {
            final IdentifiedObject next = element;
            if (next == null) {
                throw new NoSuchElementException();
            }
            toNext();
            return next;
        }

        /** Removes the last element from the underlying set. */
        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
