/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.util;

import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.pe.PeCoordinateSystem;
import com.esri.sde.sdk.pe.PeFactory;
import com.esri.sde.sdk.pe.PeGeographicCS;
import com.esri.sde.sdk.pe.PeProjectedCS;
import com.esri.sde.sdk.pe.PeProjectionException;

/**
 * ArcSDE Java API related utility methods to be shared between the gce and dataaccess
 * implementations
 * 
 * @author Gabriel Roldan
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/util/ArcSDEUtils.java $
 * @version $Id$
 * @since 2.5.6
 */
public final class ArcSDEUtils {

    public static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde.gce");

    private static final WeakHashMap<String, CoordinateReferenceSystem> PE_CS_TO_EPSG = new WeakHashMap<String, CoordinateReferenceSystem>();

    private ArcSDEUtils() {
        // private default constructor to stress the pure utility nature of this class
    }

    /**
     * Gets the coordinate system that will be associated to the {@link GridCoverage}.
     * 
     * @param rasterAttributes
     * @return if {@code seCoordRef.getcoordSys()} is {@code null} returns
     *         {@link DefaultEngineeringCRS#CARTESIAN_2D}, otherwise an equivalent CRS from the EPSG
     *         database if found, or a CRS built from the seCoordRef WKT otherwise.
     */
    public static CoordinateReferenceSystem findCompatibleCRS(
            final SeCoordinateReference seCoordRef ) throws DataSourceException {

        if (seCoordRef == null) {
            LOGGER.fine("SeCoordinateReference is null, "
                    + "using DefaultEngineeringCRS.CARTESIAN_2D");
            return DefaultEngineeringCRS.CARTESIAN_2D;
        }

        final PeCoordinateSystem coordSys = seCoordRef.getCoordSys();

        if (coordSys == null) {
            LOGGER.fine("SeCoordinateReference.getCoordSys() is null, "
                    + "using DefaultEngineeringCRS.CARTESIAN_2D");
            return DefaultEngineeringCRS.CARTESIAN_2D;
        }

        final String peCoordSysName = coordSys.getName();

        CoordinateReferenceSystem crs = PE_CS_TO_EPSG.get(peCoordSysName);

        if (crs == null) {
            Integer epsgCode = findEpsgCode(coordSys);
            try {
                if (epsgCode == null) {
                    LOGGER.warning("Couldn't determine EPSG code for this raster."
                            + "  Using SDE's WKT-like coordSysDescription() instead.");
                    crs = CRS.parseWKT(seCoordRef.getCoordSysDescription());
                } else {
                    crs = CRS.decode("EPSG:" + epsgCode);
                }
                
                PE_CS_TO_EPSG.put(peCoordSysName, crs);

            } catch (FactoryException e) {
                LOGGER.log(Level.SEVERE, "", e);
                throw new DataSourceException(e);
            }
        }

        return crs;
    }

    private static Integer findEpsgCode( final PeCoordinateSystem coordSys )
            throws DataSourceException {
        final String peCoordSysName = coordSys.getName();
        Integer epsgCode = null;
        try {
            final int[] seEpsgCodes;
            if (coordSys instanceof PeGeographicCS) {
                seEpsgCodes = PeFactory.geogcsCodelist();
            } else if (coordSys instanceof PeProjectedCS) {
                seEpsgCodes = PeFactory.projcsCodelist();
            } else {
                throw new RuntimeException("Shouldnt happen!: Unnkown SeCoordSys type: "
                        + coordSys.getClass().getName());
            }
            int seEpsgCode;
            PeCoordinateSystem candidate;
            for( int i = 0; i < seEpsgCodes.length; i++ ) {
                try {
                    seEpsgCode = seEpsgCodes[i];
                    candidate = (PeCoordinateSystem) PeFactory.factory(seEpsgCode);
                    // in ArcSDE 9.2, if the PeFactory doesn't support a projection it claimed to
                    // support, it returns 'null'. So check for it.
                    if (candidate != null && candidate.getName().trim().equals(peCoordSysName)) {
                        epsgCode = seEpsgCode;
                        break;
                    }
                } catch (PeProjectionException pe) {
                    // Strangely SDE includes codes in the projcsCodeList() that
                    // it doesn't actually support.
                    // Catch the exception and skip them here.
                }
            }

        } catch (PeProjectionException e) {
            LOGGER.log(Level.SEVERE, "", e);
            throw new DataSourceException(e);
        }
        return epsgCode;
    }
}
