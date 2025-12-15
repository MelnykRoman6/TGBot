import api.DataExporter;
import api.WitcherApiClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();

        WitcherApiClient apiClient = new WitcherApiClient();
        DataExporter exporter = new DataExporter(apiClient);

        try {
            String path = exporter.exportAllWeapons("all_weapons.json");

        } catch (IOException e) {
            e.printStackTrace();
        }

        String botToken = ""; //your token here
        botsApplication.registerBot(botToken, new Bot(botToken));

        System.out.println("Bot successfully started!");
    }
}