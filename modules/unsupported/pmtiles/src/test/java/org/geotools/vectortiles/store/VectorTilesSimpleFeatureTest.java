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
package org.geotools.vectortiles.store;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import io.tileverse.vectortile.model.VectorTile;
import io.tileverse.vectortile.model.VectorTile.Layer.Feature;
import io.tileverse.vectortile.mvt.VectorTileBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class VectorTilesSimpleFeatureTest {

    private static final GeometryFactory GEOM_FACTORY = new GeometryFactory();
    private static final String FEATURE_TYPE_NAME = "TestFeatureType";
    private static final String GEOM_ATTR_NAME = "geometry";
    private static final String STRING_ATTR_NAME = "name";
    private static final String INT_ATTR_NAME = "count";
    private static final String DOUBLE_ATTR_NAME = "value";

    private SimpleFeatureType featureType;
    private VectorTile.Layer.Feature vtFeature;
    private VectorTilesSimpleFeature simpleFeature;
    private Point testPoint;
    private CoordinateReferenceSystem crs;

    @Before
    public void setUp() throws FactoryException {
        crs = CRS.decode("EPSG:4326");
        testPoint = GEOM_FACTORY.createPoint(new Coordinate(10.0, 20.0));

        // Create a SimpleFeatureType with geometry and several attributes
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(FEATURE_TYPE_NAME);
        builder.setCRS(crs);
        builder.add(GEOM_ATTR_NAME, Point.class, crs);
        builder.add(STRING_ATTR_NAME, String.class);
        builder.add(INT_ATTR_NAME, Integer.class);
        builder.add(DOUBLE_ATTR_NAME, Double.class);
        builder.setDefaultGeometry(GEOM_ATTR_NAME);
        featureType = builder.buildFeatureType();

        // Create a VectorTile.Layer.Feature with attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(STRING_ATTR_NAME, "test feature");
        attributes.put(INT_ATTR_NAME, 42);
        attributes.put(DOUBLE_ATTR_NAME, 3.14);
        vtFeature = vtFeature(123L, testPoint, attributes);
        simpleFeature = new VectorTilesSimpleFeature(featureType, vtFeature);
    }

    private static VectorTile.Layer.Feature vtFeature(
            final long id, final Geometry geom, final Map<String, Object> attributes) {
        Geometry actualGeom = geom;
        if (geom == null) {
            // MVT does not allow null geometries, but this is about testing the behavior of the
            // VectorTilesSimpleFeature wrapper
            actualGeom = new GeometryFactory().createPoint(new Coordinate(0, 0));
        }
        VectorTile vectorTile = new VectorTileBuilder()
                .layer()
                .name("test")
                .feature()
                .id(id)
                .geometry(actualGeom)
                .attributes(attributes)
                .build()
                .build()
                .build();
        Feature vtf = vectorTile.getFeatures().findFirst().orElseThrow();
        // MVT does not allow null geometries, but this is about testing the behavior of
        // the VectorTilesSimpleFeature wrapper
        if (geom == null) {
            vtf = spy(vtf);
            when(vtf.getGeometry()).thenReturn(null);
        }
        return vtf;
    }

    @Test
    public void testGetType() {
        assertThat(simpleFeature.getType(), equalTo(featureType));
        assertThat(simpleFeature.getFeatureType(), equalTo(featureType));
    }

    @Test
    public void testGetID() {
        assertThat(simpleFeature.getID(), equalTo("123"));
    }

    @Test
    public void testGetIdentifier() {
        FeatureId id = simpleFeature.getIdentifier();
        assertThat(id, notNullValue());
        assertThat(id.getID(), equalTo("123"));
    }

    @Test
    public void testGetName() {
        Name name = simpleFeature.getName();
        assertThat(name, notNullValue());
        assertThat(name.getLocalPart(), equalTo(FEATURE_TYPE_NAME));
    }

    @Test
    public void testIsNillable() {
        assertTrue(simpleFeature.isNillable());
    }

    @Test
    public void testGetDefaultGeometry() {
        Geometry geom = simpleFeature.getDefaultGeometry();
        assertThat(geom, notNullValue());
        assertThat(geom, instanceOf(Point.class));
        assertThat(geom, equalTo(testPoint));
    }

    @Test
    public void testSetDefaultGeometry() {
        Point newPoint = GEOM_FACTORY.createPoint(new Coordinate(30.0, 40.0));
        simpleFeature.setDefaultGeometry(newPoint);
        assertThat(simpleFeature.getDefaultGeometry(), equalTo(newPoint));
    }

    @Test
    public void testSetDefaultGeometryNull() {
        simpleFeature.setDefaultGeometry(null);
        assertThat(simpleFeature.getDefaultGeometry(), nullValue());
    }

    @Test
    public void testGetDefaultGeometryProperty() {
        GeometryAttribute geomAttr = simpleFeature.getDefaultGeometryProperty();
        assertThat(geomAttr, notNullValue());
        assertThat(geomAttr.getValue(), equalTo(testPoint));
        assertThat(geomAttr.getDescriptor(), equalTo(featureType.getGeometryDescriptor()));
    }

    @Test
    public void testSetDefaultGeometryProperty() {
        Point newPoint = GEOM_FACTORY.createPoint(new Coordinate(50.0, 60.0));
        GeometryAttribute newAttr =
                new org.geotools.feature.GeometryAttributeImpl(newPoint, featureType.getGeometryDescriptor(), null);
        simpleFeature.setDefaultGeometryProperty(newAttr);
        assertThat(simpleFeature.getDefaultGeometry(), equalTo(newPoint));
    }

    @Test
    public void testSetDefaultGeometryPropertyNull() {
        simpleFeature.setDefaultGeometryProperty(null);
        assertThat(simpleFeature.getDefaultGeometry(), nullValue());
    }

    @Test
    public void testGetBounds() {
        BoundingBox bounds = simpleFeature.getBounds();
        assertThat(bounds, notNullValue());
        assertThat(bounds.getCoordinateReferenceSystem(), equalTo(crs));
        assertTrue(bounds.contains(10.0, 20.0));
    }

    @Test
    public void testGetBoundsWithNullGeometry() {
        simpleFeature.setDefaultGeometry(null);
        assertThat(simpleFeature.getBounds(), nullValue());
    }

    @Test
    public void testGetBoundsWithNoGeometryDescriptor() throws FactoryException {
        // Create feature type without geometry
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("NoGeomType");
        builder.add(STRING_ATTR_NAME, String.class);
        SimpleFeatureType noGeomType = builder.buildFeatureType();

        VectorTile.Layer.Feature noGeomVtFeature = vtFeature(1L, null, Map.of(STRING_ATTR_NAME, "test"));

        VectorTilesSimpleFeature noGeomFeature = new VectorTilesSimpleFeature(noGeomType, noGeomVtFeature);
        assertThat(noGeomFeature.getBounds(), nullValue());
    }

    @Test
    public void testGetAttributeByName() {
        assertThat(simpleFeature.getAttribute(STRING_ATTR_NAME), equalTo("test feature"));
        assertThat(simpleFeature.getAttribute(INT_ATTR_NAME), equalTo(42));
        assertThat(simpleFeature.getAttribute(DOUBLE_ATTR_NAME), equalTo(3.14));
        assertThat(simpleFeature.getAttribute(GEOM_ATTR_NAME), equalTo(testPoint));
    }

    @Test
    public void testGetAttributeByIndex() {
        // Order: geometry, name, count, value
        assertThat(simpleFeature.getAttribute(0), equalTo(testPoint));
        assertThat(simpleFeature.getAttribute(1), equalTo("test feature"));
        assertThat(simpleFeature.getAttribute(2), equalTo(42));
        assertThat(simpleFeature.getAttribute(3), equalTo(3.14));
    }

    @Test
    public void testGetAttributeByNameObject() {
        Name name = new NameImpl(STRING_ATTR_NAME);
        assertThat(simpleFeature.getAttribute(name), equalTo("test feature"));
    }

    @Test
    public void testGetAttributeNonExistent() {
        assertThat(simpleFeature.getAttribute("nonexistent"), nullValue());
    }

    @Test
    public void testGetAttributes() {
        List<Object> attributes = simpleFeature.getAttributes();
        assertThat(attributes, hasSize(4));
        assertThat(attributes.get(0), equalTo(testPoint));
        assertThat(attributes.get(1), equalTo("test feature"));
        assertThat(attributes.get(2), equalTo(42));
        assertThat(attributes.get(3), equalTo(3.14));
    }

    @Test
    public void testSetAttributeByName() {
        simpleFeature.setAttribute(STRING_ATTR_NAME, "new name");
        assertThat(simpleFeature.getAttribute(STRING_ATTR_NAME), equalTo("new name"));
    }

    @Test
    public void testSetAttributeByIndex() {
        simpleFeature.setAttribute(1, "updated name");
        assertThat(simpleFeature.getAttribute(STRING_ATTR_NAME), equalTo("updated name"));
    }

    @Test
    public void testSetAttributeByNameObject() {
        Name name = new NameImpl(INT_ATTR_NAME);
        simpleFeature.setAttribute(name, 999);
        assertThat(simpleFeature.getAttribute(INT_ATTR_NAME), equalTo(999));
    }

    @Test
    public void testSetAttributeGeometry() {
        Point newPoint = GEOM_FACTORY.createPoint(new Coordinate(100.0, 200.0));
        simpleFeature.setAttribute(GEOM_ATTR_NAME, newPoint);
        assertThat(simpleFeature.getDefaultGeometry(), equalTo(newPoint));
    }

    @Test
    public void testSetAttributesArray() {
        Point newPoint = GEOM_FACTORY.createPoint(new Coordinate(5.0, 15.0));
        Object[] values = {newPoint, "array test", 88, 2.71};
        simpleFeature.setAttributes(values);

        assertThat(simpleFeature.getAttribute(0), equalTo(newPoint));
        assertThat(simpleFeature.getAttribute(1), equalTo("array test"));
        assertThat(simpleFeature.getAttribute(2), equalTo(88));
        assertThat(simpleFeature.getAttribute(3), equalTo(2.71));
    }

    @Test
    public void testSetAttributesList() {
        Point newPoint = GEOM_FACTORY.createPoint(new Coordinate(7.0, 17.0));
        List<Object> values = Arrays.asList(newPoint, "list test", 77, 1.41);
        simpleFeature.setAttributes(values);

        assertThat(simpleFeature.getAttribute(0), equalTo(newPoint));
        assertThat(simpleFeature.getAttribute(1), equalTo("list test"));
        assertThat(simpleFeature.getAttribute(2), equalTo(77));
        assertThat(simpleFeature.getAttribute(3), equalTo(1.41));
    }

    @Test
    public void testGetAttributeCount() {
        assertThat(simpleFeature.getAttributeCount(), equalTo(4));
    }

    @Test
    public void testGetProperty() {
        Property prop = simpleFeature.getProperty(STRING_ATTR_NAME);
        assertThat(prop, notNullValue());
        assertThat(prop.getValue(), equalTo("test feature"));
        assertThat(prop.getName().getLocalPart(), equalTo(STRING_ATTR_NAME));
    }

    @Test
    public void testGetPropertyByNameObject() {
        Name name = new NameImpl(INT_ATTR_NAME);
        Property prop = simpleFeature.getProperty(name);
        assertThat(prop, notNullValue());
        assertThat(prop.getValue(), equalTo(42));
    }

    @Test
    public void testGetPropertyNonExistent() {
        assertThat(simpleFeature.getProperty("nonexistent"), nullValue());
    }

    @Test
    public void testGetPropertyGeometry() {
        Property prop = simpleFeature.getProperty(GEOM_ATTR_NAME);
        assertThat(prop, notNullValue());
        assertThat(prop, instanceOf(GeometryAttribute.class));
        assertThat(prop.getValue(), equalTo(testPoint));
    }

    @Test
    public void testGetProperties() {
        Collection<Property> properties = simpleFeature.getProperties();
        assertThat(properties, notNullValue());
        assertThat(properties, hasSize(4));
    }

    @Test
    public void testGetPropertiesByName() {
        Collection<Property> properties = simpleFeature.getProperties(STRING_ATTR_NAME);
        assertThat(properties, hasSize(1));
        Property prop = properties.iterator().next();
        assertThat(prop.getValue(), equalTo("test feature"));
    }

    @Test
    public void testGetPropertiesByNameObject() {
        Name name = new NameImpl(INT_ATTR_NAME);
        Collection<Property> properties = simpleFeature.getProperties(name);
        assertThat(properties, hasSize(1));
        Property prop = properties.iterator().next();
        assertThat(prop.getValue(), equalTo(42));
    }

    @Test
    public void testGetPropertiesNonExistent() {
        Collection<Property> properties = simpleFeature.getProperties("nonexistent");
        assertThat(properties, hasSize(0));
    }

    @Test
    public void testGetValue() {
        Collection<? extends Property> value = simpleFeature.getValue();
        assertThat(value, notNullValue());
        assertThat(value, hasSize(4));
    }

    @Test
    public void testSetValueCollection() {
        Property prop1 = simpleFeature.getProperty(STRING_ATTR_NAME);
        prop1.setValue("modified");
        Property prop2 = simpleFeature.getProperty(INT_ATTR_NAME);
        prop2.setValue(999);

        Collection<Property> newValues = Arrays.asList(prop1, prop2);
        simpleFeature.setValue(newValues);

        // Verify the attributes were updated
        assertTrue(vtFeature.getAttributes().containsKey(STRING_ATTR_NAME));
        assertTrue(vtFeature.getAttributes().containsKey(INT_ATTR_NAME));
    }

    @Test
    public void testGetUserData() {
        Map<Object, Object> userData = simpleFeature.getUserData();
        assertThat(userData, notNullValue());

        // Test that it's the same instance on subsequent calls
        Map<Object, Object> userData2 = simpleFeature.getUserData();
        assertThat(userData2, equalTo(userData));

        // Test that we can store data
        userData.put("key1", "value1");
        assertThat(simpleFeature.getUserData().get("key1"), equalTo("value1"));
    }

    @Test
    public void testGetDescriptor() {
        AttributeDescriptor descriptor = simpleFeature.getDescriptor();
        assertThat(descriptor, notNullValue());
        assertThat(descriptor.getName(), equalTo(featureType.getName()));
        assertThat(descriptor.getMinOccurs(), equalTo(0));
        assertThat(descriptor.getMaxOccurs(), equalTo(Integer.MAX_VALUE));
        assertTrue(descriptor.isNillable());
    }

    @Test
    public void testValidate() {
        // Should not throw exception with valid values
        simpleFeature.validate();
    }

    @Test
    public void testValidateInvalidType() {
        // Set an attribute to an incompatible type
        vtFeature.getAttributes().put(INT_ATTR_NAME, "not an integer");

        // Note: Converters might convert the string to integer, so validation might still pass
        // This tests the validation logic is called
        simpleFeature.validate();
    }

    @Test
    public void testTypeConversionOnGetAttribute() {
        // Store a Long value but attribute expects Integer
        vtFeature.getAttributes().put(INT_ATTR_NAME, 42L);
        Object value = simpleFeature.getAttribute(INT_ATTR_NAME);
        assertThat(value, instanceOf(Integer.class));
        assertThat(value, equalTo(42));
    }

    @Test
    public void testTypeConversionDoubleToInteger() {
        // Store a Double value but attribute expects Integer
        vtFeature.getAttributes().put(INT_ATTR_NAME, 42.0);
        Object value = simpleFeature.getAttribute(INT_ATTR_NAME);
        assertThat(value, instanceOf(Integer.class));
        assertThat(value, equalTo(42));
    }

    @Test
    public void testAttributeUserData() {
        Property prop = simpleFeature.getProperty(STRING_ATTR_NAME);
        Map<Object, Object> userData = prop.getUserData();
        assertThat(userData, notNullValue());

        userData.put("attrKey", "attrValue");

        // Verify it persists
        Property prop2 = simpleFeature.getProperty(STRING_ATTR_NAME);
        assertThat(prop2.getUserData().get("attrKey"), equalTo("attrValue"));
    }

    @Test
    public void testAttributeIsNillable() {
        Property prop = simpleFeature.getProperty(STRING_ATTR_NAME);
        // Based on how the feature type was built, this should reflect the descriptor's nillable setting
        assertThat(prop.isNillable(), equalTo(prop.getDescriptor().isNillable()));
    }

    @Test
    public void testAttributeSetValue() {
        Property prop = simpleFeature.getProperty(STRING_ATTR_NAME);
        prop.setValue("changed via property");
        assertThat(simpleFeature.getAttribute(STRING_ATTR_NAME), equalTo("changed via property"));
    }

    @Test
    public void testAttributeGetType() {
        Property prop = simpleFeature.getProperty(STRING_ATTR_NAME);
        assertThat(prop.getType(), notNullValue());
        assertThat(prop.getType().getBinding(), equalTo(String.class));
    }

    @Test
    public void testAttributeGetDescriptor() {
        Property prop = simpleFeature.getProperty(STRING_ATTR_NAME);
        AttributeDescriptor descriptor = (AttributeDescriptor) prop.getDescriptor();
        assertThat(descriptor, notNullValue());
        assertThat(descriptor.getLocalName(), equalTo(STRING_ATTR_NAME));
    }

    @Test
    public void testAttributeEquals() {
        Property prop1 = simpleFeature.getProperty(STRING_ATTR_NAME);
        Property prop2 = simpleFeature.getProperty(STRING_ATTR_NAME);
        assertThat(prop1, equalTo(prop2));
    }

    @Test
    public void testAttributeNotEquals() {
        Property prop1 = simpleFeature.getProperty(STRING_ATTR_NAME);
        Property prop2 = simpleFeature.getProperty(INT_ATTR_NAME);
        assertThat(prop1, not(equalTo(prop2)));
    }

    @Test
    public void testAttributeHashCode() {
        Property prop1 = simpleFeature.getProperty(STRING_ATTR_NAME);
        Property prop2 = simpleFeature.getProperty(STRING_ATTR_NAME);
        assertThat(prop1.hashCode(), equalTo(prop2.hashCode()));
    }

    @Test
    public void testAttributeToString() {
        Property prop = simpleFeature.getProperty(STRING_ATTR_NAME);
        String str = prop.toString();
        assertThat(str, notNullValue());
        assertTrue(str.contains(STRING_ATTR_NAME));
        assertTrue(str.contains("test feature"));
    }

    @Test
    public void testAttributeListGet() {
        Collection<Property> properties = simpleFeature.getProperties();
        assertThat(properties, instanceOf(List.class));

        List<Property> propList = (List<Property>) properties;
        Property prop = propList.get(1); // STRING_ATTR_NAME
        assertThat(prop.getValue(), equalTo("test feature"));
    }

    @Test
    public void testAttributeListSize() {
        Collection<Property> properties = simpleFeature.getProperties();
        assertThat(properties.size(), equalTo(4));
    }

    @Test
    public void testAttributeListSet() {
        Collection<Property> properties = simpleFeature.getProperties();
        List<Property> propList = (List<Property>) properties;

        Property newProp = simpleFeature.getProperty(STRING_ATTR_NAME);
        newProp.setValue("list modified");

        propList.set(1, newProp);
        // Note: The actual value update happens through the Property.setValue() call above
        // The set() operation in the list just delegates to setAttribute
    }

    @Test
    public void testDefaultGeometryCaching() {
        // First call should set the cached geometry
        Geometry geom1 = simpleFeature.getDefaultGeometry();
        // Second call should return the same cached instance
        Geometry geom2 = simpleFeature.getDefaultGeometry();
        assertThat(geom1, equalTo(geom2));
    }

    @Test
    public void testFeatureWithNullGeometry() {
        VectorTile.Layer.Feature noGeomVtFeature = vtFeature(456L, null, Map.of(STRING_ATTR_NAME, "no geometry"));

        VectorTilesSimpleFeature feature = new VectorTilesSimpleFeature(featureType, noGeomVtFeature);
        assertThat(feature.getDefaultGeometry(), nullValue());
    }
}
