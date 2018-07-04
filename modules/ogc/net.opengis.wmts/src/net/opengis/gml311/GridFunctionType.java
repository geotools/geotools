/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Grid Function Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines how values in the domain are mapped to the range set. The start point and the sequencing rule are specified here.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.GridFunctionType#getSequenceRule <em>Sequence Rule</em>}</li>
 *   <li>{@link net.opengis.gml311.GridFunctionType#getStartPoint <em>Start Point</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getGridFunctionType()
 * @model extendedMetaData="name='GridFunctionType' kind='elementOnly'"
 * @generated
 */
public interface GridFunctionType extends EObject {
    /**
     * Returns the value of the '<em><b>Sequence Rule</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * If absent, the implied value is "Linear".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Sequence Rule</em>' containment reference.
     * @see #setSequenceRule(SequenceRuleType)
     * @see net.opengis.gml311.Gml311Package#getGridFunctionType_SequenceRule()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='sequenceRule' namespace='##targetNamespace'"
     * @generated
     */
    SequenceRuleType getSequenceRule();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GridFunctionType#getSequenceRule <em>Sequence Rule</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sequence Rule</em>' containment reference.
     * @see #getSequenceRule()
     * @generated
     */
    void setSequenceRule(SequenceRuleType value);

    /**
     * Returns the value of the '<em><b>Start Point</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Index position of the first grid post, which must lie somwhere in the GridEnvelope.  If absent, the startPoint is equal to the value of gridEnvelope::low from the grid definition.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Start Point</em>' attribute.
     * @see #setStartPoint(List)
     * @see net.opengis.gml311.Gml311Package#getGridFunctionType_StartPoint()
     * @model dataType="net.opengis.gml311.IntegerList" many="false"
     *        extendedMetaData="kind='element' name='startPoint' namespace='##targetNamespace'"
     * @generated
     */
    List<BigInteger> getStartPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GridFunctionType#getStartPoint <em>Start Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Point</em>' attribute.
     * @see #getStartPoint()
     * @generated
     */
    void setStartPoint(List<BigInteger> value);

} // GridFunctionType
