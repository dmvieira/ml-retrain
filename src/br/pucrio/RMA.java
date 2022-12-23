package br.pucrio;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class RMA {

    public static void main(String[] args) throws StaleProxyException {
        Runtime rt = Runtime.instance();
        rt.setCloseVM(true);
        Profile profile = new ProfileImpl("localhost", 1200, "Retrain");
        AgentContainer mainContainer = rt.createMainContainer(profile);

        Collector.loadAllAgents(rt);
        Actuator.loadAllAgents(rt);

        AgentController rma = mainContainer.createNewAgent(
                "rma", "jade.tools.rma.rma", new Object[0]);
        rma.start();


    }
}
