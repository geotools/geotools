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

import java.util.Random;


/**
 * Utility with various math functions.
 *
 * @author Hans Häggström
 */
public final class MathUtils
{

    //======================================================================
    // Public Constants

    /**
     * Multiply with this to convert an angle from degrees to radians
     */
    public static final float DEGREES_TO_RADIANS = (float) ( Math.PI / 180.0 );


    /**
     * Multiply with this to convert an angle from radians to degrees
     */
    public static final float RADIANS_TO_DEGREES = (float) ( 180.0 / Math.PI );

    /**
     * The golden ratio.
     * <p/>
     * See e.g. <a href="http://en.wikipedia.org/wiki/Golden_ratio">Wikipedia on Golden Ratio</a>.
     */
    public static final float GOLDEN_RATIO = 1.618033989f;

    //======================================================================
    // Private Constants

    private static final Random theTempRandom = new Random();

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Static Methods

    /**
     * @param value
     *
     * @return the input value, clamped to the 0..1 range.
     */
    public static float clampToZeroToOne( float value )
    {
        if ( value < 0 )
        {
            value = 0;
        }
        if ( value > 1 )
        {
            value = 1;
        }
        return value;
    }


    /**
     * Clamps the given value to the -1..1 range.
     */
    public static float clampToMinusOneToOne( float value )
    {
        if ( value < -1 )
        {
            value = -1;
        }

        if ( value > 1 )
        {
            value = 1;
        }

        return value;
    }


    /**
     * Does a linear interpolation.
     *
     * @param t when 0, the result is a, when 1, the result is b.
     * @param a value at start of range
     * @param b value at end of range
     *
     * @return an interpolated value between a and b (or beyond), with the relative position t.
     */
    public static float interpolate( float t, float a, float b )
    {
        return a + t * ( b - a );
    }


    /**
     * Does a linear interpolation using doubles
     *
     * @param t when 0, the result is a, when 1, the result is b.
     * @param a value at start of range
     * @param b value at end of range
     *
     * @return an interpolated value between a and b (or beyond), with the relative position t.
     */
    public static double interpolate( double t, double a, double b )
    {
        return a + t * ( b - a );
    }


    /**
     * Calculates a linearily interpolated value, given a start value and position, an end value and position,
     * and the position to get the value at.
     * <p/>
     * First calculates the relative position, then does a normal linear interpolation between the start and end value,
     * using the relative position as the interpolation factor.
     *
     * @param position
     * @param startPosition
     * @param endPosition
     * @param startValue
     * @param endValue
     *
     * @return the interpolated value
     */
    public static double interpolate( final double position,
                                      final double startPosition,
                                      final double endPosition,
                                      final double startValue,
                                      final double endValue )
    {
        final double relativePosition = ( position - startPosition ) / ( endPosition - startPosition );
        return startValue + relativePosition * ( endValue - startValue );
    }


    /**
     * Calculates a linearily interpolated value, given a start value and position, an end value and position,
     * and the position to get the value at.
     * <p/>
     * If the position is outside start or end position, it is treated as if it was at the start or end position respectively.
     * <p/>
     * First calculates the relative position, then does a normal linear interpolation between the start and end value,
     * using the relative position as the interpolation factor.
     *
     * @param position
     * @param startPosition
     * @param endPosition
     * @param startValue
     * @param endValue
     */
    public static double interpolateClamp( final double position,
                                           final double startPosition,
                                           final double endPosition,
                                           final double startValue,
                                           final double endValue )
    {
        // Clamp
        double p = position;
        if ( p < startPosition )
        {
            p = startPosition;
        }
        else if ( p > endPosition )
        {
            p = endPosition;
        }

        return interpolate( p, startPosition, endPosition, startValue, endValue );
    }


    /**
     * Calculates a smoothly (cosine) interpolated value, given a start value, an end value,
     * and the position to get the value at.
     *
     * @param position
     * @param startPosition
     * @param endPosition
     * @param startValue
     * @param endValue
     */
    public static float interpolateSmoothly( final float position,
                                             final float startPosition,
                                             final float endPosition,
                                             final float startValue,
                                             final float endValue )
    {
        final float relativePosition = ( position - startPosition ) / ( endPosition - startPosition );

        // Clamp values at edges
        float result;
        if ( relativePosition <= 0 )
        {
            result = startValue;
        }
        else if ( relativePosition >= 1 )
        {
            result = endValue;
        }
        else
        {
            // Cosine interpolation
            final double relativeSmoothPosition = ( 1.0 - Math.cos( relativePosition * Math.PI ) ) / 2.0;
            result = (float) ( startValue * ( 1.0 - relativeSmoothPosition ) + endValue * relativeSmoothPosition );
        }

        return result;
    }


