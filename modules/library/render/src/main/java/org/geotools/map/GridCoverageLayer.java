/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import java.util.logging.Level;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

public class GridCoverageLayer extends Layer {

    protected Style style;

    protected GridCoverage2D coverage;

    /**
     * Add a new layer and trigger a {@link LayerListEvent}.
     * 
     * @param coverage
     *            The new layer that has been added.
     * @param style
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public GridCoverageLayer(GridCoverage2D coverage, Style style) {
        this.coverage = coverage;
        this.style = style;
    }

    public GridCoverageLayer(GridCoverage2D coverage, Style style, String title) {
        this.coverage = coverage;
        this.style = style;
        setTitle(title);
    }

    @Override
    public void dispose() {
        if( coverage != null ){
            try{
                coverage.dispose(true);
            }catch (Exception e) {
                // eat me
            }            
            coverage = null;
        }
        if( style != null ){
            style = null;
        }
        super.dispose();
    }

    public GridCoverage2D getCoverage() {
        return coverage;
    }

    public ReferencedEnvelope getBounds() {
        if (coverage != null) {
            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
            Envelope2D bounds = coverage.getEnvelope2D();
            if (bounds != null) {
                return new ReferencedEnvelope(bounds);
            } else if (crs != null) {
                return new ReferencedEnvelope(crs);
            }
        }
        return null;
    }

    /**
     * Getter for property style.
     * 
     * @return Value of property style.
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Setter for property style.
     * 
     * @param style
     *            New value of property style.
     * 
     * @throws NullPointerException
     *             DOCUMENT ME!
     */
    public void setStyle(Style style) {
        if (style == null) {
            throw new NullPointerException();
        }
        this.style = style;
        fireMapLayerListenerLayerChanged(MapLayerEvent.STYLE_CHANGED);
    }

    public SimpleFeatureCollection toFeatureCollection() {
        SimpleFeatureCollection collection;
        try {
            collection = FeatureUtilities.wrapGridCoverage(coverage);
            return collection;
        } catch (Exception e) {
            LOGGER.log(Level.FINER, "Coverage could not be converted to FeatureCollection", e);
            return null;
        }
    }
}
