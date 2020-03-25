package br.pucrio;

import br.pucrio.agents.TrainingAgent;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

public class Actuator {

    private static void loadAllAgents(Runtime rt, AgentContainer mainContainer) throws StaleProxyException {

        List<Class<? extends Agent>> classes = new ArrayList<>();
        classes.add(TrainingAgent.class);
        ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
        pContainer.setParameter(Profile.CONTAINER_NAME, "Tranining");
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

        Profile profile = new ProfileImpl(null, 1200, "Actuator");
        AgentContainer mainContainer = rt.createMainContainer(profile);

        loadAllAgents(rt, mainContainer);
        AgentController rma = mainContainer.createNewAgent(
                "rma", "jade.tools.rma.rma", new Object[0]);
        rma.start();


    }
}
