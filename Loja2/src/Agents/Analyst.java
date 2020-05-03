package Agents;

import jade.core.Agent;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Analyst extends Agent {
	
	protected void setup() {
		super.setup();

		// Initiate instances and behaviours
		// Analyst Request information for each seller, in order to determine:
		/* 
		 * 	(1)	Total Profit obtained by each Seller Agent;
			(2)	Average profit (total_profit / number_clients) of each Seller Agent;
			(3)	Product most sold by each Seller;
			
			In addition, after obtaining information from all sellers, the analyst Agent should also do a global analysis:
			(1)	Total Profit obtained by all Seller Agents;
			(2)	Average profit (total_profit / number_clients) of all Seller Agents;
			(3)	Product most sold by all Seller Agents;
		 */

	}

	protected void takeDown() {
		super.takeDown();
	}
	
}
