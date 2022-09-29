import java.util.Scanner;

public class ConsoleBotApi implements IBotApi {

    @Override
    public void registerOnUpdate(IOnUpdate updateHandler) {
        while (true) {
            Scanner in = new Scanner(System.in);
            updateHandler.onUpdate(new Update(0, new Message(in.nextLine())));
        }
    }

    @Override
    public void sendAnswer(long chatId, String text) {
        System.out.println(text);
    }
}
