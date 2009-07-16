package org.drools.ode;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.ode.store.BPELResourceTypeBuilder;
import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.ResourceTypeBuilderRegistry;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.ode.core.BPELProcess;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * This is a sample file to launch a process.
 */
public class DroolsBPELGuvnorTest extends TestCase {

	public void testDroolsBPELProcess() throws Exception {
		KnowledgeBase kbase = readKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		KnowledgeRuntimeLogger logger = 
			KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "audit");
		// start a new process instance
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("Operation", "hello");
		parameters.put("Body", 
			"<ns1:hello xmlns:ns1=\"http://ode/bpel/unit-test.wsdl\">\n" +
    		"  <TestPart xmlns=\"\">Hello</TestPart>\n" +
    		"</ns1:hello>");
		ksession.startProcess("{http://ode/bpel/unit-test}HelloWorld3-1", parameters);
		Thread.sleep(3000);
		logger.close();
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		ResourceType bpelType = new ResourceType(BPELProcess.ODE_TYPE, "ODE BPEL Process");
		ResourceTypeBuilderRegistry.getInstance().register(
			bpelType, new BPELResourceTypeBuilder());
		KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("Guvnor");
		kagent.applyChangeSet(ResourceFactory.newClassPathResource("ChangeSet.xml"));
		return kagent.getKnowledgeBase();
	}

}