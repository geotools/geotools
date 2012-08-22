/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import net.opengis.ows10.CapabilitiesBaseType;

import org.opengis.filter.capability.FilterCapabilities;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This type extends ows:CapabilitiesBaseType defined in OGC-05-008
 *          to include information about supported OGC filter components. A
 *          profile may extend this type to describe additional capabilities.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.CapabilitiesType#getFilterCapabilities <em>Filter Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getCapabilitiesType()
 * @model extendedMetaData="name='CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface CapabilitiesType extends CapabilitiesBaseType {
    /**
     * Returns the value of the '<em><b>Filter Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Filter Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Filter Capabilities</em>' containment reference.
     * @see #setFilterCapabilities(FilterCapabilities)
     * @see net.opengis.cat.csw20.Csw20Package#getCapabilitiesType_FilterCapabilities()
     * @model type="net.opengis.cat.csw20.FilterCapabilities" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Filter_Capabilities' namespace='http://www.opengis.net/ogc'"
     * @generated
     */
    FilterCapabilities getFilterCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.CapabilitiesType#getFilterCapabilities <em>Filter Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter Capabilities</em>' containment reference.
     * @see #getFilterCapabilities()
     * @generated
     */
    void setFilterCapabilities(FilterCapabilities value);

} // CapabilitiesType
