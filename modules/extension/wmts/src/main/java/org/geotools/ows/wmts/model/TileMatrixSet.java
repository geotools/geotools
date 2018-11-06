/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.model;

import java.util.ArrayList;
import java.util.List;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The geometry of the tiled space.
 *
 * <p>In a tiled map layer, the representation of the space is constrained in a discrete set of
 * parameters. A tile matrix set defines these parameters. Each tile matrix set contains one or more
 * "tile matrices" defining the tiles that are available for that coordinate reference system.
 *
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 */
public class TileMatrixSet {
    private static final CoordinateReferenceSystem WEB_MERCATOR_CRS;

    static {
        CoordinateReferenceSystem tmpCrs = null;

        try {
            tmpCrs = CRS.decode("EPSG:3857");
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }
        WEB_MERCATOR_CRS = tmpCrs;
    }

    private String identifier;

    private String crs;

    private String wellKnownScaleSet;

    CoordinateReferenceSystem coordinateReferenceSystem;

    private CRSEnvelope bbox;

    private ArrayList<TileMatrix> matrices = new ArrayList<>();

    public void setIdentifier(String id) {
        this.identifier = id;
    }

    public void setCRS(String crs) throws IllegalArgumentException {
        try {
            this.coordinateReferenceSystem = parseCoordinateReferenceSystem(crs);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Can't parse crs " + crs + ":" + ex.getMessage(), ex);
        }

        this.crs = crs;
    }

    public void addMatrix(TileMatrix tileMatrix) {
        matrices.add(tileMatrix);
    }

    /** @return the crs */
    public String getCrs() {
        return crs;
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return coordinateReferenceSystem;
    }

    /**
     * Try and parse the crs string.
     *
     * <p>Also takes care of including deprecated codes like EPSG:900913 replacing them with
     * EPSG:3857.
     */
    protected CoordinateReferenceSystem parseCoordinateReferenceSystem(String crs)
            throws NoSuchAuthorityCodeException, FactoryException {

        if (crs.equalsIgnoreCase("epsg:900913")
                || crs.equalsIgnoreCase("urn:ogc:def:crs:EPSG::900913")) {
            return WEB_MERCATOR_CRS;
        }

        return CRS.decode(crs);
    }

    /** @return the matrices */
    public List<TileMatrix> getMatrices() {
        return matrices;
    }

    /** @param matrices the matrices to set */
    public void setMatrices(ArrayList<TileMatrix> matrices) {
        this.matrices = matrices;
    }

    /** @return the identifier */
    public String getIdentifier() {
        return identifier;
    }

    public CRSEnvelope getBbox() {
        return bbox;
    }

    public void setBbox(CRSEnvelope bbox) {
        this.bbox = bbox;
    }

    public String getWellKnownScaleSet() {
        return wellKnownScaleSet;
    }

    public void setWellKnownScaleSet(String wellKnownScaleSet) {
        this.wellKnownScaleSet = wellKnownScaleSet;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIdentifier()).append("\t").append(getCrs()).append("\n");
        for (TileMatrix m : matrices) {
            sb.append("\t").append(m.toString());
        }
        return sb.toString();
    }

    /** @return the number of levels in this MatrixSet */
    public int size() {
        return matrices.size();
    }
}
