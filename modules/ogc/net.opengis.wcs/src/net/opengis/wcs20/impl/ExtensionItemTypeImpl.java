/**
 */
package net.opengis.wcs20.impl;

import java.lang.Object;

import net.opengis.wcs20.ExtensionItemType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extension Item Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.ExtensionItemTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ExtensionItemTypeImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ExtensionItemTypeImpl#getSimpleContent <em>Simple Content</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ExtensionItemTypeImpl#getObjectContent <em>Object Content</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExtensionItemTypeImpl extends EObjectImpl implements ExtensionItemType {
    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNamespace()
     * @generated
     * @ordered
     */
    protected static final String NAMESPACE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNamespace()
     * @generated
     * @ordered
     */
    protected String namespace = NAMESPACE_EDEFAULT;

    /**
     * The default value of the '{@link #getSimpleContent() <em>Simple Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSimpleContent()
     * @generated
     * @ordered
     */
    protected static final String SIMPLE_CONTENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSimpleContent() <em>Simple Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSimpleContent()
     * @generated
     * @ordered
     */
    protected String simpleContent = SIMPLE_CONTENT_EDEFAULT;

    /**
     * The cached value of the '{@link #getObjectContent() <em>Object Content</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getObjectContent()
     * @generated
     * @ordered
     */
    protected Object objectContent;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExtensionItemTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.EXTENSION_ITEM_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.EXTENSION_ITEM_TYPE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNamespace(String newNamespace) {
        String oldNamespace = namespace;
        namespace = newNamespace;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.EXTENSION_ITEM_TYPE__NAMESPACE, oldNamespace, namespace));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSimpleContent() {
        return simpleContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSimpleContent(String newSimpleContent) {
        String oldSimpleContent = simpleContent;
        simpleContent = newSimpleContent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.EXTENSION_ITEM_TYPE__SIMPLE_CONTENT, oldSimpleContent, simpleContent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getObjectContent() {
        if (objectContent != null && ((EObject)objectContent).eIsProxy()) {
            InternalEObject oldObjectContent = (InternalEObject)objectContent;
            objectContent = (Object)eResolveProxy(oldObjectContent);
            if (objectContent != oldObjectContent) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wcs20Package.EXTENSION_ITEM_TYPE__OBJECT_CONTENT, oldObjectContent, objectContent));
            }
        }
        return objectContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object basicGetObjectContent() {
        return objectContent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setObjectContent(Object newObjectContent) {
        Object oldObjectContent = objectContent;
        objectContent = newObjectContent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.EXTENSION_ITEM_TYPE__OBJECT_CONTENT, oldObjectContent, objectContent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.EXTENSION_ITEM_TYPE__NAME:
                return getName();
            case Wcs20Package.EXTENSION_ITEM_TYPE__NAMESPACE:
                return getNamespace();
            case Wcs20Package.EXTENSION_ITEM_TYPE__SIMPLE_CONTENT:
                return getSimpleContent();
            case Wcs20Package.EXTENSION_ITEM_TYPE__OBJECT_CONTENT:
                if (resolve) return getObjectContent();
                return basicGetObjectContent();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wcs20Package.EXTENSION_ITEM_TYPE__NAME:
                setName((String)newValue);
                return;
            case Wcs20Package.EXTENSION_ITEM_TYPE__NAMESPACE:
                setNamespace((String)newValue);
                return;
            case Wcs20Package.EXTENSION_ITEM_TYPE__SIMPLE_CONTENT:
                setSimpleContent((String)newValue);
                return;
            case Wcs20Package.EXTENSION_ITEM_TYPE__OBJECT_CONTENT:
                setObjectContent((Object)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wcs20Package.EXTENSION_ITEM_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case Wcs20Package.EXTENSION_ITEM_TYPE__NAMESPACE:
                setNamespace(NAMESPACE_EDEFAULT);
                return;
            case Wcs20Package.EXTENSION_ITEM_TYPE__SIMPLE_CONTENT:
                setSimpleContent(SIMPLE_CONTENT_EDEFAULT);
                return;
            case Wcs20Package.EXTENSION_ITEM_TYPE__OBJECT_CONTENT:
                setObjectContent((Object)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wcs20Package.EXTENSION_ITEM_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case Wcs20Package.EXTENSION_ITEM_TYPE__NAMESPACE:
                return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
            case Wcs20Package.EXTENSION_ITEM_TYPE__SIMPLE_CONTENT:
                return SIMPLE_CONTENT_EDEFAULT == null ? simpleContent != null : !SIMPLE_CONTENT_EDEFAULT.equals(simpleContent);
            case Wcs20Package.EXTENSION_ITEM_TYPE__OBJECT_CONTENT:
                return objectContent != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (name: ");
        result.append(name);
        result.append(", namespace: ");
        result.append(namespace);
        result.append(", simpleContent: ");
        result.append(simpleContent);
        result.append(')');
        return result.toString();
    }

} //ExtensionItemTypeImpl
