/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2024, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.ows.wms;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Iterators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.junit.Test;

public class WMSCapabilitiesTest {

    @Test
    public void getLayerListConcurrency() {
        Layer root = new Layer();
        root.setName("root");
        root.addChildren(newLayer("L1"));
        root.addChildren(newLayer("L2", "L21"));
        root.addChildren(newLayer("L3", "L31", "L32"));

        WMSCapabilities wmsCapabilities = new WMSCapabilities();
        wmsCapabilities.setLayer(root);

        Callable<List<Layer>> task = () -> {
            List<Layer> list = wmsCapabilities.getLayerList();
            // this will cause a ConcurrentModificationException as of GEOT-7669
            assertEquals(7, Iterators.size(list.iterator()));
            return list;
        };

        List<Future<List<Layer>>> futures = ForkJoinPool.commonPool().invokeAll(Collections.nCopies(100, task));
        List<List<Layer>> values = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .collect(Collectors.toList());

        List<Layer> expected = buildExpected(root);

        values.forEach(layers -> assertEquals(expected, layers));
    }

    private List<Layer> buildExpected(Layer root) {
        List<Layer> expected = new ArrayList<>();
        expected.add(root);
        addChildrenRecursive(expected, root);
        return expected;
    }

    @Test
    public void setLayerClearsCachedLayerList() {
        Layer root = new Layer();
        root.setName("root");
        root.addChildren(newLayer("LA1"));
        root.addChildren(newLayer("LA2", "LA21"));

        WMSCapabilities wmsCapabilities = new WMSCapabilities();
        wmsCapabilities.setLayer(root);

        List<Layer> expected = buildExpected(root);
        assertEquals(expected, wmsCapabilities.getLayerList());

        Layer newroot = new Layer();
        newroot.setName("newroot");
        newroot.addChildren(newLayer("LB1"));
        newroot.addChildren(newLayer("LB2", "LB21"));

        wmsCapabilities.setLayer(newroot);
        expected = buildExpected(newroot);
        assertEquals(expected, wmsCapabilities.getLayerList());
    }

    private void addChildrenRecursive(List<Layer> layers, Layer layer) {
        if (layer.getChildren() != null) {
            for (Layer child : layer.getChildren()) {
                layers.add(child);
                addChildrenRecursive(layers, child);
            }
        }
    }

    private Layer newLayer(String name, String... children) {
        Layer layer = new Layer();
        layer.setName(name);
        if (null != children) {
            List.of(children).forEach(childName -> layer.addChildren(newLayer(childName)));
        }
        return layer;
    }
}
