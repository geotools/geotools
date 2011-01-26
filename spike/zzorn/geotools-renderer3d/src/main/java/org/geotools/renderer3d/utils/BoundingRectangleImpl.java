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

/**
 * An immutable BoundingRectangle.
 *
 * @author Hans Häggström
 */
public final class BoundingRectangleImpl
        implements BoundingRectangle
{

    //======================================================================
    // Private Fields

    private final double myX1;
    private final double myY1;
    private final double myX2;
    private final double myY2;

    // Keep track of the center coordinate also.
    // This allows specifying the center coordinate when creating a parent bounding rectangle to be exacly one of the
    // corner bounding boxes of the child, this ensures there will be no situations where some point lies inside a
    // bounding rectangle but not in any of it's children (subquadrants) due to numerical innaccuracies resulting
    // from different ways to calculate the coordinates.
    // (maybe this is a minor issue that doesn't happen in practice, but at least now it should never happen :-)
    private final double myCenterX;
    private final double myCenterY;

    //======================================================================
    // Private Constants

    private static final int[] OPPOSING_SUBQUADRANT = new int[]{ 3, 2, 1, 0 };
    private static final int[] Y_FLIPPED_SUBQUADRANT = new int[]{ 2, 3, 0, 1 };

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * Creates a new axis aligned BoundingRectangleImpl with a given radius (=half side length) around a centerpoint.
     *
     * @param centerX x coordinate of the center
     * @param centerY y coordinate of the center
     * @param radius  distance to expand from the center along each coordinate axis.
     */
    public BoundingRectangleImpl( double centerX, double centerY, double radius )
    {
        this( centerX - radius,
              centerY - radius,
              centerX + radius,
              centerY + radius,
              centerX,
              centerY );
    }


    /**
     * Creates a new axis aligned BoundingRectangleImpl between the two specified points.
     * The points do not need to be in sorted order, the bounding rectangle will sort them if neccessary.
     *
     * @param xa x coordinate of one corner
     * @param ya y coordinate of one corner
     * @param xb x coordinate of another corner
     * @param yb y coordinate of another corner
     */
    public BoundingRectangleImpl( double xa, double ya, double xb, double yb )
    {
        this( xa, ya, xb, yb, ( xa + xb ) * 0.5, ( ya + yb ) * 0.5 );
    }

    //----------------------------------------------------------------------
    // BoundingRectangle Implementation

    public double getX1()
    {
        return myX1;
    }


    public double getY1()
    {
        return myY1;
    }


    public double getX2()
    {
        return myX2;
    }


    public double getY2()
    {
        return myY2;
    }


    public double getCenterX()
    {
        return myCenterX;
    }


    public double getCenterY()
    {
        return myCenterY;
    }


    public boolean isInside( final LocatedDoublePrecisionObject locatedObject )
    {
        return isInside( locatedObject.getX(), locatedObject.getY() );
    }


    public boolean isInside( double x, double y )
    {
        return MathUtils.isInsideRectangle( x, y, myX1, myY1, myX2, myY2 );
    }


    public boolean isInside( final double x, final double y, final double radius )
    {
        return x >= myX1 + radius &&
               x < myX2 - radius &&
               y >= myY1 + radius &&
               y < myY2 - radius;
    }


    public boolean isEmpty()
    {
        return myX1 >= myX2 || myY1 >= myY2;
    }


    public boolean overlaps( BoundingRectangle boundingRectangle )
    {
        return boundingRectangle.getX2() > myX1 &&
               boundingRectangle.getX1() < myX2 &&
               boundingRectangle.getY2() > myY1 &&
               boundingRectangle.getY1() < myY2;
    }


    public boolean overlaps( final double x1, final double y1,
                             final double x2, final double y2 )
    {
        return x2 > myX1 &&
               x1 < myX2 &&
               y2 > myY1 &&
               y1 < myY2;
    }


    public int getSubquadrantAt( LocatedDoublePrecisionObject position )
    {
        return getSubquadrantAt( position.getX(), position.getY() );
    }


    public int getSubquadrantAt( double x, double y )
    {
        if ( isInside( x, y ) )
        {
            return getSubsectorAt( x, y );
        }
        else
        {
            return -1;
        }
    }


    public int getSubsectorAt( LocatedDoublePrecisionObject position )
    {
        return getSubsectorAt( position.getX(), position.getY() );
    }


    public int getSubsectorAt( final double x, final double y )
    {
        final boolean inRightHalf = x >= myCenterX;
        final boolean inLowerHalf = y >= myCenterY;

        int subquadrantIndex = 0;
        if ( inRightHalf )
        {
            subquadrantIndex += 1;
        }
        if ( inLowerHalf )
        {
            subquadrantIndex += 2;
        }

        return subquadrantIndex;
    }


    public BoundingRectangle createSubquadrantBoundingRectangle( int subquadrantIndex )
    {
        ParameterChecker.checkIntegerInRange( subquadrantIndex, "subquadrantIndex", 0, 4 );

        double x1 = myX1;
        double y1 = myY1;
        double x2 = myX2;
        double y2 = myY2;

        if ( inTopRow( subquadrantIndex ) )
        {
            y2 = myCenterY;
        }
        else
        {
            y1 = myCenterY;
        }

        if ( inLeftColumn( subquadrantIndex ) )
        {
            x2 = myCenterX;
        }
        else
        {
            x1 = myCenterX;
        }

        return new BoundingRectangleImpl( x1, y1, x2, y2 );
    }


    public BoundingRectangle createParentBoundingRectangle( final int subsector )
    {
        final double xSize = myX2 - myX1;
        final double ySize = myY2 - myY1;

        double x1 = myX1;
        double y1 = myY1;
        double x2 = myX2;
        double y2 = myY2;
        final double centerX;
        final double centerY;

        if ( inTopRow( subsector ) )
        {
            y1 -= ySize;
            centerY = myY1;
        }
        else
        {
            y2 += ySize;
            centerY = myY2;
        }

        if ( inLeftColumn( subsector ) )
        {
            x1 -= xSize;
            centerX = myX1;
        }
        else
        {
            x2 += xSize;
            centerX = myX2;
        }

        return new BoundingRectangleImpl( x1, y1, x2, y2, centerX, centerY );
    }


    public int getOppositeSubquadrant( int subquadrant )
    {
        ParameterChecker.checkIntegerInRange( subquadrant, "subquadrant", 0, 4 );

        return OPPOSING_SUBQUADRANT[ subquadrant ];
    }

    public int flipSubquadrantAcrossY( final int subquadrant )
    {
        ParameterChecker.checkIntegerInRange( subquadrant, "subquadrant", 0, 4 );

        return Y_FLIPPED_SUBQUADRANT[ subquadrant ];
    }


    public double getSizeX()
    {
        return myX2 - myX1;
    }


    public double getSizeY()
    {
        return myY2 - myY1;
    }


    public double getSizeAveraged()
    {
        return 0.5 * ( getSizeX() + getSizeY() );
    }

    public BoundingRectangle transform( final double translationX,
                                        final double translationY,
                                        final double scaleX,
                                        final double scaleY )
    {
        return new BoundingRectangleImpl( ( myX1 ) * scaleX + translationX,
                                          ( myY1 ) * scaleY + translationY,
                                          ( myX2 ) * scaleX + translationX,
                                          ( myY2 ) * scaleY + translationY,
                                          ( myCenterX ) * scaleX + translationX,
                                          ( myCenterY ) * scaleY + translationY );
    }

    //----------------------------------------------------------------------
    // Caononical Methods

    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        final BoundingRectangleImpl that = (BoundingRectangleImpl) o;

        if ( Double.compare( that.myCenterX, myCenterX ) != 0 )
        {
            return false;
        }
        if ( Double.compare( that.myCenterY, myCenterY ) != 0 )
        {
            return false;
        }
        if ( Double.compare( that.myX1, myX1 ) != 0 )
        {
            return false;
        }
        if ( Double.compare( that.myX2, myX2 ) != 0 )
        {
            return false;
        }
        if ( Double.compare( that.myY1, myY1 ) != 0 )
        {
            return false;
        }
        if ( Double.compare( that.myY2, myY2 ) != 0 )
        {
            return false;
        }

        return true;
    }


    public int hashCode()
    {
        int result;
        long temp;
        temp = myX1 != +0.0d ? Double.doubleToLongBits( myX1 ) : 0L;
        result = (int) ( temp ^ ( temp >>> 32 ) );
        temp = myY1 != +0.0d ? Double.doubleToLongBits( myY1 ) : 0L;
        result = 31 * result + (int) ( temp ^ ( temp >>> 32 ) );
        temp = myX2 != +0.0d ? Double.doubleToLongBits( myX2 ) : 0L;
        result = 31 * result + (int) ( temp ^ ( temp >>> 32 ) );
        temp = myY2 != +0.0d ? Double.doubleToLongBits( myY2 ) : 0L;
        result = 31 * result + (int) ( temp ^ ( temp >>> 32 ) );
        temp = myCenterX != +0.0d ? Double.doubleToLongBits( myCenterX ) : 0L;
        result = 31 * result + (int) ( temp ^ ( temp >>> 32 ) );
        temp = myCenterY != +0.0d ? Double.doubleToLongBits( myCenterY ) : 0L;
        result = 31 * result + (int) ( temp ^ ( temp >>> 32 ) );
        return result;
    }


    public String toString()
    {
        return "BoundingRectangleImpl{" +
               "x1=" + myX1 +
               ", y1=" + myY1 +
               ", x2=" + myX2 +
               ", y2=" + myY2 +
               ", centerX=" + myCenterX +
               ", centerY=" + myCenterY +
               '}';
    }

    //======================================================================
    // Private Methods

    /**
     * Private constructor that also takes the center coordinates as input.
     */
    private BoundingRectangleImpl( double xa, double ya, double xb, double yb, double centerX, double centerY )
    {
        ParameterChecker.checkNormalNumber( xa, "xa" );
        ParameterChecker.checkNormalNumber( ya, "ya" );
        ParameterChecker.checkNormalNumber( xb, "xb" );
        ParameterChecker.checkNormalNumber( yb, "yb" );
        ParameterChecker.checkNormalNumber( centerX, "centerX" );
        ParameterChecker.checkNormalNumber( centerY, "centerY" );

        // Swap if necessary to make xa <= xb and ya <= yb
        if ( xa > xb )
        {
            final double xt = xa;
            xa = xb;
            xb = xt;
        }
        if ( ya > yb )
        {
            final double yt = ya;
            ya = yb;
            yb = yt;
        }

        myX1 = xa;
        myY1 = ya;
        myX2 = xb;
        myY2 = yb;
        myCenterX = centerX;
        myCenterY = centerY;
    }


    private boolean inLeftColumn( final int subquadrantIndex )
    {
        return subquadrantIndex == 0 || subquadrantIndex == 2;
    }


    private boolean inTopRow( final int subquadrantIndex )
    {
        return subquadrantIndex == 0 || subquadrantIndex == 1;
    }

}
