/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package electricity.market.mangement;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
/**
 *
 * @author Latif
 */
public class ElectricityMarketMangement {

    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");

        ContainerController container = runtime.createMainContainer(profile);
        try {
            // Start Supplier agents
            AgentController supplier1 = container.createNewAgent("Supplier1", SupplierAgent.class.getName(), null);
            supplier1.start();
            AgentController supplier2 = container.createNewAgent("Supplier2", SupplierAgent.class.getName(), null);
            supplier2.start();

            // Start Consumer agents
            AgentController consumer1 = container.createNewAgent("Consumer1", ConsumerAgent.class.getName(), null);
            consumer1.start();
            AgentController consumer2 = container.createNewAgent("Consumer2", ConsumerAgent.class.getName(), null);
            consumer2.start();

            // Start Observer agent
            AgentController observer = container.createNewAgent("Observer", ObserverAgent.class.getName(), null);
            observer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    

