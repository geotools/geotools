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
package org.geotools.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.geotools.factory.Hints;

public class NumericConverterFactoryTest extends TestCase {

	NumericConverterFactory factory;
	
	protected void setUp() throws Exception {
		factory = new NumericConverterFactory();
	}
	
	public void testIntegral() throws Exception {
		//to byte
		assertEquals( new Byte( (byte)127 ), convert( new Byte( (byte)127 ), Byte.class ) );
		assertEquals( new Byte( (byte)127 ), convert( new Short( (short)127 ), Byte.class ) );
		assertEquals( new Byte( (byte)127 ), convert( new Integer( 127 ), Byte.class ) );
		assertEquals( new Byte( (byte)127 ), convert( new Long( 127 ), Byte.class ) );
		assertEquals( new Byte( (byte)127 ), convert( BigInteger.valueOf( 127 ), Byte.class ) );
		
		//to short
		assertEquals( new Short( (short)127 ), convert( new Byte( (byte)127 ), Short.class ) );
		assertEquals( new Short( (short)127 ), convert( new Short( (short)127 ), Short.class ) );
		assertEquals( new Short( (short)127 ), convert( new Integer( 127 ), Short.class ) );
		assertEquals( new Short( (short)127 ), convert( new Long( 127 ), Short.class ) );
		assertEquals( new Short( (short)127 ), convert( BigInteger.valueOf( 127 ), Short.class ) );
		
		//to integer
		assertEquals( new Integer( 127 ), convert( new Byte( (byte)127 ), Integer.class ) );
		assertEquals( new Integer( 127 ), convert( new Short( (short)127 ), Integer.class ) );
		assertEquals( new Integer( 127 ), convert( new Integer( 127 ), Integer.class ) );
		assertEquals( new Integer( 127 ), convert( new Long( 127 ), Integer.class ) );
		assertEquals( new Integer( 127 ), convert( BigInteger.valueOf( 127 ), Integer.class ) );
		
		//to long
		assertEquals( new Long( 127 ), convert( new Byte( (byte)127 ), Long.class ) );
		assertEquals( new Long( 127 ), convert( new Short( (short)127 ), Long.class ) );
		assertEquals( new Long( 127 ), convert( new Integer( 127 ), Long.class ) );
		assertEquals( new Long( 127 ), convert( new Long( 127 ), Long.class ) );
		assertEquals( new Long( 127 ), convert( BigInteger.valueOf( 127 ), Long.class ) );
		
		//to big integer
		assertEquals( BigInteger.valueOf( 127 ), convert( new Byte( (byte)127 ), BigInteger.class ) );
		assertEquals( BigInteger.valueOf( 127 ), convert( new Short( (short)127 ), BigInteger.class ) );
		assertEquals( BigInteger.valueOf( 127 ), convert( new Integer( 127 ), BigInteger.class ) );
		assertEquals( BigInteger.valueOf( 127 ), convert( new Long( 127 ), BigInteger.class ) );
		assertEquals( BigInteger.valueOf( 127 ), convert( BigInteger.valueOf( 127 ), BigInteger.class ) );
	}
	
	public void testFloat() throws Exception {
		//to float
		assertEquals( new Float( 127.127 ), convert( new Float( 127.127 ), Float.class ) );
		assertEquals( new Float( 127.127 ), convert( new Double( 127.127 ), Float.class ) );
		assertEquals( new Float( 127.127 ), convert( new BigDecimal( 127.127 ), Float.class ) );
		
		//to double
		assertEquals( 
			new Double( 127.127 ).doubleValue(), 
			((Double)convert( new Float( 127.127 ), Double.class )).doubleValue(), 1e-10 
		);
		assertEquals( new Double( 127.127 ), convert( new Double( 127.127 ), Double.class ) );
		assertEquals( new Double( 127.127 ), convert( new BigDecimal( 127.127 ), Double.class ) );
		
		//to big decimal
		assertEquals( 
			new BigDecimal( 127.127 ).doubleValue(), 
			((BigDecimal) convert( new Float( 127.127 ), BigDecimal.class )).doubleValue(), 1e-10
		);
		assertEquals( new BigDecimal( "127.127" ), convert( new Double( 127.127 ), BigDecimal.class ) );
		assertEquals( new BigDecimal( 127.127 ), convert( new BigDecimal( 127.127 ), BigDecimal.class ) );
	}
	
