package br.pucrio;

import br.pucrio.agents.collectors.injestion.ClusterAgent;
import br.pucrio.agents.collectors.injestion.MetadataAgent;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

public class Collector {

    private static List<Class<? extends Agent>> getDataAgents() {
        List<Class<? extends Agent>> classes = new ArrayList();
        classes.add(MetadataAgent.class);
        classes.add(ClusterAgent.class);
        return classes;
    }

    private static void loadAllAgents(Runtime rt, AgentContainer mainContainer) throws StaleProxyException {

        List<Class<? extends Agent>> classes = getDataAgents();

        ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
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
        rt.setCloseVM(true);

        Profile profile = new ProfileImpl(null, 1200, "Collector");
        AgentContainer mainContainer = rt.createMainContainer(profile);

        loadAllAgents(rt, mainContainer);
        AgentController rma = mainContainer.createNewAgent(
                "rma", "jade.tools.rma.rma", new Object[0]);
        rma.start();


    }
}
