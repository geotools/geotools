package org.geotools.data.aggregate;

import java.util.List;

import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.opengis.feature.type.Name;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

class AggregateTypeEncoder extends TransformerBase {

    @Override
    public Translator createTranslator(ContentHandler handler) {
        return new ConfigurationTranslator(handler);
    }

    private static class ConfigurationTranslator extends TranslatorSupport {

        public ConfigurationTranslator(ContentHandler contentHandler) {
            super(contentHandler, null, null);
        }

        @Override
        public void encode(Object o) throws IllegalArgumentException {
            List<AggregateTypeConfiguration> configs = (List<AggregateTypeConfiguration>) o;

            start("AggregateTypes", attributes("version", "1.0"));
            for (AggregateTypeConfiguration config : configs) {
                start("AggregateType", attributes("name", config.getName()));
                for (SourceType st : config.getSourceTypes()) {
                    element("Source",
                            null,
                            attributes("store", st.getStoreName().getURI(), "type",
                                    st.getTypeName()));
                }
                end("AggregateType");
            }
            end("AggregateTypes");
        }

        private AttributesImpl attributes(String... kvp) {
            String[] atts = kvp;
            AttributesImpl attributes = new AttributesImpl();
            for (int i = 0; i < atts.length; i += 2) {
                String name = atts[i];
                String value = atts[i + 1];
                attributes.addAttribute("", name, name, "", value);
            }
            return attributes;
        }

    }

}
