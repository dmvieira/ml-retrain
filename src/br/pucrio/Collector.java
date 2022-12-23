package br.pucrio;

import br.pucrio.agents.collectors.injestion.ClusterAgent;
import br.pucrio.agents.collectors.injestion.CorrelationAgent;
import br.pucrio.agents.collectors.injestion.MetadataAgent;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

public class Collector {

    private static List<Class<? extends Agent>> getInjestionAgents() {
        List<Class<? extends Agent>> classes = new ArrayList();
        classes.add(MetadataAgent.class);
        classes.add(CorrelationAgent.class);
        classes.add(ClusterAgent.class);
        return classes;
    }

    public static void loadAllAgents(Runtime rt) throws StaleProxyException {

        List<Class<? extends Agent>> classes = getInjestionAgents();

        ProfileImpl pContainer = new ProfileImpl();
        pContainer.setParameter(Profile.CONTAINER_NAME, "Injestion");
        AgentContainer cont = rt.createAgentContainer(pContainer);

        for (Class<? extends Agent> klass: classes) {
            AgentController agent = cont.createNewAgent(
                    klass.getSimpleName(), klass.getName(), new Object[0]);
            agent.start();
        }

    }

    public static void main(String[] args) throws StaleProxyException {
        Runtime rt = Runtime.instance();
        loadAllAgents(rt);
    }
}
