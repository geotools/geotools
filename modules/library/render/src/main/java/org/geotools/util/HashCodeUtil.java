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

import java.lang.reflect.Array;
import java.util.Arrays;

/**
* Collected methods which allow easy implementation of <code>hashCode</code>.
*
* Example use case:
* <pre>
*  public int hashCode(){
*    int result = HashCodeUtil.SEED;
*    //collect the contributions of various fields
*    result = HashCodeUtil.hash(result, fPrimitive);
*    result = HashCodeUtil.hash(result, fObject);
*    result = HashCodeUtil.hash(result, fArray);
*    return result;
*  }
* </pre>
 *
 * @deprecated Use {@link Utilities} instead.
*/
@Deprecated
public final class HashCodeUtil {

  /**
  * An initial value for a <code>hashCode</code>, to which is added contributions
  * from fields. Using a non-zero value decreases collisons of <code>hashCode</code>
  * values.
  */
  public static final int SEED = 23;

  /**
  * booleans.
  */
  public static int hash( int aSeed, boolean aBoolean ) {
    return firstTerm( aSeed ) + ( aBoolean ? 1 : 0 );
  }
  
  public static int hash( boolean aBoolean ) {
      return ( aBoolean ? 1 : 0 );
    }

  /**
  * chars.
  */
  public static int hash( int aSeed, char aChar ) {
    return  firstTerm( aSeed )+(int)aChar;
  }
  
  public static int hash( char aChar ) {
      return (int)aChar;
    }

  /**
  * ints.
  */
  public static int hash( int aSeed , int aInt ) {
    /*
    * Implementation Note
    * Note that byte and short are handled by this method, through
    * implicit conversion.
    */
    return firstTerm( aSeed )+ aInt;
  }
  
  public static int hash(  int aInt ) {
      return  aInt;
    }

  /**
  * longs.
  */
  public static int hash( int aSeed , long aLong ) {
    return  firstTerm(aSeed)  +(int)( aLong ^ (aLong >>> 32) );
  }
  
  public static int hash(  long aLong ) {
      return (int)( aLong ^ (aLong >>> 32) );
    }

  /**
  * floats.
  */
  public static int hash( int aSeed , float aFloat ) {
    return hash( aSeed, Float.floatToIntBits(aFloat) );
  }
  public static int hash( float aFloat ) {
      return Float.floatToIntBits(aFloat);
    }
  /**
  * doubles.
  */
  public static int hash( int aSeed , double aDouble ) {
    return hash( aSeed, Double.doubleToLongBits(aDouble) );
  }
  
  public static int hash( double aDouble ) {
      return hash( 0, Double.doubleToLongBits(aDouble) );
    }

  /**
  * <code>aObject</code> is a possibly-null object field, and possibly an array.
  *
  * If <code>aObject</code> is an array, then each element may be a primitive
  * or a possibly-null object.
  */
  public static int hash( int aSeed , Object aObject ) {
    int result = aSeed;
    if ( aObject == null) {
      result = hash(result, 0);
    }
    else if ( ! isArray(aObject) ) {
      result = hash(result, aObject.hashCode());
    }
    else {
      int length = Array.getLength(aObject);
      for ( int idx = 0; idx < length; ++idx ) {
        Object item = Array.get(aObject, idx);
        //recursive call!
        result = hash(result, item);
      }
    }
    return result;
  }
  
  public static int hash(  Object aObject ) {
      int result = SEED;
      if ( aObject == null) {
        result = hash(result, 0);
      }
      else if ( ! isArray(aObject) ) {
        result = hash(result, aObject.hashCode());
      }
      else {
          if (aObject instanceof Object[]) {
              return Arrays.deepHashCode((Object[]) aObject);
          }
          else
          if (aObject instanceof double[]) {
              return Arrays.hashCode((double[]) aObject);
          }
          else
          if (aObject instanceof float[]) {
              return Arrays.hashCode((float[]) aObject);
          }
          else
          if (aObject instanceof long[]) {
              return Arrays.hashCode((long[]) aObject);
          }
          else
          if (aObject instanceof int[]) {
              return Arrays.hashCode((int[]) aObject);
          }
          else
          if (aObject instanceof short[]) {
              return Arrays.hashCode((short[]) aObject);
          }
          else
          if (aObject instanceof byte[]) {
              return Arrays.hashCode((byte[]) aObject);
          }
          else
          if (aObject instanceof char[]) {
              return Arrays.hashCode((char[]) aObject);
          }
          else
          if (aObject instanceof boolean[]) {
              return Arrays.hashCode((boolean[]) aObject);
          }
      }
      return result;
    }


  /// PRIVATE ///
  private static final int fODD_PRIME_NUMBER = 37;

  private static int firstTerm( int aSeed ){
    return fODD_PRIME_NUMBER * aSeed;
  }

  private static boolean isArray(Object aObject){
    return aObject.getClass().isArray();
  }
} 
