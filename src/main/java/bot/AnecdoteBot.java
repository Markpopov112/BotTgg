package bot;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import entity.Joke;
import service.JokeService;

@Component
public class AnecdoteBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(AnecdoteBot.class);
    @Value("${telegram.bot.name}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    private final JokeService jokeService;

    @Autowired
    public AnecdoteBot(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @PostConstruct
    public void startBot() throws TelegramApiException {
        log.info("Starting Telegram bot polling");
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            botsApi.registerBot(this);
        } catch (TelegramApiException var3) {
            log.error("Error occurred: {}", var3.getMessage());
        }

    }

    public void onUpdateReceived(Update update) {
        log.info("Received update: {}", update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            log.info("Received message from chatId {}: {}", chatId, messageText);

            try {
                switch (messageText) {
                    case "/start":
                        log.info("Handling /start command from chatId {}", chatId);
                        this.sendResponse(chatId, "Привет! Я бот для анекдотов. Напиши /joke чтобы получить анекдот.");
                        break;
                    case "/joke":
                        log.info("Handling /joke command from chatId {}", chatId);
                       Joke randomJoke = this.jokeService.findRandomJoke(chatId);
                        String jokeText = randomJoke.getText() != null ? randomJoke.getText() : "Анекдотов пока нет.";
                        this.sendResponse(chatId, jokeText);
                        break;
                    default:
                        log.info("Received unknown command from chatId {}: {}", chatId, messageText);
                        this.sendResponse(chatId, "Извините, я не понимаю эту команду.");
                }
            } catch (TelegramApiException var8) {
                log.error("Exception while sending message to chatId: {}", chatId, var8);
            }
        } else {
            log.info("Update does not contain a message with text");
        }

    }

    private void sendResponse(Long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        this.execute(message);
        log.info("Sent response to chatId: {}", chatId);
    }

    public String getBotUsername() {
        return this.username;
    }

    public String getBotToken() {
        return this.token;
    }
}
