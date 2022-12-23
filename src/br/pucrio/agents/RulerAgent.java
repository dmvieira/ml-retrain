/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package br.pucrio.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Random;


public class RulerAgent extends BaseAgent {

//    teardown to ignore services

    protected void setup() {
        System.out.println("Hello World! My name is "+getLocalName());
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            public void onTick() {
                ServiceDescription service = new ServiceDescription();
                service.setType("collector");
                DFAgentDescription dfd = new DFAgentDescription();
                dfd.addServices(service);
                try {
                    DFAgentDescription[] result = DFService.search(getAgent(), dfd);
                    for (DFAgentDescription agentDescription: result) {
                        sendMessage("my path to monitor", agentDescription.getName());
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }

            }
        });
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    Random willRetrain = new Random(10);
                    if (willRetrain.nextBoolean()) {
                        System.out.println("Retrained by msg: "+msg.getContent());
                        // send to weight new AID(agent.getSimpleName(), AID.ISLOCALNAME)
                    }
                }
            }
        });
    }
}
