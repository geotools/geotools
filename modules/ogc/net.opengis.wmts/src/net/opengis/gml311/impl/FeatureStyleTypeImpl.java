/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.FeatureStyleType;
import net.opengis.gml311.GeometryStylePropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LabelStylePropertyType;
import net.opengis.gml311.QueryGrammarEnumeration;
import net.opengis.gml311.TopologyStylePropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Style Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.FeatureStyleTypeImpl#getFeatureConstraint <em>Feature Constraint</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FeatureStyleTypeImpl#getGeometryStyle <em>Geometry Style</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FeatureStyleTypeImpl#getTopologyStyle <em>Topology Style</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FeatureStyleTypeImpl#getLabelStyle <em>Label Style</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FeatureStyleTypeImpl#getBaseType <em>Base Type</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FeatureStyleTypeImpl#getFeatureType <em>Feature Type</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FeatureStyleTypeImpl#getQueryGrammar <em>Query Grammar</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureStyleTypeImpl extends AbstractGMLTypeImpl implements FeatureStyleType {
    /**
     * The default value of the '{@link #getFeatureConstraint() <em>Feature Constraint</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureConstraint()
     * @generated
     * @ordered
     */
    protected static final String FEATURE_CONSTRAINT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFeatureConstraint() <em>Feature Constraint</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureConstraint()
     * @generated
     * @ordered
     */
    protected String featureConstraint = FEATURE_CONSTRAINT_EDEFAULT;

    /**
     * The cached value of the '{@link #getGeometryStyle() <em>Geometry Style</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometryStyle()
     * @generated
     * @ordered
     */
    protected EList<GeometryStylePropertyType> geometryStyle;

    /**
     * The cached value of the '{@link #getTopologyStyle() <em>Topology Style</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTopologyStyle()
     * @generated
     * @ordered
     */
    protected EList<TopologyStylePropertyType> topologyStyle;

    /**
     * The cached value of the '{@link #getLabelStyle() <em>Label Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLabelStyle()
     * @generated
     * @ordered
     */
    protected LabelStylePropertyType labelStyle;

    /**
     * The default value of the '{@link #getBaseType() <em>Base Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBaseType()
     * @generated
     * @ordered
     */
    protected static final String BASE_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBaseType() <em>Base Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBaseType()
     * @generated
     * @ordered
     */
    protected String baseType = BASE_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getFeatureType() <em>Feature Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureType()
     * @generated
     * @ordered
     */
    protected static final String FEATURE_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFeatureType() <em>Feature Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureType()
     * @generated
     * @ordered
     */
    protected String featureType = FEATURE_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getQueryGrammar() <em>Query Grammar</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQueryGrammar()
     * @generated
     * @ordered
     */
    protected static final QueryGrammarEnumeration QUERY_GRAMMAR_EDEFAULT = QueryGrammarEnumeration.XPATH;

    /**
     * The cached value of the '{@link #getQueryGrammar() <em>Query Grammar</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQueryGrammar()
     * @generated
     * @ordered
     */
    protected QueryGrammarEnumeration queryGrammar = QUERY_GRAMMAR_EDEFAULT;

    /**
     * This is true if the Query Grammar attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean queryGrammarESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FeatureStyleTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getFeatureStyleType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFeatureConstraint() {
        return featureConstraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFeatureConstraint(String newFeatureConstraint) {
        String oldFeatureConstraint = featureConstraint;
        featureConstraint = newFeatureConstraint;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FEATURE_STYLE_TYPE__FEATURE_CONSTRAINT, oldFeatureConstraint, featureConstraint));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<GeometryStylePropertyType> getGeometryStyle() {
        if (geometryStyle == null) {
            geometryStyle = new EObjectContainmentEList<GeometryStylePropertyType>(GeometryStylePropertyType.class, this, Gml311Package.FEATURE_STYLE_TYPE__GEOMETRY_STYLE);
        }
        return geometryStyle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TopologyStylePropertyType> getTopologyStyle() {
        if (topologyStyle == null) {
            topologyStyle = new EObjectContainmentEList<TopologyStylePropertyType>(TopologyStylePropertyType.class, this, Gml311Package.FEATURE_STYLE_TYPE__TOPOLOGY_STYLE);
        }
        return topologyStyle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LabelStylePropertyType getLabelStyle() {
        return labelStyle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLabelStyle(LabelStylePropertyType newLabelStyle, NotificationChain msgs) {
        LabelStylePropertyType oldLabelStyle = labelStyle;
        labelStyle = newLabelStyle;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.FEATURE_STYLE_TYPE__LABEL_STYLE, oldLabelStyle, newLabelStyle);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLabelStyle(LabelStylePropertyType newLabelStyle) {
        if (newLabelStyle != labelStyle) {
            NotificationChain msgs = null;
            if (labelStyle != null)
                msgs = ((InternalEObject)labelStyle).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.FEATURE_STYLE_TYPE__LABEL_STYLE, null, msgs);
            if (newLabelStyle != null)
                msgs = ((InternalEObject)newLabelStyle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.FEATURE_STYLE_TYPE__LABEL_STYLE, null, msgs);
            msgs = basicSetLabelStyle(newLabelStyle, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FEATURE_STYLE_TYPE__LABEL_STYLE, newLabelStyle, newLabelStyle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getBaseType() {
        return baseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBaseType(String newBaseType) {
        String oldBaseType = baseType;
        baseType = newBaseType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FEATURE_STYLE_TYPE__BASE_TYPE, oldBaseType, baseType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFeatureType() {
        return featureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFeatureType(String newFeatureType) {
        String oldFeatureType = featureType;
        featureType = newFeatureType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FEATURE_STYLE_TYPE__FEATURE_TYPE, oldFeatureType, featureType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueryGrammarEnumeration getQueryGrammar() {
        return queryGrammar;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setQueryGrammar(QueryGrammarEnumeration newQueryGrammar) {
        QueryGrammarEnumeration oldQueryGrammar = queryGrammar;
        queryGrammar = newQueryGrammar == null ? QUERY_GRAMMAR_EDEFAULT : newQueryGrammar;
        boolean oldQueryGrammarESet = queryGrammarESet;
        queryGrammarESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FEATURE_STYLE_TYPE__QUERY_GRAMMAR, oldQueryGrammar, queryGrammar, !oldQueryGrammarESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetQueryGrammar() {
        QueryGrammarEnumeration oldQueryGrammar = queryGrammar;
        boolean oldQueryGrammarESet = queryGrammarESet;
        queryGrammar = QUERY_GRAMMAR_EDEFAULT;
        queryGrammarESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.FEATURE_STYLE_TYPE__QUERY_GRAMMAR, oldQueryGrammar, QUERY_GRAMMAR_EDEFAULT, oldQueryGrammarESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetQueryGrammar() {
        return queryGrammarESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.FEATURE_STYLE_TYPE__GEOMETRY_STYLE:
                return ((InternalEList<?>)getGeometryStyle()).basicRemove(otherEnd, msgs);
            case Gml311Package.FEATURE_STYLE_TYPE__TOPOLOGY_STYLE:
                return ((InternalEList<?>)getTopologyStyle()).basicRemove(otherEnd, msgs);
            case Gml311Package.FEATURE_STYLE_TYPE__LABEL_STYLE:
                return basicSetLabelStyle(null, msgs);
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
            case Gml311Package.FEATURE_STYLE_TYPE__FEATURE_CONSTRAINT:
                return getFeatureConstraint();
            case Gml311Package.FEATURE_STYLE_TYPE__GEOMETRY_STYLE:
                return getGeometryStyle();
            case Gml311Package.FEATURE_STYLE_TYPE__TOPOLOGY_STYLE:
                return getTopologyStyle();
            case Gml311Package.FEATURE_STYLE_TYPE__LABEL_STYLE:
                return getLabelStyle();
            case Gml311Package.FEATURE_STYLE_TYPE__BASE_TYPE:
                return getBaseType();
            case Gml311Package.FEATURE_STYLE_TYPE__FEATURE_TYPE:
                return getFeatureType();
            case Gml311Package.FEATURE_STYLE_TYPE__QUERY_GRAMMAR:
                return getQueryGrammar();
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
            case Gml311Package.FEATURE_STYLE_TYPE__FEATURE_CONSTRAINT:
                setFeatureConstraint((String)newValue);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__GEOMETRY_STYLE:
                getGeometryStyle().clear();
                getGeometryStyle().addAll((Collection<? extends GeometryStylePropertyType>)newValue);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__TOPOLOGY_STYLE:
                getTopologyStyle().clear();
                getTopologyStyle().addAll((Collection<? extends TopologyStylePropertyType>)newValue);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__LABEL_STYLE:
                setLabelStyle((LabelStylePropertyType)newValue);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__BASE_TYPE:
                setBaseType((String)newValue);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__FEATURE_TYPE:
                setFeatureType((String)newValue);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__QUERY_GRAMMAR:
                setQueryGrammar((QueryGrammarEnumeration)newValue);
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
            case Gml311Package.FEATURE_STYLE_TYPE__FEATURE_CONSTRAINT:
                setFeatureConstraint(FEATURE_CONSTRAINT_EDEFAULT);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__GEOMETRY_STYLE:
                getGeometryStyle().clear();
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__TOPOLOGY_STYLE:
                getTopologyStyle().clear();
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__LABEL_STYLE:
                setLabelStyle((LabelStylePropertyType)null);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__BASE_TYPE:
                setBaseType(BASE_TYPE_EDEFAULT);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__FEATURE_TYPE:
                setFeatureType(FEATURE_TYPE_EDEFAULT);
                return;
            case Gml311Package.FEATURE_STYLE_TYPE__QUERY_GRAMMAR:
                unsetQueryGrammar();
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
            case Gml311Package.FEATURE_STYLE_TYPE__FEATURE_CONSTRAINT:
                return FEATURE_CONSTRAINT_EDEFAULT == null ? featureConstraint != null : !FEATURE_CONSTRAINT_EDEFAULT.equals(featureConstraint);
            case Gml311Package.FEATURE_STYLE_TYPE__GEOMETRY_STYLE:
                return geometryStyle != null && !geometryStyle.isEmpty();
            case Gml311Package.FEATURE_STYLE_TYPE__TOPOLOGY_STYLE:
                return topologyStyle != null && !topologyStyle.isEmpty();
            case Gml311Package.FEATURE_STYLE_TYPE__LABEL_STYLE:
                return labelStyle != null;
            case Gml311Package.FEATURE_STYLE_TYPE__BASE_TYPE:
                return BASE_TYPE_EDEFAULT == null ? baseType != null : !BASE_TYPE_EDEFAULT.equals(baseType);
            case Gml311Package.FEATURE_STYLE_TYPE__FEATURE_TYPE:
                return FEATURE_TYPE_EDEFAULT == null ? featureType != null : !FEATURE_TYPE_EDEFAULT.equals(featureType);
            case Gml311Package.FEATURE_STYLE_TYPE__QUERY_GRAMMAR:
                return isSetQueryGrammar();
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
        result.append(" (featureConstraint: ");
        result.append(featureConstraint);
        result.append(", baseType: ");
        result.append(baseType);
        result.append(", featureType: ");
        result.append(featureType);
        result.append(", queryGrammar: ");
        if (queryGrammarESet) result.append(queryGrammar); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //FeatureStyleTypeImpl
