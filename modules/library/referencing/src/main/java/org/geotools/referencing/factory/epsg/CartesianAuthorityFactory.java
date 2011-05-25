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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.factory.Hints;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.datum.DefaultEngineeringDatum;
import org.geotools.referencing.factory.DirectAuthorityFactory;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.SimpleInternationalString;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.EngineeringCRS;
import org.opengis.util.InternationalString;

/**
 * A factory providing a EPSG code for a cartesian engineering systems
 * 
 * @author Andrea Aime - GeoSolutions
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/referencing/src/main/java/org/geotools/referencing/factory/epsg/CartesianAuthorityFactory.java $
 */
public class CartesianAuthorityFactory extends DirectAuthorityFactory implements
        CRSAuthorityFactory {
    
    static final String GENERIC_2D_CODE = "404000";

    /**
     * A clone of {@link DefaultEngineeringCRS#GENERIC_2D} with the proper authority name
     */
    public static final DefaultEngineeringCRS GENERIC_2D = new DefaultEngineeringCRS(
            buildProperties("Wildcard 2D cartesian plane in metric unit", Citations.EPSG,  GENERIC_2D_CODE), 
            DefaultEngineeringDatum.UNKNOWN, DefaultCartesianCS.GENERIC_2D, true);
    
    static Map<String, ?> buildProperties(String name, Citation authority, String code) {
        Map<String, Object> props = new HashMap<String, Object>();
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

    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type)
            throws FactoryException {
        if (type.isAssignableFrom(EngineeringCRS.class)) {
            final Set set = new LinkedHashSet();
            set.add(GENERIC_2D_CODE);
            return set;
        } else {
            return Collections.EMPTY_SET;
        }
    }

    public InternationalString getDescriptionText(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        if(code.equals("EPSG:" + GENERIC_2D_CODE)) {
        return new SimpleInternationalString(
                "A two-dimensional wildcard coordinate system with X,Y axis in meters");
        } else {
            throw noSuchAuthorityException(code);
        }
    }

    /**
     * Creates an object from the specified code. The default implementation delegates to
     * <code>{@linkplain #createCoordinateReferenceSystem createCoordinateReferenceSystem}(code)</code>
     * .
     */
    public IdentifiedObject createObject(final String code) throws FactoryException {
        return createCoordinateReferenceSystem(code);
    }

    /**
     * Creates a coordinate reference system from the specified code. The default implementation
     * delegates to <code>{@linkplain #createEngineeringCRS(String)}(code)</code>.
     */
    public CoordinateReferenceSystem createCoordinateReferenceSystem(final String code)
            throws FactoryException {
        return createEngineeringCRS(code);
    }

    @Override
    public EngineeringCRS createEngineeringCRS(String code) throws NoSuchAuthorityCodeException,
            FactoryException {
        if (GENERIC_2D_CODE.equals(code) || ("EPSG:" + GENERIC_2D_CODE).equals(code)) {
            return GENERIC_2D;
        } else {
            throw noSuchAuthorityException(code);
        }
    }

    private NoSuchAuthorityCodeException noSuchAuthorityException(String code) throws NoSuchAuthorityCodeException {
        String authority = "EPSG";
        return new NoSuchAuthorityCodeException(Errors.format(ErrorKeys.NO_SUCH_AUTHORITY_CODE_$3,
                   code, authority, EngineeringCRS.class), authority, code);
    }

   
    

}
