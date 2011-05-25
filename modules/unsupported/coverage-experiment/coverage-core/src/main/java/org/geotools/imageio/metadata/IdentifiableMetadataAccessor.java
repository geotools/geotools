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
package org.geotools.imageio.metadata;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A {@link MetadataAccessor} subclass storing identification properties using
 * an inner {@link Identification} object.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-core/src/main/java/org/geotools/imageio/metadata/IdentifiableMetadataAccessor.java $
 */
public class IdentifiableMetadataAccessor extends MetadataAccessor {

    /** The inner object storing identification properties */
    private Identification identification;

    protected IdentifiableMetadataAccessor(final MetadataAccessor clone) {
        super(clone);
    }

    /**
     * Creates an accessor for the {@linkplain Element element} at the given
     * path. Paths are separated by the {@code '/'} character. See
     * {@linkplain MetadataAccessor class javadoc} for path examples.
     * 
     * @param metadata
     *                The metadata node.
     * @param parentPath
     *                The path to the {@linkplain Node node} of interest, or
     *                {@code null} if the {@code metadata} root node is directly
     *                the node of interest.
     * @param childPath
     *                The path (relative to {@code parentPath}) to the child
     *                {@linkplain Element elements}, or {@code null} if none.
     */
    protected IdentifiableMetadataAccessor(final SpatioTemporalMetadata metadata,
            final String parentPath, final String childPath) {
        super(metadata, parentPath, childPath);
    }

    protected IdentifiableMetadataAccessor(final SpatioTemporalMetadata metadata,
            final String parentPath, final String childPath, final Identification id) {
        super(metadata, parentPath, childPath);
        setIdentification(id);
    }

    protected IdentifiableMetadataAccessor(final MetadataAccessor parentNode,
            final String parentPath, final String childPath) {
        super(parentNode, parentPath, childPath);
    }

    protected IdentifiableMetadataAccessor(final MetadataAccessor parentNode,
            final String parentPath, final String childPath, final Identification id) {
        super(parentNode, parentPath, childPath);
        setIdentification(id);
    }

    /**
     * Sets the identification for this {@link IdentifiableMetadataAccessor}
     * object only in case the identification has not been set yet.
     * 
     * @param identification
     *                the identifying object
     */
    public synchronized void setIdentification(
            final Identification identification) {
        if (this.identification != null)
            return;
        this.identification = identification;

        // Set all the Attributes of the underlying element
        setAttribute(SpatioTemporalMetadataFormat.MD_COMM_NAME, identification.getName());
        setAttribute(SpatioTemporalMetadataFormat.MD_COMM_ALIAS, identification.getAlias());
        setAttribute(SpatioTemporalMetadataFormat.MD_COMM_REMARKS, identification.getRemarks());
        setAttribute(SpatioTemporalMetadataFormat.MD_COMM_IDENTIFIER, identification.getIdentifier());
    }

    /** Returns the {@linkplain Axis axis} identification * */
    public synchronized Identification getIdentification() {
        return identification;
    }

    /**
     * Set the attribute if available
     * 
     * @param attribute
     *                the attribute name to be set
     * @param value
     *                the value to be set. If {@code null} does nothing.
     */
    private final void setAttribute(final String attribute, final String value) {
        if (value != null)
            setString(attribute, value);
    }
}
