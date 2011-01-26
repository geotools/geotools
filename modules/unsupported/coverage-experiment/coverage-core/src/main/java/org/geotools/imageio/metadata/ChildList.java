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

import java.util.ArrayList;
import java.util.List;

import org.geotools.imageio.metadata.DefinedByConversion.ParameterValue;
import org.geotools.imageio.metadata.RectifiedGrid.AxisName;
import org.geotools.imageio.metadata.RectifiedGrid.OffsetVector;
import org.geotools.imageio.metadata.Band.Category;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A list of child elements, for example {@code <Bands>} or
 * {@code <Axis>}.
 * 
 * @author Martin Desruisseaux
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
abstract class ChildList<T extends MetadataAccessor> extends MetadataAccessor {
    /**
     * The list of children.
     */
    private final List<T> children;

    /**
     * Creates a parser for children. The arguments are given unchanged to the
     * {@linkplain MetadataAccessor#MetadataAccessor super-class constructor}.
     * 
     * @param metadata
     *                The metadata node.
     * @param parentPath
     *                The path to the {@linkplain Node node} of interest, or
     *                {@code null} if {@code metadata} is directly the node of
     *                interest.
     * @param childPath
     *                The path (relative to {@code parentPath}) to the child
     *                {@linkplain Element elements}, or {@code null} if none.
     */
    protected ChildList(final SpatioTemporalMetadata metadata, final String parentPath,
            final String childPath) {
        super(metadata, parentPath, childPath);
        final int count = childCount();
        children = new ArrayList<T>(count != 0 ? count : 4);
    }

    protected ChildList(final MetadataAccessor parentNode,
            final String parentPath, final String childPath) {
        super(parentNode, parentPath, childPath);
        final int count = childCount();
        children = new ArrayList<T>(count != 0 ? count : 4);
    }

    /**
     * Returns the child at the specified index.
     * 
     * @param index
     *                the child index.
     * @throws IndexOutOfBoundsException
     *                 if the index is out of bounds.
     */
    public T getChild(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= childCount()) {
            throw new IndexOutOfBoundsException(index + "out of range");
        }
        while (children.size() <= index) {
            children.add(null);
        }
        T candidate = children.get(index);
        if (candidate == null) {
            candidate = newChild(index);
            children.set(index, candidate);
        }
        return candidate;
    }

    /**
     * Creates a new child, append to the list and returns it.
     */
    public T addChild() {
        final int index = appendChild();
        final T candidate = newChild(index);
        assert index == children.size();
        children.add(candidate);
        return candidate;
    }

    /**
     * Creates a new child at the specified index.
     */
    protected abstract T newChild(int index);

    /**
     * A list of {@linkplain Band bands}.
     */
    static final class Bands extends ChildList<Band> {

        /** Creates a parser for bands. */
        public Bands(final SpatioTemporalMetadata metadata) {
            super(metadata, SpatioTemporalMetadataFormat.MD_BANDS, SpatioTemporalMetadataFormat.MD_BD);
        }

        /** Create a new band. */
        protected Band newChild(final int index) {
            return new Band(this, index);
        }
    }

    /**
     * A list of {@linkplain Category categories}.
     */
    static final class Categories extends ChildList<Band.Category> {
        /** Creates a parser for categories. */
        public Categories(final MetadataAccessor parent) {
            super(parent, SpatioTemporalMetadataFormat.MD_BD_CATEGORIES, SpatioTemporalMetadataFormat.MD_BD_CAT);
        }

        protected Category newChild(final int index) {
            return new Category(this, index);
        }
    }

    /**
     * A list of {@linkplain Axis axis}.
     */
    static final class Axes extends ChildList<Axis> {
        /** Creates a parser for axis. */
        public Axes(final SpatioTemporalMetadata metadata, final String parentCRS) {
            super(metadata, new StringBuilder(parentCRS).append(SEPARATOR)
            		.append(SpatioTemporalMetadataFormat.MD_COORDINATESYSTEM).append(SEPARATOR)
                    .append(SpatioTemporalMetadataFormat.MD_CS_AXES).toString(), 
                    SpatioTemporalMetadataFormat.MD_AXIS);
        }

        /** Create a new axis. */
        protected Axis newChild(final int index) {
            return new Axis(this, index);
        }
    }

    /**
     * A list of {@linkplain AxisName axis}.
     */
    static final class AxesNames extends ChildList<RectifiedGrid.AxisName> {
        /** Creates a parser for axis. */
        public AxesNames(final SpatioTemporalMetadata metadata) {
            super(metadata, new StringBuilder(SpatioTemporalMetadataFormat.MD_RECTIFIEDGRID)
            	.append(SEPARATOR).append(SpatioTemporalMetadataFormat.MD_RG_AXESNAMES).toString(),
                    SpatioTemporalMetadataFormat.MD_RG_AX_AXISNAME);
        }

        protected AxisName newChild(final int index) {
            return new AxisName(this, index);
        }
    }

    /**
     * A list of {@linkplain OffsetVector vectors}.
     */
    static final class OffsetVectors extends
            ChildList<RectifiedGrid.OffsetVector> {
        /** Creates a parser for offset Vectors. */
        public OffsetVectors(final SpatioTemporalMetadata metadata) {
            super(metadata, new StringBuilder(SpatioTemporalMetadataFormat.MD_RECTIFIEDGRID)
            	.append(SEPARATOR).append(SpatioTemporalMetadataFormat.MD_RG_OFFSETVECTORS).toString(),
                    SpatioTemporalMetadataFormat.MD_RG_OV_OFFSETVECTOR);
        }

        protected OffsetVector newChild(int index) {
            return new OffsetVector(this, index);
        }
    }

    /**
     * A list of {@linkplain ParameterValue parameterValue}.
     */
    static final class ParameterValues extends
            ChildList<DefinedByConversion.ParameterValue> {

        /** Creates a parser for operation. */
        public ParameterValues(final MetadataAccessor parent) {
            super(parent, SpatioTemporalMetadataFormat.MD_SCRS_DBC_PARAMETERS, SpatioTemporalMetadataFormat.MD_SCRS_DBC_PARAMETER_VALUE);
        }

        protected ParameterValue newChild(int index) {
            return new ParameterValue(this, index);
        }
    }
}