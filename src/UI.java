import engine.BitboardGameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import static engine.BitboardGameState.BLACK;
import static engine.BitboardGameState.WHITE;

public class UI extends JPanel implements MouseListener, KeyListener {

    //This class contains the UI.

    public static final int TILES = 8;
    public static final int TILE_SIZE = 70;
    public static final int PIECE_SIZE = TILE_SIZE - TILE_SIZE / 10;
    public static final int PIECE_FILL = (TILE_SIZE - PIECE_SIZE) / 2;

    private final BitboardGameState game;
    private final InformationPanel informationPanel;

    private int lastMove = -1;

    public static final boolean AUTOPLAY_ENABLED = true;
    private final PlayOnline autoPlayer = new PlayOnline();


    public UI(BitboardGameState game) throws AWTException {
        this.game = game;

        //Game panel (UI)
        setPreferredSize(new Dimension(TILE_SIZE * TILES, TILE_SIZE * TILES));

        //Information panel
        informationPanel = new InformationPanel(this, game);

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

    //Draws the board.
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

    //Tell game state to undo the last move.
    private void undoMove() {
        game.undoMove();
        lastMove = -1;
    }

    //Tell AI to make a move.
    private void makeAIMove() throws InterruptedException {
        int move = AI.findNextMove(game);
        if (move != -1) {
            game.makeMove(move);
            if(AUTOPLAY_ENABLED) autoPlayer.moveAndClickOnline(move, true);
        }
        lastMove = move;
    }

    //Mouse clicked on the board.
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("mouse clicked x: " + e.getX() + " y: " + e.getY());
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

    //Shortcuts for undoing, running AI move and checking the current evaluation.
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'u') {
            game.undoMove();
            lastMove = -1;
        } else if (e.getKeyChar() == 'a') {
            int move = AI.findNextMove(game);
            if (move != -1) {
                game.makeMove(move);
                if(AUTOPLAY_ENABLED){
                    try {
                        autoPlayer.moveAndClickOnline(move, true); // TODO: need delay after??
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    autoPlayer.updateBoard(move); // TODO: not necessary if we run on other thread instead
                    System.out.println("====READY====");
                }
            }
            lastMove = move;
        } else if (e.getKeyChar() == 's') {
            System.out.println("Current evaluation of board: " + AI.getEvaluation(game));
        } else if (e.getKeyChar() == 'm') {
            int move = MCTS.MCTS(game);
            if (move != -1) {
                game.makeMove(move);
            }
            lastMove = move;
        } else if (e.getKeyChar() == 'o') {
            if(AUTOPLAY_ENABLED) {
                autoPlayer.getPlayerMove(true);
                System.out.println("====READY====");
            }
        } else if (e.getKeyChar() == 'j') {
            if(AUTOPLAY_ENABLED) autoPlayer.printCurrentBoard();
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private class InformationPanel extends JPanel {

        //This class contains the UI above the board.

        private UI ui;
        private BitboardGameState bitboardGame;

        private JButton undoButton;
        private JButton aiButton;

        public InformationPanel(UI ui, BitboardGameState game) {
            this.ui = ui;
            this.bitboardGame = game;
            setPreferredSize(new Dimension(TILE_SIZE * TILES, TILE_SIZE * 2));
            setLayout(null);

            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
            buttonPane.setBounds(TILE_SIZE*4 - TILE_SIZE - TILE_SIZE*3/4, TILE_SIZE + TILE_SIZE/4, TILE_SIZE*4, TILE_SIZE);

            undoButton = new JButton("Undo move (U)");
            aiButton = new JButton("Make AI move (A)");
            undoButton.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE/2));
            aiButton.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE/2));
            undoButton.setFocusable(false);
            aiButton.setFocusable(false);
            buttonPane.add(undoButton);
            buttonPane.add(aiButton);

            add(buttonPane, BorderLayout.SOUTH);

            //Undo button should undo move
            undoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    undoMove();
                    ui.repaint();
                }
            });

            //AI button should make AI move
            aiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        makeAIMove();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    ui.repaint();
                }
            });
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

            //Buttons
            undoButton.repaint();
            aiButton.repaint();
        }
    }
}
