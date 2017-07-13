/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Arc String Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An ArcString is a curve segment that uses three-point circular arc interpolation.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ArcStringType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.ArcStringType#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.ArcStringType#getPointProperty <em>Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.ArcStringType#getPointRep <em>Point Rep</em>}</li>
 *   <li>{@link net.opengis.gml311.ArcStringType#getPosList <em>Pos List</em>}</li>
 *   <li>{@link net.opengis.gml311.ArcStringType#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.ArcStringType#getInterpolation <em>Interpolation</em>}</li>
 *   <li>{@link net.opengis.gml311.ArcStringType#getNumArc <em>Num Arc</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getArcStringType()
 * @model extendedMetaData="name='ArcStringType' kind='elementOnly'"
 * @generated
 */
public interface ArcStringType extends AbstractCurveSegmentType {
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
     * @see net.opengis.gml311.Gml311Package#getArcStringType_Group()
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
     * @see net.opengis.gml311.Gml311Package#getArcStringType_Pos()
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
     * @see net.opengis.gml311.Gml311Package#getArcStringType_PointProperty()
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
     * @see net.opengis.gml311.Gml311Package#getArcStringType_PointRep()
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
     * @see net.opengis.gml311.Gml311Package#getArcStringType_PosList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='posList' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionListType getPosList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ArcStringType#getPosList <em>Pos List</em>}' containment reference.
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
     * @see net.opengis.gml311.Gml311Package#getArcStringType_Coordinates()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coordinates' namespace='##targetNamespace'"
     * @generated
     */
    CoordinatesType getCoordinates();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ArcStringType#getCoordinates <em>Coordinates</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinates</em>' containment reference.
     * @see #getCoordinates()
     * @generated
     */
    void setCoordinates(CoordinatesType value);

    /**
     * Returns the value of the '<em><b>Interpolation</b></em>' attribute.
     * The default value is <code>"circularArc3Points"</code>.
     * The literals are from the enumeration {@link net.opengis.gml311.CurveInterpolationType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute "interpolation" specifies the curve interpolation mechanism used for this segment. This mechanism
     * uses the control points and control parameters to determine the position of this curve segment. For an ArcString the interpolation is fixed as "circularArc3Points".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interpolation</em>' attribute.
     * @see net.opengis.gml311.CurveInterpolationType
     * @see #isSetInterpolation()
     * @see #unsetInterpolation()
     * @see #setInterpolation(CurveInterpolationType)
     * @see net.opengis.gml311.Gml311Package#getArcStringType_Interpolation()
     * @model default="circularArc3Points" unsettable="true"
     *        extendedMetaData="kind='attribute' name='interpolation'"
     * @generated
     */
    CurveInterpolationType getInterpolation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ArcStringType#getInterpolation <em>Interpolation</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.gml311.ArcStringType#getInterpolation <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetInterpolation()
     * @see #getInterpolation()
     * @see #setInterpolation(CurveInterpolationType)
     * @generated
     */
    void unsetInterpolation();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.ArcStringType#getInterpolation <em>Interpolation</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Interpolation</em>' attribute is set.
     * @see #unsetInterpolation()
     * @see #getInterpolation()
     * @see #setInterpolation(CurveInterpolationType)
     * @generated
     */
    boolean isSetInterpolation();

    /**
     * Returns the value of the '<em><b>Num Arc</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The number of arcs in the arc string can be explicitly stated in this attribute. The number of control points in the arc string must be 2 * numArc + 1.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Num Arc</em>' attribute.
     * @see #setNumArc(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getArcStringType_NumArc()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='attribute' name='numArc'"
     * @generated
     */
    BigInteger getNumArc();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ArcStringType#getNumArc <em>Num Arc</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Num Arc</em>' attribute.
     * @see #getNumArc()
     * @generated
     */
    void setNumArc(BigInteger value);

} // ArcStringType
