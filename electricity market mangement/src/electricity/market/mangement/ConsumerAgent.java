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
    private double credit = 500.0;

    protected void setup() {
        System.out.println(getLocalName() + " is initialized.");
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
            query.addReceiver(getAID("Supplier1"));
            query.addReceiver(getAID("Supplier2"));
            send(query);

            System.out.println(getLocalName() + " sent price query to suppliers.");

            for (int i = 0; i < 2; i++) {
                ACLMessage response = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM), 2000);
                if (response != null) {
                    String[] parts = response.getContent().split(",");
                    double price = Double.parseDouble(parts[0].split(":")[1].trim());

                    if (price < subscriptionCost || currentSupplier == null) {
                        String newSupplier = response.getSender().getLocalName();
                        System.out.println(getLocalName() + " is switching to " + newSupplier + " with price " + price);
                        currentSupplier = newSupplier;
                        subscriptionCost = price;
                    }
                }
            }
        }
    }
}
