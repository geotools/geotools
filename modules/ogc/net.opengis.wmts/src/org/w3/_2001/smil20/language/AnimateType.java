/**
 */
package org.w3._2001.smil20.language;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.emf.ecore.util.FeatureMap;

import org.w3._2001.smil20.AnimatePrototype;
import org.w3._2001.smil20.CalcModeType;
import org.w3._2001.smil20.FillDefaultType;
import org.w3._2001.smil20.FillTimingAttrsType;
import org.w3._2001.smil20.RestartDefaultType;
import org.w3._2001.smil20.RestartTimingType;
import org.w3._2001.smil20.SyncBehaviorDefaultType;
import org.w3._2001.smil20.SyncBehaviorType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Animate Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getAny <em>Any</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getAlt <em>Alt</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getBegin <em>Begin</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getCalcMode <em>Calc Mode</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getClass_ <em>Class</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getDur <em>Dur</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getEnd <em>End</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getFill <em>Fill</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getFillDefault <em>Fill Default</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getId <em>Id</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getLang <em>Lang</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getLongdesc <em>Longdesc</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getMax <em>Max</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getMin <em>Min</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getRepeat <em>Repeat</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getRepeatCount <em>Repeat Count</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getRepeatDur <em>Repeat Dur</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getRestart <em>Restart</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getRestartDefault <em>Restart Default</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#isSkipContent <em>Skip Content</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getSyncBehavior <em>Sync Behavior</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getSyncBehaviorDefault <em>Sync Behavior Default</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getSyncTolerance <em>Sync Tolerance</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getSyncToleranceDefault <em>Sync Tolerance Default</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getTargetElement <em>Target Element</em>}</li>
 *   <li>{@link org.w3._2001.smil20.language.AnimateType#getAnyAttribute <em>Any Attribute</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType()
 * @model extendedMetaData="name='animateType' kind='elementOnly'"
 * @generated
 */
public interface AnimateType extends AnimatePrototype {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:8'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Any()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':9' processing='lax' group='#group:8'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Alt</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Alt</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Alt</em>' attribute.
     * @see #setAlt(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Alt()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='alt'"
     * @generated
     */
    String getAlt();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getAlt <em>Alt</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Alt</em>' attribute.
     * @see #getAlt()
     * @generated
     */
    void setAlt(String value);

    /**
     * Returns the value of the '<em><b>Begin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Begin</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Begin</em>' attribute.
     * @see #setBegin(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Begin()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='begin'"
     * @generated
     */
    String getBegin();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getBegin <em>Begin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Begin</em>' attribute.
     * @see #getBegin()
     * @generated
     */
    void setBegin(String value);

    /**
     * Returns the value of the '<em><b>Calc Mode</b></em>' attribute.
     * The default value is <code>"linear"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.CalcModeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Calc Mode</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Calc Mode</em>' attribute.
     * @see org.w3._2001.smil20.CalcModeType
     * @see #isSetCalcMode()
     * @see #unsetCalcMode()
     * @see #setCalcMode(CalcModeType)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_CalcMode()
     * @model default="linear" unsettable="true"
     *        extendedMetaData="kind='attribute' name='calcMode'"
     * @generated
     */
    CalcModeType getCalcMode();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getCalcMode <em>Calc Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Calc Mode</em>' attribute.
     * @see org.w3._2001.smil20.CalcModeType
     * @see #isSetCalcMode()
     * @see #unsetCalcMode()
     * @see #getCalcMode()
     * @generated
     */
    void setCalcMode(CalcModeType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getCalcMode <em>Calc Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetCalcMode()
     * @see #getCalcMode()
     * @see #setCalcMode(CalcModeType)
     * @generated
     */
    void unsetCalcMode();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.language.AnimateType#getCalcMode <em>Calc Mode</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Calc Mode</em>' attribute is set.
     * @see #unsetCalcMode()
     * @see #getCalcMode()
     * @see #setCalcMode(CalcModeType)
     * @generated
     */
    boolean isSetCalcMode();

    /**
     * Returns the value of the '<em><b>Class</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Class</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Class</em>' attribute.
     * @see #setClass(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Class()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='class'"
     * @generated
     */
    String getClass_();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getClass_ <em>Class</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Class</em>' attribute.
     * @see #getClass_()
     * @generated
     */
    void setClass(String value);

    /**
     * Returns the value of the '<em><b>Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dur</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dur</em>' attribute.
     * @see #setDur(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Dur()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='dur'"
     * @generated
     */
    String getDur();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getDur <em>Dur</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dur</em>' attribute.
     * @see #getDur()
     * @generated
     */
    void setDur(String value);

    /**
     * Returns the value of the '<em><b>End</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>End</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>End</em>' attribute.
     * @see #setEnd(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_End()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='end'"
     * @generated
     */
    String getEnd();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getEnd <em>End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End</em>' attribute.
     * @see #getEnd()
     * @generated
     */
    void setEnd(String value);

    /**
     * Returns the value of the '<em><b>Fill</b></em>' attribute.
     * The default value is <code>"default"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.FillTimingAttrsType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Fill</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Fill</em>' attribute.
     * @see org.w3._2001.smil20.FillTimingAttrsType
     * @see #isSetFill()
     * @see #unsetFill()
     * @see #setFill(FillTimingAttrsType)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Fill()
     * @model default="default" unsettable="true"
     *        extendedMetaData="kind='attribute' name='fill'"
     * @generated
     */
    FillTimingAttrsType getFill();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getFill <em>Fill</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Fill</em>' attribute.
     * @see org.w3._2001.smil20.FillTimingAttrsType
     * @see #isSetFill()
     * @see #unsetFill()
     * @see #getFill()
     * @generated
     */
    void setFill(FillTimingAttrsType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getFill <em>Fill</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetFill()
     * @see #getFill()
     * @see #setFill(FillTimingAttrsType)
     * @generated
     */
    void unsetFill();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.language.AnimateType#getFill <em>Fill</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Fill</em>' attribute is set.
     * @see #unsetFill()
     * @see #getFill()
     * @see #setFill(FillTimingAttrsType)
     * @generated
     */
    boolean isSetFill();

    /**
     * Returns the value of the '<em><b>Fill Default</b></em>' attribute.
     * The default value is <code>"inherit"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.FillDefaultType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Fill Default</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Fill Default</em>' attribute.
     * @see org.w3._2001.smil20.FillDefaultType
     * @see #isSetFillDefault()
     * @see #unsetFillDefault()
     * @see #setFillDefault(FillDefaultType)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_FillDefault()
     * @model default="inherit" unsettable="true"
     *        extendedMetaData="kind='attribute' name='fillDefault'"
     * @generated
     */
    FillDefaultType getFillDefault();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getFillDefault <em>Fill Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Fill Default</em>' attribute.
     * @see org.w3._2001.smil20.FillDefaultType
     * @see #isSetFillDefault()
     * @see #unsetFillDefault()
     * @see #getFillDefault()
     * @generated
     */
    void setFillDefault(FillDefaultType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getFillDefault <em>Fill Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetFillDefault()
     * @see #getFillDefault()
     * @see #setFillDefault(FillDefaultType)
     * @generated
     */
    void unsetFillDefault();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.language.AnimateType#getFillDefault <em>Fill Default</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Fill Default</em>' attribute is set.
     * @see #unsetFillDefault()
     * @see #getFillDefault()
     * @see #setFillDefault(FillDefaultType)
     * @generated
     */
    boolean isSetFillDefault();

    /**
     * Returns the value of the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' attribute.
     * @see #setId(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Id()
     * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
     *        extendedMetaData="kind='attribute' name='id'"
     * @generated
     */
    String getId();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getId <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' attribute.
     * @see #getId()
     * @generated
     */
    void setId(String value);

    /**
     * Returns the value of the '<em><b>Lang</b></em>' attribute.
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
     * @see #setLang(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Lang()
     * @model dataType="org.eclipse.emf.ecore.xml.namespace.LangType"
     *        extendedMetaData="kind='attribute' name='lang' namespace='http://www.w3.org/XML/1998/namespace'"
     * @generated
     */
    String getLang();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getLang <em>Lang</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lang</em>' attribute.
     * @see #getLang()
     * @generated
     */
    void setLang(String value);

    /**
     * Returns the value of the '<em><b>Longdesc</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Longdesc</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Longdesc</em>' attribute.
     * @see #setLongdesc(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Longdesc()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='longdesc'"
     * @generated
     */
    String getLongdesc();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getLongdesc <em>Longdesc</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Longdesc</em>' attribute.
     * @see #getLongdesc()
     * @generated
     */
    void setLongdesc(String value);

    /**
     * Returns the value of the '<em><b>Max</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Max</em>' attribute.
     * @see #setMax(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Max()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='max'"
     * @generated
     */
    String getMax();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getMax <em>Max</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Max</em>' attribute.
     * @see #getMax()
     * @generated
     */
    void setMax(String value);

    /**
     * Returns the value of the '<em><b>Min</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Min</em>' attribute.
     * @see #setMin(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Min()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='min'"
     * @generated
     */
    String getMin();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getMin <em>Min</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Min</em>' attribute.
     * @see #getMin()
     * @generated
     */
    void setMin(String value);

    /**
     * Returns the value of the '<em><b>Repeat</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Repeat</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Repeat</em>' attribute.
     * @see #setRepeat(BigInteger)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Repeat()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
     *        extendedMetaData="kind='attribute' name='repeat'"
     * @generated
     */
    BigInteger getRepeat();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getRepeat <em>Repeat</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Repeat</em>' attribute.
     * @see #getRepeat()
     * @generated
     */
    void setRepeat(BigInteger value);

    /**
     * Returns the value of the '<em><b>Repeat Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Repeat Count</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Repeat Count</em>' attribute.
     * @see #setRepeatCount(BigDecimal)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_RepeatCount()
     * @model dataType="org.w3._2001.smil20.NonNegativeDecimalType"
     *        extendedMetaData="kind='attribute' name='repeatCount'"
     * @generated
     */
    BigDecimal getRepeatCount();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getRepeatCount <em>Repeat Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Repeat Count</em>' attribute.
     * @see #getRepeatCount()
     * @generated
     */
    void setRepeatCount(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Repeat Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Repeat Dur</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Repeat Dur</em>' attribute.
     * @see #setRepeatDur(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_RepeatDur()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='repeatDur'"
     * @generated
     */
    String getRepeatDur();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getRepeatDur <em>Repeat Dur</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Repeat Dur</em>' attribute.
     * @see #getRepeatDur()
     * @generated
     */
    void setRepeatDur(String value);

    /**
     * Returns the value of the '<em><b>Restart</b></em>' attribute.
     * The default value is <code>"default"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.RestartTimingType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Restart</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Restart</em>' attribute.
     * @see org.w3._2001.smil20.RestartTimingType
     * @see #isSetRestart()
     * @see #unsetRestart()
     * @see #setRestart(RestartTimingType)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_Restart()
     * @model default="default" unsettable="true"
     *        extendedMetaData="kind='attribute' name='restart'"
     * @generated
     */
    RestartTimingType getRestart();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getRestart <em>Restart</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Restart</em>' attribute.
     * @see org.w3._2001.smil20.RestartTimingType
     * @see #isSetRestart()
     * @see #unsetRestart()
     * @see #getRestart()
     * @generated
     */
    void setRestart(RestartTimingType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getRestart <em>Restart</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetRestart()
     * @see #getRestart()
     * @see #setRestart(RestartTimingType)
     * @generated
     */
    void unsetRestart();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.language.AnimateType#getRestart <em>Restart</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Restart</em>' attribute is set.
     * @see #unsetRestart()
     * @see #getRestart()
     * @see #setRestart(RestartTimingType)
     * @generated
     */
    boolean isSetRestart();

    /**
     * Returns the value of the '<em><b>Restart Default</b></em>' attribute.
     * The default value is <code>"inherit"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.RestartDefaultType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Restart Default</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Restart Default</em>' attribute.
     * @see org.w3._2001.smil20.RestartDefaultType
     * @see #isSetRestartDefault()
     * @see #unsetRestartDefault()
     * @see #setRestartDefault(RestartDefaultType)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_RestartDefault()
     * @model default="inherit" unsettable="true"
     *        extendedMetaData="kind='attribute' name='restartDefault'"
     * @generated
     */
    RestartDefaultType getRestartDefault();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getRestartDefault <em>Restart Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Restart Default</em>' attribute.
     * @see org.w3._2001.smil20.RestartDefaultType
     * @see #isSetRestartDefault()
     * @see #unsetRestartDefault()
     * @see #getRestartDefault()
     * @generated
     */
    void setRestartDefault(RestartDefaultType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getRestartDefault <em>Restart Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetRestartDefault()
     * @see #getRestartDefault()
     * @see #setRestartDefault(RestartDefaultType)
     * @generated
     */
    void unsetRestartDefault();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.language.AnimateType#getRestartDefault <em>Restart Default</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Restart Default</em>' attribute is set.
     * @see #unsetRestartDefault()
     * @see #getRestartDefault()
     * @see #setRestartDefault(RestartDefaultType)
     * @generated
     */
    boolean isSetRestartDefault();

    /**
     * Returns the value of the '<em><b>Skip Content</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Skip Content</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Skip Content</em>' attribute.
     * @see #isSetSkipContent()
     * @see #unsetSkipContent()
     * @see #setSkipContent(boolean)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_SkipContent()
     * @model default="true" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='skip-content'"
     * @generated
     */
    boolean isSkipContent();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#isSkipContent <em>Skip Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Skip Content</em>' attribute.
     * @see #isSetSkipContent()
     * @see #unsetSkipContent()
     * @see #isSkipContent()
     * @generated
     */
    void setSkipContent(boolean value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.language.AnimateType#isSkipContent <em>Skip Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetSkipContent()
     * @see #isSkipContent()
     * @see #setSkipContent(boolean)
     * @generated
     */
    void unsetSkipContent();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.language.AnimateType#isSkipContent <em>Skip Content</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Skip Content</em>' attribute is set.
     * @see #unsetSkipContent()
     * @see #isSkipContent()
     * @see #setSkipContent(boolean)
     * @generated
     */
    boolean isSetSkipContent();

    /**
     * Returns the value of the '<em><b>Sync Behavior</b></em>' attribute.
     * The default value is <code>"default"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.SyncBehaviorType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sync Behavior</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sync Behavior</em>' attribute.
     * @see org.w3._2001.smil20.SyncBehaviorType
     * @see #isSetSyncBehavior()
     * @see #unsetSyncBehavior()
     * @see #setSyncBehavior(SyncBehaviorType)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_SyncBehavior()
     * @model default="default" unsettable="true"
     *        extendedMetaData="kind='attribute' name='syncBehavior'"
     * @generated
     */
    SyncBehaviorType getSyncBehavior();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncBehavior <em>Sync Behavior</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sync Behavior</em>' attribute.
     * @see org.w3._2001.smil20.SyncBehaviorType
     * @see #isSetSyncBehavior()
     * @see #unsetSyncBehavior()
     * @see #getSyncBehavior()
     * @generated
     */
    void setSyncBehavior(SyncBehaviorType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncBehavior <em>Sync Behavior</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetSyncBehavior()
     * @see #getSyncBehavior()
     * @see #setSyncBehavior(SyncBehaviorType)
     * @generated
     */
    void unsetSyncBehavior();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncBehavior <em>Sync Behavior</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Sync Behavior</em>' attribute is set.
     * @see #unsetSyncBehavior()
     * @see #getSyncBehavior()
     * @see #setSyncBehavior(SyncBehaviorType)
     * @generated
     */
    boolean isSetSyncBehavior();

    /**
     * Returns the value of the '<em><b>Sync Behavior Default</b></em>' attribute.
     * The default value is <code>"inherit"</code>.
     * The literals are from the enumeration {@link org.w3._2001.smil20.SyncBehaviorDefaultType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sync Behavior Default</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sync Behavior Default</em>' attribute.
     * @see org.w3._2001.smil20.SyncBehaviorDefaultType
     * @see #isSetSyncBehaviorDefault()
     * @see #unsetSyncBehaviorDefault()
     * @see #setSyncBehaviorDefault(SyncBehaviorDefaultType)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_SyncBehaviorDefault()
     * @model default="inherit" unsettable="true"
     *        extendedMetaData="kind='attribute' name='syncBehaviorDefault'"
     * @generated
     */
    SyncBehaviorDefaultType getSyncBehaviorDefault();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncBehaviorDefault <em>Sync Behavior Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sync Behavior Default</em>' attribute.
     * @see org.w3._2001.smil20.SyncBehaviorDefaultType
     * @see #isSetSyncBehaviorDefault()
     * @see #unsetSyncBehaviorDefault()
     * @see #getSyncBehaviorDefault()
     * @generated
     */
    void setSyncBehaviorDefault(SyncBehaviorDefaultType value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncBehaviorDefault <em>Sync Behavior Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetSyncBehaviorDefault()
     * @see #getSyncBehaviorDefault()
     * @see #setSyncBehaviorDefault(SyncBehaviorDefaultType)
     * @generated
     */
    void unsetSyncBehaviorDefault();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncBehaviorDefault <em>Sync Behavior Default</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Sync Behavior Default</em>' attribute is set.
     * @see #unsetSyncBehaviorDefault()
     * @see #getSyncBehaviorDefault()
     * @see #setSyncBehaviorDefault(SyncBehaviorDefaultType)
     * @generated
     */
    boolean isSetSyncBehaviorDefault();

    /**
     * Returns the value of the '<em><b>Sync Tolerance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sync Tolerance</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sync Tolerance</em>' attribute.
     * @see #setSyncTolerance(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_SyncTolerance()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='syncTolerance'"
     * @generated
     */
    String getSyncTolerance();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncTolerance <em>Sync Tolerance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sync Tolerance</em>' attribute.
     * @see #getSyncTolerance()
     * @generated
     */
    void setSyncTolerance(String value);

    /**
     * Returns the value of the '<em><b>Sync Tolerance Default</b></em>' attribute.
     * The default value is <code>"inherit"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sync Tolerance Default</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sync Tolerance Default</em>' attribute.
     * @see #isSetSyncToleranceDefault()
     * @see #unsetSyncToleranceDefault()
     * @see #setSyncToleranceDefault(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_SyncToleranceDefault()
     * @model default="inherit" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='syncToleranceDefault'"
     * @generated
     */
    String getSyncToleranceDefault();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncToleranceDefault <em>Sync Tolerance Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sync Tolerance Default</em>' attribute.
     * @see #isSetSyncToleranceDefault()
     * @see #unsetSyncToleranceDefault()
     * @see #getSyncToleranceDefault()
     * @generated
     */
    void setSyncToleranceDefault(String value);

    /**
     * Unsets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncToleranceDefault <em>Sync Tolerance Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetSyncToleranceDefault()
     * @see #getSyncToleranceDefault()
     * @see #setSyncToleranceDefault(String)
     * @generated
     */
    void unsetSyncToleranceDefault();

    /**
     * Returns whether the value of the '{@link org.w3._2001.smil20.language.AnimateType#getSyncToleranceDefault <em>Sync Tolerance Default</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Sync Tolerance Default</em>' attribute is set.
     * @see #unsetSyncToleranceDefault()
     * @see #getSyncToleranceDefault()
     * @see #setSyncToleranceDefault(String)
     * @generated
     */
    boolean isSetSyncToleranceDefault();

    /**
     * Returns the value of the '<em><b>Target Element</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Element</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Element</em>' attribute.
     * @see #setTargetElement(String)
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_TargetElement()
     * @model dataType="org.eclipse.emf.ecore.xml.type.IDREF"
     *        extendedMetaData="kind='attribute' name='targetElement'"
     * @generated
     */
    String getTargetElement();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.language.AnimateType#getTargetElement <em>Target Element</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Element</em>' attribute.
     * @see #getTargetElement()
     * @generated
     */
    void setTargetElement(String value);

    /**
     * Returns the value of the '<em><b>Any Attribute</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any Attribute</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any Attribute</em>' attribute list.
     * @see org.w3._2001.smil20.language.LanguagePackage#getAnimateType_AnyAttribute()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='attributeWildcard' wildcards='##any' name=':34' processing='strict'"
     * @generated
     */
    FeatureMap getAnyAttribute();

} // AnimateType
