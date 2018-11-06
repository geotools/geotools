package org.geotools.kml.bindings;

import javax.xml.namespace.QName;
import org.geotools.kml.Folder;
import org.geotools.kml.FolderStack;
import org.geotools.kml.v22.KML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.Binding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NameBinding extends AbstractComplexBinding {

    private final FolderStack folderStack;

    private static final String FOLDER = KML.Folder.getLocalPart();

    public NameBinding(FolderStack folderStack) {
        this.folderStack = folderStack;
    }

    @Override
    public QName getTarget() {
        return KML.name;
    }

    @Override
    public int getExecutionMode() {
        return Binding.OVERRIDE;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getType() {
        return String.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Node parent = node.getParent();
        if (parent != null) {
            String parentElementName = parent.getComponent().getName();
            if (FOLDER.equals(parentElementName)) {
                Folder folder = folderStack.peek();
                if (folder != null) {
                    folder.setName(value.toString());
                }
            }
        }
        return super.parse(instance, node, value);
    }

    public Element encode(Object object, Document document, Element value) throws Exception {
        value.setTextContent(object.toString());
        return value;
    }
}