    /**
     * Wraps the specified value, so that it is in the range 0 to rangeEnd - 1,
     *
     * @param value    the value to wrap
     * @param rangeEnd end of range, non inclusive
     *
     * @return the wrapped value.
     */
    public static int wrapToRange( final int value, final int rangeEnd )
    {
        int pos = value % rangeEnd;
        if ( pos < 0 )
        {
            pos += rangeEnd;
        }
        return pos;
    }


    /**
     * @return the smallest of the given numbers
     */
    public static int min( final int... numbers )
    {
        if ( numbers.length == 0 )
        {
            throw new IllegalArgumentException( "at least one parameter is required" );
        }

        int min = numbers[ 0 ];
        for ( int i = 1; i < numbers.length; i++ )
        {
            int number = numbers[ i ];
            if ( number < min )
            {
                min = number;
            }
        }

        return min;
    }


    /**
     * @return the largest of the given numbers
     */
    public static int max( final int... numbers )
    {
        if ( numbers.length == 0 )
        {
            throw new IllegalArgumentException( "at least one parameter is required" );
        }

        int max = numbers[ 0 ];
        for ( int i = 1; i < numbers.length; i++ )
        {
            int number = numbers[ i ];
            if ( number > max )
            {
                max = number;
            }
        }

        return max;
    }


    /**
     * Calculates an integer based on a floating point value, selecting one of the closest integer numbers randomly,
     * weighted by their relative closeness to the floating point number.
     *
     * @param random     a random number generator to use
     * @param realNumber the fractional value
     *
     * @return one of the two integers closest to the realNumber, weighted by their relative closeness.
     */
    public static int roundFloatToIntStatistically( final Random random, final float realNumber )
    {
        // Get the floor value
        int result = roundDown( realNumber );

        // Check if we should return the ceiling value instead
        if ( random.nextFloat() < calculateProbabilityOfAddingOne( realNumber, result ) )
        {
            result += 1;
        }

        return result;
    }


    /**
     * Round the number down to the closest integer
     */
    public static int roundDown( final float realNumber )
    {
        return (int) Math.round( Math.floor( realNumber ) );
    }


    /**
     * @param value   the value to clamp.
     * @param minimum lower boundary of the range, inclusive.
     * @param maximum upper boundary of the range, inclusive.
     *
     * @return the value clamped to the specified range.
     */
    @SuppressWarnings( { "AssignmentToMethodParameter" } )
    public static int clamp( int value, final int minimum, final int maximum )
    {
        if ( minimum > maximum )
        {
            throw new IllegalArgumentException(
                    "The minimum " + minimum + " is larger than the maximum " + maximum + ", possible bug?" );
        }

        if ( value < minimum )
        {
            value = minimum;
        }
        else if ( value > maximum )
        {
            value = maximum;
        }

        return value;
    }


    /**
     * @param value   the value to clamp.
     * @param minimum lower boundary of the range, inclusive.
     * @param maximum upper boundary of the range, inclusive.
     *
     * @return the value clamped to the specified range.
     */
    @SuppressWarnings( { "AssignmentToMethodParameter" } )
    public static double clampDouble( double value, final double minimum, final double maximum )
    {
        if ( minimum > maximum )
        {
            throw new IllegalArgumentException(
                    "The minimum " + minimum + " is larger than the maximum " + maximum + ", possible bug?" );
        }

        if ( value < minimum )
        {
            value = minimum;
        }
        else if ( value > maximum )
        {
            value = maximum;
        }

        return value;
    }


    /**
     * @param value          the value to test
     * @param lowerInclusive lower boundary value, inclusive.
     * @param upperExclusive upper boundary value, exclusive.
     *
     * @return true if the value is inside the bounds, false if not.
     */
    public static boolean isInsideBounds( final int value, final int lowerInclusive, final int upperExclusive )
    {
        if ( lowerInclusive > upperExclusive )
        {
            throw new IllegalArgumentException(
                    "The lower boundary  " + lowerInclusive + " is larger than the upper boundary " + upperExclusive +
                    ", possible bug?" );
        }

        return value >= lowerInclusive && value < upperExclusive;
    }


