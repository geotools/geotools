/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import net.opengis.fes20.FilterCapabilitiesType;

import net.opengis.ows11.CapabilitiesBaseType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>WFS Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.WFSCapabilitiesType#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wfs20.WFSCapabilitiesType#getFeatureTypeList <em>Feature Type List</em>}</li>
 *   <li>{@link net.opengis.wfs20.WFSCapabilitiesType#getFilterCapabilities <em>Filter Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getWFSCapabilitiesType()
 * @model extendedMetaData="name='WFS_CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface WFSCapabilitiesType extends CapabilitiesBaseType {
    /**
     * Returns the value of the '<em><b>WSDL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>WSDL</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>WSDL</em>' containment reference.
     * @see #setWSDL(WSDLType)
     * @see net.opengis.wfs20.Wfs20Package#getWFSCapabilitiesType_WSDL()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='WSDL' namespace='##targetNamespace'"
     * @generated
     */
    WSDLType getWSDL();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.WFSCapabilitiesType#getWSDL <em>WSDL</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>WSDL</em>' containment reference.
     * @see #getWSDL()
     * @generated
     */
    void setWSDL(WSDLType value);

    /**
     * Returns the value of the '<em><b>Feature Type List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Type List</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Type List</em>' containment reference.
     * @see #setFeatureTypeList(FeatureTypeListType)
     * @see net.opengis.wfs20.Wfs20Package#getWFSCapabilitiesType_FeatureTypeList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='FeatureTypeList' namespace='##targetNamespace'"
     * @generated
     */
    FeatureTypeListType getFeatureTypeList();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.WFSCapabilitiesType#getFeatureTypeList <em>Feature Type List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Type List</em>' containment reference.
     * @see #getFeatureTypeList()
     * @generated
     */
    void setFeatureTypeList(FeatureTypeListType value);

    /**
     * Returns the value of the '<em><b>Filter Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Filter Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Filter Capabilities</em>' containment reference.
     * @see #setFilterCapabilities(FilterCapabilitiesType)
     * @see net.opengis.wfs20.Wfs20Package#getWFSCapabilitiesType_FilterCapabilities()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Filter_Capabilities' namespace='http://www.opengis.net/fes/2.0'"
     * @generated
     */
    FilterCapabilitiesType getFilterCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.WFSCapabilitiesType#getFilterCapabilities <em>Filter Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter Capabilities</em>' containment reference.
     * @see #getFilterCapabilities()
     * @generated
     */
    void setFilterCapabilities(FilterCapabilitiesType value);

} // WFSCapabilitiesType
