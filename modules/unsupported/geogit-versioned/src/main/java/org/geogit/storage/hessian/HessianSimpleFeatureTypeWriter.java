package org.geogit.storage.hessian;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.geogit.storage.ObjectWriter;
import org.geotools.referencing.CRS;
import org.geotools.referencing.wkt.Formattable;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.caucho.hessian.io.Hessian2Output;
import com.google.common.base.Preconditions;

public class HessianSimpleFeatureTypeWriter implements
        ObjectWriter<SimpleFeatureType> {

    private SimpleFeatureType type;

    public HessianSimpleFeatureTypeWriter(final SimpleFeatureType type) {
        Preconditions.checkNotNull(type);
        this.type = type;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        Hessian2Output hout = new Hessian2Output(out);
        try {
            hout.startMessage();
            Name typeName = type.getName();
            hout.writeString(typeName.getNamespaceURI() == null ? "" : typeName.getNamespaceURI());
            hout.writeString(typeName.getLocalPart());
            List<AttributeDescriptor> descriptors = type.getAttributeDescriptors();
            hout.writeInt(descriptors.size());
            for(AttributeDescriptor descriptor : descriptors) {
                writeDescriptor(hout, descriptor);
            }

            hout.completeMessage();
        } finally {
            hout.flush();
            hout.close();
        }
    }

    /**
     * The format will be written as follows:
     * <ol>
     * <li>EntityType - int</li>
     * <li>nillable - boolean</li>
     * <li>property namespace - String</li>
     * <li>property name - String</li>
     * <li>max - int</li>
     * <li>min - int</li>
     * <li>type namespace - String</li>
     * <li>type name - String</li>
     * </ol>
     * If the entity type is a geometry, then there are additional fields, 
     * <ol>
     * <li>geometry type - String</li>
     * <li>crs code - boolean</li>
     * <li>crs text - String</li>
     * </ol>
     * 
     * @param hout
     */
    private void writeDescriptor(Hessian2Output hout, AttributeDescriptor descriptor) throws IOException {
        AttributeType attrType = descriptor.getType();
        GtEntityType type = GtEntityType.fromBinding(attrType.getBinding());
        hout.writeInt(type.getValue());
        hout.writeBoolean(descriptor.isNillable());
        Name propertyName = descriptor.getName();
        hout.writeString(propertyName.getNamespaceURI() == null ? "" : propertyName.getNamespaceURI());
        hout.writeString(propertyName.getLocalPart());
        hout.writeInt(descriptor.getMaxOccurs());
        hout.writeInt(descriptor.getMinOccurs());
        Name typeName = attrType.getName();
        hout.writeString(typeName.getNamespaceURI() == null ? "" : typeName.getNamespaceURI());
        hout.writeString(typeName.getLocalPart());
        if(type.equals(GtEntityType.GEOMETRY) && attrType instanceof GeometryType) {
            GeometryType gt = (GeometryType)attrType;
            hout.writeObject(gt.getBinding());
            CoordinateReferenceSystem crs = gt.getCoordinateReferenceSystem();
            String srsName;
            if(crs == null) {
                srsName = "urn:ogc:def:crs:EPSG::0";
            } else {
                srsName = CRS.toSRS(crs);
            }
            if(srsName != null) {
                hout.writeBoolean(true);
                hout.writeString(srsName);
            } else {
                String wkt;
                if(crs instanceof Formattable) {
                    wkt = ((Formattable)crs).toWKT(Formattable.SINGLE_LINE);
                } else {
                    wkt = crs.toWKT();
                }
                hout.writeBoolean(false);
                hout.writeString(wkt);
            }
        }
    }
}
