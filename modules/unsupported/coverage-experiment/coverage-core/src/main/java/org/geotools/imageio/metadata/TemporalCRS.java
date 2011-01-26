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

/**
 * A {@code TemporalCRS} node in the metadata tree.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 */
public class TemporalCRS extends AbstractCoordinateReferenceSystem {

    /**
     * // * The {@code "TemporalCRS"} node. //
     */
    protected TemporalCRS(SpatioTemporalMetadata metadata) {
        super(metadata, SpatioTemporalMetadataFormat.MD_TEMPORALCRS);
    }

    /**
     * Adds Datum {@link Identification} identification.
     */
    public void setDatum(Identification identification) {
        setDatum(Datum.TEMPORAL_DATUM, identification);
    }

    /**
     * {@link AbstractCoordinateReferenceSystem.Datum}
     * <p>
     * The date and time origin of this temporal datum.
     * </p>
     */
    public void addOrigin(String value) {
        if (datum.getSelectedChoice() == Datum.TEMPORAL_DATUM)
            datum.getChild().setString(SpatioTemporalMetadataFormat.MD_DTM_TD_ORIGIN, value);
        else
            throw new IllegalArgumentException("Could not set Origin for non-Temporal Datum type");
    }

    /**
     * Returns the date and time origin of this temporal datum.
     */
    public String getOrigin() {
        if (datum.getSelectedChoice() == Datum.TEMPORAL_DATUM)
            return datum.getChild().getString(SpatioTemporalMetadataFormat.MD_DTM_TD_ORIGIN);
        else
            throw new IllegalArgumentException("Could not get Origin for non-Temporal Datum type");
    }
}