package org.geotools.filter.v2_0.bindings;

import org.apache.commons.lang3.StringUtils;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.EnumSimpleBinding;
import org.opengis.filter.MultiValuedFilter.MatchAction;

/** Binding for encoding {@link MatchAction} enum values. */
public class MatchActionBinding extends EnumSimpleBinding {

    public MatchActionBinding() {
        super(MatchAction.class, FES.MatchActionType);
    }

    @Override
    public String encode(Object object, String value) throws Exception {
        if (StringUtils.isBlank(value)) return value;
        return StringUtils.capitalize(value.toLowerCase());
    }
}
