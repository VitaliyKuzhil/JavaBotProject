package org.example;

import net.thauvin.erik.crypto.CryptoPrice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class OwnBot extends TelegramLongPollingBot {
    public OwnBot() {
        super("6810531953:AAHNA51wzNRJ21Uson31oCc3d2puSUAhGY4");
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        var text = update.getMessage().getText();

        try {
            var message = new SendMessage();
            message.setChatId(chatId);

            if (text.contains(",")) {
                String[] currencies = text.split(",");
                StringBuilder response = new StringBuilder();

                for (String currency : currencies) {
                    var price = CryptoPrice.spotPrice(currency.trim().toUpperCase());
                    response.append(currency.toUpperCase()).append(" price: ").append(price.getAmount().doubleValue()).append("\n");
                }

                message.setText(response.toString());
            } else {
                switch (text) {
                    case "/start" -> message.setText("Hello!");
                    case "all" -> {
                        String[] stringArray = new String[5];

                        stringArray[0] = "ETH";
                        stringArray[1] = "BTC";
                        stringArray[2] = "DOGE";
                        stringArray[3] = "BNB";
                        stringArray[4] = "SOL";

                        StringBuilder response = new StringBuilder();

                        for (String currency : stringArray) {
                            var price = CryptoPrice.spotPrice(currency.trim());
                            response.append(currency).append(" price: ").append(price.getAmount().doubleValue()).append("\n");
                        }

                        message.setText(response.toString());
                    }
                    case "eth" -> {
                        var price = CryptoPrice.spotPrice("ETH");
                        message.setText("ETH price: " + price.getAmount().doubleValue());
                    }
                    case "btc" -> {
                        var price = CryptoPrice.spotPrice("BTC");
                        message.setText("BTC price: " + price.getAmount().doubleValue());
                    }
                    case "doge" -> {
                        var price = CryptoPrice.spotPrice("DOGE");
                        message.setText("ETH price: " + price.getAmount().doubleValue());
                    }
                    case "bnb" -> {
                        var price = CryptoPrice.spotPrice("BNB");
                        message.setText("ETH price: " + price.getAmount().doubleValue());
                    }
                    case "sol" -> {
                        var price = CryptoPrice.spotPrice("SOL");
                        message.setText("ETH price: " + price.getAmount().doubleValue());
                    }
                    default -> message.setText("Unknown command!");
                }
            }

            execute(message);
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }


    @Override
    public String getBotUsername() {
        return "First_java24_bot";
    }
}
