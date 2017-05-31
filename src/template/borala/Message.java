package template.borala;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.pengrad.telegrambot.model.Location;

import dbsql.borala.Crud;

public class Message {

	private String msgInput;//msg de entrada
	private String location; //localiza��o (lat|long)
	private String category; //categoria escolhida
	private String respDefault; //respostas programadas (start|suporte|n�o-compreendida)
	
	private String urlClient;
	private String urlGoogle;
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
				if(this.getMsgInput().equals("baladas") || this.getMsgInput().equals("restaurantes") ||
						this.getMsgInput().equals("lanchonetes") || this.getMsgInput().equals("pizzarias")){ //categorias
					sit.status = 3;
					if(getLocation()==null){
						sit.status = 1;
					}else{
						setCategory(this.getMsgInput());
						setRes(searchDB());
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
			setRespDefault("Infelizmente n�o conseguimos entender. Navegue em nosso <b>menu</b>!");
		}
	}

	public String getLocation(){
		return this.location;
	}

	public void setLocation(Location loc){
		this.location = loc.toString();
		String lat = loc.latitude().toString();
		String lon = loc.longitude().toString();
		
		this.location = lon+" "+lat;
		sit.status = 2;
	}

	public ArrayList<String> searchDB(){
		if(getLocation()!=null){
			ArrayList<String> result = new ArrayList<String>();

			Crud crud = new Crud();
			ResultSet res = crud.read("WHERE st_dwithin(geom, ST_GeomFromText('POINT("+getLocation()+")',4326), 0.15) AND"
					+ " upper(t_first) = upper('"+getCategory()+"') OR upper(t_second) = upper('"+getCategory()+"') OR upper(t_three) = upper('"+getCategory()+"')");

			String msgFinal = "";
			try{
				while(res.next()){
					/* ORGANIZAR RESPOSTA AO USU�RIO
					* Nome do local: 
					* info:
					* Promo��o da semana: 
					* btn com o GPS:
					* btn da url do local: 
					* */
					msgFinal = "Local: <strong>"+res.getString("name")+"</strong>. ";
					msgFinal += res.getString("info")+"! ";
					msgFinal += "Localizado em: "+res.getString("city");
					if(!res.getString("promo").equals("")){
						msgFinal += " <strong>PROMO��O:</strong> "+res.getString("promo");
					}
					
					setUrlGoogle(res.getString("geometria"));						
					setUrlClient(res.getString("url"));		
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUrlClient() {
		return urlClient;
	}

	public void setUrlClient(String urlClient) {
		this.urlClient = urlClient;
	}
	
	private void setUrlGoogle(String googleMaps) {
		String res = googleMaps.substring(googleMaps.indexOf("(")+1, googleMaps.indexOf(")"));
		String laglon[] = res.split(" ");
		
		this.urlGoogle = "https://www.google.com.br/maps/@"+laglon[1]+","+laglon[0]+",16z";
	}

	public String getUrlGoogle() {
		return this.urlGoogle;
	}


}
