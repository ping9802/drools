/**
 * Copyright 2010 JBoss Inc
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

package org.drools.bpmn2.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.bpmn2.xpath.XPathDialect;
import org.drools.compiler.xml.XmlDumper;
import org.drools.definition.process.Connection;
import org.drools.definition.process.Node;
import org.drools.definition.process.NodeContainer;
import org.drools.definition.process.WorkflowProcess;
import org.drools.process.core.ContextContainer;
import org.drools.process.core.Work;
import org.drools.process.core.context.swimlane.Swimlane;
import org.drools.process.core.context.swimlane.SwimlaneContext;
import org.drools.process.core.context.variable.Variable;
import org.drools.process.core.context.variable.VariableScope;
import org.drools.process.core.datatype.impl.type.ObjectDataType;
import org.drools.process.core.event.EventTypeFilter;
import org.drools.rule.builder.dialect.java.JavaDialect;
import org.drools.workflow.core.Constraint;
import org.drools.workflow.core.impl.DroolsConsequenceAction;
import org.drools.workflow.core.node.ActionNode;
import org.drools.workflow.core.node.CompositeNode;
import org.drools.workflow.core.node.EndNode;
import org.drools.workflow.core.node.EventNode;
import org.drools.workflow.core.node.EventTrigger;
import org.drools.workflow.core.node.FaultNode;
import org.drools.workflow.core.node.ForEachNode;
import org.drools.workflow.core.node.HumanTaskNode;
import org.drools.workflow.core.node.Split;
import org.drools.workflow.core.node.StartNode;
import org.drools.workflow.core.node.Trigger;
import org.drools.workflow.core.node.WorkItemNode;
import org.drools.xml.Handler;
import org.drools.xml.SemanticModule;

public class XmlBPMNProcessDumper {
	
	public static final String JAVA_LANGUAGE = "http://www.java.com/java";
	public static final String RULE_LANGUAGE = "http://www.jboss.org/drools/rule";
    public static final String XPATH_LANGUAGE = "http://www.w3.org/1999/XPath";
    
	public static XmlBPMNProcessDumper INSTANCE = new XmlBPMNProcessDumper();
	
    private final static String EOL = System.getProperty( "line.separator" );
    
    private SemanticModule semanticModule;
    
    private XmlBPMNProcessDumper() {
    	semanticModule = new BPMNSemanticModule();
    }
    
    public String dump(WorkflowProcess process) {
        return dump(process, true);
    }
    
    public String dump(WorkflowProcess process, boolean includeMeta) {
        StringBuilder xmlDump = new StringBuilder();
        visitProcess(process, xmlDump, includeMeta);
        return xmlDump.toString();
    }
    
    protected void visitProcess(WorkflowProcess process, StringBuilder xmlDump, boolean includeMeta) {
        String targetNamespace = (String) process.getMetaData("TargetNamespace");
        if (targetNamespace == null) {
        	targetNamespace = "http://www.jboss.org/drools";
        }
    	xmlDump.append(
    		"<?xml version=\"1.0\" encoding=\"UTF-8\"?> " + EOL +
            "<definitions id=\"Definition\"" + EOL +
            "             targetNamespace=\"" + targetNamespace + "\"" + EOL +
            "             typeLanguage=\"http://www.java.com/javaTypes\"" + EOL +
            "             expressionLanguage=\"http://www.mvel.org/2.0\"" + EOL +
            "             xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"" + EOL +
            "             xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"" + EOL +
            "             xs:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\"" + EOL +
            "             xmlns:g=\"http://www.jboss.org/drools/flow/gpd\"" + EOL +
            "             xmlns:tns=\"http://www.jboss.org/drools\">" + EOL + EOL);

    	// item definitions
    	VariableScope variableScope = (VariableScope)
    		((org.drools.process.core.Process) process).getDefaultContext(VariableScope.VARIABLE_SCOPE);
    	visitVariableScope(variableScope, "_", xmlDump);
    	visitSubVariableScopes(process.getNodes(), xmlDump);
        
	    visitInterfaces(process.getNodes(), xmlDump);
	    
	    visitEscalations(process.getNodes(), xmlDump, new ArrayList<String>());
	    visitErrors(process.getNodes(), xmlDump, new ArrayList<String>());
	       
	    // the process itself
		xmlDump.append("  <process processType=\"Private\" isExecutable=\"true\" ");
        if (process.getId() != null) {
            xmlDump.append("id=\"" + XmlDumper.replaceIllegalChars(process.getId()) + "\" ");
        }
        if (process.getName() != null) {
            xmlDump.append("name=\"" + XmlDumper.replaceIllegalChars(process.getName()) + "\" ");
        }
        String packageName = process.getPackageName();
        if (packageName != null && !"org.drools.bpmn2".equals(packageName)) {
            xmlDump.append("tns:packageName=\"" + XmlDumper.replaceIllegalChars(packageName) + "\" ");
        }
        // TODO: package, version
        xmlDump.append(">" + EOL + EOL);
        visitLanes(process, xmlDump);
        visitHeader(process, xmlDump, includeMeta);
        visitNodes(process, xmlDump, includeMeta);
        visitConnections(process.getNodes(), xmlDump, includeMeta);
        xmlDump.append("  </process>" + EOL + EOL);
        xmlDump.append("</definitions>");
    }
    
    private void visitVariableScope(VariableScope variableScope, String prefix, StringBuilder xmlDump) {
        if (variableScope != null && !variableScope.getVariables().isEmpty()) {
            for (Variable variable: variableScope.getVariables()) {
                xmlDump.append(
                    "  <itemDefinition id=\"" + XmlDumper.replaceIllegalChars(prefix + variable.getName()) + "Item\" ");
                if (variable.getType() != null) {
                    xmlDump.append("structureRef=\"" + XmlDumper.replaceIllegalChars(variable.getType().getStringType()) + "\" ");
                }
                xmlDump.append("/>" + EOL);
            }
            xmlDump.append(EOL);
        }
    }
    
    private void visitSubVariableScopes(Node[] nodes, StringBuilder xmlDump) {
        for (Node node: nodes) {
            if (node instanceof ContextContainer) {
                VariableScope variableScope = (VariableScope) 
                    ((ContextContainer) node).getDefaultContext(VariableScope.VARIABLE_SCOPE);
                if (variableScope != null) {
                    visitVariableScope(variableScope, XmlBPMNProcessDumper.getUniqueNodeId(node) + "-", xmlDump);
                }
            }
            if (node instanceof NodeContainer) {
                visitSubVariableScopes(((NodeContainer) node).getNodes(), xmlDump);
            }
        }
    }
    
    private void visitLanes(WorkflowProcess process, StringBuilder xmlDump) {
        // lanes
        Collection<Swimlane> swimlanes = ((SwimlaneContext)
            ((org.drools.workflow.core.WorkflowProcess) process)
                .getDefaultContext(SwimlaneContext.SWIMLANE_SCOPE)).getSwimlanes();
        if (!swimlanes.isEmpty()) {
            xmlDump.append("    <laneSet>" + EOL);
            for (Swimlane swimlane: swimlanes) {
                xmlDump.append("      <lane name=\"" + XmlDumper.replaceIllegalChars(swimlane.getName()) + "\" >" + EOL);
                visitLane(process, swimlane.getName(), xmlDump);
                xmlDump.append("      </lane>" + EOL);
            }
            xmlDump.append("    </laneSet>" + EOL);
        }
    }
    
    private void visitLane(NodeContainer container, String lane, StringBuilder xmlDump) {
        for (Node node: container.getNodes()) {
            if (node instanceof HumanTaskNode) {
                String swimlane = ((HumanTaskNode) node).getSwimlane();
                if (lane.equals(swimlane)) {
                    xmlDump.append("        <flowElementRef>" + XmlBPMNProcessDumper.getUniqueNodeId(node) + "</flowElementRef>" + EOL);
                }
            } else {
                String swimlane = (String) node.getMetaData("Lane");
                if (lane.equals(swimlane)) {
                    xmlDump.append("        <flowElementRef>" + XmlBPMNProcessDumper.getUniqueNodeId(node) + "</flowElementRef>" + EOL);
                }
            }
            if (node instanceof NodeContainer) {
                visitLane((NodeContainer) node, lane, xmlDump);
            }
        }
    }
    
    protected void visitHeader(WorkflowProcess process, StringBuilder xmlDump, boolean includeMeta) {
    	// TODO: imports, function imports
    	// TODO: globals
        // TODO: swimlanes
    	// TODO: exception handlers
        VariableScope variableScope = (VariableScope)
        	((org.drools.process.core.Process) process).getDefaultContext(VariableScope.VARIABLE_SCOPE);
        if (variableScope != null) {
            visitVariables(variableScope.getVariables(), xmlDump);
        }
    }
    
    public static void visitVariables(List<Variable> variables, StringBuilder xmlDump) {
    	if (!variables.isEmpty()) {
            xmlDump.append("    <!-- process variables -->" + EOL);
            for (Variable variable: variables) {
                if (variable.getMetaData("DataObject") == null) {
                    xmlDump.append("    <property id=\"" + XmlDumper.replaceIllegalChars(variable.getName()) + "\" ");
                    if (variable.getType() != null) {
                    	xmlDump.append("itemSubjectRef=\"_" + XmlDumper.replaceIllegalChars(variable.getName()) + "Item\"" );
                    }
                    // TODO: value?
                    xmlDump.append("/>" + EOL);
                }
            }
            for (Variable variable: variables) {
                if (variable.getMetaData("DataObject") != null) {
                    xmlDump.append("    <dataObject id=\"" + XmlDumper.replaceIllegalChars(variable.getName()) + "\" ");
                    if (variable.getType() != null) {
                        xmlDump.append("itemSubjectRef=\"_" + XmlDumper.replaceIllegalChars(variable.getName()) + "Item\"" );
                    }
                    // TODO: value?
                    xmlDump.append("/>" + EOL);
                }
            }
            xmlDump.append(EOL);
    	}
    }
    
    protected void visitInterfaces(Node[] nodes, StringBuilder xmlDump) {
        for (Node node: nodes) {
            if (node instanceof WorkItemNode) {
                Work work = ((WorkItemNode) node).getWork();
                if (work != null) {
                    if ("Service Task".equals(work.getName())) {
                        String interfaceName = (String) work.getParameter("Interface");
                        if (interfaceName == null) {
                            interfaceName = "";
                        }
                        String operationName = (String) work.getParameter("Operation");
                        if (operationName == null) {
                            operationName = "";
                        }
                        String parameterType = (String) work.getParameter("ParameterType");
                        if (parameterType == null) {
                            parameterType = "";
                        }
                        xmlDump.append(
                            "  <itemDefinition id=\"" + getUniqueNodeId(node) + "_InMessageType\" structureRef=\"" + parameterType + "\"/>" + EOL +
                            "  <message id=\"" + getUniqueNodeId(node) + "_InMessage\" itemRef=\"" + getUniqueNodeId(node) + "_InMessageType\" />" + EOL +
                            "  <interface id=\"" + getUniqueNodeId(node) + "_ServiceInterface\" name=\"" + interfaceName + "\">" + EOL +
                            "    <operation id=\"" + getUniqueNodeId(node) + "_ServiceOperation\" name=\"" + operationName + "\">" + EOL + 
                            "      <inMessageRef>" + getUniqueNodeId(node) + "_InMessage</inMessageRef>" + EOL +
                            "    </operation>" + EOL +
                            "  </interface>" + EOL + EOL);
                    } else if ("Send Task".equals(work.getName())) {
                        String messageType = (String) work.getParameter("MessageType");
                        if (messageType == null) {
                            messageType = "";
                        }
                        xmlDump.append(
                            "  <itemDefinition id=\"" + getUniqueNodeId(node) + "_MessageType\" structureRef=\"" + XmlDumper.replaceIllegalChars(messageType) + "\"/>" + EOL +
                            "  <message id=\"" + getUniqueNodeId(node) + "_Message\" itemRef=\"" + getUniqueNodeId(node) + "_MessageType\" />" + EOL + EOL);
                    } else if ("Receive Task".equals(work.getName())) {
                    	String messageId = (String) work.getParameter("MessageId");
                        String messageType = (String) work.getParameter("MessageType");
                        if (messageType == null) {
                            messageType = "";
                        }
                        xmlDump.append(
                            "  <itemDefinition id=\"" + getUniqueNodeId(node) + "_MessageType\" structureRef=\"" + XmlDumper.replaceIllegalChars(messageType) + "\"/>" + EOL +
                            "  <message id=\"" + messageId + "\" itemRef=\"" + getUniqueNodeId(node) + "_MessageType\" />" + EOL + EOL);
                    }
                }
            } else if (node instanceof EndNode) {
                String messageType = (String) node.getMetaData("MessageType");
                if (messageType != null) {
                    xmlDump.append(
                        "  <itemDefinition id=\"" + getUniqueNodeId(node) + "_MessageType\" structureRef=\"" + XmlDumper.replaceIllegalChars(messageType) + "\"/>" + EOL +
                        "  <message id=\"" + getUniqueNodeId(node) + "_Message\" itemRef=\"" + getUniqueNodeId(node) + "_MessageType\" />" + EOL + EOL);
                }
            } else if (node instanceof ActionNode) {
                String messageType = (String) node.getMetaData("MessageType");
                if (messageType != null) {
                    xmlDump.append(
                        "  <itemDefinition id=\"" + getUniqueNodeId(node) + "_MessageType\" structureRef=\"" + XmlDumper.replaceIllegalChars(messageType) + "\"/>" + EOL +
                        "  <message id=\"" + getUniqueNodeId(node) + "_Message\" itemRef=\"" + getUniqueNodeId(node) + "_MessageType\" />" + EOL + EOL);
                }
            } else if (node instanceof EventNode) {
            	if (node.getMetaData("AttachedTo") == null) {
	                String messageRef = ((EventTypeFilter) ((EventNode) node).getEventFilters().get(0)).getType();
	                if (messageRef.startsWith("Message-")) {
		                messageRef = messageRef.substring(8);
		                String messageType = (String) node.getMetaData("MessageType");
		                xmlDump.append(
		                    "  <itemDefinition id=\"" + XmlDumper.replaceIllegalChars(messageRef) + "Type\" structureRef=\"" + XmlDumper.replaceIllegalChars(messageType) + "\"/>" + EOL +
		                    "  <message id=\"" + XmlDumper.replaceIllegalChars(messageRef) + "\" itemRef=\"" + XmlDumper.replaceIllegalChars(messageRef) + "Type\" />" + EOL + EOL);
	                }
            	}
            } else if (node instanceof StartNode) {
                StartNode startNode = (StartNode) node;
                if (startNode.getTriggers() != null && !startNode.getTriggers().isEmpty()) {
                    Trigger trigger = startNode.getTriggers().get(0);
                    if (trigger instanceof EventTrigger) {
                        String eventType = ((EventTypeFilter) ((EventTrigger) trigger).getEventFilters().get(0)).getType();
                        if (eventType.startsWith("Message-")) {
                            eventType = eventType.substring(8);
                            String messageType = (String) node.getMetaData("MessageType");
                            xmlDump.append(
                                "  <itemDefinition id=\"" + XmlDumper.replaceIllegalChars(eventType) + "Type\" structureRef=\"" + XmlDumper.replaceIllegalChars(messageType) + "\"/>" + EOL +
                                "  <message id=\"" + XmlDumper.replaceIllegalChars(eventType) + "\" itemRef=\"" + XmlDumper.replaceIllegalChars(eventType) + "Type\" />" + EOL + EOL);
                        }
                    }
                }
            } else if (node instanceof ForEachNode) {
            	ForEachNode forEachNode = (ForEachNode) node;
            	String type = null;
            	if (forEachNode.getVariableType() instanceof ObjectDataType) {
            		type = ((ObjectDataType) forEachNode.getVariableType()).getClassName(); 
            	}
                xmlDump.append(
                    "  <itemDefinition id=\"" + XmlBPMNProcessDumper.getUniqueNodeId(forEachNode) + "_multiInstanceItemType\" " + (type == null ? "" : "structureRef=\"" + XmlDumper.replaceIllegalChars(type) + "\"") + "/>" + EOL + EOL);
            }
            if (node instanceof CompositeNode) {
            	visitInterfaces(((CompositeNode) node).getNodes(), xmlDump);
            }
        }
    }
    
    protected void visitEscalations(Node[] nodes, StringBuilder xmlDump, List<String> escalations) {
        for (Node node: nodes) {
            if (node instanceof FaultNode) {
            	FaultNode faultNode = (FaultNode) node;
            	if (!faultNode.isTerminateParent()) {
            		String escalationCode = faultNode.getFaultName();
            		if (!escalations.contains(escalationCode)) {
            			escalations.add(escalationCode);
	                    xmlDump.append(
	                        "  <escalation id=\"" + XmlDumper.replaceIllegalChars(escalationCode) + "\" escalationCode=\"" + XmlDumper.replaceIllegalChars(escalationCode) + "\" />" + EOL);
            		}
                }
            } else if (node instanceof ActionNode) {
            	ActionNode actionNode = (ActionNode) node;
            	DroolsConsequenceAction action = (DroolsConsequenceAction) actionNode.getAction();
        		if (action != null) {
        		    String s = action.getConsequence();
	            	if (s.startsWith("org.drools.process.instance.context.exception.ExceptionScopeInstance scopeInstance = (org.drools.process.instance.context.exception.ExceptionScopeInstance) ((org.drools.workflow.instance.NodeInstance) kcontext.getNodeInstance()).resolveContextInstance(org.drools.process.core.context.exception.ExceptionScope.EXCEPTION_SCOPE, \"")) {
	            		s = s.substring(327);
	                    String type = s.substring(0, s.indexOf("\""));
	            		if (!escalations.contains(type)) {
	            			escalations.add(type);
		                    xmlDump.append(
	                            "  <escalation id=\"" + XmlDumper.replaceIllegalChars(type) + "\" escalationCode=\"" + XmlDumper.replaceIllegalChars(type) + "\" />" + EOL);
	            		}
	            	}
        		}
            } else if (node instanceof EventNode) {
            	EventNode eventNode = (EventNode) node;
            	String type = (String) eventNode.getMetaData("EscalationEvent");
            	if (type != null) {
            		if (!escalations.contains(type)) {
            			escalations.add(type);
		                xmlDump.append(
		                    "  <escalation id=\"" + XmlDumper.replaceIllegalChars(type) + "\" escalationCode=\"" + XmlDumper.replaceIllegalChars(type) + "\" />" + EOL);
            		}
            	}
            }
            if (node instanceof CompositeNode) {
            	visitEscalations(((CompositeNode) node).getNodes(), xmlDump, escalations);
            }
        }
    }
    
    protected void visitErrors(Node[] nodes, StringBuilder xmlDump, List<String> errors) {
        for (Node node: nodes) {
            if (node instanceof FaultNode) {
            	FaultNode faultNode = (FaultNode) node;
            	if (faultNode.isTerminateParent()) {
            		String errorCode = faultNode.getFaultName();
            		if (!errors.contains(errorCode)) {
            			errors.add(errorCode);
	                    xmlDump.append(
	                        "  <error id=\"" + XmlDumper.replaceIllegalChars(errorCode) + "\" errorCode=\"" + XmlDumper.replaceIllegalChars(errorCode) + "\" />" + EOL);
            		}
                }
            } else if (node instanceof EventNode) {
            	EventNode eventNode = (EventNode) node;
            	String type = (String) eventNode.getMetaData("ErrorEvent");
            	if (type != null) {
            		if (!errors.contains(type)) {
            			errors.add(type);
		                xmlDump.append(
		                    "  <error id=\"" + XmlDumper.replaceIllegalChars(type) + "\" errorCode=\"" + XmlDumper.replaceIllegalChars(type) + "\" />" + EOL);
            		}
            	}
            }
            if (node instanceof CompositeNode) {
            	visitErrors(((CompositeNode) node).getNodes(), xmlDump, errors);
            }
        }
    }
    
    private void visitNodes(WorkflowProcess process, StringBuilder xmlDump, boolean includeMeta) {
    	xmlDump.append("    <!-- nodes -->" + EOL);
        for (Node node: process.getNodes()) {
            visitNode(node, xmlDump, includeMeta);
        }
        xmlDump.append(EOL);
    }
    
    public void visitNode(Node node, StringBuilder xmlDump, boolean includeMeta) {
     	Handler handler = semanticModule.getHandlerByClass(node.getClass());
        if (handler != null) {
        	((AbstractNodeHandler) handler).writeNode((org.drools.workflow.core.Node) node, xmlDump, includeMeta);
        } else {
        	throw new IllegalArgumentException(
                "Unknown node type: " + node);
        }
    }
    
    private void visitConnections(Node[] nodes, StringBuilder xmlDump, boolean includeMeta) {
    	xmlDump.append("    <!-- connections -->" + EOL);
        List<Connection> connections = new ArrayList<Connection>();
        for (Node node: nodes) {
            for (List<Connection> connectionList: node.getIncomingConnections().values()) {
                connections.addAll(connectionList);
            }
        }
        for (Connection connection: connections) {
            visitConnection(connection, xmlDump, includeMeta);
        }
        xmlDump.append(EOL);
    }
    
    public void visitConnection(Connection connection, StringBuilder xmlDump, boolean includeMeta) {
        xmlDump.append("    <sequenceFlow id=\"" +
    		getUniqueNodeId(connection.getFrom()) + "-" + 
    		getUniqueNodeId(connection.getTo()) + 
    		"\" sourceRef=\"" + getUniqueNodeId(connection.getFrom()) + "\" ");
        // TODO fromType, toType
        xmlDump.append("targetRef=\"" + getUniqueNodeId(connection.getTo()) + "\" ");
        if (includeMeta) {
            String bendpoints = (String) connection.getMetaData("bendpoints");
            if (bendpoints != null) {
                xmlDump.append("g:bendpoints=\"" + bendpoints + "\" ");
            }
        }
        if (connection.getFrom() instanceof Split) {
        	Split split = (Split) connection.getFrom();
        	if (split.getType() == Split.TYPE_XOR || split.getType() == Split.TYPE_OR) {
        		Constraint constraint = split.getConstraint(connection);
        		if (constraint == null) {
            		xmlDump.append(">" + EOL +
    					"      <conditionExpression xs:type=\"tFormalExpression\" />");
        		} else {
                    if (constraint.getName() != null && constraint.getName().trim().length() > 0) {
            			xmlDump.append("name=\"" + XmlDumper.replaceIllegalChars(constraint.getName()) + "\" ");
            		}
            		xmlDump.append(">" + EOL +
    				"      <conditionExpression xs:type=\"tFormalExpression\" ");
                    if ("code".equals(constraint.getType())) {
                        if (JavaDialect.ID.equals(constraint.getDialect())) {
                            xmlDump.append("language=\"" + JAVA_LANGUAGE + "\" ");
                        } else if (XPathDialect.ID.equals(constraint.getDialect())) {
                            xmlDump.append("language=\"" + XPATH_LANGUAGE + "\" ");
                        }
                    } else {
                        xmlDump.append("language=\"" + RULE_LANGUAGE + "\" ");
                    }
                    String constraintString = constraint.getConstraint();
                    if (constraintString == null) {
                        constraintString = "";
                    }
                    xmlDump.append(">" + XmlDumper.replaceIllegalChars(constraintString) + "</conditionExpression>");
        		}
        		xmlDump.append(EOL
    		        + "    </sequenceFlow>" + EOL);
        	} else {
            	xmlDump.append("/>" + EOL);
            }
        } else {
        	xmlDump.append("/>" + EOL);
        }
    }
    
    public static String getUniqueNodeId(Node node) {
    	String result = (String) node.getMetaData("UniqueId");
    	if (result != null) {
    		return result;
    	}
    	result = node.getId() + "";
    	NodeContainer nodeContainer = node.getNodeContainer();
    	while (nodeContainer instanceof CompositeNode) {
    		CompositeNode composite = (CompositeNode) nodeContainer;
    		result = composite.getId() + "-" + result;
    		nodeContainer = composite.getNodeContainer();
    	}
    	return "_" + result;
    }
    
}
