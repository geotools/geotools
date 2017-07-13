/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geodesic String Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A GeodesicString consists of sequence of
 *    geodesic segments. The type essentially combines a sequence of
 *    Geodesic into a single object.
 *    The GeodesicString is computed from two or more positions and an
 *    interpolation using geodesics defined from the geoid (or 
 *    ellipsoid) of the co-ordinate reference system being used.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.GeodesicStringType#getPosList <em>Pos List</em>}</li>
 *   <li>{@link net.opengis.gml311.GeodesicStringType#getGeometricPositionGroup <em>Geometric Position Group</em>}</li>
 *   <li>{@link net.opengis.gml311.GeodesicStringType#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.GeodesicStringType#getPointProperty <em>Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.GeodesicStringType#getInterpolation <em>Interpolation</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getGeodesicStringType()
 * @model extendedMetaData="name='GeodesicStringType' kind='elementOnly'"
 * @generated
 */
public interface GeodesicStringType extends AbstractCurveSegmentType {
    /**
     * Returns the value of the '<em><b>Pos List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pos List</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pos List</em>' containment reference.
     * @see #setPosList(DirectPositionListType)
     * @see net.opengis.gml311.Gml311Package#getGeodesicStringType_PosList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='posList' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionListType getPosList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeodesicStringType#getPosList <em>Pos List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pos List</em>' containment reference.
     * @see #getPosList()
     * @generated
     */
    void setPosList(DirectPositionListType value);

    /**
     * Returns the value of the '<em><b>Geometric Position Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geometric Position Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geometric Position Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getGeodesicStringType_GeometricPositionGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='GeometricPositionGroup:4'"
     * @generated
     */
    FeatureMap getGeometricPositionGroup();

    /**
     * Returns the value of the '<em><b>Pos</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.DirectPositionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pos</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pos</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getGeodesicStringType_Pos()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pos' namespace='##targetNamespace' group='#GeometricPositionGroup:4'"
     * @generated
     */
    EList<DirectPositionType> getPos();

    /**
     * Returns the value of the '<em><b>Point Property</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.PointPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a point via the XLink-attributes or contains the point element. pointProperty 
     * 			is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that 
     * 			is substitutable for Point.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Point Property</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getGeodesicStringType_PointProperty()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointProperty' namespace='##targetNamespace' group='#GeometricPositionGroup:4'"
     * @generated
     */
    EList<PointPropertyType> getPointProperty();

    /**
     * Returns the value of the '<em><b>Interpolation</b></em>' attribute.
     * The default value is <code>"geodesic"</code>.
     * The literals are from the enumeration {@link net.opengis.gml311.CurveInterpolationType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute "interpolation" specifies the
     *      curve interpolation mechanism used for this segment. This
     *      mechanism uses the control points and control parameters to
     *      determine the position of this curve segment. For an 
     *      GeodesicString the interpolation is fixed as "geodesic".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interpolation</em>' attribute.
     * @see net.opengis.gml311.CurveInterpolationType
     * @see #isSetInterpolation()
     * @see #unsetInterpolation()
     * @see #setInterpolation(CurveInterpolationType)
     * @see net.opengis.gml311.Gml311Package#getGeodesicStringType_Interpolation()
     * @model default="geodesic" unsettable="true"
     *        extendedMetaData="kind='attribute' name='interpolation'"
     * @generated
     */
    CurveInterpolationType getInterpolation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeodesicStringType#getInterpolation <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interpolation</em>' attribute.
     * @see net.opengis.gml311.CurveInterpolationType
     * @see #isSetInterpolation()
     * @see #unsetInterpolation()
     * @see #getInterpolation()
     * @generated
     */
    void setInterpolation(CurveInterpolationType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.GeodesicStringType#getInterpolation <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetInterpolation()
     * @see #getInterpolation()
     * @see #setInterpolation(CurveInterpolationType)
     * @generated
     */
    void unsetInterpolation();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.GeodesicStringType#getInterpolation <em>Interpolation</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Interpolation</em>' attribute is set.
     * @see #unsetInterpolation()
     * @see #getInterpolation()
     * @see #setInterpolation(CurveInterpolationType)
     * @generated
     */
    boolean isSetInterpolation();

} // GeodesicStringType
