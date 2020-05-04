package message_class;

import jade.core.AID;

public class message_analyst implements java.io.Serializable {
	
	public AID agent;
	public float lucro_seller;
	public float media_seller;
	public String produto_seller;
	public int freq_produto_seller;
	
	public message_analyst(AID agent, float lucro_seller, float media_seller, String produto_seller, int freq_produto_seller) {
		super();
		this.agent = agent;
		this.lucro_seller = lucro_seller;
		this.media_seller = media_seller;
		this.produto_seller = produto_seller;
		this.freq_produto_seller = freq_produto_seller;
	}

	public AID getAgent() {
		return agent;
	}

	public void setAgent(AID agent) {
		this.agent = agent;
	}

	public float get_lucro_seller() {
		return lucro_seller;
	}

	public void set_lucro_seller(float lucro_seller) {
		this.lucro_seller = lucro_seller;
	}
	
	public float get_media_seller() {
		return media_seller;
	}

	public void set_media_seller(float media_seller) {
		this.media_seller = media_seller;
	}
	
	public String get_produto_seller() {
		return produto_seller;
	}

	public void set_produto_seller(String produto_seller) {
		this.produto_seller = produto_seller;
	}
	
	public int get_freq_produto_seller() {
		return freq_produto_seller;
	}

	public void set_freq_produto_seller(int freq_produto_seller) {
		this.freq_produto_seller = freq_produto_seller;
	}

}
