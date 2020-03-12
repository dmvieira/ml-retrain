package br.pucrio;

import br.pucrio.agents.TrainingAgent;
import br.pucrio.agents.data.ClusterAgent;
import br.pucrio.agents.data.MetadataAgent;
import br.pucrio.agents.model.FeatureImportanceAgent;
import br.pucrio.agents.model.PredictionMetricAgent;
import br.pucrio.agents.model.WeightAgent;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Class<? extends Agent>> getDataAgents(String platform) {
        List<Class<? extends Agent>> classes = new ArrayList();
        if (platform.equals("Ingestion")) {
            classes.add(MetadataAgent.class);
            classes.add(ClusterAgent.class);
        }
        return classes;
    }

    private static List<Class<? extends Agent>> getModelAgents(String platform) {
        List<Class<? extends Agent>> classes = new ArrayList();
        if (platform.equals("Training")){
            classes.add(FeatureImportanceAgent.class);
            classes.add(WeightAgent.class);
        } else if (platform.equals("Prediction")){
            classes.add(PredictionMetricAgent.class);
        }

        return classes;
    }

    private static void loadAllAgents(Runtime rt, AgentContainer mainContainer, String platform) throws StaleProxyException {

        List<Class<? extends Agent>> classes = getDataAgents(platform);
        classes.addAll(getModelAgents(platform));

        if (platform.equals("Controller")) {
            classes.add(TrainingAgent.class);
        }
        ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
        pContainer.setParameter(Profile.CONTAINER_NAME, platform);
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
        List<String> platforms = new ArrayList();
        platforms.add("Ingestion");
        platforms.add("Training");
        platforms.add("Prediction");
        platforms.add("Controller");

        Profile profile = new ProfileImpl(null, 1200, "Cluster");
        AgentContainer mainContainer = rt.createMainContainer(profile);

        for (String platform : platforms) {
            loadAllAgents(rt, mainContainer, platform);
        }
        AgentController rma = mainContainer.createNewAgent(
                "rma", "jade.tools.rma.rma", new Object[0]);
        rma.start();


    }
}
