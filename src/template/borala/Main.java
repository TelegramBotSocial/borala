package template.borala;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class Main {

	public static void main(String[] args) {

		TelegramBot bot = TelegramBotAdapter.build("352316358:AAEPgQbbp0cMjlrTlNhbf6fYj1JhgqKRXu0");

		//objeto respons�vel por receber as mensagens
		GetUpdatesResponse updatesResponse;	
		//objeto respons�vel por gerenciar o envio de respostas
		SendResponse sendResponse;
		//objeto respons�vel por gerenciar o envio de a��es do chat
		BaseResponse baseResponse;		

		int m=0;
		
		Message msg = new Message();
		//cria��o do objeto, respons�vel pelo controle do sistema (sequ�ncia)
		Situation sit = new Situation();

		while (true){

			//executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));

			//lista de mensagens
			List<Update> updates = updatesResponse.updates();

			for (Update update : updates) {

				m = update.updateId()+1;

				//objeto criado no projeto
				String res = update.message().text();
				if(res!=null){
					msg.initial(res, sit);
				}else{
					msg.initial(update.message().location(), sit);
				}				
				String resposta = msg.getRespDefault();

				if(resposta!=null){
					if(sit.status==0){
						baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resposta).parseMode(ParseMode.HTML));

						while(!baseResponse.isOk() || !sendResponse.isOk()){
							System.out.println("Desculpe, estamos com problema em nosso servidor.");
						}
						if(msg.getLocation()!=null){
							sit.status=2;
						}else{
							sit.status=1;
						}
					}	
				}

				if(sit.status==1){
					//enviando localiza��o
					Keyboard keyboard = new ReplyKeyboardMarkup(
							new KeyboardButton[]{
									new KeyboardButton("Minha Localiza��o").requestLocation(true),
									new KeyboardButton("Outra Localiza��o").requestLocation(true)
							}
							);  
					bot.execute(new SendMessage(update.message().chat().id(),"Envie a localiza��o desejada:").replyMarkup(keyboard));
				}

				if(sit.status==2){
					//selecionando categoria
					Keyboard keyboard = new ReplyKeyboardMarkup(
							new KeyboardButton[]{
									new KeyboardButton("baladas"),
									new KeyboardButton("restaurantes")
							}
							);  
					bot.execute(new SendMessage(update.message().chat().id(),"Escolha uma categoria:").replyMarkup(keyboard));
				}

				if(sit.status==3){
					//definindo nova a��o 
					if(msg.getRes()!=null){
						if(msg.getRes().size()==0){
							bot.execute(new SendMessage(update.message().chat().id(), "N�o foi encontrado nenhum local nas especifica��es selecionadas, tente outras").parseMode(ParseMode.HTML));
						}else{
							for(int i=0; i<msg.getRes().size(); i++){
								bot.execute(new SendMessage(update.message().chat().id(), msg.getRes().get(i)).parseMode(ParseMode.HTML));
							}	
						}

						Keyboard keyboard = new ReplyKeyboardMarkup(
								new KeyboardButton[]{
										new KeyboardButton("nova localiza��o"),
										new KeyboardButton("nova categoria")
								}
								);  
						bot.execute(new SendMessage(update.message().chat().id(),"Deseja enviar outra localiza��o ou categoria?").replyMarkup(keyboard));
					}else{
						bot.execute(new SendMessage(update.message().chat().id(), "ERRO ao se conectar ao banco de Dados").parseMode(ParseMode.HTML));
					}
				}

			}

		}

	}


}
