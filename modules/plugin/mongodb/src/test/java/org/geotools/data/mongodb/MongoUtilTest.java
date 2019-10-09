/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.io.IOException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

/** @author tkunicki@boundlessgeo.com */
public class MongoUtilTest {

    public MongoUtilTest() {}

    @Test
    public void set() {
        DBObject dbo = new BasicDBObject();
        Object result;

        MongoUtil.setDBOValue(dbo, "root.child1", 1d);
        result = MongoUtil.getDBOValue(dbo, "root.child1");

        assertThat(result, is(CoreMatchers.instanceOf(Double.class)));
        assertThat((Double) result, is(equalTo(1d)));

        MongoUtil.setDBOValue(dbo, "root.child2", "one");
        result = MongoUtil.getDBOValue(dbo, "root.child2");

        assertThat(result, is(CoreMatchers.instanceOf(String.class)));
        assertThat((String) result, is(equalTo("one")));

        result = MongoUtil.getDBOValue(dbo, "root.doesnotexists");
        assertThat(result, is(nullValue()));

        result = MongoUtil.getDBOValue(dbo, "bugusroot.neglectedchild");
        assertThat(result, is(nullValue()));
    }

    @Test
    public void testFileNameExtractionFromUrl() throws IOException {
        String url1 = "http://www.mock.com/filename.zip?param=1&query=44";
        assertTrue(MongoUtil.extractFilesNameFromUrl(url1).equalsIgnoreCase("filename.zip"));
        String url2 = "https://mock.url.com/filename.zip";
        assertTrue(MongoUtil.extractFilesNameFromUrl(url2).equalsIgnoreCase("filename.zip"));
        String url3 = "https://mock.url.com/filename.json?param=1&query=44";
        assertTrue(MongoUtil.extractFilesNameFromUrl(url3).equalsIgnoreCase("filename.json"));
        String url4 = "https://mock.url.com/filename.json";
        assertTrue(MongoUtil.extractFilesNameFromUrl(url4).equalsIgnoreCase("filename.json"));
    }
}
