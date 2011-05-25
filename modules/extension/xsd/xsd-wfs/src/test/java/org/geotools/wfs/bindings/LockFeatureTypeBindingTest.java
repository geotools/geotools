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
package org.geotools.wfs.bindings;

import java.math.BigInteger;
import java.net.URL;
import java.util.Collections;

import javax.xml.namespace.QName;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.LockType;

import org.geotools.gml3.GML;
import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.opengis.filter.Id;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test suite for {@link LockFeatureTypeBinding}
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 *
 * @source $URL$
 */
public class LockFeatureTypeBindingTest extends WFSTestSupport {

    public LockFeatureTypeBindingTest() {
        super(WFS.LockFeatureType, LockFeatureType.class, Binding.OVERRIDE);
    }

    @Override
    public void testEncode() throws Exception {
        LockFeatureType lockFeature = factory.createLockFeatureType();
        lockFeature.setExpiry(BigInteger.valueOf(1000));
        lockFeature.setLockAction(AllSomeType.ALL_LITERAL);
        {
            LockType lock1 = factory.createLockType();
            lock1.setTypeName(new QName(GML.NAMESPACE, "TestGmlFeature"));
            lock1.setFilter(filterFac.id(Collections.singleton(filterFac.featureId("fid1"))));
            lockFeature.getLock().add(lock1);
        }
        {
            LockType lock2 = factory.createLockType();
            lock2.setTypeName(new QName(WFS.NAMESPACE, "TestWfsFeature"));
            lock2.setFilter(filterFac.id(Collections.singleton(filterFac.featureId("fid2"))));
            lockFeature.getLock().add(lock2);
        }

        final Document dom = encode(lockFeature, WFS.LockFeature);
        final Element root = dom.getDocumentElement();

        assertName(WFS.LockFeature, root);
        assertEquals("1000", root.getAttribute("expiry"));
        assertEquals("ALL", root.getAttribute("lockAction"));
    }

    @Override
    public void testParse() throws Exception {
        final URL resource = TestData.getResource(this, "LockFeatureTypeBindingTest.xml");
        buildDocument(resource);

        Object parsed = parse(WFS.LockFeatureType);
        assertTrue(parsed instanceof LockFeatureType);

        LockFeatureType lockFeature = (LockFeatureType) parsed;
        assertEquals(BigInteger.valueOf(1000), lockFeature.getExpiry());
        assertEquals(AllSomeType.SOME_LITERAL, lockFeature.getLockAction());

        assertEquals(1, lockFeature.getLock().size());
        LockType lock = (LockType) lockFeature.getLock().get(0);
        assertEquals(new QName(WFS.NAMESPACE, "TestTypeName"), lock.getTypeName());
        assertTrue(lock.getFilter() instanceof Id);
    }

}
