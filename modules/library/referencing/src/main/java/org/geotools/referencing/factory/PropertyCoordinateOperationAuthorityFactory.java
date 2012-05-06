/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.DefaultOperation;
import org.geotools.referencing.operation.DefaultOperationMethod;
import org.geotools.referencing.operation.transform.AbstractMathTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.SimpleInternationalString;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.util.InternationalString;

/**
 * A {@link CoordinateOperationAuthorityFactory} backed by a properties file.
 * Allows custom transform definitions across two CRSs, expressed as WKT math transforms.
 * Entries in the properties file take this format:
 * <pre>
 * [source crs code],[target crs code]=[WKT math transform]
 * </pre>
 * Examples:
 * <pre>
 * 4230,4258=PARAM_MT["NTv2", PARAMETER["Latitude and longitude difference file", "100800401.gsb"]]
 * 23031,25831=PARAM_MT["Similarity transformation", \
 *   PARAMETER["Ordinate 1 of evaluation point in target CRS", -129.549], \
 *   PARAMETER["Ordinate 2 of evaluation point in target CRS", -208.185], \
 *   PARAMETER["Scale difference", 1.0000015504], \
 *   PARAMETER["Rotation angle of source coordinate reference system axes", 1.56504]]
 * </pre>
 * For more compact definitions, parameter names can be replaced by their corresponding EPSG codes.
 * Following examples are the same as former ones:
 * <pre>
 * 4230,4258=PARAM_MT["9615", PARAMETER["8656", "100800401.gsb"]]
 * 23031,25831=PARAM_MT["9621", \
 *   PARAMETER["8621", -129.549], \
 *   PARAMETER["8622", -208.185], \
 *   PARAMETER["8611", 1.0000015504], \
 *   PARAMETER["8614", 1.56504]]
 * </pre>
 * References:
 * <p>
 * See <a href="http://www.geoapi.org/3.0/javadoc/org/opengis/referencing/doc-files/WKT.html">
 * <cite>Well-Known Text format</cite></a> for math transform syntax.
 * Visit the <a href="http://www.epsg-registry.org/"> <cite>EPSG Geodetic Parameter Registry</cite>
 * </a> for EPSG parameter codes and values.
 * <p>
 * Note that invertible transforms will be used in both directions.
 * <p>
 * This factory doesn't cache any result. Any call to a {@code createFoo} method will trig a new
 * WKT parsing. For caching, this factory should be wrapped in some buffered factory like
 * {@link BufferedAuthorityFactory}.
 *
 * @source $URL$
 * @version $Id$
 * @author Oscar Fonts
 */
