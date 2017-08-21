/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Linear Ring Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A LinearRing is defined by four or more coordinate tuples, with linear interpolation between them; the first and last coordinates must be coincident.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.LinearRingType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.LinearRingType#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.LinearRingType#getPointProperty <em>Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.LinearRingType#getPointRep <em>Point Rep</em>}</li>
 *   <li>{@link net.opengis.gml311.LinearRingType#getPosList <em>Pos List</em>}</li>
 *   <li>{@link net.opengis.gml311.LinearRingType#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.LinearRingType#getCoord <em>Coord</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getLinearRingType()
 * @model extendedMetaData="name='LinearRingType' kind='elementOnly'"
 * @generated
 */
public interface LinearRingType extends AbstractRingType {
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
     * @see net.opengis.gml311.Gml311Package#getLinearRingType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:10'"
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
     * @see net.opengis.gml311.Gml311Package#getLinearRingType_Pos()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pos' namespace='##targetNamespace' group='#group:10'"
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
     * @see net.opengis.gml311.Gml311Package#getLinearRingType_PointProperty()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointProperty' namespace='##targetNamespace' group='#group:10'"
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
     * @see net.opengis.gml311.Gml311Package#getLinearRingType_PointRep()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointRep' namespace='##targetNamespace' group='#group:10'"
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
     * @see net.opengis.gml311.Gml311Package#getLinearRingType_PosList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='posList' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionListType getPosList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.LinearRingType#getPosList <em>Pos List</em>}' containment reference.
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
     * @see net.opengis.gml311.Gml311Package#getLinearRingType_Coordinates()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coordinates' namespace='##targetNamespace'"
     * @generated
     */
    CoordinatesType getCoordinates();

    /**
     * Sets the value of the '{@link net.opengis.gml311.LinearRingType#getCoordinates <em>Coordinates</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinates</em>' containment reference.
     * @see #getCoordinates()
     * @generated
     */
    void setCoordinates(CoordinatesType value);

    /**
     * Returns the value of the '<em><b>Coord</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CoordType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML version 3.0 and included for backwards compatibility with GML 2. Use "pos" elements instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coord</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getLinearRingType_Coord()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coord' namespace='##targetNamespace'"
     * @generated
     */
    EList<CoordType> getCoord();

} // LinearRingType
