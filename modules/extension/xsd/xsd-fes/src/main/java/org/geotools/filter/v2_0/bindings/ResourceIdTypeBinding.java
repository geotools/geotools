package org.geotools.filter.v2_0.bindings;

import java.util.Calendar;
import java.util.Date;

import javax.xml.namespace.QName;

import org.geotools.filter.identity.ResourceIdImpl;
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
        final String previousRid = (String) node.getAttributeValue("previousRid");
        final Version version = (Version) node.getAttributeValue("version");
        final Calendar startTimeAtt = (Calendar) node.getAttributeValue("startDate");
        final Calendar endTimeAtt = (Calendar) node.getAttributeValue("endDate");

        Date startTime = startTimeAtt == null ? null : startTimeAtt.getTime();
        Date endTime = endTimeAtt == null ? null : endTimeAtt.getTime();

        String fid;
        String featureVersion = null;
        int idx = rid.indexOf(ResourceId.VERSION_SEPARATOR);
        if (idx == -1) {
            fid = rid;
        } else {
            fid = rid.substring(0, idx);
            featureVersion = rid.substring(idx + 1);
        }
        ResourceIdImpl resourceId = null;
        if(version == null)
        	resourceId = new ResourceIdImpl(fid, featureVersion);
        else
        	resourceId = new ResourceIdImpl(fid, featureVersion, version );
        resourceId.setPreviousRid(previousRid);
        resourceId.setStartTime(startTime);
        resourceId.setEndTime(endTime);
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
            if (fid instanceof ResourceId) {
                return ((ResourceId) fid).getRid();
            } else {
                return fid.getID();
            }
        }
        if ("previousRid".equals(localName) && fid instanceof ResourceId) {
            return ((ResourceId) fid).getPreviousRid();
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
