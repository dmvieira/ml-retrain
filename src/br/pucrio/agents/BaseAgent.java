package br.pucrio.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class BaseAgent extends Agent {
    public void sendMessage(String body, Class<? extends BaseAgent> agent) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(body);
        message.addReceiver(new AID(agent.getSimpleName(), AID.ISLOCALNAME));
        send(message);
        System.out.println("Sent: "+message.getContent()+" to "+agent.getSimpleName());
    }
}
