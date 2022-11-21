public class SceneFactory {
    IBotApi botApi;
    QuestionProvider questionProvider;

    public SceneFactory(IBotApi botApi, QuestionProvider questionProvider) {
        this.botApi = botApi;
        this.questionProvider = questionProvider;
    }

    public IScene createMainMenuScene() {
        return new MainMenuScene(botApi, this, questionProvider);
    };

    public IScene createGameScene() {
        return new GameScene(botApi, this, questionProvider);
    };

    public IScene createHintScene() {
        return new HintScene(botApi, this, questionProvider);
    };
}
