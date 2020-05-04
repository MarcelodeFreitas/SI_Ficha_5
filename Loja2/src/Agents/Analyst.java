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
		this.addBehaviour(new Requests());
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
	
	private class Request extends OneShotBehaviour {
		
		private AID seller;
		
		public Request(AID sellerAID) {
			this.seller = sellerAID;
		}
		
		
		public void action() {
			
			System.out.println("Analyst start request");
			
			ACLMessage mensagem = new ACLMessage(ACLMessage.REQUEST);
			mensagem.addReceiver(seller);
			myAgent.send(mensagem);
				
			System.out.println("Analyst sent request to seller");

		}
	}
	
	private class Requests extends OneShotBehaviour{
		public void action() {
			// CONTACTING ALL SELLERS

			int numSellers;
			
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			String Seller_number = getLocalName().substring(getLocalName().length() - 1);
			String Seller = "Seller" + Seller_number;
			sd.setType(Seller);
			template.addServices(sd);

			DFAgentDescription[] result;
			
			try {
				result = DFService.search(myAgent, template);
				AID[] sellers;
				sellers = new AID[result.length];
				numSellers = result.length;

				ParallelBehaviour pb = new ParallelBehaviour(myAgent, ParallelBehaviour.WHEN_ALL) {

					public int onEnd() {
						System.out.println("All sellers inquired.");
						return super.onEnd();
					}
				};
				myAgent.addBehaviour(pb);

				for (int i = 0; i < result.length; ++i) {
					sellers[i] = result[i].getName();
					System.out.println(sellers[i].getName());
					pb.addSubBehaviour(new Request(sellers[i]));
				}

			} catch (FIPAException e) {
				e.printStackTrace();

			}
		}
	}
	
	
	
	private class Receiver extends CyclicBehaviour {
		private float lucro_seller1, lucro_seller2, lucro_seller3;
		private float media_seller1, media_seller2, media_seller3;
		private String produto_seller1, produto_seller2, produto_seller3;
		private int freq_produto_seller1, freq_produto_seller2, freq_produto_seller3;
		private AID seller;
		private AID customer;
		
		public void action() {
			
			ACLMessage msg = receive();
			if (msg != null) {
				if (msg.getPerformative() == ACLMessage.INFORM) { // como faze rpara apenas dar match com seller 1 2 ou 3?
					System.out.println("Analyst received data from seller1");
					
					try {
						message_analyst content = (message_analyst) msg.getContentObject();
						lucro_seller1 = content.get_lucro_seller();
						media_seller1 = content.get_media_seller();
						produto_seller1 = content.get_produto_seller();
						freq_produto_seller1 = content.get_freq_produto_seller();
						
						System.out.println("lucroooooooooooooooooooooooo" + lucro_seller1);
						
					} catch (UnreadableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} 
		}
	}
}
