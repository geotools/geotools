/**
 */
package net.opengis.wcs20;

import java.lang.Object;
import org.eclipse.emf.common.util.EList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extension Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.ExtensionType#getContents <em>Contents</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getExtensionType()
 * @model extendedMetaData="name='ExtensionType' kind='elementOnly'"
 * @generated
 */
public interface ExtensionType extends EObject {
    /**
     * Returns the value of the '<em><b>Contents</b></em>' reference list.
     * The list contents are of type {@link java.lang.Object}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Contents</em>' reference list.
     * @see net.opengis.wcs20.Wcs20Package#getExtensionType_Contents()
     * @model extendedMetaData="kind='elementWildcard' wildcards='##other' name=':0' processing='lax'"
     */
    EList<ExtensionItemType> getContents();

} // ExtensionType
