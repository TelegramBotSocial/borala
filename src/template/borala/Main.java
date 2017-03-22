package template.borala;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import dbsql.borala.Connections;

public class Main {

	public static void main(String[] args) {

		TelegramBot bot = TelegramBotAdapter.build("352316358:AAEPgQbbp0cMjlrTlNhbf6fYj1JhgqKRXu0");
		Connections conexao = new Connections();

		//objeto responsável por receber as mensagens
		GetUpdatesResponse updatesResponse;	
		//objeto responsável por gerenciar o envio de respostas
		SendResponse sendResponse;
		//objeto responsável por gerenciar o envio de ações do chat
		BaseResponse baseResponse;

		int m=0;

		while (true){

			//executa comando no Telegram para obter as mensagens pendentes a partir de um off-set (limite inicial)
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(m));

			//lista de mensagens
			List<Update> updates = updatesResponse.updates();

			for (Update update : updates) {

				m = update.updateId()+1;

				//objeto criado no projeto
				Message msg = new Message(update.message().text());
				String resposta = msg.getResult();
				if(resposta!=null){
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), resposta));
					//envio de "Escrevendo" antes de enviar a resposta
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
					while(!baseResponse.isOk() || !sendResponse.isOk()){}
				
				}else{
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Ops... Não conseguimos entender"));
				}

			}

		}

	}


}
