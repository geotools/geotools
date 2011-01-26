/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.ows10.KeywordsType;

import net.opengis.wfs.GMLObjectTypeType;
import net.opengis.wfs.OutputFormatListType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>GML Object Type Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.GMLObjectTypeTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GMLObjectTypeTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GMLObjectTypeTypeImpl#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GMLObjectTypeTypeImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GMLObjectTypeTypeImpl#getOutputFormats <em>Output Formats</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GMLObjectTypeTypeImpl extends EObjectImpl implements GMLObjectTypeType {
	/**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
	protected static final QName NAME_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
	protected QName name = NAME_EDEFAULT;

	/**
     * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
	protected static final String TITLE_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTitle()
     * @generated
     * @ordered
     */
	protected String title = TITLE_EDEFAULT;

	/**
     * The default value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
	protected static final String ABSTRACT_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getAbstract() <em>Abstract</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getAbstract()
     * @generated
     * @ordered
     */
	protected String abstract_ = ABSTRACT_EDEFAULT;

	/**
     * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getKeywords()
     * @generated
     * @ordered
     */
	protected EList keywords;

	/**
     * The cached value of the '{@link #getOutputFormats() <em>Output Formats</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getOutputFormats()
     * @generated
     * @ordered
     */
	protected OutputFormatListType outputFormats;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected GMLObjectTypeTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.GML_OBJECT_TYPE_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public QName getName() {
        return name;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setName(QName newName) {
        QName oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GML_OBJECT_TYPE_TYPE__NAME, oldName, name));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getTitle() {
        return title;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTitle(String newTitle) {
        String oldTitle = title;
        title = newTitle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GML_OBJECT_TYPE_TYPE__TITLE, oldTitle, title));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getAbstract() {
        return abstract_;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setAbstract(String newAbstract) {
        String oldAbstract = abstract_;
        abstract_ = newAbstract;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT, oldAbstract, abstract_));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getKeywords() {
        if (keywords == null) {
            keywords = new EObjectContainmentEList(KeywordsType.class, this, WfsPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS);
        }
        return keywords;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public OutputFormatListType getOutputFormats() {
        return outputFormats;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetOutputFormats(OutputFormatListType newOutputFormats, NotificationChain msgs) {
        OutputFormatListType oldOutputFormats = outputFormats;
        outputFormats = newOutputFormats;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS, oldOutputFormats, newOutputFormats);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setOutputFormats(OutputFormatListType newOutputFormats) {
        if (newOutputFormats != outputFormats) {
            NotificationChain msgs = null;
            if (outputFormats != null)
                msgs = ((InternalEObject)outputFormats).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
            if (newOutputFormats != null)
                msgs = ((InternalEObject)newOutputFormats).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS, null, msgs);
            msgs = basicSetOutputFormats(newOutputFormats, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS, newOutputFormats, newOutputFormats));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
                return ((InternalEList)getKeywords()).basicRemove(otherEnd, msgs);
            case WfsPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
                return basicSetOutputFormats(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_TYPE__NAME:
                return getName();
            case WfsPackage.GML_OBJECT_TYPE_TYPE__TITLE:
                return getTitle();
            case WfsPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT:
                return getAbstract();
            case WfsPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
                return getKeywords();
            case WfsPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
                return getOutputFormats();
        }
        return super.eGet(featureID, resolve, coreType);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_TYPE__NAME:
                setName((QName)newValue);
                return;
            case WfsPackage.GML_OBJECT_TYPE_TYPE__TITLE:
                setTitle((String)newValue);
                return;
            case WfsPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT:
                setAbstract((String)newValue);
                return;
            case WfsPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
                getKeywords().clear();
                getKeywords().addAll((Collection)newValue);
                return;
            case WfsPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
                setOutputFormats((OutputFormatListType)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eUnset(int featureID) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case WfsPackage.GML_OBJECT_TYPE_TYPE__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case WfsPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT:
                setAbstract(ABSTRACT_EDEFAULT);
                return;
            case WfsPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
                getKeywords().clear();
                return;
            case WfsPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
                setOutputFormats((OutputFormatListType)null);
                return;
        }
        super.eUnset(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean eIsSet(int featureID) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case WfsPackage.GML_OBJECT_TYPE_TYPE__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case WfsPackage.GML_OBJECT_TYPE_TYPE__ABSTRACT:
                return ABSTRACT_EDEFAULT == null ? abstract_ != null : !ABSTRACT_EDEFAULT.equals(abstract_);
            case WfsPackage.GML_OBJECT_TYPE_TYPE__KEYWORDS:
                return keywords != null && !keywords.isEmpty();
            case WfsPackage.GML_OBJECT_TYPE_TYPE__OUTPUT_FORMATS:
                return outputFormats != null;
        }
        return super.eIsSet(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (name: ");
        result.append(name);
        result.append(", title: ");
        result.append(title);
        result.append(", abstract: ");
        result.append(abstract_);
        result.append(')');
        return result.toString();
    }

} //GMLObjectTypeTypeImpl