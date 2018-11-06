package org.geotools.gml3.bindings;

import javax.xml.namespace.QName;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.SingleCurvedGeometry;
import org.geotools.gml3.ArcParameters;
import org.geotools.gml3.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

/**
 * Binding object for the type http://www.opengis.net/gml:ArcStringType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="ArcStringType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;An ArcString is a curve segment that uses three-point circular arc interpolation.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractCurveSegmentType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;choice&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;GML supports two different ways to specify the control points of a curve segment.
 *  1. A sequence of "pos" (DirectPositionType) or "pointProperty" (PointPropertyType) elements. "pos" elements are control points that are only part of this curve segment, "pointProperty" elements contain a point that may be referenced from other geometry elements or reference another point defined outside of this curve segment (reuse of existing points).
 *  2. The "posList" element allows for a compact way to specifiy the coordinates of the control points, if all control points are in the same coordinate reference systems and belong to this curve segment only. The number of direct positions in the list must be at least three.&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                      &lt;choice maxOccurs="unbounded" minOccurs="3"&gt;
 *                          &lt;element ref="gml:pos"/&gt;
 *                          &lt;element ref="gml:pointProperty"/&gt;
 *                          &lt;element ref="gml:pointRep"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;Deprecated with GML version 3.1.0. Use "pointProperty" instead. Included for backwards compatibility with GML 3.0.0.&lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/element&gt;
 *                      &lt;/choice&gt;
 *                      &lt;element ref="gml:posList"/&gt;
 *                      &lt;element ref="gml:coordinates"&gt;
 *                          &lt;annotation&gt;
 *                              &lt;documentation&gt;Deprecated with GML version 3.1.0. Use "posList" instead.&lt;/documentation&gt;
 *                          &lt;/annotation&gt;
 *                      &lt;/element&gt;
 *                  &lt;/choice&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute fixed="circularArc3Points" name="interpolation" type="gml:CurveInterpolationType"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;The attribute "interpolation" specifies the curve interpolation mechanism used for this segment. This mechanism
 *  uses the control points and control parameters to determine the position of this curve segment. For an ArcString the interpolation is fixed as "circularArc3Points".&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *              &lt;attribute name="numArc" type="integer" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;The number of arcs in the arc string can be explicitly stated in this attribute. The number of control points in the arc string must be 2 * numArc + 1.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class ArcStringTypeBinding extends AbstractComplexBinding implements Comparable {

    GeometryFactory gFactory;
    CoordinateSequenceFactory csFactory;
    ArcParameters arcParameters;

    public ArcStringTypeBinding(
            GeometryFactory gFactory,
            CoordinateSequenceFactory csFactory,
            ArcParameters arcParameters) {
        this.gFactory = gFactory;
        this.csFactory = csFactory;
        this.arcParameters = arcParameters;
    }

    /** @generated */
    public QName getTarget() {
        return GML.ArcStringType;
    }

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SingleCurvedGeometry.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        LineString arcLineString = GML3ParsingUtils.lineString(node, gFactory, csFactory);
        CoordinateSequence cs = arcLineString.getCoordinateSequence();
        if (cs.size() < 3) {
            // maybe log this instead and return null
            throw new RuntimeException(
                    "Number of coordinates in an arc string must be at least 3, "
                            + cs.size()
                            + " were specified: "
                            + arcLineString);
        }

        CurvedGeometryFactory factory =
                GML3ParsingUtils.getCurvedGeometryFactory(arcParameters, gFactory, cs);

        return factory.createCurvedGeometry(cs);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("interpolation".equals(name.getLocalPart())) {
            return "circularArc3Points";
        } else if ("posList".equals(name.getLocalPart())) {
            return GML3EncodingUtils.positions((LineString) object);
        }

        return super.getProperty(object, name);
    }

    public int compareTo(Object o) {
        if (o instanceof LineStringTypeBinding
                || o instanceof LineStringSegmentTypeBinding
                || o instanceof ArcTypeBinding) {
            return -1;
        } else {
            return 0;
        }
    }
}
