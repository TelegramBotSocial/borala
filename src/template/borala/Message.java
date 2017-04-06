package template.borala;

public class Message {
	
	private String msgInput;
	
	public int actLoc=0;
	
	Message(String msg){
		if(msg!=null){
			msg = msg.toLowerCase();
			this.setMsgInput(msg);
		}		 
	}
	
	public String getResult(){
		if(this != null){
			if(this.getMsgInput()!=null){
				return texts(this.getMsgInput());
			}else{
				return location(this.getMsgInput()); // location
			}
		}else{
			return null; //msg não recebida
		}
	}

	public String getMsgInput() {
		return msgInput;
	}

	public void setMsgInput(String msgInput) {
		this.msgInput = msgInput;
	}
	
	public String texts(String msg){
		actLoc=1;
		if(msg.equals("/start")){
			return "Seja Bem-vindo ao <strong>Borala</strong> bot :) Para utilizar nosso bot é necessário: enviar sua <b>LOCALIZAÇÃO</b>; posteriormente navegar em nossos menu de categorias para encontrar o lugar <b>IDEAL</b> mais proximo. <code>E caso precise entrar em contato conosco, digite '/suporte'</code> !";
		}else if(msg.equals("/suporte")){
			return "Caso não tenhamos conseguido te <b>ajudar</b> ou tenha alguma <b>sugestão</b>, entre em contato com nosso <i>suporte</i> na nossa central de atendimentos: www.boralabot.com.br/suporte ... <strong>Até MAIS</strong>";
		}else{
			return "Infelizmente <b>NÃO</b> conseguimos entender. Por favor, antes de <b>qualquer</b> coisa, envie sua <pre>localização</pre> para que possamos te ajudar.";
		}
	}
	
	public String location(String loc){
		//tratar localização
		return "Estamos processando sua localização...";
	}
	
}
