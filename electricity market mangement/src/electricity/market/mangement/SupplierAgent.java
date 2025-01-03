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
            bill.setContent(String.valueOf(price));
            send(bill);
        }
    }

    private void evaluateProfit() {
        double operationalCost = clients.size() * 50; // Example cost
        double profit = clients.size() * price - operationalCost;
        balance += profit;

        if (profit > 0) {
            profitStreak++;
            if (profitStreak == 4) {
                price *= 1.10;
                profitStreak = 0;
            }
        } else {
            profitStreak = 0;
            price *= 0.90;
        }
    }

    private void respondToQueries() {
        // Listen for price queries
        MessageTemplate mt = MessageTemplate.MatchContent("Price Query");
        ACLMessage query = receive(mt);
        if (query != null) {
            ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
            reply.addReceiver(query.getSender());
            reply.setContent("Price: " + price + ", Balance: " + balance);
            send(reply);
        }
    }

    public void addClient(String clientName) {
        clients.add(clientName);
    }

    public String getMetrics() {
        return "Price: " + price + ", Clients: " + clients.size() + ", Balance: " + balance;
    }
}
