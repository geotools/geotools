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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.geotools.metadata.i18n.ErrorKeys;

/**
 * Enumeration of some metadata standards. A standard is defined by a set of Java interfaces in a specific package or
 * subpackages. For example the {@linkplain #ISO_19115 ISO 19115} standard is defined by <A
 * HREF="http://geoapi.sourceforge.net">GeoAPI</A> interfaces in the {@link org.geotools.api.metadata} package and
 * subpackages.
 *
 * <p>This class provides some methods operating on metadata instances through {@linkplain java.lang.reflect Java
 * reflection}. The following rules are assumed:
 *
 * <p>
 *
 * <ul>
 *   <li>Properties (or metadata attributes) are defined by the set of {@code get*()} (arbitrary return type) or
 *       {@code is*()} (boolean return type) methods found in the <strong>interface</strong>. Getters declared in the
 *       implementation only are ignored.
 *   <li>A property is <cite>writable</cite> if a {@code set*(...)} method is defined in the implementation class for
 *       the corresponding {@code get*()} method. The setter don't need to be defined in the interface.
 * </ul>
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 */
public final class MetadataStandard {
    /**
     * An instance working on ISO 19111 standard as defined by <A HREF="http://geoapi.sourceforge.net">GeoAPI</A>
     * interfaces in the {@link org.geotools.api.referencing} package and subpackages.
     *
     * @since 2.5
     */
    public static final MetadataStandard ISO_19111 = new MetadataStandard("org.geotools.api.referencing.");

    /**
     * An instance working on ISO 19115 standard as defined by <A HREF="http://geoapi.sourceforge.net">GeoAPI</A>
     * interfaces in the {@link org.geotools.api.metadata} package and subpackages.
     */
    public static final MetadataStandard ISO_19115 = new MetadataStandard("org.geotools.api.metadata.");

    /**
     * An instance working on ISO 19119 standard as defined by <A HREF="http://geoapi.sourceforge.net">GeoAPI</A>
     * interfaces in the {@link org.geotools.api.service} package and subpackages.
     *
     * @since 2.5
     */
    public static final MetadataStandard ISO_19119 = new MetadataStandard("org.geotools.api.service.");

    /** The root packages for metadata interfaces. Must ends with {@code "."}. */
    private final String interfacePackage;

    /** Accessors for the specified implementations. */
    private final Map<Class<?>, PropertyAccessor> accessors = new HashMap<>();

    /**
     * Shared pool of {@link PropertyTree} instances, once for each thread (in order to avoid the need for thread
     * synchronization).
     */
    @SuppressWarnings("ThreadLocalUsage") // Cannot be static as it depends on specific instance
    private final ThreadLocal<PropertyTree> treeBuilders = new ThreadLocal<>() {
        @Override
        protected PropertyTree initialValue() {
            return new PropertyTree(MetadataStandard.this);
        }
    };

    /**
     * Creates a new instance working on implementation of interfaces defined in the specified package. For the ISO
     * 19115 standard reflected by GeoAPI interfaces, it should be the {@link org.geotools.api.metadata} package.
     *
     * @param interfacePackage The root package for metadata interfaces.
     */
    public MetadataStandard(String interfacePackage) {
        if (!interfacePackage.endsWith(".")) {
            interfacePackage += '.';
        }
        this.interfacePackage = interfacePackage;
    }

    /**
     * Returns the accessor for the specified implementation.
     *
     * @throws ClassCastException if the specified implementation class do not implements a metadata interface of the
     *     expected package.
     */
    private PropertyAccessor getAccessor(final Class<?> implementation) throws ClassCastException {
        final PropertyAccessor accessor = getAccessorOptional(implementation);
        if (accessor == null) {
            throw new ClassCastException(MessageFormat.format(ErrorKeys.UNKNOW_TYPE_$1, implementation.getName()));
        }
        return accessor;
    }

