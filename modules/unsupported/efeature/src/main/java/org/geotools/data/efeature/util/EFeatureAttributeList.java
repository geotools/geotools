package org.geotools.data.efeature.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.EFeatureAttribute;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureProperty;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.internal.EFeatureAttributeDelegate;

/**
 * Unmodifiable list of {@link EFeatureAttribute} instances.
 * 
 * @author kengu, 22. apr. 2011
 * 
 */
public class EFeatureAttributeList<V> extends AbstractList<EFeatureAttribute<V>> {
    private static final long serialVersionUID = 1L;

    private final List<EFeatureAttribute<V>> eItems;

    private final Class<V> type;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------


    public EFeatureAttributeList(EFeatureInfo eInfo, EObject eObject, Class<V> type) {
        this.type = type;
        List<EFeatureAttribute<V>> eList = toList(eInfo, eObject, type);
        this.eItems = Collections.unmodifiableList(eList);
    }

    @SuppressWarnings("unchecked")
    public EFeatureAttributeList(List<? extends EFeatureProperty<V, ?>> eList, Class<V> type) {
        this.type = type;
        List<EFeatureAttribute<V>> eSelected = new ArrayList<EFeatureAttribute<V>>(eList.size());
        for (EFeatureProperty<V, ?> it : eList) {
            if (it instanceof EFeatureAttribute) {
                // Perform unchecked cast. This is always safe, since
                // EFeatureGeometry<V> extends EFeatureProperty<V,GeometryAttribute>
                //
                eSelected.add((EFeatureAttribute<V>) it);
            }
        }
        this.eItems = Collections.unmodifiableList(eSelected);
    }

    public EFeatureAttributeList(EFeatureAttributeList<V> eList) {
        this.type = eList.type;
        this.eItems = Collections.unmodifiableList(eList.eItems);
    }

    // ----------------------------------------------------- 
    //  EFeatureGeometryList implementation
    // -----------------------------------------------------

    public Class<V> getType() {
        return type;
    }

    // ----------------------------------------------------- 
    //  List implementation
    // -----------------------------------------------------


    @Override
    public EFeatureAttribute<V> get(int index) {
        return eItems.get(index);
    }

    @Override
    public int size() {
        return eItems.size();
    }

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------


    public static List<EFeatureAttribute<Object>> toList(EFeatureInfo eFeatureInfo,
            EObject eObject, Collection<EAttribute> eAttributes) {
        return toList(eFeatureInfo, eObject, Object.class);
    }

    public static <V> List<EFeatureAttribute<V>> toList(EFeatureInfo eFeatureInfo, EObject eObject,
            Class<V> type) {
        EClass eClass = eFeatureInfo.eClass();
        if (eClass.isSuperTypeOf(eObject.eClass())) {
            Map<String, EAttribute> eAttrMap = EFeatureUtils.eGetAttributeMap(eClass);
            return toList(eFeatureInfo, eObject, eAttrMap.values(), type);
        }
        return null;
    }

    public static <V> List<EFeatureAttribute<V>> toList(EFeatureInfo eFeatureInfo, EObject eObject,
            Collection<EAttribute> eAttributes, Class<V> type) {
        List<EFeatureAttribute<V>> list = new ArrayList<EFeatureAttribute<V>>(eAttributes.size());
        for (EAttribute it : eAttributes) {
            EFeatureAttribute<V> d = EFeatureAttributeDelegate.create(eObject, it.getName(), type,
                    eFeatureInfo);
            if (d != null) {
                list.add(d);
            }
        }
        return list;

    }

}