    public static float getRandomNumberUsingTwoFloatSeeds( final float firstSeed, final float secondSeed )
    {
        seedRandom( theTempRandom, (long) ( firstSeed * 7237 ), (long) ( secondSeed * 5317 ) );
        return theTempRandom.nextFloat();

/* Previus implementation:
        // Combine the two floats into one long
        theTempRandom.setSeed( (long)(firstSeed*7237) ^ (long)(secondSeed * 5317) );

        // Make sure we get a fairly random value by reading away a few values and self-seeding the RNG
        theTempRandom.nextLong();
        theTempRandom.setSeed( theTempRandom.nextLong() );
        theTempRandom.nextLong();

        return theTempRandom.nextFloat();
*/
    }


    public static long getRandomSeedUsingTwoIntSeeds( final int firstSeed, final int secondSeed )
    {
        seedRandom( theTempRandom, (long) ( firstSeed * 7237 ), (long) ( secondSeed * 5317 ) );
        return theTempRandom.nextLong();
    }


    /**
     * Seeds the specified random number generator, and make the start position a bit more scrambled by reading off a few random values.
     *
     * @param random
     * @param seed
     */
    public static void seedRandom( final Random random, final long seed )
    {
        random.setSeed( seed );
        random.nextLong();
        random.nextLong();
        random.setSeed( random.nextLong() );
        random.nextLong();
        random.nextLong();
        random.nextLong();
    }


    /**
     * Seeds the specified random number generator with the combination of two different random seeds.
     * Also reads off a few random values, to get the random number generator into a bit more random state.
     *
     * @param random
     * @param firstSeed
     * @param secondSeed
     */
    public static void seedRandom( final Random random, final long firstSeed, final long secondSeed )
    {
        // TODO: check on the net if someone has some good algorithm for this..

        random.setSeed( firstSeed );
        random.nextLong();
        random.nextLong();
        random.setSeed( random.nextLong() ^ secondSeed );
        random.nextLong();
        random.nextLong();
        random.nextLong();
    }


    /**
     * Seeds the specified random number generator with the combination of three different random seeds.
     * Also reads off a few random values, to get the random number generator into a bit more random state.
     *
     * @param random
     * @param firstSeed
     * @param secondSeed
     */
    public static void seedRandom( final Random random,
                                   final long firstSeed,
                                   final long secondSeed,
                                   final long thirdSeed )
    {
        random.setSeed( firstSeed );
        random.nextLong();
        random.setSeed( random.nextLong() ^ secondSeed );
        random.nextLong();
        random.setSeed( random.nextLong() ^ thirdSeed );
        random.nextLong();
        random.nextLong();
    }


    /**
     * Rolls the specified integer number, by adding some offset to it, and wrapping the number around to start from
     * the beginning of the range if it grew past the end of the range.
     * <p/>
     * E.g. with a rangeEnd of 10, and a start value of 7, if the rollOffset is 5, the result will be 2.
     *
     * @param originalValue the value to start from when adding the rollOffset.
     * @param rangeEnd      maximum value, non inclusive.
     * @param rollOffset    an offset added to the originalValue.  Can be any positive or negative integer.
     *
     * @return the rolled value.  Will be in the range [0, rangeEnd - 1].
     */
    public static int rollInRange( final int originalValue, final int rangeEnd, final int rollOffset )
    {
        if ( rangeEnd <= 0 )
        {
            throw new IllegalArgumentException( "The rangeEnd parameter (" + rangeEnd + ") should not be smaller than the range start (0)" );
        }

        final int rolledValue = ( originalValue + rangeEnd + ( rollOffset % rangeEnd ) ) % rangeEnd;

        assert rolledValue >= 0 && rolledValue < rangeEnd : "Rolled value out of permitted range. " + rolledValue;

        return rolledValue;
    }


    /**
     * @return True if (x,y) is inside the axis aligned rectangle defined by the points (x1,y1) and (x2,y2), false otherwise.
     */
    public static boolean isInsideRectangle( final double x,
                                             final double y,
                                             final double x1,
                                             final double y1,
                                             final double x2,
                                             final double y2 )
    {
        return x >= x1 &&
               x < x2 &&
               y >= y1 &&
               y < y2;
    }

    //======================================================================
    // Protected Methods

    protected static float calculateProbabilityOfAddingOne( final float realNumber, final int result )
    {
        return realNumber - (float) result;
    }

    //======================================================================
    // Private Methods

    private MathUtils()
    {
    }

}

