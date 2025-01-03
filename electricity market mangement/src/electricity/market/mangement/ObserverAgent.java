/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electricity.market.mangement;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.*;

/**
 *
 * @author Latif
 */

public class ObserverAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " is initialized.");
        addBehaviour(new TickerBehaviour(this, 2000) {
            @Override
            protected void onTick() {
                observeSuppliers();
            }
        });
    }

    private void observeSuppliers() {
        ACLMessage query = new ACLMessage(ACLMessage.REQUEST);
        query.setContent("Metrics Request");
        query.addReceiver(getAID("Supplier1"));
        query.addReceiver(getAID("Supplier2"));
        send(query);

        System.out.println(getLocalName() + " requested metrics from suppliers.");

        ACLMessage response;
        while ((response = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM))) != null) {
            System.out.println("Observer received metrics from " + response.getSender().getLocalName() +
                               ": " + response.getContent());
        }
    }
}


