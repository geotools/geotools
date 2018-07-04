/**
 */
package net.opengis.wmts.v_1;

import net.opengis.gml311.AbstractFeatureCollectionType;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Info Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getFeatureCollectionGroup <em>Feature Collection Group</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getFeatureCollection <em>Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getTextPayload <em>Text Payload</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getBinaryPayload <em>Binary Payload</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getAnyContent <em>Any Content</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getFeatureInfoResponseType()
 * @model extendedMetaData="name='FeatureInfoResponse_._type' kind='elementOnly'"
 * @generated
 */
public interface FeatureInfoResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Feature Collection Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							This allows to define any FeatureCollection that is a substitutionGroup 
     * 							of gml:_GML and use it here. A Geography Markup Language GML 
     * 							Simple Features Profile level 0 response format is strongly 
     * 							recommended as a FeatureInfo response.
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Feature Collection Group</em>' attribute list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getFeatureInfoResponseType_FeatureCollectionGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='_FeatureCollection:group' namespace='http://www.opengis.net/gml'"
     * @generated
     */
    FeatureMap getFeatureCollectionGroup();

    /**
     * Returns the value of the '<em><b>Feature Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							This allows to define any FeatureCollection that is a substitutionGroup 
     * 							of gml:_GML and use it here. A Geography Markup Language GML 
     * 							Simple Features Profile level 0 response format is strongly 
     * 							recommended as a FeatureInfo response.
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Feature Collection</em>' containment reference.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getFeatureInfoResponseType_FeatureCollection()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_FeatureCollection' namespace='http://www.opengis.net/gml' group='http://www.opengis.net/gml#_FeatureCollection:group'"
     * @generated
     */
    AbstractFeatureCollectionType getFeatureCollection();

    /**
     * Returns the value of the '<em><b>Text Payload</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							This allows to include any text format that is not a gml:_FeatureCollection 
     * 							like HTML, TXT, etc
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Text Payload</em>' containment reference.
     * @see #setTextPayload(TextPayloadType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getFeatureInfoResponseType_TextPayload()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='TextPayload' namespace='##targetNamespace'"
     * @generated
     */
    TextPayloadType getTextPayload();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getTextPayload <em>Text Payload</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Payload</em>' containment reference.
     * @see #getTextPayload()
     * @generated
     */
    void setTextPayload(TextPayloadType value);

    /**
     * Returns the value of the '<em><b>Binary Payload</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							This allows to include any binary format. Binary formats are not 
     * 							common response for a GeFeatureInfo requests but possible for 
     * 							some imaginative implementations.
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Binary Payload</em>' containment reference.
     * @see #setBinaryPayload(BinaryPayloadType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getFeatureInfoResponseType_BinaryPayload()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='BinaryPayload' namespace='##targetNamespace'"
     * @generated
     */
    BinaryPayloadType getBinaryPayload();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getBinaryPayload <em>Binary Payload</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Binary Payload</em>' containment reference.
     * @see #getBinaryPayload()
     * @generated
     */
    void setBinaryPayload(BinaryPayloadType value);

    /**
     * Returns the value of the '<em><b>Any Content</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							This allows to include any XML content that it is not any of 
     * 							the previous ones.
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Any Content</em>' containment reference.
     * @see #setAnyContent(EObject)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getFeatureInfoResponseType_AnyContent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AnyContent' namespace='##targetNamespace'"
     * @generated
     */
    EObject getAnyContent();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.FeatureInfoResponseType#getAnyContent <em>Any Content</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Any Content</em>' containment reference.
     * @see #getAnyContent()
     * @generated
     */
    void setAnyContent(EObject value);

} // FeatureInfoResponseType
