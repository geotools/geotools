/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cubic Spline Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Cubic splines are similar to line strings in that they are a sequence of segments each with its own defining function. A cubic spline uses the control points and a set of derivative parameters to define a piecewise 3rd degree polynomial interpolation. Unlike line-strings, the parameterization by arc length is not necessarily still a polynomial. 
 * 				The function describing the curve must be C2, that is, have a continuous 1st and 2nd derivative at all points, and pass through the controlPoints in the order given. Between the control points, the curve segment is defined by a cubic polynomial. At each control point, the polynomial changes in such a manner that the 1st and 2nd derivative vectors are the same from either side. The control parameters record must contain vectorAtStart, and vectorAtEnd which are the unit tangent vectors at controlPoint[1] and controlPoint[n] where n = controlPoint.count. 
 * 				Note: only the direction of the vectors is relevant, not their length.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getPointProperty <em>Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getPointRep <em>Point Rep</em>}</li>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getPosList <em>Pos List</em>}</li>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getVectorAtStart <em>Vector At Start</em>}</li>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getVectorAtEnd <em>Vector At End</em>}</li>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getDegree <em>Degree</em>}</li>
 *   <li>{@link net.opengis.gml311.CubicSplineType#getInterpolation <em>Interpolation</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCubicSplineType()
 * @model extendedMetaData="name='CubicSplineType' kind='elementOnly'"
 * @generated
 */
public interface CubicSplineType extends AbstractCurveSegmentType {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:3'"
     * @generated
     */
    FeatureMap getGroup();

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
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_Pos()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pos' namespace='##targetNamespace' group='#group:3'"
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
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_PointProperty()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointProperty' namespace='##targetNamespace' group='#group:3'"
     * @generated
     */
    EList<PointPropertyType> getPointProperty();

    /**
     * Returns the value of the '<em><b>Point Rep</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.PointPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML version 3.1.0. Use "pointProperty" instead. Included for backwards compatibility with GML 3.0.0.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Point Rep</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_PointRep()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointRep' namespace='##targetNamespace' group='#group:3'"
     * @generated
     */
    EList<PointPropertyType> getPointRep();

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
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_PosList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='posList' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionListType getPosList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CubicSplineType#getPosList <em>Pos List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pos List</em>' containment reference.
     * @see #getPosList()
     * @generated
     */
    void setPosList(DirectPositionListType value);

    /**
     * Returns the value of the '<em><b>Coordinates</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML version 3.1.0. Use "posList" instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coordinates</em>' containment reference.
     * @see #setCoordinates(CoordinatesType)
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_Coordinates()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coordinates' namespace='##targetNamespace'"
     * @generated
     */
    CoordinatesType getCoordinates();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CubicSplineType#getCoordinates <em>Coordinates</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinates</em>' containment reference.
     * @see #getCoordinates()
     * @generated
     */
    void setCoordinates(CoordinatesType value);

    /**
     * Returns the value of the '<em><b>Vector At Start</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * "vectorAtStart" is the unit tangent vector at the start point of the spline.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Vector At Start</em>' containment reference.
     * @see #setVectorAtStart(VectorType)
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_VectorAtStart()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='vectorAtStart' namespace='##targetNamespace'"
     * @generated
     */
    VectorType getVectorAtStart();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CubicSplineType#getVectorAtStart <em>Vector At Start</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vector At Start</em>' containment reference.
     * @see #getVectorAtStart()
     * @generated
     */
    void setVectorAtStart(VectorType value);

    /**
     * Returns the value of the '<em><b>Vector At End</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * "vectorAtEnd" is the unit tangent vector at the end point of the spline.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Vector At End</em>' containment reference.
     * @see #setVectorAtEnd(VectorType)
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_VectorAtEnd()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='vectorAtEnd' namespace='##targetNamespace'"
     * @generated
     */
    VectorType getVectorAtEnd();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CubicSplineType#getVectorAtEnd <em>Vector At End</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vector At End</em>' containment reference.
     * @see #getVectorAtEnd()
     * @generated
     */
    void setVectorAtEnd(VectorType value);

    /**
     * Returns the value of the '<em><b>Degree</b></em>' attribute.
     * The default value is <code>"3"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The degree for a cubic spline is "3".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Degree</em>' attribute.
     * @see #isSetDegree()
     * @see #unsetDegree()
     * @see #setDegree(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_Degree()
     * @model default="3" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='attribute' name='degree'"
     * @generated
     */
    BigInteger getDegree();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CubicSplineType#getDegree <em>Degree</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Degree</em>' attribute.
     * @see #isSetDegree()
     * @see #unsetDegree()
     * @see #getDegree()
     * @generated
     */
    void setDegree(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.CubicSplineType#getDegree <em>Degree</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetDegree()
     * @see #getDegree()
     * @see #setDegree(BigInteger)
     * @generated
     */
    void unsetDegree();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.CubicSplineType#getDegree <em>Degree</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Degree</em>' attribute is set.
     * @see #unsetDegree()
     * @see #getDegree()
     * @see #setDegree(BigInteger)
     * @generated
     */
    boolean isSetDegree();

    /**
     * Returns the value of the '<em><b>Interpolation</b></em>' attribute.
     * The default value is <code>"cubicSpline"</code>.
     * The literals are from the enumeration {@link net.opengis.gml311.CurveInterpolationType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute "interpolation" specifies the curve interpolation mechanism used for this segment. This mechanism
     * uses the control points and control parameters to determine the position of this curve segment. For a CubicSpline the interpolation is fixed as "cubicSpline".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interpolation</em>' attribute.
     * @see net.opengis.gml311.CurveInterpolationType
     * @see #isSetInterpolation()
     * @see #unsetInterpolation()
     * @see #setInterpolation(CurveInterpolationType)
     * @see net.opengis.gml311.Gml311Package#getCubicSplineType_Interpolation()
     * @model default="cubicSpline" unsettable="true"
     *        extendedMetaData="kind='attribute' name='interpolation'"
     * @generated
     */
    CurveInterpolationType getInterpolation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CubicSplineType#getInterpolation <em>Interpolation</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.gml311.CubicSplineType#getInterpolation <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetInterpolation()
     * @see #getInterpolation()
     * @see #setInterpolation(CurveInterpolationType)
     * @generated
     */
    void unsetInterpolation();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.CubicSplineType#getInterpolation <em>Interpolation</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Interpolation</em>' attribute is set.
     * @see #unsetInterpolation()
     * @see #getInterpolation()
     * @see #setInterpolation(CurveInterpolationType)
     * @generated
     */
    boolean isSetInterpolation();

} // CubicSplineType
