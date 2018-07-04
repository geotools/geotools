/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Method Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of an algorithm used to perform a coordinate operation. Most operation methods use a number of operation parameters, although some coordinate conversions use none. Each coordinate operation using the method assigns values to these parameters. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.OperationMethodType#getMethodID <em>Method ID</em>}</li>
 *   <li>{@link net.opengis.gml311.OperationMethodType#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.OperationMethodType#getMethodFormula <em>Method Formula</em>}</li>
 *   <li>{@link net.opengis.gml311.OperationMethodType#getSourceDimensions <em>Source Dimensions</em>}</li>
 *   <li>{@link net.opengis.gml311.OperationMethodType#getTargetDimensions <em>Target Dimensions</em>}</li>
 *   <li>{@link net.opengis.gml311.OperationMethodType#getUsesParameter <em>Uses Parameter</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getOperationMethodType()
 * @model extendedMetaData="name='OperationMethodType' kind='elementOnly'"
 * @generated
 */
public interface OperationMethodType extends OperationMethodBaseType {
    /**
     * Returns the value of the '<em><b>Method ID</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IdentifierType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Set of alternative identifications of this operation method. The first methodID, if any, is normally the primary identification code, and any others are aliases. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Method ID</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getOperationMethodType_MethodID()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='methodID' namespace='##targetNamespace'"
     * @generated
     */
    EList<IdentifierType> getMethodID();

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Comments on or information about this operation method, including source information.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getOperationMethodType_Remarks()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OperationMethodType#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Method Formula</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Formula(s) used by this operation method. The value may be a reference to a publication. Note that the operation method may not be analytic, in which case this element references or contains the procedure, not an analytic formula.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Method Formula</em>' containment reference.
     * @see #setMethodFormula(CodeType)
     * @see net.opengis.gml311.Gml311Package#getOperationMethodType_MethodFormula()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='methodFormula' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getMethodFormula();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OperationMethodType#getMethodFormula <em>Method Formula</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method Formula</em>' containment reference.
     * @see #getMethodFormula()
     * @generated
     */
    void setMethodFormula(CodeType value);

    /**
     * Returns the value of the '<em><b>Source Dimensions</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Number of dimensions in the source CRS of this operation method. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Source Dimensions</em>' attribute.
     * @see #setSourceDimensions(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getOperationMethodType_SourceDimensions()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='sourceDimensions' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getSourceDimensions();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OperationMethodType#getSourceDimensions <em>Source Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Dimensions</em>' attribute.
     * @see #getSourceDimensions()
     * @generated
     */
    void setSourceDimensions(BigInteger value);

    /**
     * Returns the value of the '<em><b>Target Dimensions</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Number of dimensions in the target CRS of this operation method. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Target Dimensions</em>' attribute.
     * @see #setTargetDimensions(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getOperationMethodType_TargetDimensions()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='targetDimensions' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getTargetDimensions();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OperationMethodType#getTargetDimensions <em>Target Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Dimensions</em>' attribute.
     * @see #getTargetDimensions()
     * @generated
     */
    void setTargetDimensions(BigInteger value);

    /**
     * Returns the value of the '<em><b>Uses Parameter</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.AbstractGeneralOperationParameterRefType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of associations to the set of operation parameters and parameter groups used by this operation method. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Parameter</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getOperationMethodType_UsesParameter()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='usesParameter' namespace='##targetNamespace'"
     * @generated
     */
    EList<AbstractGeneralOperationParameterRefType> getUsesParameter();

} // OperationMethodType
