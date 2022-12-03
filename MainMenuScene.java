import java.io.IOException;

public class MainMenuScene implements IScene {
    private IBotApi botApi;
    private SceneFactory sceneFactory;
    private QuestionProvider questionProvider;

    MainMenuScene(IBotApi botApi, SceneFactory sceneFactory, QuestionProvider questionProvider) {
        this.botApi = botApi;
        this.sceneFactory = sceneFactory;
        this.questionProvider = questionProvider;
    }

    @Override
    public void handleMessage(User user, BotMessage botMessage) throws IOException {
        if (botMessage.text.startsWith("/HelpAccepted")){
            botApi.deleteMessage(user.id, botMessage.messageId );
            user.receiver=botMessage.text.substring(14);
            System.out.println(user.name+" helping "+user.receiver);
            botApi.transferQuestion(user.receiver,user.name);
            if (user.currentQuestion==null) {
                botApi.sendAnswer(user.id, "Помощь больше не требуется");
                return;
            }
            botApi.sendAnswer( user.receiver, user.name+" поможет вам" );
            Buttons answerButtons = new Buttons();
            String fullQuestionText = user.currentQuestion.getTextQuestion()+"\n\n"+user.currentQuestion.getAllAnswerText();
            answerButtons.createAnswerButtons(fullQuestionText , 4 );
            botApi.sendAnswer(user.id, fullQuestionText, answerButtons);
            user.scene = sceneFactory.createAssistScene();
            return;
        }
        switch (botMessage.text) {
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
        System.out.println("game started");
        user.createHints();
        String questionText = questionProvider.nextQuestionForUser(user);
        Buttons answerButtons = new Buttons();
        answerButtons.createAnswerButtons( questionText, 4 );
        botApi.sendAnswer(user.id, questionText, answerButtons );
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