    /** Returns the accessor for the specified implementation, or {@code null} if none. */
    final PropertyAccessor getAccessorOptional(final Class<?> implementation) {
        synchronized (accessors) {
            PropertyAccessor accessor = accessors.get(implementation);
            if (accessor == null) {
                Class<?> type = getType(implementation);
                if (type != null) {
                    accessor = new PropertyAccessor(implementation, type);
                    accessors.put(implementation, accessor);
                }
            }
            return accessor;
        }
    }

    /**
     * Returns the metadata interface implemented by the specified implementation. Only one metadata interface can be
     * implemented.
     *
     * @param implementation The metadata implementation to wraps.
     * @return The single interface, or {@code null} if none where found.
     */
    private Class<?> getType(final Class<?> implementation) {
        return PropertyAccessor.getType(implementation, interfacePackage);
    }

    /**
     * Returns the metadata interface implemented by the specified implementation class.
     *
     * @param implementation The implementation class.
     * @return The interface implemented by the given implementation class.
     * @throws ClassCastException if the specified implementation class do not implements a metadata interface of the
     *     expected package.
     * @see AbstractMap#getInterface
     */
    public Class<?> getInterface(final Class<?> implementation) throws ClassCastException {
        return getAccessor(implementation).type;
    }

    /**
     * Returns a view of the specified metadata object as a {@linkplain Map map}. The map is backed by the metadata
     * object using Java reflection, so changes in the underlying metadata object are immediately reflected in the map.
     * The keys are the property names as determined by the list of {@code get*()} methods declared in the
     * {@linkplain #getInterface metadata interface}.
     *
     * <p>The map supports the {@link Map#put put} operations if the underlying metadata object contains
     * {@link #set*(...)} methods.
     *
     * @param metadata The metadata object to view as a map.
     * @return A map view over the metadata object.
     * @throws ClassCastException if at the metadata object don't implements a metadata interface of the expected
     *     package.
     * @see AbstractMap#asMap
     */
    public Map<String, Object> asMap(final Object metadata) throws ClassCastException {
        return new PropertyMap(metadata, getAccessor(metadata.getClass()));
    }

    /**
     * Returns a view of the specified metadata as a tree. Note that while {@link TreeModel} is defined in the
     * {@link javax.swing.tree} package, it can be seen as a data structure independent of Swing. It will not force
     * class loading of Swing framework.
     *
     * <p>In current implementation, the tree is not live (i.e. changes in metadata are not reflected in the tree).
     * However it may be improved in a future Geotools implementation.
     *
     * @param metadata The metadata object to formats as a string.
     * @return A tree representation of the specified metadata.
     * @throws ClassCastException if at the metadata object don't implements a metadata interface of the expected
     *     package.
     * @see AbstractMap#asTree
     */
    public TreeModel asTree(final Object metadata) throws ClassCastException {
        final PropertyTree builder = treeBuilders.get();
        return new DefaultTreeModel(builder.asTree(metadata), true);
    }

    /**
     * Returns {@code true} if this metadata is modifiable. This method is not public because it uses heuristic rules.
     * In case of doubt, this method conservatively returns {@code true}.
     *
     * @throws ClassCastException if the specified implementation class do not implements a metadata interface of the
     *     expected package.
     * @see AbstractMap#isModifiable
     */
    final boolean isModifiable(final Class implementation) throws ClassCastException {
        return getAccessor(implementation).isModifiable();
    }

    /**
     * Replaces every properties in the specified metadata by their
     * {@linkplain ModifiableMetadata#unmodifiable unmodifiable variant.
     *
     * @throws ClassCastException if the specified implementation class do
     *         not implements a metadata interface of the expected package.
     *
     * @see ModifiableMetadata#freeze()
     */
    final void freeze(final Object metadata) throws ClassCastException {
        getAccessor(metadata.getClass()).freeze(metadata);
    }

