/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geoserver.data.geogit;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geogit.api.Ref;
import org.geogit.api.RevObject.TYPE;
import org.geogit.api.RevTree;
import org.geogit.storage.ObjectDatabase;
import org.geogit.storage.RefDatabase;
import org.geogit.storage.WrappedSerialisingFactory;
import org.geogit.storage.hessian.HessianSimpleFeatureTypeReader;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class GeoGitDataStoreTest extends GeoGITRepositoryTestCase {

    private GeoGitDataStore dataStore;

    @Override
    protected void setUpChild() throws Exception {
        dataStore = new GeoGitDataStore(repo);
    }
    
    @Override
    protected void tearDownChild() throws Exception {
        dataStore = null;
    }

    public void testCreateSchema() throws IOException {
        final RefDatabase refDatabase = repo.getRefDatabase();
        final Ref initialTypesTreeRef = refDatabase
                .getRef(GeoGitDataStore.TYPE_NAMES_REF_TREE);
        assertNotNull(initialTypesTreeRef);

        final SimpleFeatureType featureType = super.linesType;
        dataStore.createSchema(featureType);
        assertTypeRefs(Collections.singleton(super.linesType));

        dataStore.createSchema(super.pointsType);

        Set<SimpleFeatureType> expected = new HashSet<SimpleFeatureType>();
        expected.add(super.linesType);
        expected.add(super.pointsType);
        assertTypeRefs(expected);

        try {
            dataStore.createSchema(super.pointsType);
            fail("Expected IOException on existing type");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("already exists"));
        }

    }

    private void assertTypeRefs(Set<SimpleFeatureType> expectedTypes)
            throws IOException {
        final RefDatabase refDatabase = repo.getRefDatabase();

        for (SimpleFeatureType featureType : expectedTypes) {
            final Name typeName = featureType.getName();
            final Ref typesTreeRef = refDatabase
                    .getRef(GeoGitDataStore.TYPE_NAMES_REF_TREE);
            assertNotNull(typesTreeRef);

            RevTree typesTree = repo.getTree(typesTreeRef.getObjectId());
            List<String> path = Arrays.asList(typeName.getNamespaceURI(),
                    typeName.getLocalPart());
            ObjectDatabase objectDatabase = repo.getObjectDatabase();

            Ref typeRef = objectDatabase.getTreeChild(typesTree, path);
            assertNotNull(typeRef);
            assertEquals(TYPE.BLOB, typeRef.getType());

            WrappedSerialisingFactory serialisingFactory;
            serialisingFactory = WrappedSerialisingFactory.getInstance();
//            SimpleFeatureType readType = objectDatabase.get(typeRef
//                    .getObjectId(), serialisingFactory
//                    .createSimpleFeatureTypeReader(featureType.getName()));
            SimpleFeatureType readType = objectDatabase.get(typeRef.getObjectId(), 
                    new HessianSimpleFeatureTypeReader(featureType.getName()));

            assertEquals(featureType, readType);

        }
    }

    public void testGetNames() throws IOException {

        assertEquals(0, dataStore.getNames().size());

        dataStore.createSchema(super.linesType);
        assertEquals(1, dataStore.getNames().size());

        dataStore.createSchema(super.pointsType);
        assertEquals(2, dataStore.getNames().size());

        assertTrue(dataStore.getNames().contains(
                GeoGITRepositoryTestCase.linesTypeName));
        assertTrue(dataStore.getNames().contains(
                GeoGITRepositoryTestCase.pointsTypeName));
    }

    public void testGetTypeNames() throws IOException {

        assertEquals(0, dataStore.getTypeNames().length);

        dataStore.createSchema(super.linesType);
        assertEquals(1, dataStore.getTypeNames().length);

        dataStore.createSchema(super.pointsType);
        assertEquals(2, dataStore.getTypeNames().length);

        List<String> simpleNames = Arrays.asList(dataStore.getTypeNames());

        assertTrue(simpleNames.contains(GeoGITRepositoryTestCase.linesName));
        assertTrue(simpleNames.contains(GeoGITRepositoryTestCase.pointsName));
    }

    public void testGetSchemaName() throws IOException {
        try {
            dataStore.getSchema(GeoGITRepositoryTestCase.linesTypeName);
            fail("Expected SchemaNotFoundException");
        } catch (SchemaNotFoundException e) {
            assertTrue(true);
        }

        dataStore.createSchema(super.linesType);
        SimpleFeatureType lines = dataStore
                .getSchema(GeoGITRepositoryTestCase.linesTypeName);
        assertEquals(super.linesType, lines);

        try {
            dataStore.getSchema(GeoGITRepositoryTestCase.pointsTypeName);
            fail("Expected SchemaNotFoundException");
        } catch (SchemaNotFoundException e) {
            assertTrue(true);
        }

        dataStore.createSchema(super.pointsType);
        SimpleFeatureType points = dataStore
                .getSchema(GeoGITRepositoryTestCase.pointsTypeName);
        assertEquals(super.pointsType, points);
    }

    public void testGetSchemaString() throws IOException {
        try {
            dataStore.getSchema(GeoGITRepositoryTestCase.linesName);
            fail("Expected SchemaNotFoundException");
        } catch (SchemaNotFoundException e) {
            assertTrue(true);
        }

        dataStore.createSchema(super.linesType);
        SimpleFeatureType lines = dataStore
                .getSchema(GeoGITRepositoryTestCase.linesName);
        assertEquals(super.linesType, lines);

        try {
            dataStore.getSchema(GeoGITRepositoryTestCase.pointsName);
            fail("Expected SchemaNotFoundException");
        } catch (SchemaNotFoundException e) {
            assertTrue(true);
        }

        dataStore.createSchema(super.pointsType);
        SimpleFeatureType points = dataStore
                .getSchema(GeoGITRepositoryTestCase.pointsName);
        assertEquals(super.pointsType, points);
    }

    public void testGetFeatureSourceName() throws IOException {
        try {
            dataStore.getFeatureSource(GeoGITRepositoryTestCase.linesTypeName);
            fail("Expected SchemaNotFoundException");
        } catch (SchemaNotFoundException e) {
            assertTrue(true);
        }

        SimpleFeatureSource source;

        dataStore.createSchema(super.linesType);
        source = dataStore.getFeatureSource(GeoGITRepositoryTestCase.linesTypeName);
        assertTrue(source instanceof GeoGitFeatureSource);

        try {
            dataStore.getFeatureSource(GeoGITRepositoryTestCase.pointsTypeName);
            fail("Expected SchemaNotFoundException");
        } catch (SchemaNotFoundException e) {
            assertTrue(true);
        }

        dataStore.createSchema(super.pointsType);
        source = dataStore.getFeatureSource(GeoGITRepositoryTestCase.pointsTypeName);
        assertTrue(source instanceof GeoGitFeatureSource);
    }

    public void testGetFeatureSourceString() throws IOException {
        try {
            dataStore.getFeatureSource(GeoGITRepositoryTestCase.linesName);
            fail("Expected SchemaNotFoundException");
        } catch (SchemaNotFoundException e) {
            assertTrue(true);
        }

        SimpleFeatureSource source;

        dataStore.createSchema(super.linesType);
        source = dataStore.getFeatureSource(GeoGITRepositoryTestCase.linesName);
        assertTrue(source instanceof GeoGitFeatureSource);

        try {
            dataStore.getFeatureSource(GeoGITRepositoryTestCase.pointsName);
            fail("Expected SchemaNotFoundException");
        } catch (SchemaNotFoundException e) {
            assertTrue(true);
        }

        dataStore.createSchema(super.pointsType);
        source = dataStore.getFeatureSource(GeoGITRepositoryTestCase.pointsName);
        assertTrue(source instanceof GeoGitFeatureSource);
    }

}
