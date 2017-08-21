/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>User Defined CS Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A two- or three-dimensional coordinate system that consists of any combination of coordinate axes not covered by any other coordinate system type. An example is a multilinear coordinate system which contains one coordinate axis that may have any 1-D shape which has no intersections with itself. This non-straight axis is supplemented by one or two straight axes to complete a 2 or 3 dimensional coordinate system. The non-straight axis is typically incrementally straight or curved. A UserDefinedCS shall have two or three usesAxis associations. 
 * <!-- end-model-doc -->
 *
 *
 * @see net.opengis.gml311.Gml311Package#getUserDefinedCSType()
 * @model extendedMetaData="name='UserDefinedCSType' kind='elementOnly'"
 * @generated
 */
public interface UserDefinedCSType extends AbstractCoordinateSystemType {
} // UserDefinedCSType
