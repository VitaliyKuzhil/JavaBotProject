package org.example;

import io.github.cdimascio.dotenv.Dotenv;

import net.thauvin.erik.crypto.CryptoPrice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
            if (text.contains(" ")  && text.split(" ").length == 2){
                String[] values = text.split(" ");
                var value1 = CryptoPrice.spotPrice(values[0]);
                var value2 = Integer.parseInt(values[1]);
                var res = value2 / value1.getAmount().doubleValue();

                sendPicture(chatId, "res.png");
                sendMessage(chatId, value1.getBase() + " to " + value2 + " " + value1.getCurrency() +
                        " = " + String.format("%.4f", res) + " " + value1.getBase());
            }
            else if(text.contains(",")) {
                String[] currencies = text.split(",");

                for (String currency : currencies) {
                    sendPicture(chatId, currency + ".png");
                    sendPrice(chatId, currency);

                }

            } else {
                switch (text) {
                    case "/start" -> {
                        sendPicture(chatId, "hello.png");
                        sendMessage(chatId, "Hello!");
                    }
                    case "all" -> {
                        String[] stringArray = new String[5];

                        stringArray[0] = "ETH";
                        stringArray[1] = "BTC";
                        stringArray[2] = "DOGE";
                        stringArray[3] = "BNB";
                        stringArray[4] = "SOL";

                        sendPicture(chatId, "all.png");

                        for (String currency : stringArray) {
                            sendPicture(chatId, currency.toLowerCase() + ".png");
                            sendPrice(chatId, currency);
                        }
                    }
                    case "eth" -> {
                        sendPicture(chatId, "eth.png");
                        sendPrice(chatId, "ETH");
                    }
                    case "btc" -> {
                        sendPicture(chatId, "btc.png");
                        sendPrice(chatId, "BTC");
                    }
                    case "doge" -> {
                        sendPicture(chatId, "doge.png");
                        sendPrice(chatId, "DOGE");
                    }
                    case "bnb" -> {
                        sendPicture(chatId, "bnb.png");
                        sendPrice(chatId, "BNB");
                    }
                    case "sol" -> {
                        sendPicture(chatId, "sol.png");
                        sendPrice(chatId, "SOL");
                    }
                    default -> {
                        sendPicture(chatId, "nothink.png");
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

    void sendPicture(long chatId, String name) throws Exception {
        var photo = getClass().getClassLoader().getResourceAsStream(name);

        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(new InputFile(photo, name));
        execute(message);
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
