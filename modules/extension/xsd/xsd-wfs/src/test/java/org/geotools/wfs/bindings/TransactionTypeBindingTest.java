/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 * 
 * (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.wfs.bindings;

import java.math.BigInteger;
import java.net.URL;
import java.util.List;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;

import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.opengis.filter.Id;
import org.w3c.dom.Document;

/**
 * Unit test suite for {@link TransactionTypeBinding}
 * 
 * @author Daniel Leib
 */
public class TransactionTypeBindingTest extends WFSTestSupport
{
	public TransactionTypeBindingTest()
	{
		super(WFS.TransactionType, TransactionType.class, Binding.OVERRIDE);
	}

	@SuppressWarnings("unchecked")
	public void testEncode() throws Exception
	{
		GetFeatureType getFeature = factory.createGetFeatureType();
		getFeature.setHandle("handle");
		getFeature.setMaxFeatures(BigInteger.valueOf(10));
		getFeature.getQuery().add(factory.createQueryType());
		getFeature.getQuery().add(factory.createQueryType());

		Document dom = encode(getFeature, WFS.GetFeature);
		assertEquals("handle", dom.getDocumentElement().getAttribute("handle"));
		assertEquals("10", dom.getDocumentElement().getAttribute("maxFeatures"));
		assertEquals(2, getElementsByQName(dom, WFS.Query).getLength());
	}

	public void testParse() throws Exception
	{
		final URL resource = TestData.getResource(this, "TransactionTypeBindingTest.xml");
		buildDocument(resource);

		Object parsed = parse(WFS.Transaction);
		assertTrue(parsed instanceof TransactionType);
		TransactionType req = (TransactionType) parsed;
		assertEquals("WFS", req.getService());
		assertEquals("1.1.0", req.getVersion());
		assertEquals("fooHandle", req.getHandle());

		List updates = req.getUpdate();
		assertEquals(2, updates.size());
		assertTrue(updates.get(0) instanceof UpdateElementType);
		assertTrue(updates.get(1) instanceof UpdateElementType);
		
		UpdateElementType update1 = (UpdateElementType)updates.get(0);
		List properties = update1.getProperty();
		assertTrue(update1.getFilter() instanceof Id);
		assertEquals(1, properties.size());
		assertTrue(properties.get(0) instanceof PropertyType);
		PropertyType property = (PropertyType) properties.get(0);
		assertEquals("prop1", property.getName().getLocalPart());
		assertEquals("val1", property.getValue());
	}
}
