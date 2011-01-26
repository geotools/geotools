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
 * Base class for vector grid builders.
 *
 * @author mbedward
 * @since 2.7
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/grid/src/main/java/org/geotools/grid/DefaultFeatureBuilder.java $
 * @version $Id: DefaultFeatureBuilder.java 35637 2010-06-01 09:24:43Z mbedward $
 */
public abstract class AbstractGridBuilder {

    private static final Logger LOGGER = Logger.getLogger(AbstractGridBuilder.class.getName());

    protected final ReferencedEnvelope gridBounds;

    public AbstractGridBuilder(ReferencedEnvelope gridBounds) {
        this.gridBounds = new ReferencedEnvelope(gridBounds);
    }

    public boolean buildGrid(SimpleFeatureCollection fc,
            GridFeatureBuilder gridFeatureBuilder,
            double vertexSpacing) {

        boolean result = true;

        final boolean densify = isValidDenseVertexSpacing(vertexSpacing);

        final SimpleFeatureBuilder fb = new SimpleFeatureBuilder(gridFeatureBuilder.getType());
        final String geomPropName = gridFeatureBuilder.getType().getGeometryDescriptor().getLocalName();

        GridElement el0 = getFirstElement();
        GridElement el = el0;

        while (el.getBounds().getMinY() <= gridBounds.getMaxY()) {
            while (el.getBounds().getMaxX() <= gridBounds.getMaxX()) {
                if (((Envelope) gridBounds).contains(el.getBounds())) {
                    if (gridFeatureBuilder.getCreateFeature(el)) {
                        Map<String, Object> attrMap = new HashMap<String, Object>();
                        gridFeatureBuilder.setAttributes(el, attrMap);

                        if (densify) {
                            fb.set(geomPropName, el.toDensePolygon(vertexSpacing));
                        } else {
                            fb.set(geomPropName, el.toPolygon());
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

    public abstract GridElement createNeighbor(GridElement el, Neighbor neighbor);

    public abstract GridElement getFirstElement();

    public abstract GridElement getNextXElement(GridElement el);

    public abstract GridElement getNextYElement(GridElement el);

    public abstract boolean isValidDenseVertexSpacing(double v);
}
