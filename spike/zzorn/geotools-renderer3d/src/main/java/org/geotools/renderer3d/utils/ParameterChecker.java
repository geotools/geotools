/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer3d.utils;

import java.util.Collection;
import java.util.Map;


/**
 * Utility methods for doing various common checks to method parameters, and throwing IllegalArgumentException if
 * necessary.
 *
 * @author Hans Häggström
 */
public final class ParameterChecker
{

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Static Methods

    /**
     * Checks that the specified parameter is not infinite and not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     *
     * @throws IllegalArgumentException if the check fails.
     */
    public static void checkNormalNumber( double parameter, String parameterName )
    {
        if ( Double.isInfinite( parameter ) )
        {
            throwIllegalArgumentException( parameterName, parameter, "be a normal number" );
        }
        if ( Double.isNaN( parameter ) )
        {
            throwIllegalArgumentException( parameterName, parameter, "be a normal number" );
        }
    }


    /**
     * Checks that the specified parameter is not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     *
     * @throws IllegalArgumentException if the check fails.
     */
    public static void checkNotNaN( double parameter, String parameterName )
    {
        if ( Double.isNaN( parameter ) )
        {
            throwIllegalArgumentException( parameterName, parameter, "not be NaN" );
        }
    }


    /**
     * Checks that the specified parameter is positive and not infinite and not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     *
     * @throws IllegalArgumentException if the check fails.
     * @deprecated use checkPositiveOrZeroNormalNumber to avoid unnecessary complicated method name.
     */
    public static void checkNonNegativeNormalNumber( double parameter, String parameterName )
    {
        checkPositiveOrZeroNormalNumber( parameter, parameterName );
    }


    /**
     * Checks that the specified parameter is positive and not infinite and not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     *
     * @throws IllegalArgumentException if the check fails.
     */
    public static void checkPositiveOrZeroNormalNumber( double parameter, String parameterName )
    {
        checkNormalNumber( parameter, parameterName );

        if ( parameter < 0 )
        {
            throwIllegalArgumentException( parameterName, parameter, "be a normal positive number" );
        }
    }


    /**
     * Checks that the specified parameter is positive, not zero, not infinite and not NaN (not a number).
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     *
     * @throws IllegalArgumentException if the check fails.
     */
    public static void checkPositiveNonZeroNormalNumber( double parameter, String parameterName )
    {
        checkNormalNumber( parameter, parameterName );

        if ( parameter <= 0 )
        {
            throwIllegalArgumentException( parameterName, parameter, "be a normal positive non zero number" );
        }
    }


    /**
     * Checks that the specified parameter is positive and not zero.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     *
     * @throws IllegalArgumentException if the check fails.
     */
    public static void checkPositiveNonZeroInteger( int parameter, String parameterName )
    {
        if ( parameter <= 0 )
        {
            throwIllegalArgumentException( parameterName, parameter, "be a positive non zero number" );
        }
    }


    /**
     * Checks that the specified parameter is positive or zero.
     *
     * @param parameter     the parameter value to check
     * @param parameterName the name of the parameter (used in error messages)
     *
     * @throws IllegalArgumentException if the check fails.
     */
    public static void checkNonNegativeInteger( int parameter, String parameterName )
    {
        if ( parameter < 0 )
        {
            throwIllegalArgumentException( parameterName, parameter, "be a positive or zero number" );
        }
    }


    public static void checkNotNull( final Object parameter, final String parameterName )
    {
        if ( parameter == null )
        {
            throwIllegalArgumentException( parameterName, parameter, "not be null" );
        }
    }


    public static void checkZeroToOneInclusive( final double parameter, String parameterName )
    {
        checkNormalNumber( parameter, parameterName );

        if ( parameter < 0 || parameter > 1 )
        {
            throwIllegalArgumentException( parameterName, parameter, "be in the range 0 to 1 inclusive" );
        }
    }


    public static void checkNormalNumberInRange( final double parameter,
                                                 String parameterName,
                                                 double minimumValueInclusize,
                                                 double maximumValueExclusive )
    {
        checkNormalNumber( parameter, parameterName );

        if ( parameter < minimumValueInclusize || parameter >= maximumValueExclusive )
        {
            throwIllegalArgumentException( parameterName,
                                           parameter,
                                           "be in the range " + minimumValueInclusize + " (inclusive) to " + maximumValueExclusive + " (exclusive)" );
        }
    }


