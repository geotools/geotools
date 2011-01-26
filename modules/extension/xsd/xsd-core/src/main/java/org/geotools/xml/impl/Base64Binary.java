/*
 * Copyright 2003, 2004  The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.geotools.xml.impl;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/** A utility class for working with base64 encoding.
 *
 * @source $URL$
 */
public class Base64Binary {
	/** Creates a clone of the byte array <code>pValue</code>.
	 */
	public static byte[] getClone(byte[] pValue) {
		byte[] result = new byte[pValue.length];
		System.arraycopy(pValue, 0, result, 0, pValue.length);
		return result;
	}

	/** Converts the string <code>pValue</code> into a
	 * base64 encoded byte array.
	 */
	public static byte[] decode(String pValue) throws IOException {
		return (new BASE64Decoder()).decodeBuffer(pValue);
	}

	/** Converts the base64 encoded byte array <code>pValue</code>
	 * into a string.
	 */
	public static String encode(byte[] pValue) {
		return (new BASE64Encoder()).encode(pValue);
	}
}
