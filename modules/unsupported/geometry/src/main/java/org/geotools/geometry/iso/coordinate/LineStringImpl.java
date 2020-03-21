/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.util.DoubleOperation;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.ParamForPoint;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveInterpolation;

/**
 * A LineString consists of sequence of line segments, each having a parameterization like the one
 * for LineSegment. The class essentially combines a Sequence<LineSegments> into a single object,
 * with the obvious savings of storage space.
 *
 * @author Jackson Roehrig & Sanjay Jena
 */
public class LineStringImpl extends CurveSegmentImpl implements LineString {
    private static final long serialVersionUID = -4463400288452707656L;

    /**
     * The controlPoints of a LineString are a sequence of positions between which the curve is
     * linearly interpolated. The first position in the sequence is the startPoint of the
     * LineString, and the last point in the sequence is the endPoint of the LineString.
     * LineString::controlPoint : PointArray
     */
    private PointArrayImpl controlPoints;

    /** Envelope of the LineString Has to be calculated when creating the instance */
    private EnvelopeImpl envelope = null;

    public LineStringImpl(List<Position> positions) {
        this(new PointArrayImpl(positions), 0.0);
    }
    /**
     * Constructor by another LineString The constructor for LineString takes a sequence of points
     * and constructs a LineString with those points as controlPoints. The constructor of a
     * LineString takes two or more positions and creates the appropriate line string joining them.
     * LineString::LineString(points[2..n]:Position):LineString
     */
    public LineStringImpl(LineStringImpl lineString) {
        super(lineString);
        super.setInterpolation(CurveInterpolation.LINEAR);
        // this.controlPoints = new PointArrayImpl(lineString.controlPoints);

        this.controlPoints = new PointArrayImpl(lineString.controlPoints);
        this.endParam = lineString.endParam;
        // this.envelope = new EnvelopeImpl(lineString.getEnvelope());
        this.envelope = new EnvelopeImpl(lineString.getEnvelope());
    }

    /** Constructor by a PointArray and a StartParam */
    public LineStringImpl(PointArrayImpl pointArray, double startPar) {
        super(startPar);
        super.setInterpolation(CurveInterpolation.LINEAR);
        if (pointArray.length() >= 2) {
            this.controlPoints = pointArray;
        } else {
            throw new IllegalArgumentException(
                    "A LineString needs at least two control points."); // $NON-NLS-1$
        }

        /*
         * Set length of PointArray (which represents the LineString) as EndParam. The StartParam
         * and EndParam will be updated when creating a curve. The parametrisation of this
         * LineString should be continous according to the Curve
         */
        this.setEndParam(DoubleOperation.add(startPar, this.controlPoints.getDistanceSum()));
        // this.setEndParam(startPar + this.controlPoints.getDistanceSum());

        /* Create envelope */
        this.envelope = this.controlPoints.getEnvelope();
    }

