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
package org.geotools.process.feature.gs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;
import org.geotools.process.gs.WrappingIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

/**
 * Used to reshape a feature collection as defined using a series of expressions.
 * <p>
 * This is a port of the uDig Reshape operation to the GeoTools process framework.
 * <p>
 * This is a very flexable process which can be used to:
 * <ul>
 * <li>Change the order of attribtues - resulting in a new feature type</li>
 * <li>Rename attribtues - resulting in a new feature type</li>
 * <li>Process geometry - using functions like "the_geom=simplify( the_geom, 2.0 )" or "the_geom=centriod( the_geom )"</li>
 * <li>Generate additional attribtues using the form: area=area( the_geom )</li>
 * </ul>
 * <p>
 * The definition of the output feature type can be provided as a data structure or using a simple string format:
 * 
 * <pre>
 * the_geom=the_geom
 * name=name
 * area=area( the_geom )
 * </pre>
 * 
 * @author Jody Garnett (LISAsoft)
 * 
 * @source $URL$
 */
@DescribeProcess(title = "simplify", description = "Simplifies the geometry")
public class ReShapeProcess implements GSProcess {
    /**
     * Definition of an attribute used during reshape.
     * <p>
     * Note this definition is terse as we are gathering the details from the origional FeatureType.
     * 
     * @author jody
     */
    public static class Definition {
        /** Name of the AttribtueDescriptor to generate */
        public String name;

        /** Expression used to generate the target calue; most simply a PropertyName */
        public Expression expression;

        /** Class binding (if known) */
        public Class<?> binding;
    }
    @DescribeResult(name = "result", description = "rehaped feature collection")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "features", description = "The feature collection to rehaped") SimpleFeatureCollection features,
            @DescribeParameter(name = "definition", description = "Definition of output feature type using attribute=expr pairs") String definition)
            throws ProcessException {
        if (definition == null) {
            return features; // no change
        }
        List<Definition> list = toDefinition(definition);
        return executeList(features, list);
    }

    @DescribeResult(name = "result", description = "rehaped feature collection")
    public SimpleFeatureCollection executeList(
            @DescribeParameter(name = "features", description = "The feature collection to rehaped") SimpleFeatureCollection features,
            @DescribeParameter(name = "list", description = "List of Definitions for the output feature type") List<Definition> list)
            throws ProcessException {
        if (list == null) {
            return features; // no change
        }
        return new ReshapeFeatureCollection(features, list);
    }
    

    // 
    // helper methods made static for ease of JUnit testing
    //
    
    /**
     * Parse out a list of {@link Definition} from the provided text description.
     * <p>
     * The format expected here is one definition per line; using the format "name=...expression..".
     * 
     * @param definition
     * @return List of definition
     */
    public static List<Definition> toDefinition(String definition) {
        List<Definition> list = new ArrayList<Definition>();
        HashSet<String> check = new HashSet<String>();

        // clean up cross platform differences of line feed
        definition = definition.replaceAll("\r", "\n").replaceAll("[\n\r][\n\r]", "\n");

        for (String line : definition.split("\n")) {
            int mark = line.indexOf("=");
            if (mark != -1) {
                String name = line.substring(0, mark).trim();
                String expressionDefinition = line.substring(mark + 1).trim();

                if (check.contains(name)) {
                    throw new IllegalArgumentException("ReShape definition " + name
                            + " already in use");
                }
                Expression expression;
                try {
                    expression = ECQL.toExpression(expressionDefinition);
                } catch (CQLException e) {
                    throw new IllegalArgumentException("Reshape unable to parse " + name + "="
                            + expressionDefinition + " " + e, e);
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

    public static SimpleFeatureType toReShapeFeatureType(SimpleFeatureCollection delegate,
            List<Definition> definitionList) {
        
        SimpleFeature sample = null;
        SimpleFeatureIterator iterator = delegate.features();
        try {
            if( iterator.hasNext() ){
                sample = iterator.next(); 
            }
        }
        finally {
            iterator.close(); // good bye
        }
        
        SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
        SimpleFeatureType origional = delegate.getSchema();
        
        for( Definition def : definitionList ){
            String name = def.name;
            Expression expression = def.expression;
            
            Object value = null;
            if( sample != null ){
                value = expression.evaluate(sample);
            }
            Class<?> binding = def.binding; // make use of any default binding hint provided by user
            if( value == null){
                if(  expression instanceof PropertyName){
                    PropertyName propertyName = (PropertyName)expression;
                    String path = propertyName.getPropertyName();
                    AttributeDescriptor descriptor = origional.getDescriptor( name );
                    AttributeType attributeType = descriptor.getType();
                    binding = attributeType.getBinding();
                }
            } else {
                binding = value.getClass();
            }
            
            if( binding ==null ){
                // note we could consider scanning through additional samples until we get a non null hit
                throw new IllegalArgumentException("Unable to determine type for "+name);
            }
            
            if( Geometry.class.isAssignableFrom( binding )){
                CoordinateReferenceSystem crs;
                AttributeType originalAttributeType = origional.getType(name);
                if( originalAttributeType != null && originalAttributeType instanceof GeometryType ) {
                    GeometryType geometryType = (GeometryType)originalAttributeType;
                    crs = geometryType.getCoordinateReferenceSystem();
                } else {
                    crs = origional.getCoordinateReferenceSystem();
                }
                build.crs(crs);
                build.add(name, binding);
            }
            else {
                build.add(name, binding);
            }
        }
        build.setName( origional.getTypeName() );
        return build.buildFeatureType();
    }
    
    //
    // helper classes responsible for most of the work
    //
    
    /**
     * ReshapeFeatureCollection obtaining feature type by processing the list of definitions
     * against the origional delegate feature collection.
     * @author jody
     */
    static class ReshapeFeatureCollection extends DecoratingSimpleFeatureCollection {
        List<Definition> definition;
        SimpleFeatureType schema;

        public ReshapeFeatureCollection(SimpleFeatureCollection delegate, List<Definition> definition) {
            super(delegate);
            this.definition = definition;
            this.schema = toReShapeFeatureType( delegate, definition );
        }
        @Override
        public SimpleFeatureType getSchema() {
            return schema;
        }
        
        @Override
        public SimpleFeatureIterator features() {
            return new ReshapeFeatureIterator(delegate.features(), definition, schema);
        }

        @Override
        public Iterator<SimpleFeature> iterator() {
            return new WrappingIterator(features());
        }

        @Override
        public void close(Iterator<SimpleFeature> close) {
            if (close instanceof WrappingIterator) {
                ((WrappingIterator) close).close();
            }
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

        public ReshapeFeatureIterator(SimpleFeatureIterator delegate, List<Definition> definition,
                SimpleFeatureType schema) {
            this.delegate = delegate;
            this.definition = definition;
            fb = new SimpleFeatureBuilder(schema);
        }

        
        public void close() {
            delegate.close();
        }

        public boolean hasNext() {
            return delegate.hasNext();
        }

        public SimpleFeature next() throws NoSuchElementException {
            SimpleFeature feature = delegate.next();
            
            for( Definition def : definition ){
                Object value = def.expression.evaluate(feature);
                fb.add( value );
            }
            SimpleFeature created = fb.buildFeature(feature.getID());
            return created;
        }

    }

}
