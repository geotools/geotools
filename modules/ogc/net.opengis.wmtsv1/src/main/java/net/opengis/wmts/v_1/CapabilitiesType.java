/**
 */
package net.opengis.wmts.v_1;

import net.opengis.ows11.CapabilitiesBaseType;
import net.opengis.ows11.OnlineResourceType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.CapabilitiesType#getContents <em>Contents</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.CapabilitiesType#getThemes <em>Themes</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.CapabilitiesType#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.CapabilitiesType#getServiceMetadataURL <em>Service Metadata URL</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getCapabilitiesType()
 * @model extendedMetaData="name='Capabilities_._type' kind='elementOnly'"
 * @generated
 */
public interface CapabilitiesType extends CapabilitiesBaseType {
    /**
     * Returns the value of the '<em><b>Contents</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata about the data served by this server. 
     * 								For WMTS, this section SHALL contain data about layers and 
     * 								TileMatrixSets
     * <!-- end-model-doc -->
     * @return the value of the '<em>Contents</em>' containment reference.
     * @see #setContents(ContentsType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getCapabilitiesType_Contents()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Contents' namespace='##targetNamespace'"
     * @generated
     */
    ContentsType getContents();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.CapabilitiesType#getContents <em>Contents</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Contents</em>' containment reference.
     * @see #getContents()
     * @generated
     */
    void setContents(ContentsType value);

    /**
     * Returns the value of the '<em><b>Themes</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.ThemesType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 								Metadata describing a theme hierarchy for the layers
     * 								
     * <!-- end-model-doc -->
     * @return the value of the '<em>Themes</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getCapabilitiesType_Themes()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Themes' namespace='##targetNamespace'"
     * @generated
     */
    EList<ThemesType> getThemes();

    /**
     * Returns the value of the '<em><b>WSDL</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.OnlineResourceType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a WSDL resource
     * <!-- end-model-doc -->
     * @return the value of the '<em>WSDL</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getCapabilitiesType_WSDL()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='WSDL' namespace='##targetNamespace'"
     * @generated
     */
    EList<OnlineResourceType> getWSDL();

    /**
     * Returns the value of the '<em><b>Service Metadata URL</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.OnlineResourceType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 								Reference to a ServiceMetadata resource on resource 
     * 								oriented architectural style
     * 								
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Metadata URL</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getCapabilitiesType_ServiceMetadataURL()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ServiceMetadataURL' namespace='##targetNamespace'"
     * @generated
     */
    EList<OnlineResourceType> getServiceMetadataURL();

} // CapabilitiesType
