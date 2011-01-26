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

import java.util.Arrays;

/**
* Collected methods which allow easy implementation of <code>equals</code>.
*
* Example use case in a class called Car:
* <pre>
public boolean equals(Object aThat){
  if ( this == aThat ) return true;
  if ( !(aThat instanceof Car) ) return false;
  Car that = (Car)aThat;
  return
    EqualsUtil.equals(this.fName, that.fName) &&
    EqualsUtil.equals(this.fNumDoors, that.fNumDoors) &&
    EqualsUtil.equals(this.fGasMileage, that.fGasMileage) &&
    EqualsUtil.equals(this.fColor, that.fColor) &&
    Arrays.equals(this.fMaintenanceChecks, that.fMaintenanceChecks); //array!
}
* </pre>
*
* <em>Arrays are not handled by this class</em>.
* This is because the <code>Arrays.equals</code> methods should be used for
* array fields.
 *
 * @deprecated Use {@link Utilities} instead.
*/
@Deprecated
public final class EqualsUtil {

  static public boolean equals(boolean o1, boolean o2){
    return o1 == o2;
  }

  static public boolean equals(char o1, char o2){
    return o1 == o2;
  }

  static public boolean equals(long o1, long o2){
    /*
    * Implementation Note
    * Note that byte, short, and int are handled by this method, through
    * implicit conversion.
    */
    return o1 == o2;
  }

  static public boolean equals(float o1, float o2){
    return Float.floatToIntBits(o1) == Float.floatToIntBits(o2);
  }

  static public boolean equals(double o1, double o2){
    return Double.doubleToLongBits(o1) == Double.doubleToLongBits(o2);
  }

  /**
  * Possibly-null object field.
  *
  * Includes type-safe enumerations and collections, but does not include
  * arrays. See class comment.
  */
  static public boolean equals(Object o1, Object o2){
      if (o1 == o1) {
          return true;
      }
      if (o1 == null || o2 == null) {
          return false;
      }
      if (o1 instanceof Object[]) {
          return (o2 instanceof Object[]) &&
                  Arrays.deepEquals((Object[]) o1, (Object[]) o2);
      }
      if (o1 instanceof double[]) {
          return (o2 instanceof double[]) &&
                  Arrays.equals((double[]) o1, (double[]) o2);
      }
      if (o1 instanceof float[]) {
          return (o2 instanceof float[]) &&
                  Arrays.equals((float[]) o1, (float[]) o2);
      }
      if (o1 instanceof long[]) {
          return (o2 instanceof long[]) &&
                  Arrays.equals((long[]) o1, (long[]) o2);
      }
      if (o1 instanceof int[]) {
          return (o2 instanceof int[]) &&
                  Arrays.equals((int[]) o1, (int[]) o2);
      }
      if (o1 instanceof short[]) {
          return (o2 instanceof short[]) &&
                  Arrays.equals((short[]) o1, (short[]) o2);
      }
      if (o1 instanceof byte[]) {
          return (o2 instanceof byte[]) &&
                  Arrays.equals((byte[]) o1, (byte[]) o2);
      }
      if (o1 instanceof char[]) {
          return (o2 instanceof char[]) &&
                  Arrays.equals((char[]) o1, (char[]) o2);
      }
      if (o1 instanceof boolean[]) {
          return (o2 instanceof boolean[]) &&
                  Arrays.equals((boolean[]) o1, (boolean[]) o2);
      }
      return o1.equals(o2);
  }
}
 
