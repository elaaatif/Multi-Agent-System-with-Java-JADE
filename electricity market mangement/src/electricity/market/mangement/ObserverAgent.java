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
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                observeSuppliers();
            }
        });
    }

    private void observeSuppliers() {
        // Query all suppliers for their metrics
        ACLMessage query = new ACLMessage(ACLMessage.REQUEST);
        query.setContent("Metrics Request");

        // Assuming suppliers are named "Supplier1", "Supplier2", etc.
        String[] supplierNames = {"Supplier1", "Supplier2"}; // This could be dynamically discovered
        for (String supplierName : supplierNames) {
            query.addReceiver(getAID(supplierName));
        }
        send(query);

        // Listen for responses
        MessageTemplate mt = MessageTemplate.MatchContent("Price:");
        ACLMessage response = receive(mt);
        if (response != null) {
            // Print metrics received from suppliers
            System.out.println("Observer: Retrieved metrics from supplier " + response.getSender().getLocalName());
            System.out.println("Supplier: " + response.getContent());
        }
    }
}

