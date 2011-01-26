/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.LanguageStringType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of a WPS process or output object.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.DescriptionType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps10.DescriptionType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wps10.DescriptionType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wps10.DescriptionType#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getDescriptionType()
 * @model extendedMetaData="name='DescriptionType' kind='elementOnly'"
 * @generated
 */
public interface DescriptionType extends EObject {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unambiguous identifier or name of a process, unique for this server, or unambiguous identifier or name of an output, unique for this process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.wps10.Wps10Package#getDescriptionType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DescriptionType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Title of a process or output, normally available for display to a human.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title</em>' containment reference.
     * @see #setTitle(LanguageStringType)
     * @see net.opengis.wps10.Wps10Package#getDescriptionType_Title()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Title' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    LanguageStringType getTitle();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DescriptionType#getTitle <em>Title</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Title</em>' containment reference.
     * @see #getTitle()
     * @generated
     */
    void setTitle(LanguageStringType value);

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Brief narrative description of a process or output, normally available for display to a human.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference.
     * @see #setAbstract(LanguageStringType)
     * @see net.opengis.wps10.Wps10Package#getDescriptionType_Abstract()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    LanguageStringType getAbstract();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DescriptionType#getAbstract <em>Abstract</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract</em>' containment reference.
     * @see #getAbstract()
     * @generated
     */
    void setAbstract(LanguageStringType value);

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata about this process/input/output. A list of optional and/or required metadata elements for this process/input/output could be specified in an Application Profile for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getDescriptionType_Metadata()
     * @model type="net.opengis.ows11.MetadataType" containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList getMetadata();

} // DescriptionType
