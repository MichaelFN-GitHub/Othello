import Engine.BitboardGameState;
import Engine.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import static Engine.BitboardGameState.BLACK;
import static Engine.BitboardGameState.WHITE;

public class UI extends JPanel implements MouseListener, KeyListener {
    public static final int TILES = 8;
    public static final int TILE_SIZE = 70;
    public static final int PIECE_SIZE = TILE_SIZE - TILE_SIZE / 10;
    public static final int PIECE_FILL = (TILE_SIZE - PIECE_SIZE) / 2;

    private final BitboardGameState game;
    private final InformationPanel informationPanel;

    private int lastMove = -1;

    public UI(BitboardGameState game) {
        this.game = game;

        //Game panel (UI)
        setPreferredSize(new Dimension(TILE_SIZE * TILES, TILE_SIZE * TILES));

        //Information panel
        informationPanel = new InformationPanel(game);

        //Main panel
        JPanel mainComponent = new JPanel();
        mainComponent.setLayout(new BoxLayout(mainComponent, BoxLayout.PAGE_AXIS));
        mainComponent.add(informationPanel);
        mainComponent.add(this);

        //Frame
        JFrame frame = new JFrame();
        frame.setTitle("Othello");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(mainComponent);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Listeners
        addMouseListener(this);
        frame.addKeyListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //Draw tiles
        for (int i = 0; i < TILES; i++) {
            for (int j = 0; j < TILES; j++) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        //Highlight last move
        if (lastMove != -1) {
            int y = lastMove / 8;
            int x = lastMove % 8;
            g.setColor(Color.GRAY);
            g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        //Draw pieces
        for (int i = 0; i < TILES; i++) {
            for (int j = 0; j < TILES; j++) {
                int index = j * 8 + i;
                int piece = game.getPiece(index);
                if (piece == BLACK) {
                    g.setColor(Color.BLACK);
                } else if (piece == WHITE) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }
                g.fillOval(i * TILE_SIZE + PIECE_FILL,  j * TILE_SIZE + PIECE_FILL, PIECE_SIZE, PIECE_SIZE);
            }
        }

        //Draw legal moves
        List<Integer> legalMoves = game.getLegalMoves();
        if (legalMoves.size() > 0) {
            for (int move : legalMoves) {
                int y = move / 8;
                int x = move % 8;
                g.setColor(Color.GRAY);
                g.fillOval(x * TILE_SIZE + PIECE_FILL, y * TILE_SIZE + PIECE_FILL, PIECE_SIZE, PIECE_SIZE);
                g.setColor(Color.LIGHT_GRAY);
                g.fillOval(x * TILE_SIZE + PIECE_FILL * 2, y * TILE_SIZE + PIECE_FILL * 2, PIECE_SIZE - PIECE_FILL * 2, PIECE_SIZE - PIECE_FILL * 2);
            }
        }

        informationPanel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point mousePos = getMousePosition();
        int y = mousePos.x / TILE_SIZE;
        int x = mousePos.y / TILE_SIZE;
        int move = x * 8 + y;
        if (game.isLegalMove(move)) {
            game.makeMove(move);
            lastMove = move;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'u') {
            game.undoMove();
            lastMove = -1;
        } else if (e.getKeyChar() == 'a') {
            int move = AI.findNextMove(game);
            if (move != -1) {
                game.makeMove(move);
            }
            lastMove = move;
        } else if (e.getKeyChar() == 's') {
            System.out.println("Current evaluation of board: " + AI.getEvaluation(game));
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private class InformationPanel extends JPanel {
        private BitboardGameState bitboardGame;

        public InformationPanel(BitboardGameState game) {
            this.bitboardGame = game;
            setPreferredSize(new Dimension(TILE_SIZE * TILES, TILE_SIZE * 2));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            //Background
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);

            //Draw number of black and white pieces
            int textSize = getWidth() / 22;
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,  textSize));
            int numberOfBlackPieces = bitboardGame.getNumberOfBlackPieces();
            int numberOfWhitePieces = bitboardGame.getNumberOfWhitePieces();

            g.setColor(Color.BLACK);
            g.fillOval(getWidth() / 8, getHeight() / 4, TILE_SIZE, TILE_SIZE);
            g.setColor(Color.WHITE);
            String text = "" + numberOfBlackPieces;
            int textWidth = g.getFontMetrics(g.getFont()).stringWidth(text);
            g.drawString(text, getWidth() / 8 + TILE_SIZE / 2 - textWidth / 2, getHeight() / 4 + TILE_SIZE / 2 + textSize / 4);

            g.setColor(Color.WHITE);
            g.fillOval(getWidth() * 6 / 8, getHeight() / 4, TILE_SIZE, TILE_SIZE);
            g.setColor(Color.BLACK);
            text = "" + numberOfWhitePieces;
            textWidth = g.getFontMetrics(g.getFont()).stringWidth(text);
            g.drawString(text, getWidth() * 6 / 8 + TILE_SIZE / 2 - textWidth / 2, getHeight() / 4 + TILE_SIZE / 2 + textSize / 4);

            //Draw player to move
            textSize = getWidth() / 20;
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,  textSize));
            if (game.isGameOver()) {
                if (numberOfBlackPieces == numberOfWhitePieces) {
                    text = "TIE";
                } else {
                    String winner = numberOfBlackPieces > numberOfWhitePieces ? "Black" : "White";
                    text = winner + " WINS!";
                }
            } else {
                text = (game.getPlayerToMove() == BLACK ? "Black" : "White") + " to move";
            }
            textWidth = g.getFontMetrics(g.getFont()).stringWidth(text);
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() * 5 / 9);

        }
    }
}
