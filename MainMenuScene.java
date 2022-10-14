import java.io.IOException;

public class MainMenuScene implements IScene {
    private IBotApi botApi;
    private SceneFactory sceneFactory;

    MainMenuScene(IBotApi botApi, SceneFactory sceneFactory) {
        this.botApi = botApi;
        this.sceneFactory = sceneFactory;
    }

    @Override
    public void handleMessage(User user, Message message) throws IOException {
        switch (message.text) {
            case ("/help"): {
                executeHelpCommand(user);
                return;
            }
            case ("/start"): {
                executeStartGameCommand(user);
                return;
            }
            case ("/info"): {
                executeInfoCommand(user);
                return;
            }

            default: {
                botApi.sendAnswer(user.id, "Неправильный формат ввода, используйте /help для получения информации");
            }
        }
    }

    private void executeHelpCommand(User user) {
        String responseMessage = "/start – Новая игра\n" +
                "/info – Статистика\n" +
                // "/hint – Использовать подсказку\n" +
                "/exit – Выход из игры\n" +
                "/help – Показать справку";
        botApi.sendAnswer(user.id, responseMessage);
    }

    private void executeStartGameCommand(User user) throws IOException {
        user.currentQuestionIndex = 1;
        user.hints = 1;
        botApi.sendAnswer(user.id, user.nextQuestion());
        user.scene = sceneFactory.createGameScene();
    }

    private void executeInfoCommand(User user) {
        String responseMessage = "Ваша статистика:\n\n"
                + "Имя – " + user.name + '\n'
                + "Рекорд – " + user.highScore + "\n"
                + "Текущий вопрос – " + user.currentQuestionIndex;
        botApi.sendAnswer(user.id, responseMessage);
    }
}
