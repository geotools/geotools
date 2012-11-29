/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Accept Formats Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Prioritized sequence of zero or more GetCapabilities
 *       operation response formats desired by client, with preferred formats
 *       listed first. Each response format shall be identified by its MIME type.
 *       See AcceptFormats parameter use subclause for more
 *       information.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.AcceptFormatsType#getOutputFormat <em>Output Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getAcceptFormatsType()
 * @model extendedMetaData="name='AcceptFormatsType' kind='elementOnly'"
 * @generated
 */
public interface AcceptFormatsType extends EObject {
    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Format</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Format</em>' attribute.
     * @see #setOutputFormat(String)
     * @see net.opengis.ows20.Ows20Package#getAcceptFormatsType_OutputFormat()
     * @model unique="false" 
     *        extendedMetaData="kind='element' name='OutputFormat' namespace='##targetNamespace'"
     */
    EList<String> getOutputFormat();

} // AcceptFormatsType
