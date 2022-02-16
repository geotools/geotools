/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2007-2011 Refractions Research
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
package org.geotools.process.vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Used to transform a feature collection as defined using a series of expressions.
 *
 * <p>The definition of the output feature type can be provided as a {@link Definition} data
 * structure or using a simple string format:
 *
 * <pre>
 * the_geom=the_geom
 * name=name
 * area=area( the_geom )
 * </pre>
 *
 * Attribute definitions can be delimited by line breaks and/or by semicolons.
 *
 * <p>This is a very flexible process which can be used to:
 *
 * <ul>
 *   <li>Change the order of attributes - resulting in a new feature type:
 *       <pre>
 * INPUT Schema          DEFINITION                     OUTPUT Schema
 * the_geom: Polygon     the_geom                       the_geom: Polygon
 * name: String          id                             id: Long
 * id: Long              name                           name: String
 * description: String   description                    description: String
 * </pre>
 *   <li>Rename or remove attributes - resulting in a new feature type:
 *       <pre>
 * INPUT Schema          DEFINITION                     OUTPUT Schema
 * the_geom: Polygon     the_geom                       the_geom: Polygon
 * name: String          id_code=id                     id_code: Long
 * id: Long              summary=description            summary: String
 * description: String
 * </pre>
 *   <li>Process geometry - using functions like "the_geom=simplify( the_geom, 2.0 )" or
 *       "the_geom=centriod( the_geom )":
 *       <pre>
 * INPUT Schema          DEFINITION                     OUTPUT Schema
 * the_geom: Polygon     the_geom=centriod(the_geom)    the_geom: Point
 * name: String          name                           name: String
 * id: Long              id                             id: Long
 * description: String   description                    description: String
 * </pre>
 *   <li>Generate additional attributes using the form: area=area( the_geom ):
 *       <pre>
 * INPUT Schema          DEFINITION                     OUTPUT Schema
 * the_geom: Polygon     the_geom=centriod(the_geom)    the_geom: Point
 * name: String          name                           name: String
 * id: Long              id                             id: Long
 * description: String   description                    description: String
 *                       area=area( the_geom)           area: Double
 *                       text=concatenate(name,'-',id)  text: String
 * </pre>
 * </ul>
 *
 * <p>This is a port of the uDig "reshape" operation to the GeoTools process framework.
 *
 * @author Jody Garnett (LISAsoft)
 */
@DescribeProcess(
        title = "Transform",
        description =
                "Computes a new feature collection from the input one by renaming, deleting, and computing new attributes.  Attribute values are specified as ECQL expressions in the form name=expression.")
public class TransformProcess implements VectorProcess {
    /**
     * Definition of an attribute used during transform
     *
     * <p>Note this definition is terse as we are gathering the details from the origional
     * FeatureType.
     *
     * @author jody
     */
    public static class Definition {
        /** Name of the AttribtueDescriptor to generate */
        public String name;

        /** Expression used to generate the target value; most simply a PropertyName */
        public Expression expression;

        /** Class binding (if known) */
        public Class<?> binding;
    }

    @DescribeResult(name = "result", description = "Transformed feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features,
            @DescribeParameter(
                            name = "transform",
                            description =
                                    "The transform specification, as a list of specifiers of the form name=expression, delimited by newlines or semicolons.")
                    String transform)
            throws ProcessException {
        if (transform == null) {
            return features; // no change
        }
        List<Definition> list = toDefinition(transform);
        return executeList(features, list);
    }

    @DescribeResult(name = "result", description = "Transformed feature collection")
    public SimpleFeatureCollection executeList(
            @DescribeParameter(name = "features", description = "Input feature collection")
                    SimpleFeatureCollection features,
            @DescribeParameter(
                            name = "transform",
                            description = "List of Definitions for the output feature attributes")
                    List<Definition> transform)
            throws ProcessException {
        if (transform == null) {
            return features; // no change
        }
        return new ReshapeFeatureCollection(features, transform);
    }

    //
    // helper methods made static for ease of JUnit testing
    //

