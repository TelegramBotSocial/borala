package template.borala;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.pengrad.telegrambot.model.Location;

import dbsql.borala.Crud;

public class Message {

	private String msgInput;//msg de entrada
	private String location; //localiza��o (lat|long)
	private String respDefault; //respostas programadas (start|suporte|n�o-compreendida)
	private ArrayList<String> res; //respostas do banco de dados

	public Situation sit;

	public void initial(String msg, Situation sit){
		this.sit = sit;
		if(msg!=null){
			msg = msg.toLowerCase();
			this.setMsgInput(msg);
		}		 
		getResult();
	}

	public void initial(Location location, Situation sit){
		this.sit = sit;
		if(location != null){
			this.setLocation(location);
		}		 
	}

	public void getResult(){
		if(this != null){
			if(this.getMsgInput()!=null){
				if(this.getMsgInput().equals("baladas") || this.getMsgInput().equals("restaurantes")){ //categorias
					sit.status = 3;
					if(getLocation()==null){
						sit.status = 1;
					}else{
						setRes(searchDB());
						if(getRes()!=null){
							System.out.println(getRes());
						}
					}

				}else if(this.getMsgInput().equals("nova localiza��o")){
					sit.status = 1;

				}else if(this.getMsgInput().equals("nova categoria")){
					sit.status = 2;				

				}else{ //textos iniciais ou n�o entendido
					texts(this.getMsgInput());
				}

			}
		}
	}

	public String getMsgInput() {
		return msgInput;
	}

	public void setMsgInput(String msgInput) {
		this.msgInput = msgInput;
	}

	public void texts(String msg){
		sit.status = 0;
		if(msg.equals("/start")){
			setRespDefault("Seja Bem-vindo ao <strong>Borala</strong> bot :) Para utilizar nosso bot � necess�rio: enviar sua <b>LOCALIZA��O</b>; posteriormente navegar em nossos menus de categorias para encontrar o lugar <b>IDEAL</b> mais pr�ximo. E caso precise entrar em contato conosco, digite <code>'/suporte'</code> !");
		}else if(msg.equals("/suporte")){
			setRespDefault("Caso n�o tenhamos conseguido te <b>ajudar</b> ou tenha alguma <b>sugest�o</b>, entre em contato com nosso <i>Suporte</i> na nossa central de atendimentos: www.boralabot.com.br/suporte ... <strong>At� mais</strong>");
		}else{
			setRespDefault("Infelizmente n�o conseguimos entender. Por favor, antes de qualquer coisa, envie sua <b>localiza��o</b> para que possamos ajudar.");
		}
	}

	public String getLocation(){
		return this.location;
	}

	public void setLocation(Location loc){
		this.location = loc.toString();
		//define o lat | long
		//monta a string do ponto (de acordo com o padrao POSTGIS)
		//define a global 'location', com o resultado adquirido acima
		sit.status = 2;
	}

	public ArrayList<String> searchDB(){
		if(getLocation()!=null){
			ArrayList<String> result = new ArrayList<String>();

			Crud crud = new Crud();
			ResultSet res = crud.read("where id=1");

			String msgFinal = "";
			try{
				while(res.next()){
					msgFinal = "Local encontrado: "+res.getString("name");
					result.add(msgFinal);
				}
			}catch(SQLException ex){
				System.out.println("ERRO: "+ex.getMessage());
			}

			return result;
		}
		return null;
	}

	public ArrayList<String> getRes() {
		return res;
	}

	public void setRes(ArrayList<String> arrayList) {
		this.res = arrayList;
	}

	public String getRespDefault() {
		return respDefault;
	}

	public void setRespDefault(String respDefault) {
		this.respDefault = respDefault;
	}

}
