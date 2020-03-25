package br.pucrio.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public abstract class BaseAgent extends Agent {
    public void sendMessage(String body, AID agent) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(body);
        message.addReceiver(agent);
        send(message);
        System.out.println("Sent: "+message.getContent()+" to "+agent.getLocalName());
    }
}
