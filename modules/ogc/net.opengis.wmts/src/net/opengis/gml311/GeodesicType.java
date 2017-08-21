/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geodesic Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A Geodesic consists of two distinct
 *    positions joined by a geodesic curve. The control points of
 *    a Geodesic shall lie on the geodesic between its start
 *    point and end points. Between these two points, a geodesic
 *    curve defined from ellipsoid or geoid model used by the
 *    co-ordinate reference systems may be used to interpolate
 *    other positions. Any other point in the controlPoint array
 *    must fall on this geodesic.
 * <!-- end-model-doc -->
 *
 *
 * @see net.opengis.gml311.Gml311Package#getGeodesicType()
 * @model extendedMetaData="name='GeodesicType' kind='elementOnly'"
 * @generated
 */
public interface GeodesicType extends GeodesicStringType {
} // GeodesicType
