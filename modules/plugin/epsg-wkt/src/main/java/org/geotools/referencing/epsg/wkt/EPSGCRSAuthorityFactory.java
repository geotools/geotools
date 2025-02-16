/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.epsg.wkt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.ObjectFactory;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CRSFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.Version;
import org.geotools.util.factory.AbstractFactory;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Default implementation for a coordinate reference system authority factory backed by the EPSG property file. This
 * gives most of the benifits of using the EPSG database backed authority factory, in a nice, portable property file.
 *
 * @version $Id$
 * @author Jody Garnett
 * @author Rueben Schulz
 */
public class EPSGCRSAuthorityFactory extends AbstractFactory implements CRSAuthorityFactory {

    private static final Logger LOGGER = Logging.getLogger(EPSGCRSAuthorityFactory.class);

    public static final String AUTHORITY = "EPSG";
    public static final String AUTHORITY_PREFIX = "EPSG:";
    // would be nice to cache crs objects for codes that have already been requested

    /** The default coordinate system authority factory. Will be constructed only when first requested. */
    protected static EPSGCRSAuthorityFactory DEFAULT;

    /**
     * The properties object for our properties file. Keys are the EPSG code for a coordinate reference system and the
     * associated value is a WKT string for the CRS.
     */
    protected Properties epsg = new Properties();

    // object factory
    protected CRSFactory crsFactory;

    /** Cache of parsed CoordinateReferenceSystem WKT by EPSG_NUMBER */
    private Map<String, Object> cache = new HashMap<>();

    /**
     * Loads from epsg.properties if the file exists, defaults to internal defintions exported from postgis and
     * cubeworks.
     */
    public EPSGCRSAuthorityFactory() {
        this(ReferencingFactoryFinder.getCRSFactory(null));
    }

