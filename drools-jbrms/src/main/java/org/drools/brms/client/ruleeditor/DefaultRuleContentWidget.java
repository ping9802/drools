package org.drools.brms.client.ruleeditor;
/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import org.drools.brms.client.common.DirtyableComposite;
import org.drools.brms.client.rpc.MetaData;
import org.drools.brms.client.rpc.RuleAsset;
import org.drools.brms.client.rpc.RuleContentText;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;


/**
 * This is the default rule editor widget (just text editor based) - more to come later.
 * @author michael neale
 */
public class DefaultRuleContentWidget extends DirtyableComposite {
	
	private TextArea text;
	final private RuleContentText data;

    final private RuleAsset asset;
    
	public DefaultRuleContentWidget(RuleAsset a) {
        asset = a;
        data = (RuleContentText) asset.content;
        
		text = new TextArea();
		text.setWidth("100%");
		text.setHeight("100%");
		text.setVisibleLines(10);
		text.setText(data.content);
        
        text.setStyleName( "default-text-Area" );
        
        text.addChangeListener( new ChangeListener() {
            public void onChange(Widget w) {
                data.content = text.getText();
                makeDirty();
            }
        }); 
        
		initWidget(text);
	}

}