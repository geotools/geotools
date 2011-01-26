/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Complex Data Combinations Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identifies valid combinations of Format, Encoding, and Schema supported for this input or output. The process shall expect input in or produce output in this combination of Format/Encoding/Schema unless the Execute request specifies otherwise..
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ComplexDataCombinationsType#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getComplexDataCombinationsType()
 * @model extendedMetaData="name='ComplexDataCombinationsType' kind='elementOnly'"
 * @generated
 */
public interface ComplexDataCombinationsType extends EObject {
    /**
     * Returns the value of the '<em><b>Format</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wps10.ComplexDataDescriptionType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A valid combination of MimeType/Encoding/Schema supported for this Input/Output.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getComplexDataCombinationsType_Format()
     * @model type="net.opengis.wps10.ComplexDataDescriptionType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Format'"
     * @generated
     */
    EList getFormat();

} // ComplexDataCombinationsType