	public void testIntegralToFloat() throws Exception {
		assertEquals( new Float( 127.0 ), convert( new Byte( (byte)127 ), Float.class ) );
		assertEquals( new Float( 127.0 ), convert( new Short( (short)127 ), Float.class ) );
		assertEquals( new Float( 127.0 ), convert( new Integer( 127 ), Float.class ) );
		assertEquals( new Float( 127.0 ), convert( new Long( 127 ), Float.class ) );
		assertEquals( new Float( 127.0 ), convert( BigInteger.valueOf( 127 ), Float.class ) );
		
		assertEquals( new Double( 127.0 ), convert( new Byte( (byte)127 ), Double.class ) );
		assertEquals( new Double( 127.0 ), convert( new Short( (short)127 ), Double.class ) );
		assertEquals( new Double( 127.0 ), convert( new Integer( 127 ), Double.class ) );
		assertEquals( new Double( 127.0 ), convert( new Long( 127 ), Double.class ) );
		assertEquals( new Double( 127.0 ), convert( BigInteger.valueOf( 127 ), Double.class ) );
		
		assertEquals( new BigDecimal( 127.0 ), convert( new Byte( (byte)127 ), BigDecimal.class ) );
		assertEquals( new BigDecimal( 127.0 ), convert( new Short( (short)127 ), BigDecimal.class ) );
		assertEquals( new BigDecimal( 127.0 ), convert( new Integer( 127 ), BigDecimal.class ) );
		assertEquals( new BigDecimal( 127.0 ), convert( new Long( 127 ), BigDecimal.class ) );
		assertEquals( new BigDecimal( 127.0 ), convert( BigInteger.valueOf( 127 ), BigDecimal.class ) );
	}
	
	public void testFloatToIntegral() throws Exception {
		//to byte
		assertEquals( new Byte( (byte)127 ), convert( new Float( 127.127 ), Byte.class ) );
		assertEquals( new Byte( (byte)127 ), convert( new Double( 127.127 ), Byte.class ) );
		assertEquals( new Byte( (byte)127 ), convert( new BigDecimal( 127.127 ), Byte.class ) );
		
		//to short
		assertEquals( new Short( (short)127 ), convert( new Float( 127.127 ), Short.class ) );
		assertEquals( new Short( (short)127 ), convert( new Double( 127.127 ), Short.class ) );
		assertEquals( new Short( (short)127 ), convert( new BigDecimal( 127.127 ), Short.class ) );
		
		//to integer
		assertEquals( new Integer( 127 ), convert( new Float( 127.127 ), Integer.class ) );
		assertEquals( new Integer( 127 ), convert( new Double( 127.127 ), Integer.class ) );
		assertEquals( new Integer( 127 ), convert( new BigDecimal( 127.127 ), Integer.class ) );
		
		//to long
		assertEquals( new Long( 127 ), convert( new Float( 127.127 ), Long.class ) );
		assertEquals( new Long( 127 ), convert( new Double( 127.127 ), Long.class ) );
		assertEquals( new Long( 127 ), convert( new BigDecimal( 127.127 ), Long.class ) );
		
		//to big integer
		assertEquals( BigInteger.valueOf( 127 ), convert( new Float( 127.127 ), BigInteger.class ) );
		assertEquals( BigInteger.valueOf( 127 ), convert( new Double( 127.127 ), BigInteger.class ) );
		assertEquals( BigInteger.valueOf( 127 ), convert( new BigDecimal( 127.127 ), BigInteger.class ) );
		
	}

	public void testStringToInteger() throws Exception {
	    assertEquals( new Integer(127), convert( "127", Integer.class ) );
	    assertEquals( new Integer(127), convert( " 127 ", Integer.class ) );
	    assertEquals( new Integer(3), convert( " 3.0 ", Integer.class ) );
	    assertEquals( new Integer(-3), convert( "-3.0 ", Integer.class ) );
	    assertEquals( new Integer(3000), convert( "3000.0 ", Integer.class ) );
	    assertEquals( new Integer(3000), convert( "3000,0 ", Integer.class ) );	    
	}
	public void testStringToDouble() throws Exception {
	    assertEquals( new Double(4.4), convert( "4.4", Double.class ) );	    
        assertEquals( new Double(127), convert( "127", Double.class ) );
        assertEquals( new Double(127), convert( " 127 ", Double.class ) );
        assertEquals( new Double(3), convert( " 3.0 ", Double.class ) );
        assertEquals( new Double(-3), convert( "-3.0 ", Double.class ) );
        assertEquals( new Double(3000), convert( "3000.0 ", Double.class ) );
    }
	