    /**
     * Merges this LineString with another LineString, forming a new LineString The input
     * LineStrings will not be modified by this operation. The used Lists of the pointArray will be
     * cloned. The Positions which represents the control points will not be cloned.
     *
     * @param other LineString
     */
    public LineStringImpl merge(LineStringImpl other) {
        // Test ok (SJ)

        LineStringImpl result;

        if (other.getEndPoint().equals(this.getStartPoint())) {
            LinkedList<Position> posToAdd = new LinkedList<Position>(other.getControlPoints());
            posToAdd.removeLast();
            posToAdd.addAll(getControlPoints());
            result = new LineStringImpl(posToAdd);

        } else if (this.getEndPoint().equals(other.getStartPoint())) {
            LinkedList<Position> posToAdd = new LinkedList<Position>(getControlPoints());
            posToAdd.removeLast();
            posToAdd.addAll(other.getControlPoints());
            result = new LineStringImpl(posToAdd);
        } else {
            throw new IllegalArgumentException(
                    "The LineString do not agree in a start and end point");
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.LineString#getControlPoints()
     */
    public PointArray getControlPoints() {
        // ok
        return this.controlPoints;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.LineString#asLineSegments()
     * @version Not verified whether getLineSegments() in PointArray is correctly implemented
     */
    // TODO: This code depends on a specific implementation of PointArray, PointArrayImpl has very
    // little to do with its inner class LineSegmentsSequence (it can be factored out)
    //
    public List<LineSegment> asLineSegments() {
        // test OK (SJ)
        // Returns the control points in the PointArray as a sequence of LineSegments
        return (List<LineSegment>) this.controlPoints.getLineSegments(this.getCurve());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericCurve#getStartPoint()
     */
    public DirectPosition getStartPoint() {
        // ok
        /* Return DirectPosition of first Control Point */
        return controlPoints.getFirst().getDirectPosition();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericCurve#getEndPoint()
     * @version Implementation OK
     */
    public DirectPosition getEndPoint() {
        // ok
        /* Return Direct Position of last Control Point */
        return controlPoints.getLast().getDirectPosition();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.geometry.featgeom.coordinate.CurveSegmentImpl#getStartPosition()
     */
    public Position getStartPosition() {
        // ok
        // Returns the first Point of the control points PointArray
        return controlPoints.getFirst();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.geometry.featgeom.coordinate.CurveSegmentImpl#getEndPosition()
     */
    public Position getEndPosition() {
        // ok
        // Returns the last Point of the control points PointArray
        return controlPoints.getLast();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.geometry.featgeom.coordinate.CurveSegmentImpl#split(double)
     */
    public void split(double maxSpacing) {
        // TODO semantic SJ, JR
        // TODO implementation
        // TODO test
        // TODO documentation

        // gdavis: this method seems to be broken, needs fixing
        // this.controlPoints.split(maxSpacing);
        throw new UnsupportedOperationException("not implemented yet.");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.primitive.CurveSegment#getNumDerivativesAtStart()
     */
    public int getNumDerivativesAtStart() {
        // Return 0, because linestrings can not support continuity above C^0
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.primitive.CurveSegment#getNumDerivativesInterior()
     */
    public int getNumDerivativesInterior() {
        // Return 0, because linestrings can not support continuity above C^0
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.primitive.CurveSegment#getNumDerivativesAtEnd()
     */
    public int getNumDerivativesAtEnd() {
        // Return 0, because linestrings can not support continuity above C^0
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.primitive.CurveSegment#getSamplePoints()
     * @version Implementation OK
     */
    public PointArray getSamplePoints() {
        // ok
        // Return the control points PointArray as sample points of this LineString
        return this.controlPoints;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.primitive.CurveSegment#reverse()
     */
    public CurveSegmentImpl reverse() {
        // Test OK
        // Reverse the order of control points which define this line string
        // The parametrisation does not need to be corrected, because the PointArray will calculate
        // the parametrisation at runtime
        Collections.reverse(controlPoints);
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericCurve#getTangent(double)
     */
    public double[] getTangent(double distance) {
        // Test OK - Valid for n dimensional space

        if (distance < this.getStartParam() || distance > this.getEndParam())
            throw new IllegalArgumentException("Distance parameter not in parametrisation range.");

        /* Search segment at distance */
        List<LineSegment> segments = this.asLineSegments();
        int i = 0;
        while (segments.get(i).getEndParam() < distance && i < segments.size()) {
            i++;
        }
        /* Delegate work to according LineSegment */
        return ((LineSegmentImpl) segments.get(i)).getTangentInSegment(distance);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericCurve#forConstructiveParam(double)
     */
    public DirectPosition forConstructiveParam(double cp) {
        // Test ok - valid for n dimensional space

        // Return the Position at param (cp * lengthOfLineString)
        double par = DoubleOperation.mult(cp, this.getCurve().length());
        return this.forParam(par);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.geometry.featgeom.coordinate.CurveSegmentImpl#getEnvelope()
     */
    public EnvelopeImpl getEnvelope() {
        // Returns the envelope of this LineString
        return this.envelope;
        // Alternate solution is to let the point array calculate the envelope on the fly:
        // return this.controlPoints.getEnvelope();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericCurve#forParam(double)
     */
    public DirectPosition forParam(double distance) {
        // Test OK - Valid for n dimensional space

        if (distance < this.getStartParam() || distance > this.getEndParam())
            throw new IllegalArgumentException("Distance parameter not in parametrisation range.");

        /* Search segment at distance */
        List<LineSegment> segments = this.asLineSegments();
        int i = 0;
        while (((LineSegmentImpl) segments.get(i)).endParam < distance && i < segments.size()) {
            i++;
        }
        /* Delegate work to according LineSegment */
        return ((LineSegmentImpl) segments.get(i)).forParamInSegment(distance);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericCurve#getParamForPoint(org.opengis.geometry.coordinate.DirectPosition)
     */
    public ParamForPoint getParamForPoint(DirectPosition p) {
        // TODO semantic SJ, JR
        // TODO implementation
        // TODO test
        // TODO documentation

        // TO DO JR: check with a discussion in geotools/codehaus about the
        // return parameters.
        throw new UnsupportedOperationException("not implemented yet.");
        // assert false;
        // return null;
        // /* Receive LineString as LineSegments */
        // List<LineSegmentImpl> segments = this.asLineSegments();
        //
        // /* ArrayList for storing all found minimum distance positions */
        // ArrayList<DirectPositionImpl> rPositions = new
        // ArrayList<DirectPositionImpl>();
        //
        // ParamForPointImpl rParams = new ParamForPointImpl(this);
        //
        // /* Get closest Point on first Segment */
        // DirectPositionImpl actPos = segments.get(0).closestPoint(p);
        // /* Add to List of all minimum distance positions */
        // rPositions.add(actPos);
        // // JR: Sanjay, das entspricht rParams.add(segments[0].startParam() +
        // d), mit d aus closestPoint
        // double dist =
        // DirectPositionImpl.distance(segments.get(0).getStartPoint(),actPos);
        // rParams.add(segments.get(0).getStartParam() + dist);
        // /* Calculate distance between those two points */
        // double minDistance = DirectPositionImpl.distance(p,actPos);
        //
        // /* Loop all other segments and compare minimum distance */
        // for (int i=1, n=segments.size(); i<n; i++) {
        //
        // /* Check distance to next segment */
        // actPos = segments.get(i).closestPoint(p);
        // double actDistance = actDistance =
        // DirectPositionImpl.distance(p,actPos);
        //
        // /* If distance smaller or equal than previous minDistance, then safe
        // this as new minDistance */
        // if (actDistance <= minDistance) {
        // /* If actual distance is smaller than previous, clear list of all
        // minimum distance positions, because a new minium distance was found
        // */
        // if (actDistance < minDistance) {
        // rPositions.clear();
        // rParams.clear();
        // }
        // minDistance = actDistance;
        // rPositions.add(actPos);
        // rParams.add(segments.get(i).getStartParam()
        // + DirectPositionImpl.distance(segments.get(i).getStartPoint(),
        // actPos));
        // }
        //
        // }
        // assert false;
        // return rParams;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.GenericCurve#asLineString(double, double)
     */
    public LineStringImpl asLineString(double maxSpacing, double maxOffset) {
        // TODO semantic SJ, JR
        // TODO implementation
        // TODO test
        // TODO documentation

        // assert false;
        // return null;

        // TO DO the ArrayList<Position> approach must disappear, maybe
        // substituted by controlPoint.clone()

        /* If parameters of method are zero, return this object itself (with
        same controlPòints) */
        if (maxSpacing == 0 && maxOffset == 0) return this;

        /* ArrayList of Positions, which will be used as the controlPoints of
        the new LineString */
        // ArrayList<Position> positions = new ArrayList<Position>();
        List<Position> positions = new ArrayList<Position>();

        /* Add Start Point to collection of LineString Positions */
        // positions.add(new Position(this.startPoint()));

        /* The actualParam represents the actual Position on the LineString
         */
        double actualParam = this.startParam;

        /* The newSpacing represents the distance between the Posistion at the actualParam and the next Position;
         * if the maxOffset is small, it will be necessary to define a smaller distance */
        double newSpacing = maxSpacing;

        /* All segments of the actual LineString (this-object) */
        List<LineSegment> segments = this.asLineSegments();

        /* First Segment after actualParam (>=) and LastSegment before actualParam+newSpacing (<=)
         * Define the area which has to be inspected depending the maxOffset
         * In the beginning, the both point at the first Segment */
        int firstSegment = 0;
        int lastSegment = 0;

        double maxDistance = 0;

        // CoordinateFactoryImpl cf = this.getCurve().getGeometryFactory().getCoordinateFactory();

        /* Loop until end of LineString reached */
        while (actualParam < this.endParam) {

            /* If actualParam + newSpacing is greater than the endParam,
             * set the newSpacing to that value, that next Position is the at
             * the endParam of the original LineString */
            if (actualParam + newSpacing > this.endParam) {
                newSpacing = this.endParam - actualParam;
            }

            /* The maximum distance between the the line (actualParam to actualParam+newSpacing) and the Segments (between firstSegment and lastSegment) will be calculated after */
            maxDistance = 0;

            /* Search first segment after position start in LineString:
             * If position is on ControlPoint, gets segment with position as startParam
             * If position is on first segment, get first segment
             * If position is on last Segment, gets last segment */
            int i = firstSegment;
            while ((segments.get(i).getStartParam() < actualParam) && (i < segments.size() - 1)) {
                i++;
            }
            firstSegment = i;

            do {

                /* If start and end point on same LineSegment, then return a distance of 0.0 */
                // if ((segments[firstSegment].startParam() > actualParam &&
                // segments[firstSegment].startParam() < actualParam+newSpacing)
                // || (segments[firstSegment].endParam() > actualParam &&
                // segments[firstSegment].endParam() < actualParam+newSpacing)) {
                // TO DO numerical precision
                // assert false;
                double EPSILON = 0.00001;
                if ((segments.get(firstSegment).getStartParam() > actualParam
                                && segments.get(firstSegment).getStartParam() + EPSILON
                                        < actualParam + newSpacing)
                        || (segments.get(firstSegment).getEndParam() > actualParam
                                && segments.get(firstSegment).getEndParam() + EPSILON
                                        < actualParam + newSpacing)) {

                    /* If not, the segments betweens the both positions have to be
                    checked */

                    /* Search last segment before position start in LineString
                     * If position is on ControlPoint, get segment with position as endParam
                     * If position is on first segment, get first segment
                     * If position is on last segment, get last segment */
                    i = firstSegment;
                    while ((i < segments.size())
                            && (segments.get(i).getEndParam() <= actualParam + newSpacing)) {
                        i++;
                    }
                    lastSegment = i - 1;
                    DirectPosition p1 = this.forParam(actualParam);
                    DirectPosition p2 =
                            new DirectPositionImpl(this.forParam(actualParam + newSpacing));
                    LineSegmentImpl seg = new LineSegmentImpl(p1, p2, 0);
                    // (LineSegmentImpl)cf.createLineSegment(cf.createPosition(this.forParam(actualParam)),cf.createPosition(this.forParam(actualParam+newSpacing)));

                    /* Search maximum of all distances */
                    CurveImpl curve = new CurveImpl(seg);
                    maxDistance =
                            curve.distance(
                                    new PointImpl(segments.get(firstSegment).getStartPoint()));
                    // maxDistance = seg.distance(segments.get(firstSegment).getStartPoint());
                    double actDistance = 0;
                    for (i = firstSegment; i < lastSegment; i++) {
                        actDistance = curve.distance(new PointImpl(segments.get(i).getEndPoint()));
                        // actDistance = seg.distance(segments.get(i).getEndPoint());
                        if (actDistance > maxDistance) {
                            maxDistance = actDistance;
                        }
                    }

                    /* Test, ob Offset bei Param [actualParam+newSpacing] eingehalten
                    wird */
                    if (maxDistance > maxOffset) {
                        // System.out.println("Testausgabe: Musste den Abstand verkleinern. Neuer
                        // Abstand: " + (newSpacing/2));
                        newSpacing /= 2;
                    }

                } else {
                    maxDistance = 0;
                    /* Both positions are located at the same segment;
                    set the maxDistance to 0 to leave loop */
                }

            } while (maxDistance > maxOffset); /* Until Offset is accepted */

            /* Add newSpacing to actualParam */
            actualParam += newSpacing;

            /* Reset newSpacing to the original value */
            newSpacing = maxSpacing;

            /* Add Position at constrParam position of actualParam */
            positions.add(new DirectPositionImpl(this.forParam(actualParam)));
            // positions.add(cf.createPosition(this.forParam(actualParam)));
        }

        // /* Transform Arraylist to Array of Position´s */
        // Position posArray[] = new Position[positions.size()];
        // for (int i=0; i<positions.size(); i++) {
        // posArray[i] = (Position)positions.get(i);
        // }

        /* Construct new LineString, giving the positions, curve and
        startParam */
        // TO DO JR check: I think there was an error here because the new LineString
        // should not belong to any curve
        // LineString rLineString = new LineString (posArray, this.getCurve(), this.startParam);
        LineStringImpl rLineString = new LineStringImpl(positions);
        // LineStringImpl rLineString = cf.createLineString(positions, this.startParam);
        rLineString.setEndParam(this.getEndParam());

        return rLineString;
    }

    public String toString() {
        return "[LineString: " + this.controlPoints + "]"; // $NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((controlPoints == null) ? 0 : controlPoints.hashCode());
        result = PRIME * result + ((envelope == null) ? 0 : envelope.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof LineStringImpl)) return false;
        //		if (getClass() != obj.getClass())
        //			return false;
        final LineStringImpl other = (LineStringImpl) obj;
        if (controlPoints == null) {
            if (other.controlPoints != null) return false;
        } else if (!controlPoints.equals(other.controlPoints)) return false;
        if (envelope == null) {
            if (other.envelope != null) return false;
        } else if (!envelope.equals(other.envelope)) return false;
        return true;
    }

    // Not used!
    // /**
    // */
    // public void merge(double minSpacing) {
    // // TO DO test
    // // TO DO documentation
    // this.controlPoints.merge(minSpacing);
    // }

}
