/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.wkt;

import java.text.ParseException;
import java.text.ParsePosition;

import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.operation.Operation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.OperationMethod;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * Parser for {@linkplain MathTransform math transform}
 * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
 * Known Text</cite> (WKT)</A> of math transform.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Remi Eve
 * @author Martin Desruisseaux (IRD)
 * @author Rueben Schulz
 */
public class MathTransformParser extends AbstractParser {
    /**
     * The factory to use for creating math transforms.
     */
    protected final MathTransformFactory mtFactory;

    /**
     * The classification of the last math transform or projection parsed,
     * or {@code null} if none.
     */
    private String classification;

    /**
     * The method for the last math transform passed, or {@code null} if none.
     *
     * @see #getOperationMethod
     */
    private OperationMethod lastMethod;

    /**
     * Constructs a parser using the default set of symbols.
     */
    public MathTransformParser() {
        this(Symbols.DEFAULT);
    }

    /**
     * Constructs a parser using the specified set of symbols
     * and the default factories.
     *
     * @param symbols The symbols for parsing and formatting numbers.
     *
     * @todo Pass hints in argument.
     */
    public MathTransformParser(final Symbols symbols) {
        this(symbols, ReferencingFactoryFinder.getMathTransformFactory(null));
    }

    /**
     * Constructs a parser for the specified set of symbols and factory.
     *
     * @param symbols   The symbols for parsing and formatting numbers.
     * @param mtFactory The factory to use to create {@link MathTransform} objects.
     */
    public MathTransformParser(final Symbols symbols, final MathTransformFactory mtFactory) {
        super(symbols);
        this.mtFactory = mtFactory;
    }

    /**
     * Parses a math transform element.
     *
     * @param  text The text to be parsed.
     * @return The math transform.
     * @throws ParseException if the string can't be parsed.
     */
    public MathTransform parseMathTransform(final String text) throws ParseException {
        final Element element = getTree(text, new ParsePosition(0));
        final MathTransform mt = parseMathTransform(element, true);
        element.close();
        return mt;
    }

    /**
     * Parses the next element in the specified <cite>Well Know Text</cite> (WKT) tree.
     *
     * @param  element The element to be parsed.
     * @return The object.
     * @throws ParseException if the element can't be parsed.
     */
    protected Object parse(final Element element) throws ParseException {
        return parseMathTransform(element, true);
    }

    /**
     * Parses the next element (a {@link MathTransform}) in the specified
     * <cite>Well Know Text</cite> (WKT) tree.
     *
     * @param  element The parent element.
     * @param  required True if parameter is required and false in other case.
     * @return The next element as a {@link MathTransform} object.
     * @throws ParseException if the next element can't be parsed.
     */
    final MathTransform parseMathTransform(final Element element, final boolean required)
            throws ParseException
    {
        lastMethod = null;
        classification = null;
        final Object key = element.peek();
        if (key instanceof Element) {
            final String keyword = ((Element) key).keyword.trim().toUpperCase(symbols.locale);
            if ("PARAM_MT"      .equals(keyword))  return parseParamMT      (element);
            if ("CONCAT_MT"     .equals(keyword))  return parseConcatMT     (element);
            if ("INVERSE_MT"    .equals(keyword))  return parseInverseMT    (element);
            if ("PASSTHROUGH_MT".equals(keyword))  return parsePassThroughMT(element);
        }
        if (required) {
            throw element.parseFailed(null, Errors.format(ErrorKeys.UNKNOW_TYPE_$1, key));
        }
        return null;
    }

