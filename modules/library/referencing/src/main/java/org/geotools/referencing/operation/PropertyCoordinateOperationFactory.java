/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.operation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationAuthorityFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.NoninvertibleTransformException;
import org.geotools.api.referencing.operation.OperationMethod;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.BufferedAuthorityFactory;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.operation.transform.AbstractMathTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.factory.Hints;

/**
 * A {@link CoordinateOperationAuthorityFactory} backed by a properties file. It's similar to
 * {@link org.geotools.referencing.factory.PropertyCoordinateOperationAuthorityFactory} but allows custom transform
 * definitions across CRSs in different authorities. The CRSs are still expressed as WKT math transforms. Entries in the
 * properties file take this format:
 *
 * <pre>
 * [source crs code],[target crs code]=[WKT math transform]
 * </pre>
 *
 * Examples:
 *
 * <pre>
 * EPSG:4230,EPSG:4258=PARAM_MT["NTv2", PARAMETER["Latitude and longitude difference file", "100800401.gsb"]]
 * </pre>
 *
 * For more compact definitions, parameter names can be replaced by their corresponding EPSG codes. Following examples
 * are the same as former ones:
 *
 * <pre>
 * EPSG:4230,EPSG:4258=PARAM_MT["9615", PARAMETER["8656", "100800401.gsb"]]
 * </pre>
 *
 * References:
 *
 * <p>See <a href="http://www.geoapi.org/3.0/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well-Known Text
 * format</cite></a> for math transform syntax. Visit the <a href="http://www.epsg-registry.org/"><cite>EPSG Geodetic
 * Parameter Registry</cite> </a> for EPSG parameter codes and values.
 *
 * <p>Note that invertible transforms will be used in both directions.
 *
 * <p>This factory doesn't cache any result. Any call to a {@code createFoo} method will trig a new WKT parsing. For
 * caching, this factory should be wrapped in some buffered factory like {@link BufferedAuthorityFactory}.
 *
 * @version $Id$
 * @author Oscar Fonts
 */
public abstract class PropertyCoordinateOperationFactory extends DefaultCoordinateOperationFactory {

    /**
     * The properties object for our properties file. Keys are CRS code pairs separated by a comma. The associated value
     * is a WKT string for the Math Transform. See {@link PropertyCoordinateOperationFactory}.
     */
    private Properties definitions;

    ReferencingFactoryContainer factories;

    /**
     * Creates a factory for the specified authority from the specified file.
     *
     * @throws IOException if the definitions can't be read.
     */
    public PropertyCoordinateOperationFactory(Hints userHints, int priority) {
        super(userHints, priority);
        /*
         * Removes the hint processed by the super-class. This include hints like
         * LENIENT_DATUM_SHIFT, which usually don't apply to authority factories.
         * An other way to see this is to said that this class "consumed" the hints.
         * By removing them, we increase the chances to get an empty map of remaining hints,
         * which in turn help to get the default CoordinateOperationAuthorityFactory
         * (instead of forcing a new instance).
         */
        userHints = new Hints(userHints);
        userHints.keySet().removeAll(hints.keySet());
        userHints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        userHints.remove(Hints.FORCE_STANDARD_AXIS_DIRECTIONS);
        userHints.remove(Hints.FORCE_STANDARD_AXIS_UNITS);

        this.factories = ReferencingFactoryContainer.instance(userHints);
    }

    /**
     * Loads definitions from the specified input stream. The stream is closed by this method.
     *
     * @throws IOException if the definitions can't be read.
     */
    protected Properties getDefinitions() {
        if (definitions == null) {
            Properties props = new Properties();
            // Load properties
            try (InputStream in = getDefinitionsURL().openStream()) {
                props.load(in);
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Error reading definitions", e);
                }
            }
            definitions = props;
        }
        return definitions;
    }

    @Override
    public Set<CoordinateOperation> findFromDatabase(
            CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS, int limit) {
        Properties definitions = getDefinitions();
        Set<CoordinateOperation> result = Collections.emptySet();
        try {
            result = getCoordinateOperations(sourceCRS, targetCRS, definitions);
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Error creating operation from definitions", e);
            }
        }

        return result;
    }

    private Set<CoordinateOperation> getCoordinateOperations(
            CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS, Properties definitions)
            throws FactoryException {
        String sourceCode = CRS.lookupIdentifier(sourceCRS, false).toString();
        String targetCode = CRS.lookupIdentifier(targetCRS, false).toString();
        String key = sourceCode + "," + targetCode;

        String wkt = definitions.getProperty(key);
        boolean inverse = false;
        if (wkt == null) {
            // Try the inverse direction
            key = targetCode + "," + sourceCode;
            wkt = definitions.getProperty(key);
            inverse = true;
        }

        if (wkt == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("No definition found for key: " + key);
            }
            return Collections.emptySet();
        }

        // Create MathTransform from WKT
        MathTransform mt = null;
        try {
            mt = factories.getMathTransformFactory().createFromWKT(wkt);
        } catch (FactoryException e) {
            // Probably malformed WKT.
            LOGGER.log(Level.WARNING, "Error creating transformation: " + wkt, e);
            return null;
        }

        // Need to create a derived MathTransform that will handle axis order and units
        // as defined in CRS. Had to cast to DefaultMathTransformFactory because
        // createBaseToDerived is not defined in MathTransformFactory interface (GeoAPI).
        DefaultMathTransformFactory mtf = (DefaultMathTransformFactory) factories.getMathTransformFactory();
        MathTransform mt2 = mtf.createBaseToDerived(sourceCRS, mt, targetCRS.getCoordinateSystem());

        // Extract name from the transform, if possible, or use class name.
        String methodName;
        try {
            if (mt instanceof AbstractMathTransform) {
                methodName = ((AbstractMathTransform) mt)
                        .getParameterValues()
                        .getDescriptor()
                        .getName()
                        .getCode();
            } else if (mt instanceof AffineTransform2D) {
                methodName = ((AffineTransform2D) mt)
                        .getParameterValues()
                        .getDescriptor()
                        .getName()
                        .getCode();
            } else {
                methodName = mt.getClass().getSimpleName();
            }
        } catch (NullPointerException e) {
            methodName = mt.getClass().getSimpleName();
        }
        Map<String, String> props = new HashMap<>();
        props.put("name", methodName);

        // Create the OperationMethod
        OperationMethod method =
                new DefaultOperationMethod(props, mt2.getSourceDimensions(), mt2.getTargetDimensions(), null);

        // Finally create CoordinateOperation
        CoordinateOperation coordop = null;
        if (!inverse) { // Direct operation
            props.put("name", sourceCRS + " \u21E8 " + targetCRS);
            coordop = DefaultOperation.create(props, sourceCRS, targetCRS, mt2, method, CoordinateOperation.class);
        } else { // Inverse operation
            try {
                props.put("name", targetCRS + " \u21E8 " + sourceCRS);
                coordop = DefaultOperation.create(
                        props, targetCRS, sourceCRS, mt2.inverse(), method, CoordinateOperation.class);
            } catch (NoninvertibleTransformException e) {
                return null;
            }
        }
        return Set.of(coordop);
    }

    protected abstract URL getDefinitionsURL();
}