    /**
     * Copies all metadata from source to target. The source must implements the same metadata interface than the
     * target.
     *
     * @param source The metadata to copy.
     * @param target The target metadata.
     * @param skipNulls If {@code true}, only non-null values will be copied.
     * @throws ClassCastException if the source or target object don't implements a metadata interface of the expected
     *     package.
     * @throws UnmodifiableMetadataException if the target metadata is unmodifiable, or if at least one setter method
     *     was required but not found.
     * @see AbstractMap#AbstractMap(Object)
     */
    public void shallowCopy(final Object source, final Object target, final boolean skipNulls)
            throws ClassCastException, UnmodifiableMetadataException {
        ensureNonNull("target", target);
        final PropertyAccessor accessor = getAccessor(target.getClass());
        if (!accessor.type.isInstance(source)) {
            ensureNonNull("source", source);
            throw new ClassCastException(
                    MessageFormat.format(ErrorKeys.ILLEGAL_CLASS_$2, source.getClass(), accessor.type));
        }
        if (!accessor.shallowCopy(source, target, skipNulls)) {
            throw new UnmodifiableMetadataException(ErrorKeys.UNMODIFIABLE_METADATA);
        }
    }

    /**
     * Compares the two specified metadata objects. The comparaison is <cite>shallow</cite>, i.e. all metadata
     * attributes are compared using the {@link Object#equals} method without recursive call to this
     * {@code shallowEquals(...)} method for child metadata.
     *
     * <p>This method can optionaly excludes null values from the comparaison. In metadata, null value often means
     * "don't know", so in some occasion we want to consider two metadata as different only if an attribute value is
     * know for sure to be different.
     *
     * <p>The first arguments must be an implementation of a metadata interface, otherwise an exception will be thrown.
     * The two argument do not need to be the same implementation however.
     *
     * @param metadata1 The first metadata object to compare.
     * @param metadata2 The second metadata object to compare.
     * @param skipNulls If {@code true}, only non-null values will be compared.
     * @return {@code true} if the given metadata objects are equals.
     * @throws ClassCastException if at least one metadata object don't implements a metadata interface of the expected
     *     package.
     * @see AbstractMetadata#equals
     */
    public boolean shallowEquals(final Object metadata1, final Object metadata2, final boolean skipNulls)
            throws ClassCastException {
        if (metadata1 == metadata2) {
            return true;
        }
        if (metadata1 == null || metadata2 == null) {
            return false;
        }
        final PropertyAccessor accessor = getAccessor(metadata1.getClass());
        if (!accessor.type.equals(getType(metadata2.getClass()))) {
            return false;
        }
        return accessor.shallowEquals(metadata1, metadata2, skipNulls);
    }

    /**
     * Computes a hash code for the specified metadata. The hash code is defined as the sum of hash code values of all
     * non-null properties. This is the same contract than {@link java.util.Set#hashCode} and ensure that the hash code
     * value is insensitive to the ordering of properties.
     *
     * @param metadata The metadata object to compute hash code.
     * @return A hash code value for the specified metadata.
     * @throws ClassCastException if at the metadata object don't implements a metadata interface of the expected
     *     package.
     * @see AbstractMap#hashCode
     */
    public int hashCode(final Object metadata) throws ClassCastException {
        return getAccessor(metadata.getClass()).hashCode(metadata);
    }

    /**
     * Returns a string representation of the specified metadata.
     *
     * @param metadata The metadata object to formats as a string.
     * @return A string representation of the specified metadata.
     * @throws ClassCastException if at the metadata object don't implements a metadata interface of the expected
     *     package.
     * @see AbstractMap#toString
     */
    public String toString(final Object metadata) throws ClassCastException {
        final PropertyTree builder = treeBuilders.get();
        return PropertyTree.toString(builder.asTree(metadata));
    }

    /** Ensures that the specified argument is non-null. */
    private static void ensureNonNull(final String name, final Object value) {
        if (value == null) {
            throw new NullPointerException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }
}
