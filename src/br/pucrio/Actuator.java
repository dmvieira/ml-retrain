package br.pucrio;

import br.pucrio.agents.RulerAgent;
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

    public static void loadAllAgents(Runtime rt) throws StaleProxyException {

        List<Class<? extends Agent>> classes = new ArrayList<>();
        classes.add(RulerAgent.class);
        ProfileImpl pContainer = new ProfileImpl();
        pContainer.setParameter(Profile.CONTAINER_NAME, "Ruler");
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
