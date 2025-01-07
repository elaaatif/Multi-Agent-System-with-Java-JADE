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
    private double subscriptionCost = 100.0; // Initial subscription cost
    private int monthCounter = 0;
    private double credit = 500.0; // Starting credit

    protected void setup() {
        System.out.println(getLocalName() + " is initialized.");
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                if (credit > 0) {  // Only process if the consumer has credit
                    payBill();
                    evaluateSupplier();
                } else {
                    System.out.println(getLocalName() + " has no credit left. Stopping subscription renewal.");
                }
            }
        });
    }

    private void payBill() {
        if (currentSupplier != null && credit > 0) {
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
        monthCounter = 0;

        ACLMessage query = new ACLMessage(ACLMessage.REQUEST);
        query.setContent("Price Query");

        // Dynamically add all suppliers
        List<String> supplierNames = List.of("Supplier1", "Supplier2"); // Extend as needed
        for (String supplier : supplierNames) {
            query.addReceiver(getAID(supplier));
        }

        send(query);
        System.out.println(getLocalName() + " sent price query to suppliers.");

        double lowestPrice = Double.MAX_VALUE;
        String bestSupplier = null;

        for (String supplier : supplierNames) {
            ACLMessage response = blockingReceive(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                2000 // Timeout in milliseconds
            );

            if (response != null) {
                try {
                    // Parse the price from the supplier's response
                    String[] parts = response.getContent().split(",");
                    double supplierPrice = Double.parseDouble(parts[0].split(":")[1].trim());

                    System.out.println("Received price from " + response.getSender().getLocalName() + ": " + supplierPrice);

                    // Compare prices to find the lowest
                    if (supplierPrice < lowestPrice) {
                        lowestPrice = supplierPrice;
                        bestSupplier = response.getSender().getLocalName();
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing response: " + response.getContent());
                }
            } else {
                System.out.println("No response received from " + supplier);
            }
        }

        // Switch to the supplier offering the lowest price
        if (bestSupplier != null) {
            System.out.println(
                getLocalName() + " decided to switch to " + bestSupplier + " with price " + lowestPrice
            );
            currentSupplier = bestSupplier;
            subscriptionCost = lowestPrice; // Update the subscription cost to the new supplier's price
        } else {
            System.out.println(getLocalName() + ": No valid supplier responses received.");
        }
    }
}

}
