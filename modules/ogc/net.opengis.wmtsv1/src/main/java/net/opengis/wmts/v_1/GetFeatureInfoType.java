/**
 */
package net.opengis.wmts.v_1;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Feature Info Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.GetFeatureInfoType#getGetTile <em>Get Tile</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetFeatureInfoType#getJ <em>J</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetFeatureInfoType#getI <em>I</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetFeatureInfoType#getInfoFormat <em>Info Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetFeatureInfoType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetFeatureInfoType#getVersion <em>Version</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetFeatureInfoType()
 * @model extendedMetaData="name='GetFeatureInfo_._type' kind='elementOnly'"
 * @generated
 */
public interface GetFeatureInfoType extends EObject {
    /**
     * Returns the value of the '<em><b>Get Tile</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The corresponding GetTile request parameters
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get Tile</em>' containment reference.
     * @see #setGetTile(GetTileType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetFeatureInfoType_GetTile()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='GetTile' namespace='##targetNamespace'"
     * @generated
     */
    GetTileType getGetTile();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getGetTile <em>Get Tile</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Tile</em>' containment reference.
     * @see #getGetTile()
     * @generated
     */
    void setGetTile(GetTileType value);

    /**
     * Returns the value of the '<em><b>J</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Row index of a pixel in the tile
     * <!-- end-model-doc -->
     * @return the value of the '<em>J</em>' attribute.
     * @see #setJ(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetFeatureInfoType_J()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" required="true"
     *        extendedMetaData="kind='element' name='J' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getJ();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getJ <em>J</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>J</em>' attribute.
     * @see #getJ()
     * @generated
     */
    void setJ(BigInteger value);

    /**
     * Returns the value of the '<em><b>I</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Column index of a pixel in the tile
     * <!-- end-model-doc -->
     * @return the value of the '<em>I</em>' attribute.
     * @see #setI(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetFeatureInfoType_I()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" required="true"
     *        extendedMetaData="kind='element' name='I' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getI();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getI <em>I</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>I</em>' attribute.
     * @see #getI()
     * @generated
     */
    void setI(BigInteger value);

    /**
     * Returns the value of the '<em><b>Info Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Output MIME type format of the 
     * 						retrieved information
     * <!-- end-model-doc -->
     * @return the value of the '<em>Info Format</em>' attribute.
     * @see #setInfoFormat(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetFeatureInfoType_InfoFormat()
     * @model dataType="net.opengis.ows11.MimeType" required="true"
     *        extendedMetaData="kind='element' name='InfoFormat' namespace='##targetNamespace'"
     * @generated
     */
    String getInfoFormat();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getInfoFormat <em>Info Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Info Format</em>' attribute.
     * @see #getInfoFormat()
     * @generated
     */
    void setInfoFormat(String value);

    /**
     * Returns the value of the '<em><b>Service</b></em>' attribute.
     * The default value is <code>"WMTS"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Service</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Service</em>' attribute.
     * @see #isSetService()
     * @see #unsetService()
     * @see #setService(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetFeatureInfoType_Service()
     * @model default="WMTS" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='service'"
     * @generated
     */
    String getService();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getService <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service</em>' attribute.
     * @see #isSetService()
     * @see #unsetService()
     * @see #getService()
     * @generated
     */
    void setService(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getService <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
    void unsetService();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getService <em>Service</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Service</em>' attribute is set.
     * @see #unsetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
    boolean isSetService();

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * The default value is <code>"1.0.0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #isSetVersion()
     * @see #unsetVersion()
     * @see #setVersion(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetFeatureInfoType_Version()
     * @model default="1.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #isSetVersion()
     * @see #unsetVersion()
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    void unsetVersion();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.GetFeatureInfoType#getVersion <em>Version</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Version</em>' attribute is set.
     * @see #unsetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    boolean isSetVersion();

} // GetFeatureInfoType
