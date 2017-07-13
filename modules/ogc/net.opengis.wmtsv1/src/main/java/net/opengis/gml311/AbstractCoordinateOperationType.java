/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Coordinate Operation Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A mathematical operation on coordinates that transforms or converts coordinates to another coordinate reference system. Many but not all coordinate operations (from CRS A to CRS B) also uniquely define the inverse operation (from CRS B to CRS A). In some cases, the operation method algorithm for the inverse operation is the same as for the forward algorithm, but the signs of some operation parameter values must be reversed. In other cases, different algorithms are required for the forward and inverse operations, but the same operation parameter values are used. If (some) entirely different parameter values are needed, a different coordinate operation shall be defined.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateOperationType#getCoordinateOperationID <em>Coordinate Operation ID</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateOperationType#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateOperationType#getOperationVersion <em>Operation Version</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateOperationType#getValidArea <em>Valid Area</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateOperationType#getScope <em>Scope</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateOperationType#getPositionalAccuracyGroup <em>Positional Accuracy Group</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateOperationType#getPositionalAccuracy <em>Positional Accuracy</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateOperationType#getSourceCRS <em>Source CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateOperationType#getTargetCRS <em>Target CRS</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractCoordinateOperationType' kind='elementOnly'"
 * @generated
 */
public interface AbstractCoordinateOperationType extends AbstractCoordinateOperationBaseType {
    /**
     * Returns the value of the '<em><b>Coordinate Operation ID</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IdentifierType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Set of alternative identifications of this coordinate operation. The first coordinateOperationID, if any, is normally the primary identification code, and any others are aliases. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coordinate Operation ID</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType_CoordinateOperationID()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coordinateOperationID' namespace='##targetNamespace'"
     * @generated
     */
    EList<IdentifierType> getCoordinateOperationID();

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Comments on or information about this coordinate operation, including source information. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType_Remarks()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoordinateOperationType#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Operation Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Version of the coordinate transformation (i.e., instantiation due to the stochastic nature of the parameters). Mandatory when describing a transformation, and should not be supplied for a conversion. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Operation Version</em>' attribute.
     * @see #setOperationVersion(String)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType_OperationVersion()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='operationVersion' namespace='##targetNamespace'"
     * @generated
     */
    String getOperationVersion();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoordinateOperationType#getOperationVersion <em>Operation Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Version</em>' attribute.
     * @see #getOperationVersion()
     * @generated
     */
    void setOperationVersion(String value);

    /**
     * Returns the value of the '<em><b>Valid Area</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Area or region in which this CRS object is valid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Valid Area</em>' containment reference.
     * @see #setValidArea(ExtentType)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType_ValidArea()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='validArea' namespace='##targetNamespace'"
     * @generated
     */
    ExtentType getValidArea();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoordinateOperationType#getValidArea <em>Valid Area</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Area</em>' containment reference.
     * @see #getValidArea()
     * @generated
     */
    void setValidArea(ExtentType value);

    /**
     * Returns the value of the '<em><b>Scope</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description of domain of usage, or limitations of usage, for which this CRS object is valid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Scope</em>' attribute.
     * @see #setScope(String)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType_Scope()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='scope' namespace='##targetNamespace'"
     * @generated
     */
    String getScope();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoordinateOperationType#getScope <em>Scope</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scope</em>' attribute.
     * @see #getScope()
     * @generated
     */
    void setScope(String value);

    /**
     * Returns the value of the '<em><b>Positional Accuracy Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered set of estimates of the impact of this coordinate operation on point position accuracy. Gives position error estimates for target coordinates of this coordinate operation, assuming no errors in source coordinates. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Positional Accuracy Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType_PositionalAccuracyGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='_positionalAccuracy:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getPositionalAccuracyGroup();

    /**
     * Returns the value of the '<em><b>Positional Accuracy</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.AbstractPositionalAccuracyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered set of estimates of the impact of this coordinate operation on point position accuracy. Gives position error estimates for target coordinates of this coordinate operation, assuming no errors in source coordinates. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Positional Accuracy</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType_PositionalAccuracy()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_positionalAccuracy' namespace='##targetNamespace' group='_positionalAccuracy:group'"
     * @generated
     */
    EList<AbstractPositionalAccuracyType> getPositionalAccuracy();

    /**
     * Returns the value of the '<em><b>Source CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the source CRS (coordinate reference system) of this coordinate operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Source CRS</em>' containment reference.
     * @see #setSourceCRS(CRSRefType)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType_SourceCRS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='sourceCRS' namespace='##targetNamespace'"
     * @generated
     */
    CRSRefType getSourceCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoordinateOperationType#getSourceCRS <em>Source CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source CRS</em>' containment reference.
     * @see #getSourceCRS()
     * @generated
     */
    void setSourceCRS(CRSRefType value);

    /**
     * Returns the value of the '<em><b>Target CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the target CRS (coordinate reference system) of this coordinate operation. For constraints on multiplicity of "sourceCRS" and "targetCRS", see UML model of Coordinate Operation package in OGC Abstract Specification topic 2. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Target CRS</em>' containment reference.
     * @see #setTargetCRS(CRSRefType)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateOperationType_TargetCRS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='targetCRS' namespace='##targetNamespace'"
     * @generated
     */
    CRSRefType getTargetCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoordinateOperationType#getTargetCRS <em>Target CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target CRS</em>' containment reference.
     * @see #getTargetCRS()
     * @generated
     */
    void setTargetCRS(CRSRefType value);

} // AbstractCoordinateOperationType
