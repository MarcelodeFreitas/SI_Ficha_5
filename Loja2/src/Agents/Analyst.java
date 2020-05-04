package Agents;

import jade.core.Agent;

import java.io.IOException;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import message_class.message_analyst;

public class Analyst extends Agent {
	
	protected void setup() {
		super.setup();
		this.addBehaviour(new Receiver());

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
	
	private class Request extends SimpleBehaviour {
		
		public void action() {
			
			AID receiver = new AID();
			receiver.setLocalName("Seller");
			ACLMessage mensagem = new ACLMessage(ACLMessage.REQUEST);
			mensagem.addReceiver(receiver);
			
			myAgent.send(mensagem);
				
			System.out.println("Analyst sent request to seller");

		}
		
		public boolean done() {
			return true;
		}
	}
	
	
	
	private class Receiver extends CyclicBehaviour {
		private float lucro_seller;
		private float media_seller;
		private String produto_seller;
		private int freq_produto_seller;
		private AID seller;
		private AID customer;
		
		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				if (msg.getPerformative() == ACLMessage.INFORM && msg.getSender().getLocalName() == "Seller[123]") { // como faze rpara apenas dar match com seller 1 2 ou 3?
					System.out.println("Analyst received data from seller");
					
					try {
						message_analyst content = (message_analyst) msg.getContentObject();
						lucro_seller = content.get_lucro_seller();
						media_seller = content.get_media_seller();
						produto_seller = content.get_produto_seller();
						freq_produto_seller = content.get_freq_produto_seller();
						
						System.out.println("lucroooooooooooooooooooooooo" + lucro_seller);
						
					} catch (UnreadableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
		
			}
	
		}
	}
	
}
