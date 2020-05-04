package Agents;

import java.io.IOException;
import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import message_class.message_analyst;

public class Seller extends Agent {

	private ArrayList<String> products;
	private int nr_clientes;
	private int aVendido; // número de produtos A vendidos
	private int bVendido; // número de produtos B vendidos
	private int cVendido; // número de produtos C vendidos
	private int dVendido; // número de produtos D vendidos
	private static int aValor = 5; // valor de venda do produto A
	private static int bValor = 2; // valor de venda do produto B
	private static int cValor = 7; // valor de venda do produto C
	private static int dValor = 3; // valor de venda do produto D

	protected void setup() {
		super.setup();

		// Register Agent
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getLocalName());
		sd.setType(getLocalName());
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		products = new ArrayList<String>();
		products.add("A");
		products.add("B");
		products.add("C");
		products.add("D");
		aVendido = 0;
		bVendido = 0;
		cVendido = 0;
		dVendido = 0;

		addBehaviour(new ReceberPedidosEProdutos());
		addBehaviour(new VerLucro(this, 10000));

	}

	private class ReceberPedidosEProdutos extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = receive();
			// RESPONDER AO ANALYST
			if (msg != null && msg.getPerformative() == ACLMessage.REQUEST && msg.getSender().getLocalName() == "Analyst") {
				try {
					ACLMessage resp = msg.createReply();
					resp.setPerformative(ACLMessage.INFORM);
					float lucro_seller = aValor * aVendido + bValor * bVendido + cValor * cVendido + dValor * dVendido;
					float media_seller = lucro_seller/nr_clientes;
					int freq_produto_seller = Math.max(Math.max(aVendido, bVendido), Math.max(cVendido, dVendido));
					String produto_seller = null;
					if (freq_produto_seller == aVendido) {
						produto_seller = "A";
					}
					if (freq_produto_seller == bVendido) {
						produto_seller = "B";
					}
					if (freq_produto_seller == cVendido) {
						produto_seller = "C";
					}
					if (freq_produto_seller == dVendido) {
						produto_seller = "D";
					}
					message_analyst created_instance = new message_analyst(getAID(), lucro_seller, media_seller, produto_seller, freq_produto_seller);
					resp.setContentObject(created_instance);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// RESPONDER AOS CLIENTES
			if (msg != null && msg.getPerformative() == ACLMessage.REQUEST && msg.getSender().getLocalName()!= "Analyst") { // receber pedidos de requisição
				nr_clientes = nr_clientes + 1;
				String clienteP = msg.getSender().getLocalName();
				ACLMessage resp = msg.createReply();
				String produtoPedido = msg.getContent();
				if (products.contains(produtoPedido)) {
					if (produtoPedido.equals("A")) {
						aVendido++;
						System.out.println(getLocalName() + ": produto A requisitado por " + clienteP);
						resp.setContent("A");
						resp.setPerformative(ACLMessage.CONFIRM);
					} else if (produtoPedido.equals("B")) {
						bVendido++;
						System.out.println(getLocalName() + ": produto B requisitado por " + clienteP);
						resp.setContent("B");
						resp.setPerformative(ACLMessage.CONFIRM);
					} else if (produtoPedido.equals("C")) {
						cVendido++;
						System.out.println(getLocalName() + ": produto C requisitado por " + clienteP);
						resp.setContent("C");
						resp.setPerformative(ACLMessage.CONFIRM);
					} else if (produtoPedido.equals("D")) {
						dVendido++;
						System.out.println(getLocalName() + ": produto D requisitado por " + clienteP);
						resp.setContent("D");
						resp.setPerformative(ACLMessage.CONFIRM);
					} else {
						System.out.println(getLocalName() + ": produto " + produtoPedido + " pedido por " + clienteP + " não disponível");
						resp.setContent(produtoPedido);
						resp.setPerformative(ACLMessage.REFUSE);
					}
				} else {
					System.out.println(getLocalName() + ": produto " + produtoPedido + " pedido por " + clienteP + " não existe");
					resp.setContent(produtoPedido);
					resp.setPerformative(ACLMessage.REFUSE);
				}
				send(resp);
			}

			block();
		}
	}

	private class VerLucro extends TickerBehaviour {
		public VerLucro(Agent a, long period) {
			super(a, period);
			// TODO Auto-generated constructor stub
		}

		protected void onTick() {
			int total = aValor * aVendido + bValor * bVendido + cValor * cVendido + dValor * dVendido;
			System.out.println(getLocalName() + ": Valor Angariado: " + total);
		}
	}

	protected void takeDown() {
		super.takeDown();
		// De-register Agent from DF before killing it
		try {
			DFService.deregister(this);
		} catch (Exception e) {
		}
	}

}
