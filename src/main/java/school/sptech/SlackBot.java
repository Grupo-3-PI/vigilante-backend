package school.sptech;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class SlackBot {

    private static final String BOT_TOKEN = "";
    private static String CHANNEL_ID = "C09SGAC0N21";
    private static String MESSAGE_TEXT = "teste";

    public static void main(String[] args) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(SlackBot::sendAviso, 0, 10, TimeUnit.SECONDS);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage() {
        try {
            Slack slack = Slack.getInstance();
            slack.methods(BOT_TOKEN).chatPostMessage(req -> req
                    .channel(CHANNEL_ID)
                    .text(MESSAGE_TEXT)
            );
            System.out.println("Mensagem enviada!");

        } catch (IOException | SlackApiException e) {
            System.err.println("Error no envio da mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void sendAviso() {
         MESSAGE_TEXT = "Aumentou em 30%!";
         CHANNEL_ID = "C09VAA4SNAX";
        try {
            Slack slack = Slack.getInstance();
            slack.methods(BOT_TOKEN).chatPostMessage(req -> req
                    .channel(CHANNEL_ID)
                    .text(MESSAGE_TEXT)
            );
            System.out.println("Mensagem enviada!");

        } catch (IOException | SlackApiException e) {
            System.err.println("Error no envio da mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

}