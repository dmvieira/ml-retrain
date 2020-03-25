package br.pucrio.agents.collectors;

import br.pucrio.agents.BaseAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public abstract class BaseCollectorAgent extends BaseAgent {
    private HashSet<AID> actuators = new HashSet<>();
    private boolean changed = false;

    protected void setup() {
        System.out.println("Hello World! My name is " + getLocalName());
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

    private Iterator<AID> getActuators() {
        return actuators.iterator();
    }
}
