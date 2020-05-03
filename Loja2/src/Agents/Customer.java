package Agents;

import java.util.ArrayList;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Customer extends Agent {

	private ArrayList<String> products;
	private String produtoRequisitado;

	private int flag = 0;

	protected void setup() {
		super.setup();

		products = new ArrayList<String>();
		products.add("A");
		products.add("B");
		products.add("C");
		products.add("D");
		products.add("E");
		products.add("F");
		produtoRequisitado = "";

		addBehaviour(new Comprarproduto()); // Behaviour responsible to buy the product
		addBehaviour(new ReceberConfirmacao()); // Know if buys are successful

	}

	private class Comprarproduto extends OneShotBehaviour {
		public void action() {
			Random randomizer = new Random();
			String random = products.get(randomizer.nextInt(products.size())); // Escolhe um produto random da lista

			try {
				// Build the description used as template for the search
				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				
				// e.g., Customer1 will communicate with Seller1, Customer2 will communicate with Seller2 and Customer3 will communicate with Seller3
				// Obtain last character to know which Seller to enter in contact, using the DF Service
				String Seller_number = getLocalName().substring(getLocalName().length() - 1);
				String Seller = "Seller" + Seller_number;
				
				sd.setType(Seller);
				dfd.addServices(sd);

				// Search DF
				DFAgentDescription[] results = DFService.search(this.myAgent, dfd);

				if (results.length > 0) {
					System.out.println(getLocalName() + ": " + Seller + " Agent found!");
					for (int i = 0; i < results.length; ++i) {
						// Agent Found
						DFAgentDescription dfd1 = results[i];
						AID provider = dfd1.getName();

						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.addReceiver(provider);
						msg.setContent(random);
						send(msg);

					}
				} else {
					System.out.println(getLocalName() + ": " + Seller + " Agent not found!");
				}
			} catch (FIPAException fe) {
				fe.printStackTrace();
			}

		}
	}

	private class ReceberConfirmacao extends CyclicBehaviour {
		public void action() {
			ACLMessage confirmacao = receive();
			if (confirmacao != null && confirmacao.getPerformative() == ACLMessage.CONFIRM) {
				produtoRequisitado = confirmacao.getContent();
			}
			block();
			doDelete();
		}
	}

	protected void takeDown() {
		super.takeDown();
	}

}
