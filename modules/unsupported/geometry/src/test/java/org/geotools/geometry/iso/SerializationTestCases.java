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
package org.geotools.geometry.iso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.geotools.factory.Hints;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;

import junit.framework.TestCase;

public class SerializationTestCases extends TestCase {

	// Serialize the given object and return the deserialized copy
	public Object serializeAndDeSerialize(Object object)
	   throws IOException, ClassNotFoundException {

	    // serialize
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(out);
	    oos.writeObject(object);
	    oos.close();

	    //deserialize
	    byte[] pickled = out.toByteArray();
	    InputStream in = new ByteArrayInputStream(pickled);
	    ObjectInputStream ois = new ObjectInputStream(in);
	    Object o = ois.readObject();

	    // return the newly deserialized object
	    return o;

	}
	
//	// make it and test it's serialization
//	public void testPositionFactory() throws IOException, ClassNotFoundException {
//		
//		// create object, serialize, deserialize and compare results
//		PositionFactoryImpl pf = new PositionFactoryImpl((Hints) null);
//		PositionFactoryImpl copy = (PositionFactoryImpl) serializeAndDeSerialize(pf);
//		System.out.println(pf);
//		System.out.println(copy);
//		assertTrue(pf.equals(copy));
//	}
//	
//	// make it and test it's serialization
//	public void testPrimitiveFactory() throws IOException, ClassNotFoundException {
//		
//		// create object, serialize, deserialize and compare results
//		PrimitiveFactoryImpl pf = new PrimitiveFactoryImpl((Hints) null);
//		PrimitiveFactoryImpl copy = (PrimitiveFactoryImpl) serializeAndDeSerialize(pf);
//		System.out.println(pf);
//		System.out.println(copy);
//		assertTrue(pf.equals(copy));
//	}
	
	// make it and test it's serialization
	public void testPointFactory() throws IOException, ClassNotFoundException {
		
		// create object, serialize, deserialize and compare results
		DirectPosition dp = new DirectPositionImpl(DefaultGeographicCRS.WGS84, new double[]{1,2});
		PointImpl point = new PointImpl(dp);
		PointImpl copy = (PointImpl) serializeAndDeSerialize(point);
		assertTrue(point.equals(copy));
	}
	
}