    /**
     * Parse out a list of {@link Definition} from the provided text description.
     *
     * <p>The format expected here is one definition per line; using the format
     * "name=...expression..".
     *
     * @return List of definition
     */
    public static List<Definition> toDefinition(String definition) {
        List<Definition> list = new ArrayList<>();
        HashSet<String> check = new HashSet<>();

        // clean up cross platform differences of line feed
        String[] defs = splitDefinitions(definition);

        for (String line : defs) {
            int mark = line.indexOf("=");
            if (mark != -1) {
                String name = line.substring(0, mark).trim();
                String expressionDefinition = line.substring(mark + 1).trim();

                if (check.contains(name)) {
                    throw new IllegalArgumentException(
                            "Attribute " + name + " defined more than once");
                }
                Expression expression;
                try {
                    expression = ECQL.toExpression(expressionDefinition);
                } catch (CQLException e) {
                    throw new IllegalArgumentException(
                            "Unable to parse expression "
                                    + name
                                    + "="
                                    + expressionDefinition
                                    + " "
                                    + e,
                            e);
                }
                Definition def = new Definition();
                def.name = name;
                def.expression = expression;
                list.add(def);
                check.add(name); // to catch duplicates!
            }
        }
        return list;
    }

    /**
     * Splits single-string definition list into a list of definitions. Either line breaks or ';'
     * can be used as definition delimiters.
     *
     * @param defList the definition list string
     * @return the separate definitions
     */
    private static String[] splitDefinitions(String defList) {
        // clean up cross platform differences of linefeed
        // convert explicit delimiter to linefeed
        String defListLF = defList.replaceAll(";", "\n");

        // split on linefeed
        return defListLF.split("\n");
    }

    public static SimpleFeatureType toReShapeFeatureType(
            SimpleFeatureCollection delegate, List<Definition> definitionList) {

        SimpleFeature sample = null;
        try (SimpleFeatureIterator iterator = delegate.features()) {
            if (iterator.hasNext()) {
                sample = iterator.next();
            }
        }

        SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
        SimpleFeatureType origional = delegate.getSchema();

        for (Definition def : definitionList) {
            String name = def.name;
            Expression expression = def.expression;

            Object value = null;
            if (sample != null) {
                value = expression.evaluate(sample);
            }
            Class<?> binding = def.binding; // make use of any default binding hint provided by user
            if (value == null) {
                if (expression instanceof PropertyName) {
                    AttributeDescriptor descriptor = origional.getDescriptor(name);
                    AttributeType attributeType = descriptor.getType();
                    binding = attributeType.getBinding();
                }
            } else {
                binding = value.getClass();
            }

            if (binding == null) {
                // note we could consider scanning through additional samples until we get a non
                // null hit
                throw new IllegalArgumentException("Unable to determine type for " + name);
            }

            if (Geometry.class.isAssignableFrom(binding)) {
                CoordinateReferenceSystem crs;
                AttributeType originalAttributeType = origional.getType(name);
                if (originalAttributeType != null
                        && originalAttributeType instanceof GeometryType) {
                    GeometryType geometryType = (GeometryType) originalAttributeType;
                    crs = geometryType.getCoordinateReferenceSystem();
                } else {
                    crs = origional.getCoordinateReferenceSystem();
                }
                build.crs(crs);
                build.add(name, binding);
            } else {
                build.add(name, binding);
            }
        }
        build.setName(origional.getTypeName());
        return build.buildFeatureType();
    }

    //
    // helper classes responsible for most of the work
    //

    /**
     * ReshapeFeatureCollection obtaining feature type by processing the list of definitions against
     * the origional delegate feature collection.
     *
     * @author jody
     */
    static class ReshapeFeatureCollection extends DecoratingSimpleFeatureCollection {
        List<Definition> definition;
        SimpleFeatureType schema;

        public ReshapeFeatureCollection(
                SimpleFeatureCollection delegate, List<Definition> definition) {
            super(delegate);
            this.definition = definition;
            this.schema = toReShapeFeatureType(delegate, definition);
        }

        @Override
        public SimpleFeatureType getSchema() {
            return schema;
        }

        @Override
        public SimpleFeatureIterator features() {
            return new ReshapeFeatureIterator(delegate.features(), definition, schema);
        }
    }
    /**
     * Process one feature at time; obtaining values by evaulating the provided list of definitions.
     *
     * @author jody
     */
    static class ReshapeFeatureIterator implements SimpleFeatureIterator {
        SimpleFeatureIterator delegate;

        List<Definition> definition;

        SimpleFeatureBuilder fb;

        public ReshapeFeatureIterator(
                SimpleFeatureIterator delegate,
                List<Definition> definition,
                SimpleFeatureType schema) {
            this.delegate = delegate;
            this.definition = definition;
            fb = new SimpleFeatureBuilder(schema);
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            SimpleFeature feature = delegate.next();

            for (Definition def : definition) {
                Object value = def.expression.evaluate(feature);
                fb.add(value);
            }
            SimpleFeature created = fb.buildFeature(feature.getID());
            return created;
        }
    }
}
