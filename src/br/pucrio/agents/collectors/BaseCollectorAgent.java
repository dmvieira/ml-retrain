package br.pucrio.agents.collectors;

import br.pucrio.agents.BaseAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Iterator;

public abstract class BaseCollectorAgent extends BaseAgent {
    private HashSet<AID> actuators = new HashSet<>();
    private boolean changed = false;

    protected void setup() {
        System.out.println("Hello World! My name is " + getLocalName());
        registerCollector();
        registerActuator();
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
            if (changed) {
                Iterator<AID> actuators = getActuators();
                while (actuators.hasNext()) {
                    sendMessage(getLocalName() + " changed", actuators.next());
                }
                changed = false;
            }
            }
        });
    }
// need support for just one actuator changing
    protected void setChanged(){
        changed = true;
    }

    private void registerActuator() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    actuators.add(msg.getSender());
                }
            }
        });
    }

    private void registerCollector() {
        addBehaviour(new OneShotBehaviour(this) {
            @Override
            public void action() {
                ServiceDescription service = new ServiceDescription();
                service.setType("collector");
                service.setName(getLocalName());
                DFAgentDescription dfd = new DFAgentDescription();
                dfd.setName(getAID());
                dfd.addServices(service);
                try {
                    DFService.register(getAgent(), dfd);
                }
                catch (FIPAException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private Iterator<AID> getActuators() {
        return actuators.iterator();
    }
}
