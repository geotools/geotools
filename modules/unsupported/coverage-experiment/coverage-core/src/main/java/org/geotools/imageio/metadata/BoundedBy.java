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

import java.text.ParseException;

import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.opengis.temporal.Instant;

/**
 * A {@code boundedBy} node in the metadata tree.
 * <p>
 * The boundedBy element reports the values of the spatial horizontal bounding
 * box and, if present, the vertical and time positions or intervals.
 * </p>
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-core/src/main/java/org/geotools/imageio/metadata/BoundedBy.java $
 */
public class BoundedBy extends MetadataAccessor {

    /** the {@boundedBy/BoundingBox} metadata element */
    private final MetadataAccessor boundingBox;

    /** the {@boundedBy/TemporalExtent} metadata element */
    private TemporalExtent temporalExtent;

    /** the {@boundedBy/VerticalExtent} metadata element */
    private VerticalExtent verticalExtent;

    /**
     * Implicit Constructor.
     * 
     * @param metadata
     */
    protected BoundedBy(final SpatioTemporalMetadata metadata) {
        super(metadata, SpatioTemporalMetadataFormat.MD_BOUNDEDBY, null);
        boundingBox = new MetadataAccessor(this, SpatioTemporalMetadataFormat.MD_BB_BOUNDINGBOX, null);
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // Envelope settings
    //
    // ////////////////////////////////////////////////////////////////////////
    /**
     * Return the lower corner coordinates of the envelope of this 2D slice as a
     * double array.
     */
    public double[] getLowerCorner() {
        return boundingBox.getDoubles(SpatioTemporalMetadataFormat.MD_BB_LC, false);
    }

    /**
     * Return the upper corner coordinates of the envelope of this 2D slice as a
     * double array.
     */
    public double[] getUpperCorner() {
        return boundingBox.getDoubles(SpatioTemporalMetadataFormat.MD_BB_UC, false);
    }

    /**
     * Set the lower corner coordinates of the envelope of this 2D slice.
     * 
     * @param lowerCorner
     *                the double array containing lower corner coordinates.
     */
    public void setLowerCorner(final double[] lowerCorner) {
        boundingBox.setDoubles(SpatioTemporalMetadataFormat.MD_BB_LC, lowerCorner);
    }

    /**
     * Set the upper corner coordinates of the envelope of this 2D slice.
     * 
     * @param upperCorner
     *                the double array containing upper corner coordinates.
     */
    public void setUpperCorner(final double[] upperCorner) {
        boundingBox.setDoubles(SpatioTemporalMetadataFormat.MD_BB_UC, upperCorner);
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // Temporal extent settings
    //
    // ////////////////////////////////////////////////////////////////////////
    /**
     * A simple {@link MetadataAccessor} to access temporal extent information.
     * 
     * @author Daniele Romagnoli, GeoSolutions
     */
    public final class TemporalExtent extends ChildChoice<MetadataAccessor> {

        /** Child index related to a Time position element */
        public static final int TIME_POSITION = 0;

        /** Child index related to a Time Period element */
        public static final int TIME_PERIOD = 1;

        /**
         * Base Constructor for a {@code TemporalExtent} element.
         * 
         * @param metadata
         *                the {@link SpatioTemporalMetadata} instance.
         */
        public TemporalExtent(final MetadataAccessor parent) {
            super(parent, SpatioTemporalMetadataFormat.MD_BB_TEMPORALEXTENT,
                    new String[] { SpatioTemporalMetadataFormat.MD_BB_TE_TIMEPOSITION,
                            SpatioTemporalMetadataFormat.MD_BB_TE_TIMEPERIOD });
        }

        protected MetadataAccessor newChild(String choice) {
            return new MetadataAccessor(this, choice, null);
        }
        
        public Object getValue() {
            try {
                if (this.getChild() == null)
                    return null;
            } catch (IllegalStateException e) {
                return null;
            }

            if (this.getSelectedChoice() == TIME_POSITION) {
                String instant = this.getChild().getString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE);
                try {
                    return new DefaultInstant(new DefaultPosition(new SimpleInternationalString(instant)));
                } catch (ParseException e) {
                    return null;
                }
            } else if (this.getSelectedChoice() == TIME_PERIOD) {
                String begin = this.getChild().getString(SpatioTemporalMetadataFormat.MD_BB_TE_TP_BEGIN);
                String end   = this.getChild().getString(SpatioTemporalMetadataFormat.MD_BB_TE_TP_END);
                    
                Instant beginTime;
                try {
                    beginTime = new DefaultInstant(new DefaultPosition(new SimpleInternationalString(begin)));
                    Instant endTime   = new DefaultInstant(new DefaultPosition(new SimpleInternationalString(end)));
                    return new DefaultPeriod(beginTime, endTime);
                } catch (ParseException e) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * @return the {@code boundedBy/TemporalExtent} metadata element.
     */
    public synchronized TemporalExtent getTemporalExtent() {
        if (temporalExtent == null) {
            temporalExtent = new TemporalExtent(this);
        }
        return temporalExtent;
    }

    /**
     * Set a single value for the temporal extent. The value will be set as a
     * {@code TimePosition} element.
     * 
     * @param instant
     *                an ISO8601 {@code String} representing a single time
     *                position.
     */
    public void setTemporalExtent(final String instant) {
        getTemporalExtent();
        temporalExtent.addChild(TemporalExtent.TIME_POSITION).setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, instant);
        temporalExtent.setString(SpatioTemporalMetadataFormat.MD_BB_TE_TYPE, SpatioTemporalMetadataFormat.MD_BB_TE_TIMEPOSITION);
    }

    /**
     * Set a temporal period for the temporal extent. The value will be set as a
     * {@code TimePeriod} element.
     * 
     * @param timePeriods
     *                an ISO8601 formatted {@code String}'s array containing
     *                the beginning instant and the ending instant of the
     *                period.
     */
    public void setTemporalExtent(final String timePeriods[]) {
        getTemporalExtent();
        temporalExtent.addChild(TemporalExtent.TIME_PERIOD).setString(SpatioTemporalMetadataFormat.MD_BB_TE_TP_BEGIN, timePeriods[0]);
        temporalExtent.getChild().setString(SpatioTemporalMetadataFormat.MD_BB_TE_TP_END, timePeriods[1]);
        temporalExtent.setString(SpatioTemporalMetadataFormat.MD_BB_TE_TYPE, SpatioTemporalMetadataFormat.MD_BB_TE_TIMEPERIOD);
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // Vertical extent settings
    //
    // ////////////////////////////////////////////////////////////////////////
    /**
     * A simple {@link MetadataAccessor} to access vertical extent information.
     * 
     * @author Daniele Romagnoli, GeoSolutions
     */
    public final class VerticalExtent extends ChildChoice<MetadataAccessor> {

        /** Child index related to a single value element */
        public static final int SINGLE_VALUE = 0;

        /** Child index related to a Vertical Range element */
        public static final int VERTICAL_RANGE = 1;

        /**
         * Base Constructor for a {@code VerticalExtent} element.
         * 
         * @param metadata
         *                the {@link SpatioTemporalMetadata} instance.
         */
        public VerticalExtent(final MetadataAccessor parent) {
            super(parent, SpatioTemporalMetadataFormat.MD_BB_VERTICALEXTENT,
                    new String[] { SpatioTemporalMetadataFormat.MD_BB_VE_SINGLEVALUE,
                            SpatioTemporalMetadataFormat.MD_BB_VE_VERTICALRANGE });
        }

        @Override
        protected MetadataAccessor newChild(String choice) {
            return new MetadataAccessor(this, choice, null);
        }
        
        public Object getValue() {
            try {
                if (this.getChild() == null)
                    return null;
            } catch (IllegalStateException e) {
                return null;
            }

            if (this.getSelectedChoice() == SINGLE_VALUE) {
                return this.getChild().getDouble(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE);
            } else if (this.getSelectedChoice() == VERTICAL_RANGE) {
                final MetadataAccessor child = this.getChild();
                Double min = child.getDouble(SpatioTemporalMetadataFormat.MD_BB_VE_VR_MIN);
                Double max = child.getDouble(SpatioTemporalMetadataFormat.MD_BB_VE_VR_MAX);
                    
                return NumberRange.create(min, max);
            } else {
                return null;
            }
        }
    }

    /**
     * @return the {@code boundedBy/VerticalExtent} metadata element.
     */
    public synchronized VerticalExtent getVerticalExtent() {
        if (verticalExtent == null) {
            verticalExtent = new VerticalExtent(this);
        }
        return verticalExtent;
    }

    /**
     * Set a single value for the vertical extent. The value will be set as a
     * symbolic vertical level.
     * 
     * @param value
     *                the value identifying a not-numeric vertical extent.
     */
    public void setVerticalExtent(final String value) {
        getVerticalExtent();
        verticalExtent.addChild(VerticalExtent.SINGLE_VALUE).setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, value);
        verticalExtent.setString(SpatioTemporalMetadataFormat.MD_BB_VE_TYPE, SpatioTemporalMetadataFormat.MD_BB_VE_SINGLEVALUE);
    }

    /**
     * Set a single value for the vertical extent. The value will be set as a
     * numeric vertical level.
     * 
     * @param value
     *                the value of the vertical extent.
     */
    public void setVerticalExtent(final double value) {
        getVerticalExtent();
        verticalExtent.addChild(VerticalExtent.SINGLE_VALUE).setDouble(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, value);
        verticalExtent.setString(SpatioTemporalMetadataFormat.MD_BB_VE_TYPE, SpatioTemporalMetadataFormat.MD_BB_VE_SINGLEVALUE);
    }

    /**
     * Set a vertical range for the vertical extent.
     * 
     * @param verticalRange
     *                a {@code NumberRange} containing min and max values of the
     *                vertical range.
     */
    public void setVerticalExtent(final NumberRange range) {
        getVerticalExtent();
        MetadataAccessor child = verticalExtent.addChild(VerticalExtent.VERTICAL_RANGE);
        child.setDouble(SpatioTemporalMetadataFormat.MD_BB_VE_VR_MIN, range.getMinimum());
        child.setDouble(SpatioTemporalMetadataFormat.MD_BB_VE_VR_MAX, range.getMaximum());
        verticalExtent.setString(SpatioTemporalMetadataFormat.MD_BB_VE_TYPE, SpatioTemporalMetadataFormat.MD_BB_VE_VERTICALRANGE);
    }
    
}
