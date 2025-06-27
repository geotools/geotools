/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util.geom;

import java.util.Collection;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    private static GeometryFactory geomFactory;
    private static PrecisionModel precModel;

    public static boolean isEqual(Coordinate[] c1, Coordinate[] c2) {
        return isEqual(c1, c2, false);
    }

    public static boolean isEqual(Coordinate[] c1, Coordinate[] c2, boolean reverse) {
        if (c1.length != c2.length) return false;

        if (!reverse) {
            for (int i = 0; i < c1.length; i++) {
                if (!c1[i].equals(c2[i])) return false;
            }
            return true;
        }

        for (int i = 0; i < c1.length; i++) {
            if (!c1[i].equals(c2[c1.length - i - 1])) return false;
        }
        return true;
    }

    public static LineString joinLinestrings(LineString l1, LineString l2) {
        Coordinate[] merged = new Coordinate[l1.getNumPoints() + l2.getNumPoints() - 1];

        // linestrings could join in one of 4 ways:
        // tip to tail
        // tail to tip
        // tip to tip
        // tail to tail
        if (l1.getCoordinateN(l1.getNumPoints() - 1).equals(l2.getCoordinateN(0))) {
            // tip to tail
            for (int i = 0; i < l1.getNumPoints(); i++) {
                merged[i] = l1.getCoordinateN(i);
            }
            for (int i = 0; i < l2.getNumPoints() - 1; i++) {
                merged[i + l1.getNumPoints()] = l2.getCoordinateN(i + 1);
            }
        } else if (l2.getCoordinateN(l2.getNumPoints() - 1).equals(l1.getCoordinateN(0))) {
            // tail to tip
            for (int i = 0; i < l2.getNumPoints(); i++) {
                merged[i] = l2.getCoordinateN(i);
            }
            for (int i = 0; i < l1.getNumPoints() - 1; i++) {
                merged[i + l2.getNumPoints()] = l1.getCoordinateN(i + 1);
            }
        } else if (l1.getCoordinateN(l1.getNumPoints() - 1).equals(l2.getCoordinateN(l2.getNumPoints() - 1))) {
            // tip to tip
            for (int i = 0; i < l1.getNumPoints(); i++) {
                merged[i] = l1.getCoordinateN(i);
            }
            for (int i = 0; i < l2.getNumPoints() - 1; i++) {
                merged[i + l1.getNumPoints()] = l2.getCoordinateN(l2.getNumPoints() - 2 - i);
            }
        } else if (l1.getCoordinateN(0).equals(l2.getCoordinateN(0))) {
            // tail to tail
            for (int i = 0; i < l2.getNumPoints(); i++) {
                merged[i] = l2.getCoordinateN(l2.getNumPoints() - 1 - i);
            }

            for (int i = 0; i < l1.getNumPoints() - 1; i++) {
                merged[i + l2.getNumPoints()] = l1.getCoordinateN(i + 1);
            }
        } else return null;

        return gf().createLineString(merged);
    }

    public static double angleBetween(LineSegment l1, LineSegment l2, double tol) {
        // analyze slopes
        // TODO straight vertical lines
        double s1 = (l1.p1.y - l1.p0.y) / (l1.p1.x - l1.p0.x);
        double s2 = (l2.p1.y - l2.p0.y) / (l2.p1.x - l2.p0.x);

        if (Math.abs(s1 - s2) < tol) return 0;
        if (Math.abs(s1 + s2) < tol) return Math.PI;

        // not of equal slope, transform lines so that they are tail to tip and
        // use the cosine law to calculate angle between

        // transform line segments tail to tail, originating at (0,0)
        LineSegment tls1 = new LineSegment(new Coordinate(0, 0), new Coordinate(l1.p1.x - l1.p0.x, l1.p1.y - l1.p0.y));
        LineSegment tls2 = new LineSegment(new Coordinate(0, 0), new Coordinate(l2.p1.x - l2.p0.x, l2.p1.y - l2.p0.y));

        // line segment for third side of triangle
        LineSegment ls3 = new LineSegment(tls1.p1, tls2.p1);

        double c = ls3.getLength();
        double a = tls1.getLength();
        double b = tls2.getLength();

        return Math.acos((a * a + b * b - c * c) / (2 * a * b));
    }

    public static double angleBetween(LineString l1, LineString l2, double tol) {
        LineSegment ls1 =
                new LineSegment(l1.getCoordinateN(l1.getNumPoints() - 2), l1.getCoordinateN(l1.getNumPoints() - 1));
        LineSegment ls2 = new LineSegment(l2.getCoordinateN(0), l2.getCoordinateN(1));

        return angleBetween(ls1, ls2, tol);
    }

    public static double dx(LineString ls) {
        return ls.getPointN(ls.getNumPoints() - 1).getX() - ls.getPointN(0).getX();
    }

    public static double dy(LineString ls) {
        return ls.getPointN(ls.getNumPoints() - 1).getY() - ls.getPointN(0).getY();
    }

    //  public static Geometry reverseGeometry(Geometry geometry) {
    //    if (geometry instanceof Point) return(geometry);
    //    if (geometry instanceof LineString) {
    //      return(
    //        gf().createLineString(reverseCoordinates(geometry.getCoordinates()))
    //      );
    //    }
    //    //TODO: implement polygon and multi geometries
    //    return(null);
    //  }

    //  public static Coordinate[] reverseCoordinates(Coordinate[] c) {
    //    int n = c.length;
    //    Coordinate[] reversed = new Coordinate[n];
    //    for (int i = 0; i < n; i++) reversed[i] = c[n-i-1];
    //    return(reversed);
    //  }

    public static Geometry reverseGeometry(Geometry geom, boolean modify) {
        if (geom instanceof Point) return geom;
        if (geom instanceof LineString) {
            Coordinate[] reversed = reverseCoordinates(geom.getCoordinates(), modify);
            if (modify) return geom;
            else return gf().createLineString(reversed);
        }
        return null;
    }

    public static Coordinate[] reverseCoordinates(Coordinate[] c, boolean modify) {
        if (modify) {
            int n = c.length / 2; // truncate if odd number

            for (int i = 0; i < n; i++) {
                Coordinate tmp = c[i];
                c[i] = c[c.length - 1 - i];
                c[c.length - 1 - i] = tmp;
            }

            return c;
        } else {
            Coordinate[] cnew = new Coordinate[c.length];
            for (int i = 0; i < c.length; i++) {
                cnew[i] = c[c.length - 1 - i];
            }
            return cnew;
        }
    }

    public static double averageDistance(LineString to, Collection from) {
        double avg = 0;
        int n = 0;
        for (Object o : from) {
            LineString ls = (LineString) o;
            n += ls.getNumPoints();
            for (int i = 0; i < ls.getNumPoints(); i++) {
                avg += ls.getPointN(i).distance(to);
            }
        }
        return avg / n;
    }

    public static LineString simplifyLineString(LineString line) {
        double x = 0d;
        double y = 0d;
        int n = line.getNumPoints();

        for (int i = 0; i < n; i++) {
            Coordinate c = line.getCoordinateN(i);
            x += c.x;
            y += c.y;
        }

        x /= n;
        y /= n;

        LineString simple = gf().createLineString(
                        new Coordinate[] {line.getCoordinateN(0), new Coordinate(x, y), line.getCoordinateN(n - 1)});

        return simple;
    }

    public static PrecisionModel basicPrecisionModel() {
        return pm();
    }

    public static GeometryFactory gf() {
        if (geomFactory == null) {
            geomFactory = new GeometryFactory();
        }
        return geomFactory;
    }

    public static PrecisionModel pm() {
        if (precModel == null) {
            precModel = new PrecisionModel();
        }
        return precModel;
    }

    //  public static LineString normalize(LineString line, double sample) {
    //    Coordinate[] orig = line.getCoordinates();
    //    ArrayList normal = new ArrayList();
    //
    //
    //    return(null);
    //  }

    public static LineString normalizeLinestring(LineString line, double sample) {
        Coordinate[] c = line.getCoordinates();
        boolean[] remove = new boolean[c.length];
        int nremove = 0;

        double[] add = new double[c.length];
        int nadd = 0;

        // special case if linestirng 2 coordinates
        if (c.length == 2) {
            if (distance(c, 0, 1) > sample) {
                // do the point interepolation
                int n = (int) (distance(c, 0, 1) / sample);
                if (n > 1) {
                    nadd += n - 1;
                    add[0] = distance(c, 0, 1) / n;
                }
            } else return line;
        } else {
            int i = 0;
            while (i < c.length - 2) {
                // Coordinate c1 = c[i];
                int j = i + 1;

                while (j < c.length - 1) {
                    // Coordinate c2 = c[j];
                    if (distance(c, i, j) < sample) {
                        remove[j] = true;
                        nremove++;
                        j++;
                    } else break;
                }

                int n = (int) (distance(c, i, j) / sample);
                if (n > 1) {
                    add[i] = distance(c, i, j) / n;
                    nadd += n - 1;
                }

                i = j;
            }

            // the last two points that will not be removed may need to be adjusted
            for (int k = c.length - 2; k >= 1; k--) {
                if (!remove[k]) {
                    // if distance between this point and last point is less then sample
                    // remove this point, and find the point before it, and asjust the
                    // interval in which to add points

                    if (distance(c, c.length - 1, k) < sample) {
                        remove[k] = true;
                        nremove++;

                        // move backward to find the last coordinate that wasn't removed
                        // and readjust any points that were added
                        int l = k - 1;
                        for (; l >= 0; l--) {
                            if (!remove[l]) break;
                        }

                        if (l > -1) {
                            int n = (int) (distance(c, l, k) / sample);
                            if (n > 1) {
                                add[l] = 0d;
                                nadd -= n - 1;
                            }

                            // recalculate
                            n = (int) (distance(c, l, c.length - 1) / sample);
                            if (n > 1) {
                                add[l] = distance(c, l, c.length - 1) / n;
                                nadd += n - 1;
                            }
                        }
                    } else {
                        // if the last point is the second to last in the coordinate
                        // array, we may have to add points inbetween
                        if (k == c.length - 2) {
                            // determine if we need to add any points between last two points
                            int n = (int) (distance(c, k, c.length - 1) / sample);
                            if (n > 1) {
                                nadd += n - 1;
                                add[k] = distance(c, k, c.length - 1) / n;
                            }
                        }
                    }

                    break;
                }
            }

            //	    if (!remove[c.length-2]) {
            //	      if (c[c.length-1].distance(c[c.length-2]) < sample) {
            //		      remove[c.length-2] = true;
            //		      nremove++;
            //
            //		      //move backward to find the last coordinate that wasn't removed
            //		      // and readjust any points that were added
            //		      int k = c.length-3;
            //		      for (; k >= 0; k--) {
            //		        if (!remove[k]) break;
            //		      }
            //
            //		      if (k > -1) {
            //		        int n = (int) (c[k].distance(c[c.length-2]) / sample);
            //		        if (n > 1) {
            //		          add[k] = 0d;
            //		          nadd -= (n-1);
            //		        }
            //
            //		        //recalculate
            //		        n = (int)(c[k].distance(c[c.length-1]) / sample);
            //		        if (n > 1) {
            //		          add[k] = c[k].distance(c[c.length-1]) / ((double)n);
            //		          nadd += n-1;
            //		        }
            //
            //		      }
            //		    }
            //		    else {
            //		      //determine if we need to add any points between last two points
            //		      int n = (int) (c[c.length-2].distance(c[c.length-1]) / sample);
            //	        if (n > 1) {
            //	          nadd += n-1;
            //	          add[c.length-2] = c[c.length-2].distance(c[c.length-1]) / ((double)n);
            //	        }
            //
            //		    }
            //	    }
        }

        Coordinate[] newc = new Coordinate[c.length - nremove + nadd];
        // Coordinate[] newc = new Coordinate[c.length-nremove];
        int j = 0;
        for (int i = 0; i < c.length; i++) {
            if (!remove[i]) {
                newc[j++] = c[i];
                if (add[i] > 0d) {
                    int next = -1;
                    for (int k = i + 1; k < c.length && next == -1; k++) {
                        if (!remove[k]) next = k;
                    }
                    if (next == -1) continue;

                    double dx = (c[next].x - c[i].x) * add[i] / distance(c, i, next);
                    double dy = (c[next].y - c[i].y) * add[i] / distance(c, i, next);

                    int n = (int) (distance(c, i, next) / add[i] + +0.000001);
                    for (int k = 1; k < n; k++) {
                        newc[j++] = new Coordinate(c[i].x + k * dx, c[i].y + k * dy);
                    }
                }
            }
        }

        //    for (int i = 0; i < newc.length; i++) {
        //      Coordinate coord = newc[i];
        //      if (coord != null)
        //        System.out.println("POINT(" + coord.x + " " + coord.y + ")");
        //      else System.out.println("null");
        //    }

        return gf().createLineString(newc);
    }

    public static double distance(Coordinate[] c, int i, int j) {
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }

        double dist = 0d;
        for (int k = i; k < j; k++) {
            dist += c[k].distance(c[k + 1]);
        }

        return dist;
    }
}
