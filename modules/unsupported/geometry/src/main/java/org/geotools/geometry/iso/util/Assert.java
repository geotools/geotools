/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.util;


// TODO SJ: It has to be discussed whether the Implementation uses Asserts or Throwable Exceptions; probably the Exceptions are the better solutions.

/**
 * A utility for making programming assertions.
 *
 *
 * @source $URL$
 */
public class Assert {

	/**
	 * Throws an <code>AssertionFailedException</code> if the given assertion
	 * is not true.
	 * 
	 * @param assertion
	 *            a condition that is supposed to be true
	 * @throws AssertionFailedException
	 *             if the condition is false
	 */
	public static void isTrue(boolean assertion) {
		isTrue(assertion, null);
	}

	/**
	 * Throws an <code>AssertionFailedException</code> with the given message
	 * if the given assertion is not true.
	 * 
	 * @param assertion
	 *            a condition that is supposed to be true
	 * @param message
	 *            a description of the assertion
	 * @throws AssertionFailedException
	 *             if the condition is false
	 */
	public static void isTrue(boolean assertion, String message) {
		if (!assertion) {
			if (message == null) {
				throw new AssertionFailedException();
			} else {
				throw new AssertionFailedException(message);
			}
		}
	}

	/**
	 * Throws an <code>AssertionFailedException</code> if the given objects
	 * are not equal, according to the <code>equals</code> method.
	 * 
	 * @param expectedValue
	 *            the correct value
	 * @param actualValue
	 *            the value being checked
	 * @throws AssertionFailedException
	 *             if the two objects are not equal
	 */
	public static void equals(Object expectedValue, Object actualValue) {
		equals(expectedValue, actualValue, null);
	}

	/**
	 * Throws an <code>AssertionFailedException</code> with the given message
	 * if the given objects are not equal, according to the <code>equals</code>
	 * method.
	 * 
	 * @param expectedValue
	 *            the correct value
	 * @param actualValue
	 *            the value being checked
	 * @param message
	 *            a description of the assertion
	 * @throws AssertionFailedException
	 *             if the two objects are not equal
	 */
	public static void equals(Object expectedValue, Object actualValue,
			String message) {
		if (!actualValue.equals(expectedValue)) {
			throw new AssertionFailedException("Expected " + expectedValue
					+ " but encountered " + actualValue
					+ (message != null ? ": " + message : ""));
		}
	}

	/**
	 * Always throws an <code>AssertionFailedException</code>.
	 * 
	 * @throws AssertionFailedException
	 *             thrown always
	 */
	public static void shouldNeverReachHere() {
		shouldNeverReachHere(null);
	}

	/**
	 * Always throws an <code>AssertionFailedException</code> with the given
	 * message.
	 * 
	 * @param message
	 *            a description of the assertion
	 * @throws AssertionFailedException
	 *             thrown always
	 */
	public static void shouldNeverReachHere(String message) {
		throw new AssertionFailedException("Should never reach here"
				+ (message != null ? ": " + message : ""));
	}
}
