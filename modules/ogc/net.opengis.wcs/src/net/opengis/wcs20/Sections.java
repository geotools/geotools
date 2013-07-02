/**
 */
package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sections</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.Sections#getSection <em>Section</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getSections()
 * @model extendedMetaData="name='Sections' kind='elementOnly'"
 * @generated
 */
public interface Sections extends EObject {
    /**
     * @model unique="false" required="true"
     *        extendedMetaData="kind='element' name='Section' namespace='##targetNamespace'"
     */
    EList<Section> getSection();

} // DescribeCoverageType
