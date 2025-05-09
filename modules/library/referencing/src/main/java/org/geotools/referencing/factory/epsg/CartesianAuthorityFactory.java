/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.EngineeringCRS;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.datum.DefaultEngineeringDatum;
import org.geotools.referencing.factory.DirectAuthorityFactory;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Hints;

/**
 * A factory providing a EPSG code for a cartesian engineering systems
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CartesianAuthorityFactory extends DirectAuthorityFactory implements CRSAuthorityFactory {

    public static final String GENERIC_2D_CODE = "404000";

    /** A clone of {@link DefaultEngineeringCRS#GENERIC_2D} with the proper authority name */
    public static final DefaultEngineeringCRS GENERIC_2D = new DefaultEngineeringCRS(
            buildProperties("Wildcard 2D cartesian plane in metric unit", Citations.EPSG, GENERIC_2D_CODE),
            DefaultEngineeringDatum.UNKNOWN,
            DefaultCartesianCS.GENERIC_2D,
            true);

    static Map<String, ?> buildProperties(String name, Citation authority, String code) {
        Map<String, Object> props = new HashMap<>();
        props.put(IdentifiedObject.NAME_KEY, name);
        props.put(IdentifiedObject.IDENTIFIERS_KEY, new NamedIdentifier(authority, code));
        return props;
    }

    public CartesianAuthorityFactory() {
        this(null);
    }

    public CartesianAuthorityFactory(Hints hints) {
        super(hints, ThreadedEpsgFactory.PRIORITY - 10);
    }

    @Override
    public Citation getAuthority() {
        return Citations.EPSG;
    }

    @Override
    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type) throws FactoryException {
        if (type.isAssignableFrom(EngineeringCRS.class)) {
            final Set<String> set = new LinkedHashSet<>();
            set.add(GENERIC_2D_CODE);
            return set;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public InternationalString getDescriptionText(String code) throws NoSuchAuthorityCodeException, FactoryException {
        if (code.equals("EPSG:" + GENERIC_2D_CODE)) {
            return new SimpleInternationalString(
                    "A two-dimensional wildcard coordinate system with X,Y axis in meters");
        } else {
            throw noSuchAuthorityException(code);
        }
    }

    /**
     * Creates an object from the specified code. The default implementation delegates to <code>
     * {@linkplain #createCoordinateReferenceSystem createCoordinateReferenceSystem}(code)</code> .
     */
    @Override
    public IdentifiedObject createObject(final String code) throws FactoryException {
        return createCoordinateReferenceSystem(code);
    }

    /**
     * Creates a coordinate reference system from the specified code. The default implementation delegates to <code>
     * {@linkplain #createEngineeringCRS(String)}(code)</code>.
     */
    @Override
    public CoordinateReferenceSystem createCoordinateReferenceSystem(final String code) throws FactoryException {
        return createEngineeringCRS(code);
    }

    @Override
    public EngineeringCRS createEngineeringCRS(String code) throws NoSuchAuthorityCodeException, FactoryException {
        if (GENERIC_2D_CODE.equals(code) || ("EPSG:" + GENERIC_2D_CODE).equals(code)) {
            return GENERIC_2D;
        } else {
            throw noSuchAuthorityException(code);
        }
    }

    private NoSuchAuthorityCodeException noSuchAuthorityException(String code) throws NoSuchAuthorityCodeException {
        String authority = "EPSG";
        return new NoSuchAuthorityCodeException(
                MessageFormat.format(ErrorKeys.NO_SUCH_AUTHORITY_CODE_$3, code, authority, EngineeringCRS.class),
                authority,
                code);
    }
}
