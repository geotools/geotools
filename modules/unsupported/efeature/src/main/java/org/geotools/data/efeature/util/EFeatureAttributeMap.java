package org.geotools.data.efeature.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.EFeatureAttribute;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureUtils;
import org.geotools.data.efeature.internal.EFeatureAttributeDelegate;

/**
 * Unmodifiable map of {@link EFeatureAttribute} instances.
 * 
 * @author kengu, 22. apr. 2011
 * 
 */
public class EFeatureAttributeMap<V> extends AbstractMap<String, EFeatureAttribute<V>> {
    private static final long serialVersionUID = 1L;

    private EFeatureAttributeList<V> eList;

    private final Map<String, EFeatureAttribute<V>> eMap;

    private final Class<V> type;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------


    public EFeatureAttributeMap(EFeatureInfo eInfo, EObject eObject, Class<V> type) {
        this(toMap(eInfo, eObject, type), type);
    }

    public EFeatureAttributeMap(EFeatureInfo eInfo, EObject eObject, List<EAttribute> eList,
            Class<V> type) {
        this(toMap(eInfo, eObject, eList, type), type);
    }

    public EFeatureAttributeMap(EFeatureAttributeList<V> eList, Class<V> type) {
        this(toMap(eList, type), type);
    }

    public EFeatureAttributeMap(Map<String, EFeatureAttribute<V>> eMap, Class<V> type) {
        this.type = type;
        this.eMap = Collections.unmodifiableMap(eMap);
    }

    // ----------------------------------------------------- 
    //  Map implementation
    // -----------------------------------------------------

    @Override
    public Set<Entry<String, EFeatureAttribute<V>>> entrySet() {
        return eMap.entrySet();
    }

    public EFeatureAttributeList<V> getList() {
        if (eList == null) {
            List<EFeatureAttribute<V>> list = new ArrayList<EFeatureAttribute<V>>(eMap.values());
            eList = new EFeatureAttributeList<V>(list, type);
        }
        return eList;
    }

    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------

    public static Map<String, EFeatureAttribute<Object>> toMap(EFeatureInfo eFeatureInfo,
            EObject eObject) {
        EClass eClass = eFeatureInfo.eClass();
        if (eClass.isSuperTypeOf(eObject.eClass())) {
            Map<String, EAttribute> eAttrMap = EFeatureUtils.eGetAttributeMap(eClass);
            return toMap(eFeatureInfo, eObject, eAttrMap.values(), Object.class);
        }
        return null;
    }

    public static <V> Map<String, EFeatureAttribute<V>> toMap(EFeatureInfo eFeatureInfo,
            EObject eObject, Class<V> type) {
        EClass eClass = eFeatureInfo.eClass();
        if (eClass.isSuperTypeOf(eObject.eClass())) {
            Map<String, EAttribute> eAttrMap = EFeatureUtils.eGetAttributeMap(eClass);
            return toMap(eFeatureInfo, eObject, eAttrMap.values(), type);
        }
        return null;
    }

    public static <V> Map<String, EFeatureAttribute<V>> toMap(EFeatureAttributeList<V> eList,
            Class<V> type) {
        Map<String, EFeatureAttribute<V>> eMap = new HashMap<String, EFeatureAttribute<V>>();
        for (EFeatureAttribute<V> it : eList) {
            eMap.put(it.getStructure().eName(), it);
        }
        return eMap;
    }

    public static <V> Map<String, EFeatureAttribute<V>> toMap(EFeatureInfo eFeatureInfo,
            EObject eObject, Collection<EAttribute> eAttributes, Class<V> type) {
        Map<String, EFeatureAttribute<V>> map = new HashMap<String, EFeatureAttribute<V>>(
                eAttributes.size());
        for (EAttribute it : eAttributes) {
            String eName = it.getName();
            EFeatureAttribute<V> d = EFeatureAttributeDelegate.create(eObject, it.getName(), type,
                    eFeatureInfo);
            if (d != null) {
                map.put(eName, d);
            }
        }
        return map;

    }

}
