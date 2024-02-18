package org.example;

import io.github.cdimascio.dotenv.Dotenv;

import net.thauvin.erik.crypto.CryptoPrice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public class OwnBot extends TelegramLongPollingBot {

    private static final Dotenv dotenv = Dotenv.configure().load();
    private static final String NAME_BOT = dotenv.get("NAME_BOT");
    private static final String TOKEN = dotenv.get("TOKEN");

    public OwnBot() {
        super(TOKEN);
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        var text = update.getMessage().getText();

        try {
            if (text.contains(",")) {
                String[] currencies = text.split(",");

                for (String currency : currencies) {
                    sendPrice(chatId, currency);
                }

            } else {
                switch (text) {
                    case "/start" -> {
                        sendMessage(chatId, "Hello!");
                    }
                    case "all" -> {
                        String[] stringArray = new String[5];

                        stringArray[0] = "ETH";
                        stringArray[1] = "BTC";
                        stringArray[2] = "DOGE";
                        stringArray[3] = "BNB";
                        stringArray[4] = "SOL";

                        for (String currency : stringArray) {
                            sendPrice(chatId, currency);
                        }
                    }
                    case "eth" -> {
                        sendPrice(chatId, "ETH");
                    }
                    case "btc" -> {
                        sendPrice(chatId, "BTC");
                    }
                    case "doge" -> {
                        sendPrice(chatId, "DOGE");
                    }
                    case "bnb" -> {
                        sendPrice(chatId, "BNB");
                    }
                    case "sol" -> {
                        sendPrice(chatId, "SOL");
                    }
                    default -> {
                        sendMessage(chatId, "Unknown command!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    void sendPrice(long chatId, String name) throws Exception {
        var price = CryptoPrice.spotPrice(name);
        sendMessage(chatId, name + " price: " + price.getAmount().doubleValue());
    }

    void sendMessage(long chatId, String text) throws Exception {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
    }


    @Override
    public String getBotUsername() {
        return NAME_BOT;
    }
}
