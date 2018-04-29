/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

import org.geotools.ysld.YamlObject;
import org.junit.Test;

public class YamlParseContextTest {

    @Test
    public void testDocHints() throws Exception {
        YamlParseContext ctxt = new YamlParseContext();
        YamlParseHandler handler = createMock(YamlParseHandler.class);
        YamlObject obj1 = createMock(YamlObject.class);

        replay(handler);

        ctxt.push(obj1, handler);

        assertThat(ctxt.getDocHint("testHint1"), nullValue());
        ctxt.setDocHint("testHint1", "th1v1");
        assertThat((String) ctxt.getDocHint("testHint1"), is("th1v1"));
        ctxt.setDocHint("testHint1", "th1v2");
        assertThat((String) ctxt.getDocHint("testHint1"), is("th1v2"));

        assertThat(ctxt.getDocHint("testHint2"), nullValue());
        ctxt.setDocHint("testHint2", "th2v1");
        assertThat((String) ctxt.getDocHint("testHint2"), is("th2v1"));
        assertThat((String) ctxt.getDocHint("testHint1"), is("th1v2"));

        verify(handler);
    }
}
