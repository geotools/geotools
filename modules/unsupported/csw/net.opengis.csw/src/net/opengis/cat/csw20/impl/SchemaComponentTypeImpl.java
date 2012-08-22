/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.SchemaComponentType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Schema Component Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.SchemaComponentTypeImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SchemaComponentTypeImpl#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SchemaComponentTypeImpl#getParentSchema <em>Parent Schema</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SchemaComponentTypeImpl#getSchemaLanguage <em>Schema Language</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SchemaComponentTypeImpl#getTargetNamespace <em>Target Namespace</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SchemaComponentTypeImpl extends EObjectImpl implements SchemaComponentType {
    /**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMixed()
     * @generated
     * @ordered
     */
    protected FeatureMap mixed;

    /**
     * The default value of the '{@link #getParentSchema() <em>Parent Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParentSchema()
     * @generated
     * @ordered
     */
    protected static final String PARENT_SCHEMA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getParentSchema() <em>Parent Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParentSchema()
     * @generated
     * @ordered
     */
    protected String parentSchema = PARENT_SCHEMA_EDEFAULT;

    /**
     * The default value of the '{@link #getSchemaLanguage() <em>Schema Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchemaLanguage()
     * @generated
     * @ordered
     */
    protected static final String SCHEMA_LANGUAGE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSchemaLanguage() <em>Schema Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchemaLanguage()
     * @generated
     * @ordered
     */
    protected String schemaLanguage = SCHEMA_LANGUAGE_EDEFAULT;

    /**
     * The default value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetNamespace()
     * @generated
     * @ordered
     */
    protected static final String TARGET_NAMESPACE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetNamespace()
     * @generated
     * @ordered
     */
    protected String targetNamespace = TARGET_NAMESPACE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SchemaComponentTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.SCHEMA_COMPONENT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, Csw20Package.SCHEMA_COMPONENT_TYPE__MIXED);
        }
        return mixed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAny() {
        return (FeatureMap)getMixed().<FeatureMap.Entry>list(Csw20Package.Literals.SCHEMA_COMPONENT_TYPE__ANY);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getParentSchema() {
        return parentSchema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParentSchema(String newParentSchema) {
        String oldParentSchema = parentSchema;
        parentSchema = newParentSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SCHEMA_COMPONENT_TYPE__PARENT_SCHEMA, oldParentSchema, parentSchema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSchemaLanguage() {
        return schemaLanguage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSchemaLanguage(String newSchemaLanguage) {
        String oldSchemaLanguage = schemaLanguage;
        schemaLanguage = newSchemaLanguage;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SCHEMA_COMPONENT_TYPE__SCHEMA_LANGUAGE, oldSchemaLanguage, schemaLanguage));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetNamespace(String newTargetNamespace) {
        String oldTargetNamespace = targetNamespace;
        targetNamespace = newTargetNamespace;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SCHEMA_COMPONENT_TYPE__TARGET_NAMESPACE, oldTargetNamespace, targetNamespace));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.SCHEMA_COMPONENT_TYPE__MIXED:
                return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
            case Csw20Package.SCHEMA_COMPONENT_TYPE__ANY:
                return ((InternalEList<?>)getAny()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.SCHEMA_COMPONENT_TYPE__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case Csw20Package.SCHEMA_COMPONENT_TYPE__ANY:
                if (coreType) return getAny();
                return ((FeatureMap.Internal)getAny()).getWrapper();
            case Csw20Package.SCHEMA_COMPONENT_TYPE__PARENT_SCHEMA:
                return getParentSchema();
            case Csw20Package.SCHEMA_COMPONENT_TYPE__SCHEMA_LANGUAGE:
                return getSchemaLanguage();
            case Csw20Package.SCHEMA_COMPONENT_TYPE__TARGET_NAMESPACE:
                return getTargetNamespace();
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
            case Csw20Package.SCHEMA_COMPONENT_TYPE__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case Csw20Package.SCHEMA_COMPONENT_TYPE__ANY:
                ((FeatureMap.Internal)getAny()).set(newValue);
                return;
            case Csw20Package.SCHEMA_COMPONENT_TYPE__PARENT_SCHEMA:
                setParentSchema((String)newValue);
                return;
            case Csw20Package.SCHEMA_COMPONENT_TYPE__SCHEMA_LANGUAGE:
                setSchemaLanguage((String)newValue);
                return;
            case Csw20Package.SCHEMA_COMPONENT_TYPE__TARGET_NAMESPACE:
                setTargetNamespace((String)newValue);
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
            case Csw20Package.SCHEMA_COMPONENT_TYPE__MIXED:
                getMixed().clear();
                return;
            case Csw20Package.SCHEMA_COMPONENT_TYPE__ANY:
                getAny().clear();
                return;
            case Csw20Package.SCHEMA_COMPONENT_TYPE__PARENT_SCHEMA:
                setParentSchema(PARENT_SCHEMA_EDEFAULT);
                return;
            case Csw20Package.SCHEMA_COMPONENT_TYPE__SCHEMA_LANGUAGE:
                setSchemaLanguage(SCHEMA_LANGUAGE_EDEFAULT);
                return;
            case Csw20Package.SCHEMA_COMPONENT_TYPE__TARGET_NAMESPACE:
                setTargetNamespace(TARGET_NAMESPACE_EDEFAULT);
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
            case Csw20Package.SCHEMA_COMPONENT_TYPE__MIXED:
                return mixed != null && !mixed.isEmpty();
            case Csw20Package.SCHEMA_COMPONENT_TYPE__ANY:
                return !getAny().isEmpty();
            case Csw20Package.SCHEMA_COMPONENT_TYPE__PARENT_SCHEMA:
                return PARENT_SCHEMA_EDEFAULT == null ? parentSchema != null : !PARENT_SCHEMA_EDEFAULT.equals(parentSchema);
            case Csw20Package.SCHEMA_COMPONENT_TYPE__SCHEMA_LANGUAGE:
                return SCHEMA_LANGUAGE_EDEFAULT == null ? schemaLanguage != null : !SCHEMA_LANGUAGE_EDEFAULT.equals(schemaLanguage);
            case Csw20Package.SCHEMA_COMPONENT_TYPE__TARGET_NAMESPACE:
                return TARGET_NAMESPACE_EDEFAULT == null ? targetNamespace != null : !TARGET_NAMESPACE_EDEFAULT.equals(targetNamespace);
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
        result.append(" (mixed: ");
        result.append(mixed);
        result.append(", parentSchema: ");
        result.append(parentSchema);
        result.append(", schemaLanguage: ");
        result.append(schemaLanguage);
        result.append(", targetNamespace: ");
        result.append(targetNamespace);
        result.append(')');
        return result.toString();
    }

} //SchemaComponentTypeImpl