	public void testStringToNumber() throws Exception {
	    assertEquals( new Double(4.4), convert( "4.4", Number.class ));
	}
	
	Object convert( Object source, Class target ) throws Exception {
		return factory.createConverter( source.getClass(), target, null ).convert( source, target );
	}
	
	public static void testIntegralHandling(){
	    assertEquals( "3", NumericConverterFactory.toIntegral("3"));
	    assertEquals( "3", NumericConverterFactory.toIntegral("3.0"));
	    assertEquals( "-3", NumericConverterFactory.toIntegral("-3"));
	    assertEquals( "-3", NumericConverterFactory.toIntegral("-3.0"));
	    assertEquals( "3000", NumericConverterFactory.toIntegral("3000.0"));
	    assertEquals( "3000", NumericConverterFactory.toIntegral("3000,0"));
	}
	


    Object convertSafe(Object source, Class<?> target) throws Exception {
        Hints hints = new Hints();
        hints.put(ConverterFactory.SAFE_CONVERSION, new Boolean(true));
        return factory.createConverter(source.getClass(), target, hints).convert(source, target);
    }

    public void testSafeConversion() throws Exception {
        // byte
        assertEquals(new Byte((byte) 127), convertSafe(new Byte((byte) 127), Byte.class));
        assertNull(convertSafe(new Short((short) 128), Byte.class));
        assertNull(convertSafe(new Integer(128), Byte.class));
        assertNull(convertSafe(new Long(128), Byte.class));
        assertNull(convertSafe(BigInteger.valueOf(128), Byte.class));
        assertNull(convertSafe(new Double(128.1), Byte.class));
        assertNull(convertSafe(new BigDecimal(128.1), Byte.class));
        assertNull(convertSafe(new Float(128.1), Byte.class));

        // short
        assertEquals(new Short((short) 127), convertSafe(new Byte((byte) 127), Short.class));
        assertEquals(new Short((short) 1111), convertSafe(new Short((short) 1111), Short.class));
        assertNull(convertSafe(new Integer(128), Short.class));
        assertNull(convertSafe(new Long(128), Short.class));
        assertNull(convertSafe(BigInteger.valueOf(128), Short.class));
        assertNull(convertSafe(new Double(128.1), Short.class));
        assertNull(convertSafe(new BigDecimal(128.1), Short.class));
        assertNull(convertSafe(new Float(128.1), Short.class));

        // integer
        assertEquals(new Integer(127), convertSafe(new Byte((byte) 127), Integer.class));
        assertEquals(new Integer(1111), convertSafe(new Short((short) 1111), Integer.class));
        assertEquals(new Integer(12345), convertSafe(new Integer(12345), Integer.class));
        assertNull(convertSafe(new Long(128), Integer.class));
        assertNull(convertSafe(BigInteger.valueOf(128), Integer.class));
        assertNull(convertSafe(new Double(128.1), Integer.class));
        assertNull(convertSafe(new BigDecimal(128.1), Integer.class));
        assertNull(convertSafe(new Float(128.1), Integer.class));

        // long
        assertEquals(new Long(127), convertSafe(new Byte((byte) 127), Long.class));
        assertEquals(new Long(1111), convertSafe(new Short((short) 1111), Long.class));
        assertEquals(new Long(12345), convertSafe(new Integer(12345), Long.class));
        assertEquals(new Long(1234567), convertSafe(new Integer(1234567), Long.class));
        assertNull(convertSafe(BigInteger.valueOf(128), Long.class));
        assertNull(convertSafe(new Double(128.1), Long.class));
        assertNull(convertSafe(new BigDecimal(128.1), Long.class));
        assertNull(convertSafe(new Float(128.1), Long.class));

        // big integer
        assertEquals(BigInteger.valueOf(127), convertSafe(new Byte((byte) 127), BigInteger.class));
        assertEquals(BigInteger.valueOf(1111), convertSafe(new Short((short) 1111),
                BigInteger.class));
        assertEquals(BigInteger.valueOf(12345), convertSafe(new Integer(12345), BigInteger.class));
        assertEquals(BigInteger.valueOf(1234567), convertSafe(new Integer(1234567),
                BigInteger.class));
        assertEquals(BigInteger.valueOf(12345678), convertSafe(BigInteger.valueOf(12345678),
                BigInteger.class));
        assertNull(convertSafe(new Double(128.1), Long.class));
        assertNull(convertSafe(new BigDecimal(128.1), Long.class));
        assertNull(convertSafe(new Float(128.1), Long.class));

        // double
        assertEquals(new Double(127), convertSafe(new Byte((byte) 127), Double.class));
        assertEquals(new Double(1111), convertSafe(new Short((short) 1111), Double.class));
        assertEquals(new Double(12345), convertSafe(new Integer(12345), Double.class));
        assertEquals(new Double(1234567), convertSafe(new Integer(1234567), Double.class));
        //assertEquals(new Double(12345678), convertSafe(BigInteger.valueOf(12345678), Double.class));
        assertEquals(new Double(12.123456), convertSafe(new Double(12.123456), Double.class));
        assertNull(convertSafe(new BigDecimal(128.1), Long.class));
        assertEquals(new Double(12.12), convertSafe(new Float(12.12), Double.class));

        // float
        assertEquals(new Float(127), convertSafe(new Byte((byte) 127), Float.class));
        assertEquals(new Float(1111), convertSafe(new Short((short) 1111), Float.class));
        assertEquals(new Float(12345), convertSafe(new Integer(12345), Float.class));
        assertEquals(new Float(1234567), convertSafe(new Integer(1234567), Float.class));
        assertNull(convertSafe(BigInteger.valueOf(12345678), Float.class));
        assertNull(convertSafe(new Double(128.1), Float.class));
        assertNull(convertSafe(new BigDecimal(128.1), Float.class));
        assertEquals(new Float(12.12), convertSafe(new Float(12.12), Float.class));

        // Big Decimal
        assertEquals(new BigDecimal(127), convertSafe(new Byte((byte) 127), BigDecimal.class));
        assertEquals(new BigDecimal(1111), convertSafe(new Short((short) 1111), BigDecimal.class));
        assertEquals(new BigDecimal(12345), convertSafe(new Integer(12345), BigDecimal.class));
        assertEquals(new BigDecimal(1234567), convertSafe(new Integer(1234567), BigDecimal.class));
        assertEquals(new BigDecimal(12345678), convertSafe(BigInteger.valueOf(12345678),
                BigDecimal.class));
        assertEquals(new BigDecimal((new Double(12.123456)).toString()), convertSafe(new Double(
                12.123456), BigDecimal.class));
        assertEquals(new BigDecimal(128.1), convertSafe(new BigDecimal(128.1), BigDecimal.class));
        assertEquals(new BigDecimal((new Float(12.12)).toString()), convertSafe(new Float(12.12),
                BigDecimal.class));
        
        
        // test strings
        assertEquals(new BigDecimal(127), convertSafe("127", BigDecimal.class));
        assertNull(convertSafe("127f", BigDecimal.class));
        assertEquals(new Double(127.123), convertSafe("127.123", Double.class));
        assertNull(convertSafe("123.456.456", Double.class));
        assertEquals(new Float(127.123), convertSafe("127.123", Float.class));
        assertNull(convertSafe("123.456.456", Float.class));
        assertEquals(BigInteger.valueOf(1234567), convertSafe("1234567", BigInteger.class));
        assertNull(convertSafe("123.456", BigInteger.class));
        assertEquals(new Long(54), convertSafe("54", Long.class));
        assertNull(convertSafe("123.6", Long.class));
        assertEquals(new Integer(54), convertSafe("54", Integer.class));
        assertNull(convertSafe("123.6", Integer.class));
        assertEquals(new Short((short) 54), convertSafe("54", Short.class));
        assertNull(convertSafe("123.6", Short.class));
        assertEquals(new Byte("1"), convertSafe("1", Byte.class));
        assertNull(convertSafe("123.6", Byte.class));
    }      
    
    public void testPrimitiveTypes() throws Exception {
        assertEquals(1, convert(new Integer(1), int.class));
        assertEquals(new Integer(1), convert(new Integer(1), int.class));
        assertEquals(1, convert(1, Integer.class));
        assertEquals(new Integer(1), convert(1, Integer.class));
        
        assertEquals(1, convert("1", int.class));
        assertEquals(new Integer(1), convert("1", int.class));
        assertEquals(1, convert("1", Integer.class));
        assertEquals(new Integer(1), convert("1", Integer.class));
    }
}
