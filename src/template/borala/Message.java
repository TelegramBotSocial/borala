package template.borala;

public class Message {
	
	private String msgInput;
	
	Message(String msg){
		if(msg!=null){
			msg = msg.toLowerCase();
			this.setMsgInput(msg);
		} else{
			
		}
		 
	}
	
	public String getResult(){
		if(this.getMsgInput().equals("/start")){
			return "Seja bem-vindo ao Borala :)";
		}else{
			return null;
		}
	}

	public String getMsgInput() {
		return msgInput;
	}

	public void setMsgInput(String msgInput) {
		this.msgInput = msgInput;
	}
	
}
