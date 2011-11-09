package org.geotools.filter.v2_0.bindings;

import java.util.Date;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.ResourceId;
import org.opengis.filter.identity.Version;

public class ResourceIdTypeBinding extends AbstractComplexBinding {

    FilterFactory factory;

    public ResourceIdTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @return {@code FeatureId.class}, meant to catch {@code ResourceId.class} too
     */
    public Class<?> getType() {
        return FeatureId.class;
    }

    public QName getTarget() {
        return FES.ResourceIdType;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        final String rid = (String) node.getAttributeValue("rid");
        //final String previousRid = (String) node.getAttributeValue("previousRid");
        final Version version = (Version) node.getAttributeValue("version");
        final Date startTime = (Date) node.getAttributeValue("startDate");
        final Date endTime = (Date) node.getAttributeValue("endDate");

        String fid;
        String featureVersion = null;
        int idx = rid.indexOf(ResourceId.VERSION_SEPARATOR);
        if (idx == -1) {
            fid = rid;
        } else {
            fid = rid.substring(0, idx);
            featureVersion = rid.substring(idx + 1);
        }
        ResourceId resourceId;
        if (startTime != null || endTime != null) {
            resourceId = factory.resourceId(fid, startTime, endTime);
        } else {
            resourceId = factory.resourceId(fid, featureVersion, version);
        }
        //resourceId.setPreviousRid(previousRid);

        return resourceId;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (object == null) {
            return null;
        }

        final FeatureId fid = (FeatureId) object;
        final String localName = name.getLocalPart();
        if ("id".equals(localName)) {
            return fid.getID();
        }
        if ("rid".equals(localName)) {
            return fid.getRid();
        }
        if ("previousRid".equals(localName)) {
            return fid.getPreviousRid();
        }
        if ("version".equals(localName) && fid instanceof ResourceId) {
            return ((ResourceId) fid).getVersion();
        }
        if ("startDate".equals(localName) && fid instanceof ResourceId) {
            return ((ResourceId) fid).getStartTime();
        }
        if ("endDate".equals(localName) && fid instanceof ResourceId) {
            return ((ResourceId) fid).getEndTime();
        }

        return null;
    }
}
