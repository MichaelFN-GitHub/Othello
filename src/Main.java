import engine.BitboardGameState;
import static engine.BitboardGameState.BLACK;
import static engine.BitboardGameState.WHITE;

import java.awt.*;

public class Main {
    public static void main(String[] args) throws AWTException, InterruptedException {
        BitboardGameState game = new BitboardGameState();
//        new UI(game);
        AutoPlayer autoPlayer = new AutoPlayer(game);
        autoPlayer.play(BLACK);
    }
}
