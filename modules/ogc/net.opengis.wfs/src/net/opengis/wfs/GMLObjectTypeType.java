/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>GML Object Type Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             An element of this type that describes a GML object in an
 *             application namespace shall have an xml xmlns specifier,
 *             e.g. xmlns:bo="http://www.BlueOx.org/BlueOx"
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.GMLObjectTypeType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs.GMLObjectTypeType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs.GMLObjectTypeType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs.GMLObjectTypeType#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wfs.GMLObjectTypeType#getOutputFormats <em>Output Formats</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getGMLObjectTypeType()
 * @model extendedMetaData="name='GMLObjectTypeType' kind='elementOnly'"
 * @generated
 */
public interface GMLObjectTypeType extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   Name of this GML Object type, including any namespace prefix.
	 *                
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(Object)
	 * @see net.opengis.wfs.WFSPackage#getGMLObjectTypeType_Name()
	 * @model 
	 */
	QName getName();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GMLObjectTypeType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
	void setName(QName value);

	/**
     * Returns the value of the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   Title of this GML Object type, normally used for display
     *                   to a human.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title</em>' attribute.
     * @see #setTitle(String)
     * @see net.opengis.wfs.WfsPackage#getGMLObjectTypeType_Title()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='Title' namespace='##targetNamespace'"
     * @generated
     */
	String getTitle();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GMLObjectTypeType#getTitle <em>Title</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Title</em>' attribute.
     * @see #getTitle()
     * @generated
     */
	void setTitle(String value);

	/**
     * Returns the value of the '<em><b>Abstract</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   Brief narrative description of this GML Object type, normally
     *                   used for display to a human.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract</em>' attribute.
     * @see #setAbstract(String)
     * @see net.opengis.wfs.WfsPackage#getGMLObjectTypeType_Abstract()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='Abstract' namespace='##targetNamespace'"
     * @generated
     */
	String getAbstract();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GMLObjectTypeType#getAbstract <em>Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract</em>' attribute.
     * @see #getAbstract()
     * @generated
     */
	void setAbstract(String value);

	/**
     * Returns the value of the '<em><b>Keywords</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows10.KeywordsType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Keywords</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Keywords</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getGMLObjectTypeType_Keywords()
     * @model type="net.opengis.ows10.KeywordsType" containment="true"
     *        extendedMetaData="kind='element' name='Keywords' namespace='http://www.opengis.net/ows'"
     * @generated
     */
	EList getKeywords();

	/**
     * Returns the value of the '<em><b>Output Formats</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Formats</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Output Formats</em>' containment reference.
     * @see #setOutputFormats(OutputFormatListType)
     * @see net.opengis.wfs.WfsPackage#getGMLObjectTypeType_OutputFormats()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='OutputFormats' namespace='##targetNamespace'"
     * @generated
     */
	OutputFormatListType getOutputFormats();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GMLObjectTypeType#getOutputFormats <em>Output Formats</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Formats</em>' containment reference.
     * @see #getOutputFormats()
     * @generated
     */
	void setOutputFormats(OutputFormatListType value);

} // GMLObjectTypeType
