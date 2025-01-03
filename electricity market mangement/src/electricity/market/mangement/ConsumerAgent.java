/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electricity.market.mangement;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.*;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author Latif
 */
public class ConsumerAgent extends Agent { 


    private String currentSupplier;
    private double subscriptionCost;
    private int monthCounter = 0;
    private double credit = 500.0; // Example initial credit
    private double newSubscriptionCost;

    protected void setup() {
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                payBill();
                evaluateSupplier();
            }
        });
    }

    private void payBill() {
        if (currentSupplier != null) {
            // Deduct the payment from the credit balance
            credit -= subscriptionCost;
            ACLMessage payment = new ACLMessage(ACLMessage.INFORM);
            payment.addReceiver(getAID(currentSupplier));
            payment.setContent("Payment: " + subscriptionCost);
            send(payment);
            System.out.println(getLocalName() + " paid " + subscriptionCost + " to " + currentSupplier +
                               ". Remaining credit: " + credit);
        }
    }

    private void evaluateSupplier() {
        monthCounter++;
        if (monthCounter == 3) {
            // Query all suppliers for their current offers
            ACLMessage query = new ACLMessage(ACLMessage.REQUEST);
            query.setContent("Price Query");

            // Add supplier agents to the receivers list (assuming we know their names)
            String[] supplierNames = {"Supplier1", "Supplier2"}; // You can dynamically discover agents in a real case
            for (String supplierName : supplierNames) {
                query.addReceiver(getAID(supplierName));
            }
            send(query);

            // Listen for responses and evaluate
            MessageTemplate mt = MessageTemplate.MatchContent("Price:");
            ACLMessage response = receive(mt);
            if (response != null) {
                // Parse the response and choose the supplier with the best offer
                String[] parts = response.getContent().split(",");
                double price = Double.parseDouble(parts[0].split(":")[1].trim());
                double balance = Double.parseDouble(parts[1].split(":")[1].trim());

                if (price < subscriptionCost || currentSupplier == null) {
                    // Switch supplier if a better price is found
                    String newSupplier = response.getSender().getLocalName();
                    System.out.println(getLocalName() + " is switching from " + currentSupplier + " to " +
                                       newSupplier + " with new subscription cost: " + price);

                    currentSupplier = newSupplier;
                    subscriptionCost = price;
                }
            }

            monthCounter = 0;
        }
    }
}

