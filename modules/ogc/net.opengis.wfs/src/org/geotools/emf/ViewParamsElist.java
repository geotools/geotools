/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.emf;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * Wrapper around EMF EList to support parsing the required
 * List<Map<String,String> data structure from the raw string parsed in the XML.
 * These are in the format:
 *
 * <pre>VIEWPARAMS=opt1:val1,val2;opt2:val1;opt3:...[,opt1:val1,val2;opt2:val1;opt3:...]</pre>
 */
public class ViewParamsElist extends EDataTypeEList<Object> implements EList<Object> {

    public ViewParamsElist(Class<?> dataClass, InternalEObject owner, int featureID) {
        super(dataClass, owner, featureID);
    }

    @Override
    public boolean addAll(Collection<? extends Object> collection) {
        List<Map<String, String>> values;
        // if a raw string was passed in it will be wrapped in a Collections.SingletonList
        // instance.  This can't be detected because the SingletonList subclass has
        // private access, so instead detect this condition by looking for a list that has
        // a single element with a value of String instead of Map
        if (collection.size() == 1 && ((AbstractList) collection).get(0) instanceof String) {

            values = new ArrayList<Map<String, String>>();
            String raw = (String) ((AbstractList) collection).get(0);

            // split up the string and put it back in the superclass as an elist

            // split on ','
            String[] lists = raw.split(",");
            for (String list : lists) {
                HashMap m = new HashMap();
                values.add(m);
                // split on ';'
                String[] arg = list.split(";");
                for (String pair : arg) {
                    // split on ':'
                    String[] map = pair.split(":");
                    m.put(map[0], map[1]);
                }
            }
        } else {
            values = (List<Map<String, String>>) collection;
        }
        return super.addAll(values);
    }
}