    public static void checkIntegerInRange( final int parameter,
                                            String parameterName,
                                            int minimumValueInclusize,
                                            int maximumValueExclusive )
    {
        if ( parameter < minimumValueInclusize || parameter >= maximumValueExclusive )
        {
            throwIllegalArgumentException( parameterName,
                                           parameter,
                                           "be in the range " + minimumValueInclusize + " (inclusive) to " + maximumValueExclusive + " (exclusive)" );
        }
    }


    public static void checkIntegerEqualsOrLargerThan( final int parameter,
                                                       String parameterName,
                                                       int minimumValueInclusize )
    {
        if ( parameter < minimumValueInclusize )
        {
            throwIllegalArgumentException( parameterName, parameter, "be larger or equal to " + minimumValueInclusize );
        }
    }


    public static void checkLargerThan( final double parameter,
                                        String parameterName,
                                        double minimumValueExclusive,
                                        String thresholdName )
    {
        if ( parameter <= minimumValueExclusive )
        {
            throwIllegalArgumentException( parameterName,
                                           parameter,
                                           "be larger than " + thresholdName + " (" + minimumValueExclusive + ")" );
        }
    }


    public static void checkNotAlreadyContained( final Object element,
                                                 final Collection collection,
                                                 final String listName )
    {
        if ( collection.contains( element ) )
        {
            throw new IllegalArgumentException( "The " + listName + " already contains " + describeElementType( element ) );
        }
    }


    public static void checkNotAlreadyContained( final Object key, final Map map, final String mapName )
    {
        if ( map.containsKey( key ) )
        {
            throw new IllegalArgumentException( "The " + mapName + " already contains the key '" + key + "'" );
        }
    }


    public static void checkContained( final Object element,
                                       final Collection collection,
                                       final String collectionName )
    {
        if ( !collection.contains( element ) )
        {
            throw new IllegalArgumentException( "The " + collectionName + " doesn't contain " + describeElementType(
                    element ) );
        }
    }


    public static void checkIsInstanceOf( final Object parameter,
                                          final String parameterName,
                                          final Class expectedParameterType )
    {
        ParameterChecker.checkNotNull( parameter, parameterName );

        if ( !expectedParameterType.isInstance( parameter ) )
        {
            throwIllegalArgumentException( parameterName,
                                           "of type '" + parameter.getClass() + "'",
                                           "be of type '" + expectedParameterType + "'" );
        }
    }


    /**
     * Throws an exception if the specified size of a parameter array is not equal to the expected size.
     */
    public static void checkArrayLength( final int parameterArrayLength,
                                         final String parameterArrayName,
                                         final int expectedLength )
    {
        checkNonNegativeInteger( expectedLength, "expectedLength" );

        if ( parameterArrayLength != expectedLength )
        {
            throwIllegalArgumentException( parameterArrayName,
                                           "of length " + parameterArrayLength,
                                           "have the length " + expectedLength );
        }
    }


    public static void checkNotEqual( final Object firstParameter, final Object secondParameter,
                                      final String firstParameterName, final String secondParameterName )
    {
        if ( ( firstParameter == null && secondParameter == null ) ||
             ( firstParameter != null && firstParameter.equals( secondParameter ) ) ||
             ( secondParameter != null && secondParameter.equals( firstParameter ) ) )
        {
            throw new IllegalArgumentException(
                    "The parameters '" + firstParameterName + "' and '" + secondParameterName +
                    "' should not be equal, but they were '" + firstParameter + "' and '" + secondParameter + "'." );
        }
    }

    //======================================================================
    // Private Methods

    private ParameterChecker()
    {
    }


    private static void throwIllegalArgumentException( final String parameterName,
                                                       final double parameter,
                                                       final String expectedCondition )
    {
        throwIllegalArgumentException( parameterName, new Double( parameter ), expectedCondition );
    }


    private static void throwIllegalArgumentException( final String parameterName,
                                                       final Object parameter,
                                                       final String expectedCondition )
    {
        throw new IllegalArgumentException(
                "The parameter '" + parameterName + "' should " + expectedCondition + ", but it was '" + parameter + "'." );
    }


    private static String describeElementType( final Object element )
    {
        final String elementDesc;
        if ( element == null )
        {
            elementDesc = "a null element.";
        }
        else
        {
            elementDesc = "the " + element.getClass() + "  '" + element + "'.";
        }

        return elementDesc;
    }

}
