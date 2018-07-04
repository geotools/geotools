/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.DictionaryEntryType;
import net.opengis.gml311.DictionaryType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.IndirectEntryType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dictionary Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.DictionaryTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DictionaryTypeImpl#getDictionaryEntryGroup <em>Dictionary Entry Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DictionaryTypeImpl#getDictionaryEntry <em>Dictionary Entry</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DictionaryTypeImpl#getIndirectEntry <em>Indirect Entry</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DictionaryTypeImpl extends DefinitionTypeImpl implements DictionaryType {
    /**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap group;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DictionaryTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getDictionaryType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, Gml311Package.DICTIONARY_TYPE__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getDictionaryEntryGroup() {
        return (FeatureMap)getGroup().<FeatureMap.Entry>list(Gml311Package.eINSTANCE.getDictionaryType_DictionaryEntryGroup());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DictionaryEntryType> getDictionaryEntry() {
        return getDictionaryEntryGroup().list(Gml311Package.eINSTANCE.getDictionaryType_DictionaryEntry());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IndirectEntryType> getIndirectEntry() {
        return getGroup().list(Gml311Package.eINSTANCE.getDictionaryType_IndirectEntry());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.DICTIONARY_TYPE__GROUP:
                return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY_GROUP:
                return ((InternalEList<?>)getDictionaryEntryGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY:
                return ((InternalEList<?>)getDictionaryEntry()).basicRemove(otherEnd, msgs);
            case Gml311Package.DICTIONARY_TYPE__INDIRECT_ENTRY:
                return ((InternalEList<?>)getIndirectEntry()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.DICTIONARY_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY_GROUP:
                if (coreType) return getDictionaryEntryGroup();
                return ((FeatureMap.Internal)getDictionaryEntryGroup()).getWrapper();
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY:
                return getDictionaryEntry();
            case Gml311Package.DICTIONARY_TYPE__INDIRECT_ENTRY:
                return getIndirectEntry();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Gml311Package.DICTIONARY_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY_GROUP:
                ((FeatureMap.Internal)getDictionaryEntryGroup()).set(newValue);
                return;
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY:
                getDictionaryEntry().clear();
                getDictionaryEntry().addAll((Collection<? extends DictionaryEntryType>)newValue);
                return;
            case Gml311Package.DICTIONARY_TYPE__INDIRECT_ENTRY:
                getIndirectEntry().clear();
                getIndirectEntry().addAll((Collection<? extends IndirectEntryType>)newValue);
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
            case Gml311Package.DICTIONARY_TYPE__GROUP:
                getGroup().clear();
                return;
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY_GROUP:
                getDictionaryEntryGroup().clear();
                return;
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY:
                getDictionaryEntry().clear();
                return;
            case Gml311Package.DICTIONARY_TYPE__INDIRECT_ENTRY:
                getIndirectEntry().clear();
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
            case Gml311Package.DICTIONARY_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY_GROUP:
                return !getDictionaryEntryGroup().isEmpty();
            case Gml311Package.DICTIONARY_TYPE__DICTIONARY_ENTRY:
                return !getDictionaryEntry().isEmpty();
            case Gml311Package.DICTIONARY_TYPE__INDIRECT_ENTRY:
                return !getIndirectEntry().isEmpty();
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
        result.append(" (group: ");
        result.append(group);
        result.append(')');
        return result.toString();
    }

} //DictionaryTypeImpl
