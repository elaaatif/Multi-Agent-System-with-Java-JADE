# Electricity Market Management System

A multi-agent system (MAS) simulation of an electricity market, where suppliers, consumers, and an observer agent interact to simulate real-world electricity market dynamics. The project is built using the JADE (Java Agent Development Framework) platform, showcasing agent communication, behavior modeling, and market regulation.

![image](https://github.com/user-attachments/assets/d219edab-1433-4481-8cee-1e2d456552b8)

---

## **Features**
1. **Supplier Agents**:
   - Set and dynamically adjust electricity prices based on profit margins and operational costs.
   - Evaluate profit/loss and adjust prices accordingly.
   - Bill customers for electricity usage.

2. **Consumer Agents**:
   - Subscribe to electricity suppliers and pay bills.
   - Automatically stop subscription if the account balance reaches zero.
   - Simulate realistic consumer behavior in a market.

3. **Observer Agent**:
   - Monitors the market by querying supplier metrics (e.g., price, number of clients, balance).
   - Displays collected data for analysis.

4. **Dynamic Market Behavior**:
   - Pricing strategies and client management modeled realistically.
   - Automated agent communication using JADE's ACL messaging.

---

## **System Requirements**
- **Java**: JDK 8 or later
- **JADE Framework**: Download from [JADE Website](http://jade.tilab.com)
- An IDE like IntelliJ IDEA, Eclipse, or NetBeans (optional for development)

---

## **Setup Instructions**
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/elaaatif/Multi-Agent-System-with-Java-JADE
   cd electricity-market-management

