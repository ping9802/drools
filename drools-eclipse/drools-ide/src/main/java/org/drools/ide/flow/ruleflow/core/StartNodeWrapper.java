package org.drools.ide.flow.ruleflow.core;
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

import org.drools.ide.flow.common.editor.core.ElementConnection;
import org.drools.ruleflow.core.IConnection;
import org.drools.ruleflow.core.IStartNode;
import org.drools.ruleflow.core.impl.StartNode;

/**
 * Wrapper for a start node.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class StartNodeWrapper extends NodeWrapper {

    private static final long serialVersionUID = 3978428031117375030L;

    public StartNodeWrapper() {
        setNode(new StartNode());
        setName("Start");
    }
    
    public IStartNode getStartNode() {
        return (IStartNode) getNode();
    }
    
    public boolean acceptsIncomingConnection(ElementConnection connection) {
        return false;
    }

    public boolean acceptsOutgoingConnection(ElementConnection connection) {
        return connection.getType() == IConnection.TYPE_NORMAL
    		&& getOutgoingConnections().isEmpty();
    }
}