    /**
     * Parses a "PARAM_MT" element. This element has the following pattern:
     *
     * <blockquote><code>
     * PARAM_MT["<classification-name>" {,<parameter>}* ]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "PARAM_MT" element as an {@link MathTransform} object.
     * @throws ParseException if the "PARAM_MT" element can't be parsed.
     */
    private MathTransform parseParamMT(final Element parent) throws ParseException {
        final Element element = parent.pullElement("PARAM_MT");
        classification = element.pullString("classification");
        final ParameterValueGroup parameters;
        try {
            parameters = mtFactory.getDefaultParameters(classification);
        } catch (NoSuchIdentifierException exception) {
            throw element.parseFailed(exception, null);
        }
        /*
         * Scan over all PARAMETER["name", value] elements and
         * set the corresponding parameter in the parameter group.
         */
        Element param;
        while ((param=element.pullOptionalElement("PARAMETER")) != null) {
            final String name = param.pullString("name");
            final ParameterValue parameter = parameters.parameter(name);
            final Class type = parameter.getDescriptor().getValueClass();
            if (Integer.class.equals(type)) {
                parameter.setValue(param.pullInteger("value"));
            } else if (Double.class.equals(type)) {
                parameter.setValue(param.pullDouble("value"));
            } else {
                parameter.setValue(param.pullString("value"));
            }
            param.close();
        }
        element.close();
        /*
         * We now have all informations for constructing the math transform. If the factory is
         * a Geotools implementation, we will use a special method that returns the operation
         * method used. Otherwise, we will use the ordinary method and will performs a slower
         * search for the operation method later if the user ask for it.
         */
        final MathTransform transform;
        try {
            transform = mtFactory.createParameterizedTransform(parameters);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
        lastMethod = mtFactory.getLastMethodUsed();
        return transform;
    }

    /**
     * Parses a "INVERSE_MT" element. This element has the following pattern:
     *
     * <blockquote><code>
     * INVERSE_MT[<math transform>]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "INVERSE_MT" element as an {@link MathTransform} object.
     * @throws ParseException if the "INVERSE_MT" element can't be parsed.
     */
    private MathTransform parseInverseMT(final Element parent) throws ParseException {
        final Element element = parent.pullElement("INVERSE_MT");
        try {
            final MathTransform transform;
            transform = parseMathTransform(element, true).inverse();
            element.close();
            return transform;
        }
        catch (NoninvertibleTransformException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "PASSTHROUGH_MT" element. This element has the following pattern:
     *
     * <blockquote><code>
     * PASSTHROUGH_MT[<integer>, <math transform>]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "PASSTHROUGH_MT" element as an {@link MathTransform} object.
     * @throws ParseException if the "PASSTHROUGH_MT" element can't be parsed.
     */
    private MathTransform parsePassThroughMT(final Element parent) throws ParseException {
        final Element           element = parent.pullElement("PASSTHROUGH_MT");
        final int firstAffectedOrdinate = parent.pullInteger("firstAffectedOrdinate");
        final MathTransform   transform = parseMathTransform(element, true);
        element.close();
        try {
            return mtFactory.createPassThroughTransform(firstAffectedOrdinate, transform, 0);
        } catch (FactoryException exception) {
            throw element.parseFailed(exception, null);
        }
    }

    /**
     * Parses a "CONCAT_MT" element. This element has the following pattern:
     *
     * <blockquote><code>
     * CONCAT_MT[<math transform> {,<math transform>}*]
     * </code></blockquote>
     *
     * @param  parent The parent element.
     * @return The "CONCAT_MT" element as an {@link MathTransform} object.
     * @throws ParseException if the "CONCAT_MT" element can't be parsed.
     */
    private MathTransform parseConcatMT(final Element parent) throws ParseException {
        final Element element = parent.pullElement("CONCAT_MT");
        MathTransform transform = parseMathTransform(element, true);
        MathTransform optionalTransform;
        while ((optionalTransform = parseMathTransform(element, false)) != null) {
            try {
                transform = mtFactory.createConcatenatedTransform(transform, optionalTransform);
            } catch (FactoryException exception) {
                throw element.parseFailed(exception, null);
            }
        }
        element.close();
        return transform;
    }

    /**
     * Returns the operation method for the last math transform parsed. This is used by
     * {@link Parser} in order to built {@link org.opengis.referencing.crs.DerivedCRS}.
     */
    final OperationMethod getOperationMethod() {
        if (lastMethod == null) {
            /*
             * Safety in case come MathTransformFactory implementation do not support
             * getLastMethod(). Performs a slower and less robust check as a fallback.
             */
            if (classification != null) {
                for (final OperationMethod method : mtFactory.getAvailableMethods(Operation.class)) {
                    if (AbstractIdentifiedObject.nameMatches(method, classification)) {
                        lastMethod = method;
                        break;
                    }
                }
            }
        }
        return lastMethod;
    }
}
