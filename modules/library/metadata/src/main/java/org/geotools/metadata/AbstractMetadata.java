/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata;

import java.util.Map;
import java.util.logging.Logger;
import javax.swing.tree.TreeModel;
import org.geotools.util.logging.Logging;


/**
 * Base class for metadata implementations. Subclasses must implement the interfaces
 * of some {@linkplain MetadataStandard metadata standard}. This class uses
 * {@linkplain java.lang.reflect Java reflection} in order to provide default
 * implementation of {@linkplain #AbstractMetadata(Object) copy constructor},
 * {@link #equals} and {@link #hashCode} methods.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 */
public abstract class AbstractMetadata {
    /**
     * The logger for metadata implementation.
     */
    protected static final Logger LOGGER = Logging.getLogger("org.geotools.metadata");

    /**
     * Hash code value, or 0 if not yet computed. This field is reset to 0 by
     * {@link #invalidate} in order to account for a change in metadata content.
     */
    private transient int hashCode;

    /**
     * A view of this metadata as a map. Will be created only when first needed.
     */
    private transient Map<String,Object> asMap;

    /**
     * Creates an initially empty metadata.
     */
    protected AbstractMetadata() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     * The {@code source} metadata must implements the same metadata interface (defined by
     * the {@linkplain #getStandard standard}) than this class, but don't need to be the same
     * implementation class. The copy is performed using Java reflections.
     *
     * @param  source The metadata to copy values from.
     * @throws ClassCastException if the specified metadata don't implements the expected
     *         metadata interface.
     * @throws UnmodifiableMetadataException if this class don't define {@code set} methods
     *         corresponding to the {@code get} methods found in the implemented interface,
     *         or if this instance is not modifiable for some other reason.
     */
    protected AbstractMetadata(final Object source)
            throws ClassCastException, UnmodifiableMetadataException
    {
        getStandard().shallowCopy(source, this, true);
    }

    /**
     * Returns the metadata standard implemented by subclasses.
     *
     * @return The metadata standard implemented.
     */
    public abstract MetadataStandard getStandard();

    /**
     * Returns the metadata interface implemented by this class. It should be one of the
     * interfaces defined in the {@linkplain #getStandard metadata standard} implemented
     * by this class.
     *
     * @return The standard interface implemented by this implementation class.
     */
    public Class<?> getInterface() {
        // No need to sychronize, since this method do not depends on property values.
        return getStandard().getInterface(getClass());
    }

    /**
     * Returns {@code true} if this metadata is modifiable. The default implementation
     * uses heuristic rules which return {@code false} if and only if:
     * <p>
     * <ul>
     *   <li>this class do not contains any {@code set*(...)} method</li>
     *   <li>All {@code get*()} methods return a presumed immutable object.
     *       The maining of "<cite>presumed immutable</cite>" may vary in
     *       different Geotools versions.</li>
     * </ul>
     * <p>
     * Otherwise, this method conservatively returns {@code true}. Subclasses
     * should override this method if they can provide a more rigorous analysis.
     */
    boolean isModifiable() {
        return getStandard().isModifiable(getClass());
    }

    /**
     * Invoked when the metadata changed. Some cached informations will need
     * to be recomputed.
     */
    void invalidate() {
        assert Thread.holdsLock(this);
        hashCode = 0; // Will recompute when needed.
    }

    /**
     * Returns a view of this metadata object as a {@linkplain Map map}. The map is backed by this
     * metadata object using Java reflection, so changes in the underlying metadata object are
     * immediately reflected in the map. The keys are the property names as determined by the list
     * of {@code get*()} methods declared in the {@linkplain #getInterface metadata interface}.
     * <p>
     * The map supports the {@link Map#put put} operations if the underlying
     * metadata object contains {@link #set*(...)} methods.
     *
     * @return A view of this metadata object as a map.
     */
    public synchronized Map<String,Object> asMap() {
        if (asMap == null) {
            asMap = getStandard().asMap(this);
        }
        return asMap;
    }

    /**
     * Returns a view of this metadata as a tree. Note that while {@link TreeModel} is
     * defined in the {@link javax.swing.tree} package, it can be seen as a data structure
     * independent of Swing. It will not force class loading of Swing framework.
     * <p>
     * In current implementation, the tree is not live (i.e. changes in metadata are not
     * reflected in the tree). However it may be improved in a future Geotools implementation.
     *
     * @return A view of this metadata object as a tree.
     */
    public synchronized TreeModel asTree() {
        return getStandard().asTree(this);
    }

    /**
     * Compares this metadata with the specified object for equality. The default
     * implementation uses Java reflection. Subclasses may override this method
     * for better performances.
     * <p>
     * This method performs a <cite>deep</cite> comparaison (i.e. if this metadata contains
     * other metadata, the comparaison will walk through the other metadata content as well)
     * providing that every childs implement the {@link Object#equals} method as well. This
     * is the case by default if every childs are subclasses of {@code AbstractMetadata}.
     *
     * @param  object The object to compare with this metadata.
     * @return {@code true} if the given object is equals to this metadata.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object==null || !object.getClass().equals(getClass())) {
            return false;
        }
        /*
         * Opportunist usage of hash code if they are already computed. If they are not, we will
         * not compute them - they are not sure to be faster than checking directly for equality,
         * and hash code could be invalidated later anyway if the object change. Note that we
         * don't need to synchronize since reading int fields are garanteed to be atomic in Java.
         */
        final int c0 = hashCode;
        if (c0 != 0) {
            final int c1 = ((AbstractMetadata) object).hashCode;
            if (c1 != 0 && c0 != c1) {
                return false;
            }
        }
        final MetadataStandard standard = getStandard();
        /*
         * DEADLOCK WARNING: A deadlock may occur if the same pair of objects is being compared
         * in an other thread (see http://jira.codehaus.org/browse/GEOT-1777). Ideally we would
         * synchronize on 'this' and 'object' atomically (RFE #4210659). Since we can't in Java
         * a workaround is to always get the locks in the same order. Unfortunatly we have no
         * garantee that the caller didn't looked the object himself. For now the safest approach
         * is to not synchronize at all.
         */
        return standard.shallowEquals(this, object, false);
    }

    /**
     * Computes a hash code value for this metadata using Java reflection. The hash code
     * is defined as the sum of hash code values of all non-null properties. This is the
     * same contract than {@link java.util.Set#hashCode} and ensure that the hash code
     * value is insensitive to the ordering of properties.
     */
    @Override
    public synchronized int hashCode() {
        int code = hashCode;
        if (code == 0) {
            code = getStandard().hashCode(this);
            if (!isModifiable()) {
                // In current implementation, we do not store the hash code if this metadata is
                // modifiable because we can not track change in dependencies (e.g. a change in
                // a metadata contained in this metadata).
                hashCode = code;
            }
        }
        return code;
    }

    /**
     * Returns a string representation of this metadata.
     */
    @Override
    public synchronized String toString() {
        return getStandard().toString(this);
    }
}
