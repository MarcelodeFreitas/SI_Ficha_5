package Agents;

import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;

import java.io.IOException;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import message_class.message_analyst;

public class Analyst extends Agent {
	
	private ArrayList<Float> lucro;
	private ArrayList<Float> media_lucro;
	private ArrayList<String> produto_mais_vendido;
	private ArrayList<Integer> freq_mais_vendido;

	private int container;
	private Location nextSite;
	
	protected void setup() {
		super.setup();

		container = 1;
		
		lucro = new ArrayList<Float>(3);
		media_lucro = new ArrayList<Float>(3);
		produto_mais_vendido = new ArrayList<String>(3);
		freq_mais_vendido = new ArrayList<Integer>(3);
		
		this.addBehaviour(new Request());
		this.addBehaviour(new Receiver());

	}

	protected void takeDown() {
		super.takeDown();
	}
	
	
	private class Request extends Behaviour {

		public void action() {
			
		
			ContainerID destination = new ContainerID();
			destination.setName("Container" + container);
			System.out.println("Analyst: Movido para container " + container);
			doMove(destination);

			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST_WHENEVER);
			AID reader = new AID("Seller" + container, AID.ISLOCALNAME);
			msg.addReceiver(reader);
			msg.setContent("Pedido de Analyst");
			send(msg);

			System.out.println("Analyst: Mensagem enviada ao container" + container);

			container++;

		}

		public boolean done() {
			if (container == 4)
				return true;
			return false;
		}

	}
	
	private class Receiver extends CyclicBehaviour {
		
		public void action() {
			
			ACLMessage msg = receive();
			if (msg != null) {
				if (msg.getPerformative() == ACLMessage.INFORM) { // como faze rpara apenas dar match com seller 1 2 ou 3?
					
					System.out.println("Analyst: Message received from " + msg.getSender().getLocalName() );
					
					try {
						message_analyst content = (message_analyst) msg.getContentObject();
						
						String seller = msg.getSender().getLocalName();
						seller = seller.substring(getLocalName().length() - 1);
						
						lucro.add(content.get_lucro_seller());
						media_lucro.add(content.get_media_seller());
						produto_mais_vendido.add(content.get_produto_seller());
						freq_mais_vendido.add(content.get_freq_produto_seller());
						
						
					} catch (UnreadableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					// quando o ultimo array list estiver preenchido o tamanho desse array é 3
					
					if (freq_mais_vendido.size() == 3) {
					
						float lucro_final = 0;
						float media_final = 0;
						String mais_vendido_final = null;
						int freq_mais_vendido_final = 0;
						
						
						for (int i = 0; i < lucro.size(); i++) {
							lucro_final += lucro.get(i);
							media_final += media_lucro.get(i);
							if (freq_mais_vendido_final < freq_mais_vendido.get(i)) {
								mais_vendido_final = produto_mais_vendido.get(i);
								freq_mais_vendido_final = freq_mais_vendido.get(i);
							}
							System.out.println("________________________Container"+(i + 1)+"________________________________");
							System.out.println("Lucro: " + lucro.get(i));
							System.out.println("Média do Lucro: " + media_lucro.get(i));
							System.out.println("Produto mais vendido: " + produto_mais_vendido.get(i) + " -> " + freq_mais_vendido.get(i));
							
						}
						
						System.out.println("_____________________Considerações Finais________________________");
						System.out.println("Lucro Total: " + lucro_final);
						System.out.println("Média do Lucro: " + (media_final/3));
						System.out.println("Produto mais vendido: " + mais_vendido_final + " -> " + freq_mais_vendido_final);
						System.out.println("_________________________________________________________________");
						
						doDelete();
					
					}
					
				} else {
					block();
					};
			} 
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
