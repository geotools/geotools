/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Accept Versions Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Prioritized sequence of one or more specification
 *       versions accepted by client, with preferred versions listed first. See
 *       Version negotiation subclause for more information.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.AcceptVersionsType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getAcceptVersionsType()
 * @model extendedMetaData="name='AcceptVersionsType' kind='elementOnly'"
 * @generated
 */
public interface AcceptVersionsType extends EObject {
    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see net.opengis.ows20.Ows20Package#getAcceptVersionsType_Version()
     * @model unique="false" required="true"
     *        extendedMetaData="kind='element' name='Version' namespace='##targetNamespace'"
     */
     EList<String> getVersion();

} // AcceptVersionsType
