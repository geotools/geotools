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

import java.net.URI;

/**
 * A {@code rectifiedGrid} node in the metadata tree.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-core/src/main/java/org/geotools/imageio/metadata/RectifiedGrid.java $
 */
public class RectifiedGrid extends MetadataAccessor {

    /** The {@code RectifiedGrid/limits/RasterLayout} metadata element */
    private final MetadataAccessor rasterLayoutLimits;

    /** The {@code RectifiedGrid/origin/Point} metadata element */
    private final OriginPoint originPoint;

    final ChildList<AxisName> axesName;

    final ChildList<OffsetVector> offsetVectors;

    protected RectifiedGrid(SpatioTemporalMetadata metadata) {
        super(metadata, null, SpatioTemporalMetadataFormat.MD_RECTIFIEDGRID);
        rasterLayoutLimits = new MetadataAccessor(metadata,
        		new StringBuilder(SpatioTemporalMetadataFormat.MD_RECTIFIEDGRID).append(SEPARATOR)
        		.append(SpatioTemporalMetadataFormat.MD_RG_LIMITS).append(SEPARATOR)
        		.append(SpatioTemporalMetadataFormat.MD_RG_LI_RASTERLAYOUT).toString(), null);

        originPoint = new OriginPoint(metadata);
        axesName = new ChildList.AxesNames(metadata);
        offsetVectors = new ChildList.OffsetVectors(metadata);
    }

    /**
     * A simple class to handle {@code RectifiedGrid/Origin/Point} node of GML.
     * 
     * @author Daniele Romagnoli, GeoSolutions
     */
    static final class OriginPoint extends MetadataAccessor {

        /** The {@code RectifiedGrid/Origin/Point/coordinates} metadata element */
        private final MetadataAccessor coordinates;

        protected OriginPoint(SpatioTemporalMetadata metadata) {
            super(metadata, new StringBuilder(SpatioTemporalMetadataFormat.MD_RECTIFIEDGRID).append(SEPARATOR)
                    .append(SpatioTemporalMetadataFormat.MD_RG_ORIGIN).append(SEPARATOR)
                    .append(SpatioTemporalMetadataFormat.MD_RG_OR_POINT).toString(), null);

            coordinates = new MetadataAccessor(metadata,
            		new StringBuilder(SpatioTemporalMetadataFormat.MD_RECTIFIEDGRID).append(SEPARATOR)
                            .append(SpatioTemporalMetadataFormat.MD_RG_ORIGIN).append(SEPARATOR)
                            .append(SpatioTemporalMetadataFormat.MD_RG_OR_POINT).append(SEPARATOR)
                            .append(SpatioTemporalMetadataFormat.MD_RG_OR_PT_COORD).toString(), null);
        }

        /** Set the coordinates for this {@code Origin/Point} element */
        public void setCoordinates(final double[] coords) {
            coordinates.setDoubles(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, coords);
        }
        
        /** get the coordinates for this {@code Origin/Point} element */
        public double[] getCoordinates(){
            return coordinates.getDoubles(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, false);
        }
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // Limits node methods
    //
    // ////////////////////////////////////////////////////////////////////////
    /**
     * Set the <B>low</B> field of the limits->GridEnvelope node.
     */
    public void setLow(final int[] lowerLimit) {
        rasterLayoutLimits.setIntegers(SpatioTemporalMetadataFormat.MD_RG_LI_RL_LOW, lowerLimit);
    }

    /**
     * Set the <B>high</B> field of the limits->GridEnvelope node.
     */
    public void setHigh(final int[] upperLimit) {
        rasterLayoutLimits.setIntegers(SpatioTemporalMetadataFormat.MD_RG_LI_RL_HIGH, upperLimit);
    }

    /**
     * Return the lower limit of the grid envelope of this 2D slice, as an
     * {@code int} array.
     */
    public int[] getLow() {
        return rasterLayoutLimits.getIntegers(SpatioTemporalMetadataFormat.MD_RG_LI_RL_LOW, false);
    }

