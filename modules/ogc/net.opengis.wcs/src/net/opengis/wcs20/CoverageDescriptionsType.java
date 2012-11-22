/**
 */
package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Descriptions Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.CoverageDescriptionsType#getCoverageDescription <em>Coverage Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getCoverageDescriptionsType()
 * @model extendedMetaData="name='CoverageDescriptionsType' kind='elementOnly'"
 * @generated
 */
public interface CoverageDescriptionsType extends EObject {
    /**
     * Returns the value of the '<em><b>Coverage Description</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs20.CoverageDescriptionType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description of a coverage available from a WCS server. This description shall include sufficient information to allow all valid GetCoverage operation requests to be prepared by a WCS client.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Description</em>' containment reference list.
     * @see net.opengis.wcs20.Wcs20Package#getCoverageDescriptionsType_CoverageDescription()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='CoverageDescription' namespace='##targetNamespace'"
     * @generated
     */
    EList<CoverageDescriptionType> getCoverageDescription();

} // CoverageDescriptionsType
