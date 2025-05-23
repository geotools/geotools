/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.crs;

import java.util.Set;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CompoundCRS;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.DerivedCRS;
import org.geotools.api.referencing.crs.EngineeringCRS;
import org.geotools.api.referencing.crs.GeocentricCRS;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.ImageCRS;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.crs.TemporalCRS;
import org.geotools.api.referencing.crs.VerticalCRS;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.util.factory.AbstractFactory;

/**
 * A disabled version of {@link EPSGCRSAuthorityFactory} that exists solely as a workaround for Eclipse capture of
 * transitive dependencies. The real implementation in gt-epsg-wkt; Maven does the right thing and does not include the
 * provider at test time, but Eclipse captures it as a transitive dependency, causing the wrong provider to be used for
 * srsName encoding. The presence of this class allows gt-xsd-gml3 unit tests to pass in Eclipse.
 *
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 * @see <a href="http://jira.codehaus.org/browse/GEOT-3112">GEOT-3112</a>
 */
public class EPSGCRSAuthorityFactory extends AbstractFactory implements CRSAuthorityFactory {

    private static final String EXCEPTION_MESSAGE = "Factory disabled for gt-xsd-gml3 testing (see GEOT-3112)";

    public EPSGCRSAuthorityFactory() {
        super(MINIMUM_PRIORITY);
    }

    @Override
    public Citation getAuthority() {
        CitationImpl c = new CitationImpl(EXCEPTION_MESSAGE);
        c.freeze();
        return c;
    }

    @Override
    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type) throws FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public InternationalString getDescriptionText(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public IdentifiedObject createObject(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public Citation getVendor() {
        return Citations.GEOTOOLS;
    }

    @Override
    public CoordinateReferenceSystem createCoordinateReferenceSystem(String code)
            throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public CompoundCRS createCompoundCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public DerivedCRS createDerivedCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public EngineeringCRS createEngineeringCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public GeographicCRS createGeographicCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public GeocentricCRS createGeocentricCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public ImageCRS createImageCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public ProjectedCRS createProjectedCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public TemporalCRS createTemporalCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }

    @Override
    public VerticalCRS createVerticalCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        throw new FactoryException(EXCEPTION_MESSAGE);
    }
}
