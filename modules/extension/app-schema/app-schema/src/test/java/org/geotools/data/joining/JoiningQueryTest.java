/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.joining;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class JoiningQueryTest {

    private static final String PRIMARY_JOIN_TYPE_NAME = "joined_feature_type";
    private static final String SECONDARY_JOIN_TYPE_NAME = "nested_feature_type";

    @Test
    public void testCopyConstructorDoesNotShareMutableState() {
        JoiningQuery.QueryJoin join = new JoiningQuery.QueryJoin();
        join.setJoiningTypeName(PRIMARY_JOIN_TYPE_NAME);
        join.addId("id_temp");

        JoiningQuery original = new JoiningQuery();
        original.setQueryJoins(Collections.singletonList(join));
        original.addId("root_id");

        JoiningQuery copy = new JoiningQuery(original);
        copy.getQueryJoins().get(0).addId("another_id");
        copy.addId("other_root_id");

        assertNotSame(original.getQueryJoins().get(0), copy.getQueryJoins().get(0));
        assertEquals(1, original.getQueryJoins().get(0).getIds().size());
        assertFalse(original.getIds().contains("other_root_id"));
    }

    @Test
    public void testSetQueryJoinsCopiesInputJoins() {
        JoiningQuery.QueryJoin externalJoin = new JoiningQuery.QueryJoin();
        externalJoin.setJoiningTypeName(SECONDARY_JOIN_TYPE_NAME);
        externalJoin.addId("id_temp");
        List<JoiningQuery.QueryJoin> joins = new ArrayList<>();
        joins.add(externalJoin);

        JoiningQuery query = new JoiningQuery();
        query.setQueryJoins(joins);

        externalJoin.addId("leaked_id");
        joins.clear();

        assertEquals(1, query.getQueryJoins().size());
        assertEquals(1, query.getQueryJoins().get(0).getIds().size());
    }
}
