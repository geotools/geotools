/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.net.URL;
import org.geotools.api.style.ExternalGraphic;
import org.junit.Assert;
import org.junit.Test;

public class ExternalGraphicImplTest {

    private static final String TEST_URL = "http://test.io/graphic.png";

    private static final String TEST_FORMAT = "image/png";

    /**
     * The Url of OnlineResource could be set by three independent set'ers. Test that they all ends up in the location
     * field of onlineResource
     */
    @Test
    public void testOnlineResourceConsistency() throws Exception {
        CheckOnlineResource testObject = new CheckOnlineResource();

        ExternalGraphic external = new ExternalGraphicImpl();
        external.setLocation(new URL(TEST_URL));

        external.accept(testObject);

        external.setURI(TEST_URL);
        external.accept(testObject);

        StyleFactoryImpl fact = new StyleFactoryImpl();
        external = fact.createExternalGraphic(TEST_URL, TEST_FORMAT);
        external.accept(testObject);
    }

    private static class CheckOnlineResource extends AbstractStyleVisitor {

        @Override
        public void visit(ExternalGraphic exgr) {
            Assert.assertNotNull(exgr.getOnlineResource());
            Assert.assertEquals(TEST_URL, exgr.getOnlineResource().getLinkage().toString());
        }
    }
}