    /**
     * Return the upper limit of the grid envelope of this 2D slice, as an
     * {@code int} array.
     */
    public int[] getHigh() {
        return rasterLayoutLimits.getIntegers(SpatioTemporalMetadataFormat.MD_RG_LI_RL_HIGH, false);
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // Origin node methods
    //
    // ////////////////////////////////////////////////////////////////////////
    /**
     * Set the coordinates for the {@code Origin/Point} element
     * 
     * @param coordinates
     *                the coordinates of this point.
     */
    public void setCoordinates(final double[] coordinates) {
        originPoint.setCoordinates(coordinates);
    }
    
    public double[] getCoordinates() {
        return originPoint.getCoordinates();
    }
    

    public void setDimension(final int dimensions) {
        setInteger(SpatioTemporalMetadataFormat.MD_RG_DIMENSION, dimensions);
    }
    
    public void setPointId(String id) {
        originPoint.setString(SpatioTemporalMetadataFormat.MD_RG_OR_PT_ID, id);
    }

    public void setPointSrs(URI srsUri) {
        originPoint.setString(SpatioTemporalMetadataFormat.MD_RG_OR_PT_SRSURI, srsUri.toString());
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // AxisName methods
    //
    // ////////////////////////////////////////////////////////////////////////
    static final class AxisName extends MetadataAccessor {
        protected AxisName(final RectifiedGrid metadata, final int index) {
            super(metadata);
            selectChild(index);
        }

        /**
         * Creates a parser for an axis.
         * 
         * @param parent
         *                The set of all axis.
         * @param index
         *                The axis index for this instance.
         */
        AxisName(final ChildList<AxisName> parent, final int index) {
            super(parent);
            selectChild(index);
        }

        /**
         * Returns the name for this axis, or {@code null} if none.
         */
        public String getName() {
            return getString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE);
        }

        /**
         * Sets the name for this axis.
         * 
         * @param name
         *                The axis name, or {@code null} if none.
         */
        public void setName(final String name) {
            setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, name);
        }
    }

    /**
     * Returns the number of dimensions.
     */
    public int getDimension() {
        return axesName.childCount();
    }

    /**
     * Returns the axisName at the specified index.
     * 
     * @param index
     *                the axis index, ranging from 0 inclusive to
     *                {@link #getDimension} exclusive.
     * @throws IndexOutOfBoundsException
     *                 if the index is out of bounds.
     */
    public AxisName getAxisName(final int index) throws IndexOutOfBoundsException {
        return axesName.getChild(index);
    }

    /**
     * Adds an {@code AxisName} to the {@code RectifiedGrid/AxesNames} node.
     * 
     * @param name
     *                The axis name, or {@code null} if unknown.
     */
    public AxisName addAxisName(final String name) {
        final AxisName axis = axesName.addChild();
        axis.setName(name);
        return axis;
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // OffsetVector methods
    //
    // ////////////////////////////////////////////////////////////////////////
    static final class OffsetVector extends MetadataAccessor {
        protected OffsetVector(final RectifiedGrid metadata, final int index) {
            super(metadata);
            selectChild(index);
        }

        /**
         * Creates a parser for an offsetVector.
         * 
         * @param parent
         *                The set of all vectors.
         * @param index
         *                The offsetVector index for this instance.
         */
        OffsetVector(final ChildList<OffsetVector> parent, final int index) {
            super(parent);
            selectChild(index);
        }

        /**
         * Returns the value for this offset vector, or {@code null} if none.
         */
        public String getValue() {
            return getString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE);
        }
        
        public double[] getValues(){
            return getDoubles(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, false);
        }

        /**
         * Sets the value for this offset vector.
         * 
         * @param value
         *                The offset vector value, or {@code null} if none.
         */
        public void setValue(final String value) {
            setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, value);
        }
    }

    /**
     * Returns the offsetVector values as a String at the specified index.
     * 
     * @param index
     *                the vector index, ranging from 0 inclusive to
     *                {@link #getDimension} exclusive.
     * @throws IndexOutOfBoundsException
     *                 if the index is out of bounds.
     */
    public String getOffsetVectorValue(final int index) throws IndexOutOfBoundsException {
        return offsetVectors.getChild(index).getValue();
    }
    
    /**
     * Returns the offsetVector values at the specified index.
     * 
     * @param index
     *                the vector index, ranging from 0 inclusive to
     *                {@link #getDimension} exclusive.
     * @throws IndexOutOfBoundsException
     *                 if the index is out of bounds.
     */
    public double[] getOffsetVectorValues(final int index) throws IndexOutOfBoundsException {
        return offsetVectors.getChild(index).getValues();
    }

    /**
     * Adds an {@code OffsetVector} to the {@code RectifiedGrid/OffsetVectors}
     * node.
     * 
     * @param value
     *                The offsetVector value, or {@code null} if unknown.
     */
    public OffsetVector addOffsetVector(final double values[]) {
        final OffsetVector offVector = offsetVectors.addChild();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<values.length; i++) {
            sb.append(Double.toString(values[i])).append(" ");
        }
        offVector.setValue(sb.toString());
        return offVector;
    }
}
