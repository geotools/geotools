/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.AbstractType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wfs20.AbstractType#getLang <em>Lang</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getAbstractType()
 * @model extendedMetaData="name='Abstract_._type' kind='simple'"
 * @generated
 */
public interface AbstractType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see net.opengis.wfs20.Wfs20Package#getAbstractType_Value()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.AbstractType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

    /**
     * Returns the value of the '<em><b>Lang</b></em>' attribute.
     * The default value is <code>"en"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *     <div xmlns="http://www.w3.org/1999/xhtml">
     *      
     *       <h3>lang (as an attribute name)</h3>
     *       <p>
     *        denotes an attribute whose value
     *        is a language code for the natural language of the content of
     *        any element; its value is inherited.  This name is reserved
     *        by virtue of its definition in the XML specification.</p>
     *      
     *     </div>
     *     <div xmlns="http://www.w3.org/1999/xhtml">
     *      <h4>Notes</h4>
     *      <p>
     *       Attempting to install the relevant ISO 2- and 3-letter
     *       codes as the enumerated possible values is probably never
     *       going to be a realistic possibility.  
     *      </p>
     *      <p>
     *       See BCP 47 at <a href="http://www.rfc-editor.org/rfc/bcp/bcp47.txt">
     *        http://www.rfc-editor.org/rfc/bcp/bcp47.txt</a>
     *       and the IANA language subtag registry at
     *       <a href="http://www.iana.org/assignments/language-subtag-registry">
     *        http://www.iana.org/assignments/language-subtag-registry</a>
     *       for further information.
     *      </p>
     *      <p>
     *       The union allows for the 'un-declaration' of xml:lang with
     *       the empty string.
     *      </p>
     *     </div>
     *    
     * <!-- end-model-doc -->
     * @return the value of the '<em>Lang</em>' attribute.
     * @see #isSetLang()
     * @see #unsetLang()
     * @see #setLang(String)
     * @see net.opengis.wfs20.Wfs20Package#getAbstractType_Lang()
     * @model default="en" unsettable="true" dataType="org.eclipse.emf.ecore.xml.namespace.LangType"
     *        extendedMetaData="kind='attribute' name='lang' namespace='http://www.w3.org/XML/1998/namespace'"
     * @generated
     */
    String getLang();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.AbstractType#getLang <em>Lang</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lang</em>' attribute.
     * @see #isSetLang()
     * @see #unsetLang()
     * @see #getLang()
     * @generated
     */
    void setLang(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.AbstractType#getLang <em>Lang</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetLang()
     * @see #getLang()
     * @see #setLang(String)
     * @generated
     */
    void unsetLang();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.AbstractType#getLang <em>Lang</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Lang</em>' attribute is set.
     * @see #unsetLang()
     * @see #getLang()
     * @see #setLang(String)
     * @generated
     */
    boolean isSetLang();

} // AbstractType
