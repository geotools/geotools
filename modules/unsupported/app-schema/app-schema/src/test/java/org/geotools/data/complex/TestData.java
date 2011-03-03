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

package org.geotools.data.complex;

import java.util.LinkedList;
import java.util.List;

import org.geotools.data.complex.filter.XPath;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.TypeBuilder;
import org.geotools.feature.Types;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class TestData {

    public final static Name WATERSAMPLE_TYPENAME = Types.typeName("wq_ir_results");

    private TestData() {
    }

    /**
     * Complex type:
     * <ul>
     * wq_plus
     * <li>sitename
     * <li>anzlic_no
     * <li>project_no
     * <li>measurement (0..*)
     * <ul>
     * <li>determinand_description</li>
     * <li>result</li>
     * </ul>
     * </li>
     * <li>location
     * </ul>
     * 
     * @return
     */
    public static FeatureType createComplexWaterQualityType() {
        FeatureTypeFactory tfac = new UniqueNameFeatureTypeFactoryImpl();
        TypeBuilder builder = new TypeBuilder(tfac);

        FeatureType wq_plusType;

        AttributeType detdesc = builder.name("determinand_description").bind(String.class)
                .attribute();
        AttributeType result = builder.name("result").bind(Float.class).attribute();

        builder.setName("measurement");
        builder.addAttribute("determinand_description", detdesc);
        builder.addAttribute("result", result);

        ComplexType MEASUREMENT = builder.complex();

        /*
         * <li>sitename <li>anzlic_no <li>project_no <li>location <li>measurement
         * (0..*) <ul> <li>determinand_description</li> <li>result</li>
         * </ul>
         */

        AttributeType sitename = builder.name("sitename").bind(String.class).attribute();
        AttributeType anzlic_no = builder.name("anzlic_no").bind(String.class).attribute();
        AttributeType project_no = builder.name("project_no").bind(String.class).attribute();
        AttributeType location = builder.name("location").bind(Point.class).geometry();

        builder.setName("wq_plus");
        builder.addAttribute("sitename", sitename);
        builder.addAttribute("anzlic_no", anzlic_no);
        builder.addAttribute("project_no", project_no);

        builder.cardinality(0, Integer.MAX_VALUE);
        builder.addAttribute("measurement", MEASUREMENT);

        builder.cardinality(1, 1);
        builder.addAttribute("location", location);

        wq_plusType = builder.feature();

        return wq_plusType;
    }

    public static FeatureType createComplexWaterSampleType() {
        FeatureTypeFactory tfac = new UniqueNameFeatureTypeFactoryImpl();
        TypeBuilder builder = new TypeBuilder(tfac);

        FeatureType sampleType;

        AttributeType parameter = builder.name("parameter").bind(String.class).attribute();
        AttributeType value = builder.name("value").bind(Double.class).attribute();

        builder.setName("measurement");
        builder.addAttribute("parameter", parameter);
        builder.addAttribute("value", value);
        ComplexType MEASUREMENT = builder.complex();

        builder.setName("sample");
        builder.cardinality(0, Integer.MAX_VALUE);
        builder.addAttribute("measurement", MEASUREMENT);

        sampleType = builder.feature();

        return sampleType;
    }

    /**
     * 
     * <pre>
     * </pre>
     * 
     * @param targetFeature
     * @return
     * @throws Exception
     */
    public static List/* <AttributeMapping> */createMappingsColumnsAndValues(
            AttributeDescriptor targetFeature) throws Exception {

        List mappings = new LinkedList();
        AttributeMapping attMapping;
        Expression source;
        String target;

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        source = ff.literal("ph");
        target = "sample/measurement[1]/parameter";
        // empty nssupport as the test properties have no namespace
        NamespaceSupport namespaces = new NamespaceSupport();
        attMapping = new AttributeMapping(null, source, XPath.steps(targetFeature, target, namespaces));
        mappings.add(attMapping);

        source = ff.property("ph");
        target = "sample/measurement[1]/value";
        attMapping = new AttributeMapping(null, source, XPath.steps(targetFeature, target, namespaces));
        mappings.add(attMapping);

        source = ff.literal("temp");
        target = "sample/measurement[2]/parameter";
        attMapping = new AttributeMapping(null, source, XPath.steps(targetFeature, target, namespaces));
        mappings.add(attMapping);

        source = ff.property("temp");
        target = "sample/measurement[2]/value";
        attMapping = new AttributeMapping(null, source, XPath.steps(targetFeature, target, namespaces));
        mappings.add(attMapping);

        source = ff.literal("turbidity");
        target = "sample/measurement[3]/parameter";
        attMapping = new AttributeMapping(null, source, XPath.steps(targetFeature, target, namespaces));
        mappings.add(attMapping);

        source = ff.property("turbidity");
        target = "sample/measurement[3]/value";
        attMapping = new AttributeMapping(null, source, XPath.steps(targetFeature, target, namespaces));
        mappings.add(attMapping);

        return mappings;
    }

    /**
     * <p>
     * Flat FeatureType:
     * <ul>
     * wq_ir_results
     * <li> station_no</li>
     * <li> sitename</li>
     * <li> anzlic_no</li>
     * <li> project_no</li>
     * <li> id</li>
     * <li> sample_collection_date</li>
     * <li> determinand_description</li>
     * <li> results_value</li>
     * <li> location</li>
     * </ul>
     * </p>
     * <p>
     * Complex type:
     * <ul>
     * wq_plus
     * <li>sitename
     * <li>anzlic_no
     * <li>project_no
     * <li>location
     * <li>measurement (0..*)
     * <ul>
     * <li>determinand_description</li>
     * <li>result</li>
     * </ul>
     * </li>
     * </ul>
     * </p>
     * <p>
     * Mappings definition:
     * 
     * <pre>
     *       &lt;strong&gt;wq_ir_results&lt;/strong&gt;			&lt;strong&gt;wq_plus&lt;/strong&gt;
     *        station_no		--&gt;	wq_plus@id
     *        sitename		--&gt;	sitename	
     *        anzlic_no		--&gt;	anzlic_no
     *        project_no		--&gt;	project_no
     *        id		--&gt;	measurement/@id
     *        sample_collection_date--&gt; [not used]
     *        determinand_description--&gt;measurement/determinand_description	
     *        results_value		--&gt;measurement/result
     *        location		--&gt;location
     * </pre>
     * 
     * </p>
     * 
     * @param simpleStore
     * @return
     * @throws Exception
     */
    public static FeatureTypeMapping createMappingsGroupByStation(MemoryDataStore simpleStore)
            throws Exception {
        Name sourceTypeName = WATERSAMPLE_TYPENAME;
        final SimpleFeatureSource wsSource = simpleStore.getFeatureSource(sourceTypeName);

        FeatureType targetType = createComplexWaterQualityType();
        FeatureTypeFactory tf = new UniqueNameFeatureTypeFactoryImpl();
        AttributeDescriptor targetFeature = tf.createAttributeDescriptor(targetType, targetType
                .getName(), 0, Integer.MAX_VALUE, true, null);

        List mappings = new LinkedList();
        Expression id;
        Expression source;
        String target;

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        id = ff.property("station_no");
        source = Expression.NIL;
        target = "wq_plus";
        NamespaceSupport namespaces = new NamespaceSupport();
        mappings.add(new AttributeMapping(id, source, XPath
                .steps(targetFeature, target, namespaces)));

        source = ff.property("sitename");
        target = "wq_plus/sitename";
        mappings.add(new AttributeMapping(null, source, XPath.steps(targetFeature, target,
                namespaces)));

        source = ff.property("anzlic_no");
        target = "wq_plus/anzlic_no";
        mappings.add(new AttributeMapping(null, source, XPath.steps(targetFeature, target,
                namespaces)));

        source = ff.property("project_no");
        target = "wq_plus/project_no";
        mappings.add(new AttributeMapping(null, source, XPath.steps(targetFeature, target,
                namespaces)));

        id = ff.property("id[1]");
        source = null;
        target = "wq_plus/measurement";
        mappings.add(new AttributeMapping(id, source, XPath
                .steps(targetFeature, target, namespaces), null, true, null));

        source = ff.property("determinand_description");
        target = "wq_plus/measurement/determinand_description";
        mappings.add(new AttributeMapping(null, source, XPath.steps(targetFeature, target,
                namespaces)));

        source = ff.property("results_value");
        target = "wq_plus/measurement/result";
        mappings.add(new AttributeMapping(null, source, XPath.steps(targetFeature, target,
                namespaces)));

        source = ff.property("location");
        target = "wq_plus/location";
        mappings.add(new AttributeMapping(null, source, XPath.steps(targetFeature, target,
                namespaces)));

        return new FeatureTypeMapping(wsSource, targetFeature, mappings,
                namespaces);

    }

    /**
     * Creates a flat FeatureType <code>wq_ir_results</code> with a structure
     * like the following, from which a complex one should be constructed
     * grouping by station_no attribute.
     * <p>
     * Following this sample schema, a total of 10 unique station_no identifiers
     * will be created, and for each one, a total of N desagregate rows with the
     * same station_no, where N goes from 1 to 10. So for the first station_no
     * there will be just one occurrence and the last one will have 10.
     * </p>
     * <p>
     * <table>
     * <tr>
     * <td> station_no (string) </td>
     * <td> sitename (string)</td>
     * <td> anzlic_no (string)</td>
     * <td> project_no (string)</td>
     * <td> id (string)</td>
     * <td> sample_collection_date (string)</td>
     * <td> determinand_description (string)</td>
     * <td> results_value (float)</td>
     * <td> location (Point)</td>
     * </tr>
     * <tr>
     * <td> station_1 </td>
     * <td> sitename_1 </td>
     * <td> anzlic_no_1 </td>
     * <td> project_no_1 </td>
     * <td> id_1_1 </td>
     * <td> sample_collection_date_1_1 </td>
     * <td> determinand_description_1_1 </td>
     * <td> 1.1 </td>
     * <td> POINT(1, 1) </td>
     * </tr>
     * <tr>
     * <td> station_2 </td>
     * <td> sitename_2 </td>
     * <td> anzlic_no_2 </td>
     * <td> project_no_2 </td>
     * <td> id_2_1 </td>
     * <td> sample_collection_date_2_1 </td>
     * <td> determinand_description_2_1 </td>
     * <td> 2.1 </td>
     * <td> POINT(2, 2) </td>
     * </tr>
     * <tr>
     * <td> station_2 </td>
     * <td> sitename_2 </td>
     * <td> anzlic_no_2 </td>
     * <td> project_no_2 </td>
     * <td> id_2_2 </td>
     * <td> sample_collection_date_2_2 </td>
     * <td> determinand_description_2_2 </td>
     * <td> 2.2 </td>
     * <td> POINT(2, 2) </td>
     * </tr>
     * <tr>
     * <td colspan="9">...</td>
     * </tr>
     * <tr>
     * <td> station_10 </td>
     * <td> sitename_10 </td>
     * <td> anzlic_no_10 </td>
     * <td> project_no_10 </td>
     * <td> id_10_10 </td>
     * <td> sample_collection_date_10_9 </td>
     * <td> determinand_description_10_9 </td>
     * <td> 10.10 </td>
     * <td> POINT(10, 10) </td>
     * </tr>
     * <tr>
     * <td> station_10 </td>
     * <td> sitename_10 </td>
     * <td> anzlic_no_10 </td>
     * <td> project_no_10 </td>
     * <td> id_10_10 </td>
     * <td> sample_collection_date_10_10 </td>
     * <td> determinand_description_10_10 </td>
     * <td> 10.10 </td>
     * <td> POINT(10, 10) </td>
     * </tr>
     * </table>
     * </p>
     * 
     * @return
     * @throws Exception
     */
    public static MemoryDataStore createDenormalizedWaterQualityResults() throws Exception {
        MemoryDataStore dataStore = new MemoryDataStore();
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

        builder.setName(TestData.WATERSAMPLE_TYPENAME.getLocalPart());

        builder.add("station_no", String.class);
        builder.add("sitename", String.class);
        builder.add("anzlic_no", String.class);
        builder.add("project_no", String.class);
        builder.add("id", String.class);
        builder.add("sample_collection_date", String.class);
        builder.add("determinand_description", String.class);
        builder.add("results_value", Float.class);
        builder.add("location", Point.class);

        SimpleFeatureType type = builder.buildFeatureType();

        dataStore.createSchema(type);

        final int NUM_STATIONS = 10;
        GeometryFactory gf = new GeometryFactory();

        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
        
        for (int groupValue = 1; groupValue <= NUM_STATIONS; groupValue++) {

            for (int measurement = 1; measurement <= groupValue; measurement++) {
                String fid = type.getName().getLocalPart() + "." + groupValue + "." + measurement;
                
                fb.add("station_no." + groupValue);
                fb.add("sitename" + groupValue);
                fb.add("anzlic_no" + groupValue);
                fb.add("project_no" + groupValue);

                String sufix = "_" + groupValue + "_" + measurement;
                fb.add("id" + sufix);
                fb.add("sample_collection_date" + sufix);
                fb.add("determinand_description" + sufix);
                fb.add(new Float(groupValue + "." + measurement));

                fb.add(gf.createPoint(new Coordinate(groupValue, groupValue)));

                SimpleFeature f = fb.buildFeature(fid);
                dataStore.addFeature(f);
            }
        }
        return dataStore;
    }

}
