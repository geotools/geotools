/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pass Through Operation Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A pass-through operation specifies that a subset of a coordinate tuple is subject to a specific coordinate operation. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.PassThroughOperationType#getModifiedCoordinate <em>Modified Coordinate</em>}</li>
 *   <li>{@link net.opengis.gml311.PassThroughOperationType#getUsesOperation <em>Uses Operation</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getPassThroughOperationType()
 * @model extendedMetaData="name='PassThroughOperationType' kind='elementOnly'"
 * @generated
 */
public interface PassThroughOperationType extends AbstractCoordinateOperationType {
    /**
     * Returns the value of the '<em><b>Modified Coordinate</b></em>' attribute list.
     * The list contents are of type {@link java.math.BigInteger}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered sequence of positive integers defining the positions in a coordinate tuple of the coordinates affected by this pass-through operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Modified Coordinate</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getPassThroughOperationType_ModifiedCoordinate()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='modifiedCoordinate' namespace='##targetNamespace'"
     * @generated
     */
    EList<BigInteger> getModifiedCoordinate();

    /**
     * Returns the value of the '<em><b>Uses Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the operation applied to the specified ordinates. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Operation</em>' containment reference.
     * @see #setUsesOperation(OperationRefType)
     * @see net.opengis.gml311.Gml311Package#getPassThroughOperationType_UsesOperation()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='usesOperation' namespace='##targetNamespace'"
     * @generated
     */
    OperationRefType getUsesOperation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.PassThroughOperationType#getUsesOperation <em>Uses Operation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Operation</em>' containment reference.
     * @see #getUsesOperation()
     * @generated
     */
    void setUsesOperation(OperationRefType value);

} // PassThroughOperationType
