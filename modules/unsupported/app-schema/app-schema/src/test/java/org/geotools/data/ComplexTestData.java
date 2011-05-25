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

package org.geotools.data;

import java.util.Set;

import junit.framework.TestCase;

import org.geotools.feature.TypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public abstract class ComplexTestData extends TestCase {

    public static final String NSURI = "http://online.socialchange.net.au";

    public static final String GML_NSURI = "http://www.opengis.net/gml";

    /**
     * <pre>
     * <code>
     *           	 FeatureType[
     *           		name = wq_plus
     *           	 	identified = true
     *           	 	super = Feature
     *           	 	abstract = false
     *           	 	binding = Feature.class
     *           	 	restrictions = EMPTY_SET
     *           	 	nillable = false
     *           	 	defaultGeometry = #location
     *           	 	descriptor = OrderedDescriptor(1, 1)[
     *           	 		sequence = List[
     *           	 			AttributeDescriptor(1, 1)[
     *           	 					type = AttributeType[
     *           	 					name = sitename
     *           	 					identified = false
     *           	 					super = null 
     *           	 					abstract = false
     *           	 					binding = String.class
     *           	 					restrictions = EMPTY_SET
     *           	 					nillable = false
     *           	 				]
     *           	 			],
     *           	 			AttributeDescriptor(0, 1)[
     *           	 				type = AttributeType[
     *           						name = anzlic_no
     *           						identified = false
     *           						super = null 
     *           						abstract = false
     *           						binding = String.class
     *           						restrictions = EMPTY_SET
     *           						nillable = true
     *           	 				]
     *           	 			],
     *           	 			AttributeDescriptor(0, 1)[
     *           	 				type = GeometryAttribute[
     *           	 					name = location
     *           	 					identified = false
     *           	 					super = HERE WE NEED TO REFER TO  gml:LocationPropertyType
     *           	 					abstract = false
     *           	 					binding = Point.class
     *           	 					restrictions = EMPTY_SET
     *           	 					nillable = true
     *           	 				]
     *           	 			],
     *           	 			AttributeDescriptor (0, Integer.MAX_VALUE)[
     *           	 				type = ComplexType[
     *           	 					name = measurement
     *           	 					identified = true
     *           	 					super = null 
     *           	 					abstract = false
     *           	 					binding = null
     *           	 					restrictions = EMPTY_SET
     *           	 					nillable = true
     *           	 					descriptor = OrderedDescriptor(0, Integer.MAX_VALUE)[
     *           	 						AttributeDescriptor(1, 1)[
     *           	 							type = AttributeType[
     *           	 								name = determinand_description
     *           	 								identified = false
     *           	 								super = null 
     *           	 								abstract = false
     *           	 								binding = String.class
     *           	 								restrictions = EMPTY_SET
     *           	 								nillable = false
     *           	 							]
     *           	 						],
     *           	 						AttributeDescriptor(1, 1)[
     *           	 							type = AttributeType[
     *           	 								name = result
     *           	 								identified = false
     *           	 								super = null 
     *           	 								abstract = false
     *           	 								binding = String.class
     *           	 								restrictions = EMPTY_SET
     *           	 								nillable = false
     *           	 							]
     *           	 						]
     *           	 					]//OrderedDescriptor
     *           	 				] //ComplexType
     *           	 			], //measurement
     *           	 			AttributeDescriptor(0, 1)[
     *           	 				type = AttributeType[
     *           	 					name = project_no
     *           	 					identified = false
     *           	 					super = null 
     *           	 					abstract = false
     *           	 					binding = String.class
     *           	 					restrictions = EMPTY_SET
     *           	 					nillable = false
     *           	 				]
     *           	 			]
     *           	 		]
     *           	 	]
     *           	 ]	 
     * </code>
     * </pre>
     * 
     * @param typeFactory
     * @param descFactory
     * @return
     */
    public static FeatureType createExample01MultiValuedComplexProperty(
            FeatureTypeFactory typeFactory) {
        FeatureType wqPlusType;

        TypeBuilder builder = new TypeBuilder(typeFactory);
        builder.setNamespaceURI(NSURI);

        builder.setName("sitename");
        builder.setBinding(String.class);
        AttributeType SITENAME = builder.attribute();

        builder.setName("anzlic_noType");
        builder.setBinding(String.class);
        AttributeType ANZLIC_NO = builder.attribute();

        builder.setName("locationType");
        builder.setBinding(Point.class);
        GeometryType LOCATION = builder.geometry();

        // build complex attribute
        AttributeType MEASUREMENT = createMeasurementType(typeFactory);

        builder.setName("project_noType");
        builder.setBinding(String.class);
        AttributeType PROJECT_NO = builder.attribute();

        builder.setName("wq_plus");

        builder.cardinality(1, 1);
        builder.addAttribute("sitename", SITENAME);

        builder.cardinality(0, 1);
        builder.addAttribute("anzlic_no", ANZLIC_NO);

        builder.cardinality(0, 1);
        builder.addAttribute("location", LOCATION);

        builder.cardinality(0, Integer.MAX_VALUE);
        builder.addAttribute("measurement", MEASUREMENT);

        builder.cardinality(0, 1);
        builder.addAttribute("project_no", PROJECT_NO);

        wqPlusType = builder.feature();

        return wqPlusType;
    }

    /**
     * A feature type that has various multi-valued properties.
     * <p>
     * Multi valued properties: meassurement(0:unbounded), sitename(1:unbounded).
     * 
     * <pre>
     * <code>
     * </code>
     * </pre>
     * 
     * </p>
     * 
     * @param typeFactory
     * @param descFactory
     * @return
     */
    public static FeatureType createExample02MultipleMultivalued(FeatureTypeFactory typeFactory) {

        TypeBuilder builder = new TypeBuilder(typeFactory);
        builder.setNamespaceURI(NSURI);

        AttributeType measurement = createMeasurementType(typeFactory);
        AttributeType the_geom = builder.name("the_geom").bind(Geometry.class).attribute();
        AttributeType sitename = builder.name("sitename").bind(String.class).attribute();

        builder.cardinality(0, Integer.MAX_VALUE);
        builder.addAttribute("measurement", measurement);

        builder.cardinality(1, 1);
        builder.addAttribute("the_geom", the_geom);

        builder.nillable(true);
        builder.cardinality(1, Integer.MAX_VALUE);
        builder.addAttribute("sitename", sitename);

        builder.setName("wq_plus");
        FeatureType wqPlusType = builder.feature();
        return wqPlusType;
    }

    /**
     * A feature may have multiple geometries
     * 
     * <pre><code>
     *    	 &lt;xs:complexType name=&quot;measurement_Type&quot;&gt;
     *    	 &lt;xs:sequence&gt;
     *    	 &lt;xs:element name=&quot;determinand_description&quot; type=&quot;xs:string&quot;/&gt;
     *    	 &lt;xs:element name=&quot;result&quot; type=&quot;xs:string&quot;/&gt;
     *    	 &lt;/xs:sequence&gt;            
     *    	 &lt;xs:attribute ref=&quot;gml:id&quot; use=&quot;optional&quot;/&gt;
     *    	 &lt;/xs:complexType&gt; 
     *    	 
     *    	 &lt;xs:complexType name=&quot;wq_plus_Type&quot; xmlns:xs=&quot;http://www.w3.org/2001/XMLSchema&quot;&gt;
     *    	 &lt;xs:complexContent&gt;
     *    	 &lt;xs:extension base=&quot;gml:AbstractFeatureType&quot;&gt;
     *    	 &lt;xs:sequence&gt;
     *    	 &lt;xs:element name=&quot;measurement&quot; maxOccurs=&quot;unbounded&quot; type=&quot;sco:measurement_Type&quot;/&gt;
     *    	 
     *    	 &lt;xs:element name=&quot;location&quot; type=&quot;gml:LocationPropertyType&quot;/&gt;
     *    	 &lt;xs:element name=&quot;nearestSlimePit&quot; type=&quot;gml:PointPropertyType&quot;/&gt; 
     *    	 &lt;xs:element name=&quot;sitename&quot; maxOccurs=&quot;unbounded&quot; nillable=&quot;false&quot; type=&quot;xs:string&quot; /&gt;
     *    	 &lt;/xs:sequence&gt;
     *    	 &lt;/xs:extension&gt;
     *    	 &lt;/xs:complexContent&gt;
     *    	 &lt;/xs:complexType&gt;
     *    	 
     *    	 &lt;xs:element name='wq_plus' type='sco:wq_plus_Type' substitutionGroup=&quot;gml:_Feature&quot; /&gt;
     * </code></pre>
     * 
     * @param typeFactory
     * @param descFactory
     * @return
     */
    public static FeatureType createExample03MultipleGeometries(FeatureTypeFactory typeFactory) {
        TypeBuilder builder = new TypeBuilder(typeFactory);
        builder.setNamespaceURI(NSURI);

        AttributeType measurement = createMeasurementType(typeFactory);

        AttributeType gmlLocationAssociation = createGmlLocation(typeFactory);

        AttributeType gmlPointAssociation = createGmlPoint(typeFactory);

        builder.setName("wq_plus");
        builder.cardinality(0, Integer.MAX_VALUE);
        builder.addAttribute("measurement", measurement);

        builder.cardinality(1, 1);
        builder.nillable(true);
        builder.addAttribute("location", gmlLocationAssociation);

        builder.cardinality(1, 1);
        builder.nillable(true);
        builder.addAttribute("nearestSlimePit", gmlPointAssociation);

        builder.cardinality(1, Integer.MAX_VALUE);
        builder.addAttribute("sitename", String.class);

        // use the second geometry attribute as the default one just for testing
        builder.defaultGeometry("nearestSlimePit");

        FeatureType wqPlusType = builder.feature();

        return wqPlusType;
    }

    /**
     * 
     * @param typeFactory
     * @param descFactory
     * @return
     */
    public static FeatureType createExample04Type(FeatureTypeFactory typeFactory) {
        TypeBuilder builder = new TypeBuilder(typeFactory);
        builder.setNamespaceURI(NSURI);

        builder.cardinality(1, Integer.MAX_VALUE);
        builder.addAttribute("name", String.class);

        builder.setName(GML_NSURI);
        builder.cardinality(1, Integer.MAX_VALUE);
        builder.addAttribute("name", String.class);

        builder.setNamespaceURI(NSURI);
        builder.setName("wq_plus");

        FeatureType wqPlusType = builder.feature();
        return wqPlusType;
    }

    public static ComplexType createMeasurementType(FeatureTypeFactory typeFactory) {
        TypeBuilder builder = new TypeBuilder(typeFactory);
        builder.setNamespaceURI(NSURI);

        builder.setName("determinand_description");
        builder.setBinding(String.class);
        AttributeType detdesc = builder.attribute();

        builder.setName("result");
        builder.setBinding(String.class);

        AttributeType result = builder.attribute();

        builder.setName("measurementType");
        builder.cardinality(1, 1);
        builder.addAttribute("determinand_description", detdesc);
        builder.addAttribute("result", result);

        ComplexType measurement = builder.complex();

        return measurement;
    }

    /**
     * Creates a representation of a gml:LocationPropertyType association. This would be better done
     * by obtaining the type from a registry, so we can have GML2TypeRegistry, GML3TypeRegistry,
     * DefaultTypeRegistry, etc.
     * 
     * @return
     */
    public static AttributeType createGmlLocation(FeatureTypeFactory typeFactory) {
        TypeBuilder builder = new TypeBuilder(typeFactory);
        builder.setNamespaceURI(GML_NSURI);
        builder.setName("LocationPropertyType");
        builder.setBinding(Point.class);

        AttributeType type = builder.geometry();

        return type;
    }

    /**
     * Creates a representation of a gml:PointPropertyType association as an AttributeType. This
     * would be better done by obtaining the type from a registry, so we can have GML2TypeRegistry,
     * GML3TypeRegistry, DefaultTypeRegistry, etc.
     * 
     * @return
     */
    public static AttributeType createGmlPoint(FeatureTypeFactory typeFactory) {
        TypeBuilder builder = new TypeBuilder(typeFactory);
        builder.setNamespaceURI(GML_NSURI);

        builder.setName("PointPropertyType");
        CoordinateReferenceSystem fakeCrs = null;
        try {
            fakeCrs = CRS.decode("EPSG:4326");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        builder.setCRS(fakeCrs);
        builder.setBinding(Point.class);

        GeometryType type = builder.geometry();
        return type;
    }

    /**
     * Asserts the corresponding properties of <code>type</code> for equality with the provided
     * parameter values
     * 
     * @param type
     * @param name
     * @param binding
     * @param restrictions
     * @param identified
     * @param _abstract
     * @param superType
     * @param nillable
     */
    public static void checkType(AttributeType type, Name name, Class<?> binding,
            Set<Filter> restrictions, boolean identified, boolean _abstract, AttributeType superType) {

        assertNotNull(type);
        assertEquals(name, type.getName());
        assertEquals(binding, type.getBinding());
        assertNotNull(type.getRestrictions());
        assertEquals(restrictions, type.getRestrictions());
        assertEquals(identified, type.isIdentified());
        assertEquals(_abstract, type.isAbstract());
        assertEquals(superType, type.getSuper());
    }

}
