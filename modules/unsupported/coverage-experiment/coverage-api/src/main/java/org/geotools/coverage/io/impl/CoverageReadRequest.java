/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.impl;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.util.NumberRange;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.temporal.TemporalGeometricPrimitive;


/**
 * Request information from a   {@link CoverageSource}. 
 * <p> Note that we are working with the assumption that the queried coverage has separable dimensions.
 */
public class CoverageReadRequest extends CoverageRequest  {
	
	/**
	 * The requested area in the destination raster space. <p> This field shall basically contain the screen dimension of the requested area in pixels.
	 * @uml.property  name="rasterArea"
	 */
	private Rectangle rasterArea;
	
	/**
	 * The requested area in geographic coordinates, which means the area in destination world space  which we want to get data for.
	 * @uml.property  name="geographicArea"
	 */
	private BoundingBox geographicArea;
	
	/**
	 * The request   {@link MathTransform2D}   which would map the pixel into the requested world area. <p> Note that having a raster are and a world area is not enough, unless we have a simple scale-and-translate grid-to-workd transform.
	 * @uml.property  name="gridToWorldTransform"
	 */
	private MathTransform2D gridToWorldTransform;
	
	/**
	 * The subset of the original    {@link RangeType}    we want to obtain.
	 * @uml.property  name="rangeSubset"
	 * @uml.associationEnd  
	 */
	private RangeType rangeSubset;

	/**
	 * The vertical positions for which we want to get some data.
	 * @uml.property  name="verticalSubset"
	 */
	private Set<NumberRange<Double>> verticalSubset;
	
	/**
	 * The temporal positions for which we want to get some data. 
	 * @uml.property  name="temporalSubset"
	 */
	private SortedSet<TemporalGeometricPrimitive> temporalSubset;
	
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#getRangeSubset()
	 */
	public RangeType getRangeSubset(){
		return this.rangeSubset;
	}

	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#setDomainSubset(java.awt.Rectangle, org.opengis.referencing.operation.MathTransform2D, org.opengis.referencing.crs.CoordinateReferenceSystem)
	 */
	public void setDomainSubset(final Rectangle rasterArea, final MathTransform2D gridToWorldTrasform, final CoordinateReferenceSystem crs) throws MismatchedDimensionException, TransformException{
		
		//get input elements
		this.rasterArea=(Rectangle) rasterArea.clone();
		this.gridToWorldTransform=gridToWorldTrasform;
		
		//create a bbox 
		this.geographicArea=new ReferencedEnvelope(CRS.transform(gridToWorldTrasform, new ReferencedEnvelope(rasterArea.getBounds2D(),crs)));
	}
	
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#setDomainSubset(java.awt.Rectangle, org.opengis.geometry.BoundingBox, org.opengis.referencing.datum.PixelInCell)
	 */
	public void setDomainSubset(final Rectangle rasterArea, final BoundingBox worldArea, PixelInCell anchor){
		//get input elements
		this.rasterArea=(Rectangle) rasterArea.clone();
		this.geographicArea=worldArea;
		
		//create a math transform
		final GridToEnvelopeMapper mapper = new GridToEnvelopeMapper(new GridEnvelope2D(rasterArea), new ReferencedEnvelope(worldArea));
		mapper.setPixelAnchor(anchor);
		this.gridToWorldTransform=(MathTransform2D) mapper.createTransform();
	}
	
	
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#setDomainSubset(org.opengis.geometry.BoundingBox)
	 */
	public void setDomainSubset( final BoundingBox worldArea){
		this.geographicArea= new ReferencedEnvelope(worldArea);
	}
	
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#setDomainSubset(com.vividsolutions.jts.geom.Envelope)
	 */
	public void setDomainSubset(final Envelope worldArea){
		this.geographicArea= new ReferencedEnvelope(worldArea);
	}	
	
	
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#setDomainSubset(java.awt.Rectangle, org.opengis.geometry.BoundingBox)
	 */
	public void setDomainSubset(final Rectangle rasterArea, final BoundingBox worldArea){
		setDomainSubset(rasterArea, worldArea,PixelInCell.CELL_CENTER);
	}

	
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#setDomainSubset(java.awt.Rectangle, com.vividsolutions.jts.geom.Envelope)
	 */
	public void setDomainSubset(final Rectangle rasterArea, final Envelope worldArea){
		setDomainSubset(rasterArea,(BoundingBox) new ReferencedEnvelope((org.opengis.geometry.Envelope)worldArea));
	}	
	
	
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#setDomainSubset(java.awt.Rectangle, com.vividsolutions.jts.geom.Envelope, org.opengis.referencing.datum.PixelInCell)
	 */
	public void setDomainSubset(final Rectangle rasterArea, final Envelope worldArea, PixelInCell anchor){
		setDomainSubset(rasterArea,(BoundingBox) new ReferencedEnvelope((org.opengis.geometry.Envelope)worldArea),anchor);
	}	

        /**
         * Set the range subset we are requesting. <p> Note that a null  {@link RangeType}  means get everything.
         * @param  value
         * @uml.property  name="rangeSubset"
         */
	public void setRangeSubset(final RangeType value) {
		this.rangeSubset = value;
	}
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#getVerticalSubset()
	 */
	public Set<NumberRange<Double>> getVerticalSubset() {
	    if (verticalSubset==null){
                Set<NumberRange<Double>> empty = Collections.emptySet();
                verticalSubset = new HashSet<NumberRange<Double>>(empty);
                return verticalSubset;
            }
            else
		return new HashSet<NumberRange<Double>>(verticalSubset);
	}
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#setVerticalSubset(java.util.SortedSet)
	 */
	public void setVerticalSubset(Set<NumberRange<Double>> verticalSubset) {
		this.verticalSubset = new HashSet<NumberRange<Double>>(verticalSubset);
	}
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#getTemporalSubset()
	 */
	public SortedSet<TemporalGeometricPrimitive> getTemporalSubset() {
	    if (temporalSubset==null){
	        Set<TemporalGeometricPrimitive> empty = Collections.emptySet();
	        temporalSubset = new TreeSet<TemporalGeometricPrimitive>(empty);
	        return temporalSubset;
	    }
	    else return new TreeSet<TemporalGeometricPrimitive>(temporalSubset);
	}
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#setTemporalSubset(java.util.SortedSet)
	 */
	public void setTemporalSubset(
			SortedSet<TemporalGeometricPrimitive> temporalSubset) {
		this.temporalSubset = new TreeSet<TemporalGeometricPrimitive>(temporalSubset);
	}
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#getRasterArea()
	 */
	public Rectangle getRasterArea() {
		return rasterArea!=null?(Rectangle) rasterArea.clone():rasterArea;
	}
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#getGeographicArea()
	 */
	public BoundingBox getGeographicArea() {
		return geographicArea;
	}
	/**
	 * @see org.geotools.coverage.io.CoverageReadRequest#getGridToWorldTransform()
	 */
	public MathTransform2D getGridToWorldTransform() {
		return gridToWorldTransform;
	}
}
