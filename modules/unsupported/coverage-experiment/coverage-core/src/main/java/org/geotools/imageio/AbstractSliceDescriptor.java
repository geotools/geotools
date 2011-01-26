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
package org.geotools.imageio;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.imageio.metadata.SpatioTemporalMetadata;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.temporal.TemporalGeometricPrimitive;

/**
 * Class holding main properties of each 2D Raster.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 */
public abstract class AbstractSliceDescriptor implements SliceDescriptor {

    /**
     * The factories to use for constructing ellipsoids, projections, coordinate
     * reference systems...
     */
    private final ReferencingFactoryContainer factoryContainer;

    /**
     * A coordinate reference system identifying the CRS needed to refer this
     * {@code SliceDescriptor}. Note that in the more complex case, this field
     * may be an instance of {@link CompoundCRS}, containing several single
     * CRSs. As an instance, a compoundCRS may contain a {@link TemporalCRS}, a
     * {@link VerticalCRS} as well as a 2D{@link GeographicCRS}/{@link ProjectedCRS}.
     * 
     * For convention, when defining a {@link CompoundCRS} using
     * {@link DefaultCompoundCRS} we will set a {@link TemporalCRS} as a first
     * element (if available), a {@link VerticalCRS} as a second element (if
     * available) and finally a {@link GeographicCRS}/{@link ProjectedCRS}.
     * (The last one should always be available for the minimal 2D case).
     * 
     * Note that {@link VerticalCRS} cannot be used for vertical extents
     * referring to ellipsoidal height which is component of a 3D
     * {@link GeographicCRS}. In such a case, the
     * {@link #coordinateReferenceSystem} will be a {@link CompoundCRS} composed
     * of a {@link GeographicCRS} and a {@link TemporalCRS} if available, or a
     * simple {@link GeographicCRS}.
     */
    private CoordinateReferenceSystem coordinateReferenceSystem;

    /** The imageIndex referring this specific Slice descriptor */
    private int imageIndex;

    public AbstractSliceDescriptor(SpatioTemporalMetadata metadata,
            ReferencingFactoryContainer factories) {
        this.factoryContainer = factories;
        setImageIndex(metadata.getImageIndex());
        setCoordinateReferenceSystem(metadata);
        setBoundedBy(metadata);
        setName(metadata);
    }

    /**
     * Set the element name of this slice, given a
     * {@link SpatioTemporalMetadata} instance.
     * @TODO: Should be improved using range.
     * 
     * @param metadata
     */
    protected abstract void setName(SpatioTemporalMetadata metadata);

    /**
     * Set the bounding box information of this slice, given a
     * {@link SpatioTemporalMetadata} instance.
     * 
     * @param metadata
     */
    protected abstract void setBoundedBy(SpatioTemporalMetadata metadata);

    /**
     * Set the {@link CoordinateReferenceSystem} for this slice, given a
     * {@link SpatioTemporalMetadata} instance.
     */
    protected abstract void setCoordinateReferenceSystem(
            SpatioTemporalMetadata metadata);

    /**
     * The {@link VerticalLevel} defining the vertical extent of this slice in
     * case of not geographic3D/projected3D crs
     */
    private VerticalExtent verticalExtent;

    /** The {@link TemporalGeometricPrimitive} defining the temporal extent of this slice */
    private TemporalGeometricPrimitive temporalExtent;

    /** The {@link GeneralEnvelope} defining the extent of this slice */
    private GeneralEnvelope generalEnvelope;
    
    /**
     * The {@link BoundingBox} defining the horizontal extent of this slice.
     */
    private BoundingBox horizontalExtent;

    /**
     * The name of the element contained in this slice (usually, a coverage
     * name)
     */
    private String elementName;

    /**
     * 
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * 
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return coordinateReferenceSystem;
    }

    /**
     * 
     * @param coordinateReferenceSystem
     */
    public void setCoordinateReferenceSystem(
            CoordinateReferenceSystem coordinateReferenceSystem) {
        this.coordinateReferenceSystem = coordinateReferenceSystem;
    }

    /**
     * return the {@link TimeExtentImpl} defining the temporal extent of this 2D
     * slice.
     * 
     * @return the temporalExtent
     */
    public TemporalGeometricPrimitive getTemporalExtent() {
        return temporalExtent;
    }

    /**
     * return the {@link VerticalLevel} defining the vertical extent of this 2D
     * slice.
     */
    public VerticalExtent getVerticalExtent() {
        return verticalExtent;
    }
    
    public BoundingBox getHorizontalExtent() {
        return horizontalExtent;
    }

    /**
     * 
     */
    public GeneralEnvelope getGeneralEnvelope() {
        return generalEnvelope;
    }

    /**
     * 
     * @return
     */
    public int getImageIndex() {
        return imageIndex;
    }

    /**
     * 
     * @param imageIndex
     */
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    protected ReferencingFactoryContainer getFactoryContainer() {
        return factoryContainer;
    }

    public void setVerticalExtent(VerticalExtent verticalExtent) {
        this.verticalExtent = verticalExtent;
    }

    public void setTemporalExtent(TemporalGeometricPrimitive temporalExtent) {
        this.temporalExtent = temporalExtent;
    }

    public void setGeneralEnvelope(GeneralEnvelope generalEnvelope) {
        this.generalEnvelope = generalEnvelope;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public void setHorizontalExtent(BoundingBox horizontalExtent) {
        this.horizontalExtent = horizontalExtent;
    }
}
