/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.client;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Single collection document */
public class Collection<T> extends AbstractDocument {

    static final Logger LOGGER = Logging.getLogger(Collection.class);

    protected CollectionExtent extent;

    public CollectionExtent getExtent() {
        return extent;
    }

    public void setExtent(CollectionExtent extent) {
        this.extent = extent;
    }

    /**
     * Gets a single {@link ReferencedEnvelope} representing the full bounds of the collection, or
     * null if they are not found.
     *
     * @return
     */
    public ReferencedEnvelope getBounds() {
        return Optional.ofNullable(extent)
                .map(e -> e.getSpatial())
                .map(s -> toEnvelope(s))
                .orElse(null);
    }

    private ReferencedEnvelope toEnvelope(CollectionExtent.SpatialExtents ext) {
        ReferencedEnvelope result = null;

        CoordinateReferenceSystem crs =
                Optional.ofNullable(ext.getCrs())
                        .map(Collection::decodeSafe)
                        .orElse(DefaultGeographicCRS.WGS84);
        for (List<Double> box : ext.getBbox()) {
            ReferencedEnvelope re = toEnvelope(box, crs);
            if (result == null) result = re;
            else result.expandToInclude(re);
        }

        return result;
    }

    private static CoordinateReferenceSystem decodeSafe(String s) {
        try {
            return CRS.decode(s);
        } catch (FactoryException e) {
            throw new RuntimeException("Could not decode CRS: " + s, e);
        }
    }

    private ReferencedEnvelope toEnvelope(List<Double> box, CoordinateReferenceSystem crs) {
        int size = box.size();
        if (size == 4) {
            return new ReferencedEnvelope(box.get(0), box.get(2), box.get(1), box.get(3), crs);
        } else if (size == 6) {
            return new ReferencedEnvelope3D(
                    box.get(0), box.get(3), box.get(1), box.get(4), box.get(2), box.get(5), crs);
        }
        LOGGER.log(
                Level.FINE,
                "Don't know how to represent a bounding box with " + size + " ordinates, skipping");
        return null;
    }
}
