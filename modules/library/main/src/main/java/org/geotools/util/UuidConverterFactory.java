/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-20010, Open Source Geospatial Foundation (OSGeo)
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

import java.util.UUID;
import org.geotools.factory.Hints;

/**
 * ConverterFactory for handling uuid (uniqueidentifier) conversions.
 * <p>
 * Supported conversions:
 * <ul>
 * 	<li>string -> UUID
 * 	<li>byte[] -> UUID
 * </ul>
 * </p>
 * @author Andrea Briganti <kbyte@ciotoni.net>
 *
 */
public class UuidConverterFactory implements ConverterFactory {

	public Converter createConverter(Class source, Class target, Hints hints) {
		if ( target.equals( UUID.class ) ) {

			//string to boolean
			if ( source.equals( String.class ) ) {
				return new Converter() {

					public Object convert(Object source, Class target) throws Exception {
						return UUID.fromString((String)source);
					}
				};
			}

			//integer to boolean
			if ( source.equals( byte[].class ) ) {
				return new Converter() {

					public Object convert(Object source, Class target) throws Exception {
						return UUID.nameUUIDFromBytes((byte[])source);
					}

				};
			}

		}

		return null;
	}

}