    /**
     * Loads from epsg.properties if the file exists, defaults to internal defintions exported from postgis and
     * cubeworks.
     */
    protected EPSGCRSAuthorityFactory(final CRSFactory factory) {
        super(MINIMUM_PRIORITY); // Select EPSG-HSQL factory first.
        this.crsFactory = factory;
        // Add hints that EPSG-HSQL set's, to avoid this being chosen when those are asked for.
        hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE);
        hints.put(Hints.VERSION, new Version(""));
        try {
            loadDefault();
        } catch (IOException oops) {
            LOGGER.log(Level.SEVERE, "Could not load epsg.properties", oops);
        }
    }

    /** */
    protected EPSGCRSAuthorityFactory(final CRSFactory factory, URL definition) throws FactoryException {
        this(factory);

        try {
            epsg.load(definition.openStream());
        } catch (IOException io) {
            // could not load properties file
            throw new FactoryException("Could not load properties file: " + definition);
        }
    }

    /**
     * Loads from epsg.properties if the file exists, defaults to internal defintions exported from postgis and
     * cubeworks.
     */
    protected void loadDefault() throws IOException {
        // Check the application directory first
        //
        File file = new File("epsg.properties");
        if (file.exists()) {
            epsg.load(new FileInputStream(file));
        }
        // Use the built-in property defintions
        //
        URL url = EPSGCRSAuthorityFactory.class.getResource("epsg.properties");
        epsg.load(url.openStream());
    }

    /**
     * Returns a default coordinate system factory backed by the EPSG property file.
     *
     * @return The default factory.
     * @throws SQLException if the connection to the database can't be etablished.
     */
    public static synchronized CRSAuthorityFactory getDefault() {
        if (DEFAULT == null) {
            DEFAULT = new EPSGCRSAuthorityFactory();
        }
        return DEFAULT;
    }

    @Override
    public synchronized CoordinateReferenceSystem createCoordinateReferenceSystem(String code) throws FactoryException {
        if (code == null) {
            return null;
        }
        if (!code.startsWith(AUTHORITY_PREFIX)) {
            throw new NoSuchAuthorityCodeException("This factory only understand EPSG codes", AUTHORITY, code);
        }
        final String EPSG_NUMBER = code.substring(code.indexOf(':') + 1).trim();

        if (cache.containsKey(EPSG_NUMBER)) {
            Object value = cache.get(EPSG_NUMBER);
            if (value instanceof Throwable) {
                throw new FactoryException("WKT for " + code + " could not be parsed", (Throwable) value);
            }
            if (value instanceof CoordinateReferenceSystem) {
                return (CoordinateReferenceSystem) value;
            }
        }
        String wkt = epsg.getProperty(EPSG_NUMBER);
        if (wkt == null) {
            throw new NoSuchAuthorityCodeException("Unknown EPSG_NUMBER", AUTHORITY, code);
        }
        if (wkt.indexOf(EPSG_NUMBER) == -1) {
            wkt = wkt.trim();
            wkt = wkt.substring(0, wkt.length() - 1);
            wkt += ",AUTHORITY[\"EPSG\",\"" + EPSG_NUMBER + "\"]]";
            LOGGER.log(
                    Level.WARNING,
                    "EPSG:"
                            + EPSG_NUMBER
                            + " lacks a proper identifying authority in its Well-Known Text. It is being added programmatically.");
        }
        try {
            CoordinateReferenceSystem crs = crsFactory.createFromWKT(wkt);
            cache.put(EPSG_NUMBER, crs);
            return crs;
        } catch (FactoryException fex) {
            cache.put(EPSG_NUMBER, fex);
            throw fex;
        }
    }

    @Override
    public IdentifiedObject createObject(String code) throws FactoryException {
        return createCoordinateReferenceSystem(code);
    }

    @Override
    public ProjectedCRS createProjectedCRS(String code) throws FactoryException {
        return (ProjectedCRS) createCoordinateReferenceSystem(code);
    }

    @Override
    public GeographicCRS createGeographicCRS(String code) throws FactoryException {
        return (GeographicCRS) createCoordinateReferenceSystem(code);
    }

    @Override
    public Citation getAuthority() {
        return Citations.EPSG;
    }

    /**
     * Returns the set of authority codes of the given type. The type argument specify the base class. For example if
     * this factory is an instance of CRSAuthorityFactory, then:
     *
     * <ul>
     *   <li>CoordinateReferenceSystem.class asks for all authority codes accepted by createGeographicCRS,
     *       createProjectedCRS, createVerticalCRS, createTemporalCRS and their friends.
     *   <li>ProjectedCRS.class asks only for authority codes accepted by createProjectedCRS.
     * </ul>
     *
     * The following implementaiton filters the set of codes based on the "PROJCS" and "GEOGCS" at the start of the WKT
     * strings. It is assumed that we only have GeographicCRS and ProjectedCRS's here.
     *
     * @param clazz The spatial reference objects type (may be Object.class).
     * @return The set of authority codes for spatial reference objects of the given type. If this factory doesn't
     *     contains any object of the given type, then this method returns an empty set.
     * @throws FactoryException if access to the underlying database failed.
     */
    @Override
    public Set<String> getAuthorityCodes(Class clazz) throws FactoryException {
        // could cashe this info if it is time consuming to filter
        if (clazz.getName().equalsIgnoreCase(CoordinateReferenceSystem.class.getName())) {
            Set<String> all = new java.util.TreeSet<>();
            for (Object o : epsg.keySet()) {
                String code = (String) o;
                all.add(AUTHORITY_PREFIX + code);
            }
            return all;
        } else if (clazz.getName().equalsIgnoreCase(GeographicCRS.class.getName())) {
            Set<Object> all = epsg.keySet();
            Set<String> geoCRS = new java.util.TreeSet<>();
            for (Object o : all) {
                String code = (String) o;
                String wkt = epsg.getProperty(code);
                if (wkt.startsWith("GEOGCS")) {
                    geoCRS.add(AUTHORITY_PREFIX + code);
                }
            }
            return geoCRS;

        } else if (clazz.getName().equalsIgnoreCase(ProjectedCRS.class.getName())) {
            Set<Object> all = epsg.keySet();
            Set<String> projCRS = new java.util.TreeSet<>();
            for (Object o : all) {
                String code = (String) o;
                String wkt = epsg.getProperty(code);
                if (wkt.startsWith("PROJCS")) {
                    projCRS.add(AUTHORITY_PREFIX + code);
                }
            }
            return projCRS;

        } else {
            return new java.util.TreeSet<>();
        }
    }

    public ObjectFactory getObjectFactory() {
        return crsFactory;
    }

    @Override
    public Citation getVendor() {
        return Citations.GEOTOOLS;
    }

    @Override
    public InternationalString getDescriptionText(String code) throws FactoryException {
        if (code == null) {
            return null;
        }
        if (code.startsWith("EPSG:")) { // EPSG:26907
            code = code.substring(5);
        }
        code = code.trim();
        String wkt = epsg.getProperty(code);
        if (wkt == null) {
            throw new FactoryException("Unknonwn EPSG code: '" + code + "'");
        }
        wkt = wkt.trim();
        int start = wkt.indexOf('"');
        int end = wkt.indexOf('"', start + 1);
        return new org.geotools.util.SimpleInternationalString(wkt.substring(start + 1, end));
    }

    @Override
    public org.geotools.api.referencing.crs.CompoundCRS createCompoundCRS(String str) throws FactoryException {
        throw new FactoryException("Not implemented");
    }

    @Override
    public org.geotools.api.referencing.crs.DerivedCRS createDerivedCRS(String str) throws FactoryException {
        throw new FactoryException("Not implemented");
    }

    @Override
    public org.geotools.api.referencing.crs.EngineeringCRS createEngineeringCRS(String str) throws FactoryException {
        throw new FactoryException("Not implemented");
    }

    @Override
    public org.geotools.api.referencing.crs.GeocentricCRS createGeocentricCRS(String str) throws FactoryException {
        throw new FactoryException("Not implemented");
    }

    @Override
    public org.geotools.api.referencing.crs.ImageCRS createImageCRS(String str) throws FactoryException {
        throw new FactoryException("Not implemented");
    }

    @Override
    public org.geotools.api.referencing.crs.TemporalCRS createTemporalCRS(String str) throws FactoryException {
        throw new FactoryException("Not implemented");
    }

    @Override
    public org.geotools.api.referencing.crs.VerticalCRS createVerticalCRS(String str) throws FactoryException {
        throw new FactoryException("Not implemented");
    }
}
