package org.drools.bpmn2.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.drools.bpmn2.core.ItemDefinition;
import org.drools.xml.BaseAbstractHandler;
import org.drools.xml.ExtensibleXmlParser;
import org.drools.xml.Handler;
import org.drools.xml.ProcessBuildData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ItemDefinitionHandler extends BaseAbstractHandler implements Handler {
	
	@SuppressWarnings("unchecked")
	public ItemDefinitionHandler() {
		if ((this.validParents == null) && (this.validPeers == null)) {
			this.validParents = new HashSet();
			this.validParents.add(null);

			this.validPeers = new HashSet();
			this.validPeers.add(null);
            this.validPeers.add(ItemDefinition.class);

			this.allowNesting = false;
		}
	}

	@SuppressWarnings("unchecked")
    public Object start(final String uri, final String localName,
			            final Attributes attrs, final ExtensibleXmlParser parser)
			throws SAXException {
		parser.startElementBuilder(localName, attrs);

		String id = attrs.getValue("id");
		String type = attrs.getValue("structureRef");

		ProcessBuildData buildData = (ProcessBuildData) parser.getData();
		Map<String, ItemDefinition> itemDefintions = (Map<String, ItemDefinition>)
            ((ProcessBuildData) parser.getData()).getMetaData("ItemDefinitions");
        if (itemDefintions == null) {
            itemDefintions = new HashMap<String, ItemDefinition>();
            buildData.setMetaData("ItemDefinitions", itemDefintions);
        }
        ItemDefinition itemDefinition = new ItemDefinition(id); 
        itemDefinition.setStructureRef(type);
        itemDefintions.put(id, itemDefinition);
		return itemDefinition;
	}

	public Object end(final String uri, final String localName,
			          final ExtensibleXmlParser parser) throws SAXException {
		parser.endElementBuilder();
		return parser.getCurrent();
	}

	public Class<?> generateNodeFor() {
		return ItemDefinition.class;
	}

}