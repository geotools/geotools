/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid;

import com.vividsolutions.jts.geom.Envelope;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * The base class for builders that generate polygonal grid elements.
 * 
 * @see org.geotools.grid.hexagon.HexagonGridBuilder
 * @see org.geotools.grid.oblong.OblongGridBuilder
 *
 * @author mbedward
 * @since 8.0
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class PolygonBuilder {

    private static final Logger LOGGER = Logger.getLogger("org.geotools.grid");
    
    protected final ReferencedEnvelope gridBounds;

    public PolygonBuilder(ReferencedEnvelope gridBounds) {
        this.gridBounds = gridBounds;
    }

    public boolean buildGrid(GridFeatureBuilder gridFeatureBuilder,
            double vertexSpacing,
            SimpleFeatureCollection fc) {

        boolean result = true;

        final boolean densify = isValidDenseVertexSpacing(vertexSpacing);

        final SimpleFeatureBuilder fb = new SimpleFeatureBuilder(gridFeatureBuilder.getType());
        final String geomPropName = gridFeatureBuilder.getType().getGeometryDescriptor().getLocalName();

        PolygonElement el0 = getFirstElement();
        PolygonElement el = el0;

        while (el.getBounds().getMinY() <= gridBounds.getMaxY()) {
            while (el.getBounds().getMaxX() <= gridBounds.getMaxX()) {
                if (((Envelope) gridBounds).contains(el.getBounds())) {
                    if (gridFeatureBuilder.getCreateFeature(el)) {
                        Map<String, Object> attrMap = new HashMap<String, Object>();
                        gridFeatureBuilder.setAttributes(el, attrMap);

                        if (densify) {
                            fb.set(geomPropName, el.toDenseGeometry(vertexSpacing));
                        } else {
                            fb.set(geomPropName, el.toGeometry());
                        }

                        for (String propName : attrMap.keySet()) {
                            fb.set(propName, attrMap.get(propName));
                        }

                        fc.add(fb.buildFeature(gridFeatureBuilder.getFeatureID(el)));
                    }
                }

                el = getNextXElement(el);
            }

            el0 = getNextYElement(el0);
            el = el0;
        }

        return result;
    }

    public abstract boolean isValidNeighbor(Neighbor neighbor);

    public abstract PolygonElement createNeighbor(PolygonElement el, Neighbor neighbor);

    public abstract PolygonElement getFirstElement();

    public abstract PolygonElement getNextXElement(PolygonElement el);

    public abstract PolygonElement getNextYElement(PolygonElement el);

    public abstract boolean isValidDenseVertexSpacing(double v);
}
