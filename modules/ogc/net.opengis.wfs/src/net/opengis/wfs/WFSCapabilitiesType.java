/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import net.opengis.ows10.CapabilitiesBaseType;
import org.opengis.filter.capability.FilterCapabilities;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             XML encoded WFS GetCapabilities operation response. This
 *             document provides clients with service metadata about a
 *             specific service instance, including metadata about the
 *             tightly-coupled data served. If the server does not implement
 *             the updateSequence parameter, the server shall always return
 *             the complete Capabilities document, without the updateSequence
 *             parameter. When the server implements the updateSequence
 *             parameter and the GetCapabilities operation request included
 *             the updateSequence parameter with the current value, the server
 *             shall return this element with only the "version" and
 *             "updateSequence" attributes. Otherwise, all optional elements
 *             shall be included or not depending on the actual value of the
 *             Contents parameter in the GetCapabilities operation request.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.WFSCapabilitiesType#getFeatureTypeList <em>Feature Type List</em>}</li>
 *   <li>{@link net.opengis.wfs.WFSCapabilitiesType#getServesGMLObjectTypeList <em>Serves GML Object Type List</em>}</li>
 *   <li>{@link net.opengis.wfs.WFSCapabilitiesType#getSupportsGMLObjectTypeList <em>Supports GML Object Type List</em>}</li>
 *   <li>{@link net.opengis.wfs.WFSCapabilitiesType#getFilterCapabilities <em>Filter Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getWFSCapabilitiesType()
 * @model extendedMetaData="name='WFS_CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface WFSCapabilitiesType extends CapabilitiesBaseType {
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
     * @see net.opengis.wfs.WfsPackage#getWFSCapabilitiesType_FeatureTypeList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='FeatureTypeList' namespace='##targetNamespace'"
     * @generated
     */
	FeatureTypeListType getFeatureTypeList();

	/**
     * Sets the value of the '{@link net.opengis.wfs.WFSCapabilitiesType#getFeatureTypeList <em>Feature Type List</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Type List</em>' containment reference.
     * @see #getFeatureTypeList()
     * @generated
     */
	void setFeatureTypeList(FeatureTypeListType value);

	/**
     * Returns the value of the '<em><b>Serves GML Object Type List</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Serves GML Object Type List</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Serves GML Object Type List</em>' containment reference.
     * @see #setServesGMLObjectTypeList(GMLObjectTypeListType)
     * @see net.opengis.wfs.WfsPackage#getWFSCapabilitiesType_ServesGMLObjectTypeList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ServesGMLObjectTypeList' namespace='##targetNamespace'"
     * @generated
     */
	GMLObjectTypeListType getServesGMLObjectTypeList();

	/**
     * Sets the value of the '{@link net.opengis.wfs.WFSCapabilitiesType#getServesGMLObjectTypeList <em>Serves GML Object Type List</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Serves GML Object Type List</em>' containment reference.
     * @see #getServesGMLObjectTypeList()
     * @generated
     */
	void setServesGMLObjectTypeList(GMLObjectTypeListType value);

	/**
     * Returns the value of the '<em><b>Supports GML Object Type List</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Supports GML Object Type List</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Supports GML Object Type List</em>' containment reference.
     * @see #setSupportsGMLObjectTypeList(GMLObjectTypeListType)
     * @see net.opengis.wfs.WfsPackage#getWFSCapabilitiesType_SupportsGMLObjectTypeList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='SupportsGMLObjectTypeList' namespace='##targetNamespace'"
     * @generated
     */
	GMLObjectTypeListType getSupportsGMLObjectTypeList();

	/**
     * Sets the value of the '{@link net.opengis.wfs.WFSCapabilitiesType#getSupportsGMLObjectTypeList <em>Supports GML Object Type List</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Supports GML Object Type List</em>' containment reference.
     * @see #getSupportsGMLObjectTypeList()
     * @generated
     */
	void setSupportsGMLObjectTypeList(GMLObjectTypeListType value);

	/**
     * Returns the value of the '<em><b>Filter Capabilities</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Filter Capabilities</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Filter Capabilities</em>' attribute.
     * @see #setFilterCapabilities(FilterCapabilities)
     * @see net.opengis.wfs.WfsPackage#getWFSCapabilitiesType_FilterCapabilities()
     * @model unique="false" dataType="net.opengis.wfs.FilterCapabilities" required="true"
     *        extendedMetaData="kind='element' name='Filter_Capabilities' namespace='http://www.opengis.net/ogc'"
     * @generated
     */
	FilterCapabilities getFilterCapabilities();

	/**
     * Sets the value of the '{@link net.opengis.wfs.WFSCapabilitiesType#getFilterCapabilities <em>Filter Capabilities</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter Capabilities</em>' attribute.
     * @see #getFilterCapabilities()
     * @generated
     */
    void setFilterCapabilities(FilterCapabilities value);

} // WFSCapabilitiesType
