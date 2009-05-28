package org.drools.integration.console;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.process.Process;
import org.drools.io.ResourceFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.process.audit.ProcessInstanceDbLog;
import org.drools.process.audit.ProcessInstanceLog;
import org.drools.process.audit.WorkingMemoryDbLogger;
import org.drools.process.workitem.wsht.CommandBasedWSHumanTaskHandler;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;

public class DroolsFlowCommandDelegate {
	
	private static StatefulKnowledgeSession ksession;
	
	private StatefulKnowledgeSession newStatefulKnowledgeSession() {
		try {
			KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("Guvnor default");
			kagent.applyChangeSet(ResourceFactory.newClassPathResource("ChangeSet.xml"));
			kagent.monitorResourceChangeEvents(false);
			KnowledgeBase kbase = kagent.getKnowledgeBase();
			StatefulKnowledgeSession ksession = null;
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(
					"org.drools.persistence.jpa");
	        Environment env = KnowledgeBaseFactory.newEnvironment();
	        env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
			try {
				ksession = JPAKnowledgeService.loadStatefulKnowledgeSession(
					1, kbase, null, env);
				System.out.println("Loading session data ...");
			} catch (RuntimeException e) {
				if ("Could not find session data for id 1".equals(e.getMessage())) {
					ksession = JPAKnowledgeService.newStatefulKnowledgeSession(
						kbase, null, env);
					System.out.println("Creating new session data ...");
				} else {
					throw e;
				}
			}
			new WorkingMemoryDbLogger(ksession);
			ksession.getWorkItemManager().registerWorkItemHandler(
				"Human Task", new CommandBasedWSHumanTaskHandler(ksession));
			System.out.println("Successfully loaded default package from Guvnor");
			return ksession;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(
				"Could not initialize stateful knowledge session: "
					+ t.getMessage());
		}
	}
	
	private StatefulKnowledgeSession getSession() {
		if (ksession == null) {
			ksession = newStatefulKnowledgeSession();
		}
		return ksession;
	}
	
	public List<Process> getProcesses() {
		List<Process> result = new ArrayList<Process>();
		for (KnowledgePackage kpackage: getSession().getKnowledgeBase().getKnowledgePackages()) {
			result.addAll(kpackage.getProcesses());
		}
		return result;
	}
	
	public Process getProcess(String processId) {
		for (KnowledgePackage kpackage: getSession().getKnowledgeBase().getKnowledgePackages()) {
			for (Process process: kpackage.getProcesses()) {
				if (processId.equals(process.getId())) {
					return process;
				}
			}
		}
		return null;
	}
	
	public Process getProcessByName(String name) {
		for (KnowledgePackage kpackage: getSession().getKnowledgeBase().getKnowledgePackages()) {
			for (Process process: kpackage.getProcesses()) {
				if (name.equals(process.getName())) {
					return process;
				}
			}
		}
		return null;
	}

	public void removeProcess(String processId) {
		throw new UnsupportedOperationException();
	}
	
	public ProcessInstanceLog getProcessInstanceLog(String processInstanceId) {
		return ProcessInstanceDbLog.findProcessInstance(new Long(processInstanceId));
	}

	public List<ProcessInstanceLog> getProcessInstanceLogsByProcessId(String processId) {
		return ProcessInstanceDbLog.findProcessInstances(processId);
	}
	
	public ProcessInstanceLog startProcess(String processId) {
		long processInstanceId = ksession.startProcess(processId).getId();
		return ProcessInstanceDbLog.findProcessInstance(processInstanceId);
	}
	
	public void abortProcessInstance(String processInstanceId) {
		ProcessInstance processInstance = ksession.getProcessInstance(new Long(processInstanceId));
		if (processInstance != null) {
			ksession.abortProcessInstance(new Long(processInstanceId));
		} else {
			throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
		}
	}
	
	public void signalExecution(String executionId, String signal) {
		ksession.getProcessInstance(new Long(executionId))
			.signalEvent("signal", signal);
	}

}