public class PropertyCoordinateOperationAuthorityFactory extends
        DirectAuthorityFactory implements CoordinateOperationAuthorityFactory {

    /**
     * The authority for this factory.
     */
    private final Citation authority;
    
    /**
     * The properties object for our properties file. Keys are CRS code pairs
     * separated by a comma. The associated value is a WKT string for the Math
     * Transform. See {@link PropertyCoordinateOperationAuthorityFactory}.
     */
    private final Properties definitions = new Properties();
    
    /**
     * An unmodifiable view of the authority keys. This view is always up to date
     * even if entries are added or removed in the {@linkplain #definitions} map.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final Set<String> codes = Collections.unmodifiableSet((Set) definitions.keySet());
    
    /**
     * Creates a factory for the specified authority from the specified file.
     *
     * @param  factories   The underlying factories used for objects creation.
     * @param  authority   The organization or party responsible for definition and maintenance of
     *                     the database.
     * @param  definitions URL to the definition file.
     * @throws IOException if the definitions can't be read.
     */
    public PropertyCoordinateOperationAuthorityFactory(
            final ReferencingFactoryContainer  factories,
            final Citation                     authority,
            final URL                          definitions)
        throws IOException
    {
        // Set priority low
        super(factories, MINIMUM_PRIORITY + 10);
        
        // Set authority
        this.authority = authority;
        ensureNonNull("authority", authority);
        
        // Load properties
        final InputStream in = definitions.openStream();
        this.definitions.load(in);
        in.close();
    }
    
    /**
     * Creates an operation from a single operation code.
     *
     * @param  code Coded value for operation.
     * @return The operation for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    @Override    
    public CoordinateOperation createCoordinateOperation(String code)
            throws NoSuchAuthorityCodeException, FactoryException {
        String[] crsPair = trimAuthority(code).split(",");
        if (crsPair.length == 2) {
            Set<CoordinateOperation> coordopset = createFromCoordinateReferenceSystemCodes(
                    trimAuthority(crsPair[0]), trimAuthority(crsPair[1]));
            if (!coordopset.isEmpty()) {
                return coordopset.iterator().next();
            }
        }
        return null;
    }

    /**
     * Creates a {@link CoordinateOperation} from
     * {@linkplain CoordinateReferenceSystem coordinate reference system} codes.
     * This method returns a single operation from the properties file.
     * If operation is invertible, will check also for the inverse one.
     * If operation not found, it will return an empty set.
     *
     * @param  sourceCRS   Coded value of source coordinate reference system.
     * @param  targetCRS   Coded value of target coordinate reference system.
     * @return The operation from {@code sourceCRS} to {@code targetCRS} (one single element).
     * @throws NoSuchAuthorityCodeException if a specified code was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    @Override
    public Set<CoordinateOperation> createFromCoordinateReferenceSystemCodes(
            String sourceCRS, String targetCRS)
                    throws NoSuchAuthorityCodeException, FactoryException {

        Set<CoordinateOperation> coordops = new HashSet<CoordinateOperation>(1);

        CoordinateOperation coordop = createFromCoordinateReferenceSystemCodes(sourceCRS,
                targetCRS, false);
        if (coordop == null) {
            // Not found. Try to create from the inverse.
            coordop = createFromCoordinateReferenceSystemCodes(targetCRS, sourceCRS, true);
        }
        if (coordop != null) {
            // Add to set if found.
            coordops.add(coordop);
        }
        return coordops;
    }
    
    
    /**
     * Seeks for a WKT definition in the properties file from a CRS pair, parses it,
     * and creates the corresponding CoordinateOperation. Returns {@code null}
     * if something went wrong.
     * <p>
     * Will log a WARNING message if a parsing error occurred.
     * 
     * @param  sourceCRS   Coded value of source coordinate reference system.
     * @param  targetCRS   Coded value of target coordinate reference system.
     * @param  inverse     {@code true} to create operation from the inverse definition.
     * @return The operation from {@code sourceCRS} to {@code targetCRS},
     *         or {@code null} if not found.
     * @throws NoSuchAuthorityCodeException if a specified code was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    CoordinateOperation createFromCoordinateReferenceSystemCodes(
            String sourceCRS, String targetCRS, boolean inverse)
                    throws NoSuchAuthorityCodeException, FactoryException {

        // Get WKT definition from properties
        sourceCRS = trimAuthority(sourceCRS);
        targetCRS = trimAuthority(targetCRS);
        String id = sourceCRS+","+targetCRS;
        String WKT = definitions.getProperty(id);        
        if(WKT == null) {
            // No definition found.
            return null;
        }
       
        // Create MathTransform from WKT
        MathTransform mt = null;
        try {
            mt = factories.getMathTransformFactory().createFromWKT(WKT);
        } catch (FactoryException e) {
            // Probably malformed WKT.
            LOGGER.warning("Error creating transformation: " + WKT);
            return null;
        }

        // Create the CRS definitions
        String s = this.authority.getIdentifiers().iterator().next().getCode();
        CoordinateReferenceSystem source = CRS.decode(s+":"+sourceCRS);
        CoordinateReferenceSystem target = CRS.decode(s+":"+targetCRS);
        
        // Need to create a derived MathTransform that will handle axis order and units
        // as defined in CRS. Had to cast to DefaultMathTransformFactory because
        // createBaseToDerived is not defined in MathTransformFactory interface (GeoAPI).
        DefaultMathTransformFactory mtf = (DefaultMathTransformFactory)factories.
                getMathTransformFactory();
        MathTransform mt2 = mtf.createBaseToDerived(source, mt, target.getCoordinateSystem());
        
        // Extract name from the transform, if possible, or use class name. 
        String methodName;
        try {
            if (mt instanceof AbstractMathTransform) {
                methodName = ((AbstractMathTransform)mt).getParameterValues().getDescriptor().getName().getCode();
            } else if (mt instanceof AffineTransform2D) {
                methodName = ((AffineTransform2D)mt).getParameterValues().getDescriptor().getName().getCode();
            } else {
                methodName = mt.getClass().getSimpleName();
            }
        } catch (NullPointerException e) {
            methodName = mt.getClass().getSimpleName();
        }
        Map<String, String> props = new HashMap<String, String>();
        props.put("name", methodName);
        
        // Create the OperationMethod
        OperationMethod method = new DefaultOperationMethod(props,
                mt2.getSourceDimensions(), mt2.getTargetDimensions(), null);
        
        // Finally create CoordinateOperation
        CoordinateOperation coordop = null;
        if (!inverse) { // Direct operation
            props.put("name", sourceCRS + " \u21E8 " + targetCRS);
            coordop = DefaultOperation.create(props, source, target,
                    mt2, method, CoordinateOperation.class);
        } else { // Inverse operation
            try {
                props.put("name", targetCRS + " \u21E8 " + sourceCRS);
                coordop = DefaultOperation.create(props, target, source,
                    mt2.inverse(), method, CoordinateOperation.class);
            } catch (NoninvertibleTransformException e) {
                return null;
            }        
        }
        return coordop;
    }    

    /**
     * Returns the set of authority codes of the given type.
     * Only CoordinateOperation.class is accepted as type.
     * 
     * This factory will not filter codes for its subclasses.
     *
     * @param  type The CoordinateOperation type (or null, same effect).
     * @return All of available authority codes, or an empty set.
     */
    @Override
    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type) {
        if (type==null || type.isAssignableFrom(CoordinateOperation.class)) {
            return codes;
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * Gets a description of the object corresponding to a code.
     *
     * @param  code Value allocated by authority.
     * @return A description of the object, or {@code null} if the object
     *         corresponding to the specified {@code code} has no description.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the query failed for some other reason.
     */
    @Override
    public InternationalString getDescriptionText(String code)
            throws NoSuchAuthorityCodeException, FactoryException {
        
        final String wkt = definitions.getProperty(trimAuthority(code));
        
        if (wkt == null) {
            throw noSuchAuthorityCode(IdentifiedObject.class, code);
        }
        
        // The first string literal in WKT will be considered the description text.
        int start = wkt.indexOf('"');
        if (start >= 0) {
            final int end = wkt.indexOf('"', ++start);
            if (end >= 0) {
                return new SimpleInternationalString(wkt.substring(start, end).trim());
            }
        }
        return null;
    }

    /**
     * Returns the organization or party responsible for definition and maintenance of the
     * database.
     */
    @Override
    public Citation getAuthority() {
        return authority;
    }
}
