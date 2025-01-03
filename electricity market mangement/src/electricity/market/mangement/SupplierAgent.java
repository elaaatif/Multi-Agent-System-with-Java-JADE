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
public class SupplierAgent extends Agent {
    private double price = 100.0;
    private int profitStreak = 0;
    private double balance = 0.0;
    private List<String> clients = new ArrayList<>();

    protected void setup() {
        System.out.println(getLocalName() + " is initialized.");
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                sendBills();
                evaluateProfit();
                respondToQueries();
            }
        });
    }

    private void sendBills() {
        for (String client : clients) {
            ACLMessage bill = new ACLMessage(ACLMessage.INFORM);
            bill.addReceiver(getAID(client));
            bill.setContent("Price: " + price);
            send(bill);
            System.out.println(getLocalName() + " sent bill to " + client + ": " + price);
        }
    }

    private void evaluateProfit() {
        double operationalCost = clients.size() * 50;
        double profit = clients.size() * price - operationalCost;
        balance += profit;

        if (profit > 0) {
            profitStreak++;
            if (profitStreak == 4) {
                price *= 1.10;
                profitStreak = 0;
                System.out.println(getLocalName() + " increased price to " + price);
            }
        } else {
            profitStreak = 0;
            price *= 0.90;
            System.out.println(getLocalName() + " decreased price to " + price);
        }
    }

    private void respondToQueries() {
        ACLMessage query;
        while ((query = receive(MessageTemplate.MatchContent("Price Query"))) != null) {
            ACLMessage reply = query.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("Price: " + price + ", Balance: " + balance);
            send(reply);
            System.out.println(getLocalName() + " responded to price query.");
        }
    }

    public void addClient(String clientName) {
        clients.add(clientName);
    }
}
