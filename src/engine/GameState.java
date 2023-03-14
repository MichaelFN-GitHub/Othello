package engine;

import java.util.ArrayList;

public class GameState {

    //This is the old version of the game state. BitboardGameState is now used instead.

    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private int[][] board;
    private int player = BLACK;

    private int moveCount = 0;
    private final int[][] moveHistory = new int[128][];
    private final int[][][] flippedHistory = new int[128][128][];
    private final ArrayList<int[]>[] legalMoveHistory = new ArrayList[128];

    private int[] numberOfPieces;
    private boolean gameOver = false;

    public GameState() {
        initializeStandardBoard();
        legalMoveHistory[moveCount] = getLegalMoves();
    }

    public void makeMove(int x, int y) {
        int opponent = player == BLACK ? WHITE : BLACK;

        board[x][y] = player;

        //Flip opponent pieces
        int flipped = 0;
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                int newX = x + di;
                int newY = y + dj;
                if (newX == x && newY == y) {
                    continue;
                }
                while (inBounds(newX, newY) && board[newX][newY] == opponent) {
                    newX += di;
                    newY += dj;
                }
                if (inBounds(newX, newY) && board[newX][newY] == player) {
                    newX -= di;
                    newY -= dj;
                    while (newX != x || newY != y) {
                        board[newX][newY] = player;
                        flippedHistory[moveCount][flipped++] = new int[]{newX, newY};
                        newX -= di;
                        newY -= dj;
                    }
                }
            }
        }

        numberOfPieces[player - 1] += flipped + 1;
        numberOfPieces[opponent - 1] -= flipped;

        moveHistory[moveCount++] = new int[]{x, y};
        player = opponent;
        legalMoveHistory[moveCount] = getLegalMoves();

        //Change turn if no available moves
        if (legalMoveHistory[moveCount].size() == 0) {
            player = player == BLACK ? WHITE : BLACK;
            moveCount++;
            legalMoveHistory[moveCount] = getLegalMoves();

            //End game if no player has any legal moves
            if (legalMoveHistory[moveCount].size() == 0) {
                gameOver = true;
            }
        }
    }

    public void undoMove() {
        if (moveCount == 0) {
            System.out.println("Cannot undo any more moves");
            return;
        }

        int opponent = player == BLACK ? WHITE : BLACK;

        legalMoveHistory[moveCount] = null;

        //Get last move
        moveCount--;

        //Go back one more move if no available moves
        if (legalMoveHistory[moveCount].size() == 0) {
            player = player == BLACK ? WHITE : BLACK;
            opponent = player == BLACK ? WHITE : BLACK;
            moveCount--;
        }

        int x = moveHistory[moveCount][0];
        int y = moveHistory[moveCount][1];
        moveHistory[moveCount] = null;

        //Flip pieces back
        int flipped = 0;
        while (flippedHistory[moveCount][flipped] != null) {
            board[flippedHistory[moveCount][flipped][0]][flippedHistory[moveCount][flipped][1]] = player;
            flippedHistory[moveCount][flipped] = null;
            flipped++;
        }

        numberOfPieces[player - 1] += flipped;
        numberOfPieces[opponent - 1] -= flipped + 1;

        player = opponent;
        board[x][y] = EMPTY;
        gameOver = false;
    }

    public ArrayList<int[]> getLegalMoves() {
        ArrayList<int[]> result = new ArrayList<>();

        int opponent = player == WHITE ? BLACK : WHITE;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != 0) {
                    continue;
                }
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        int x = i + di;
                        int y = j + dj;
                        if (x == i && y == j) {
                            continue;
                        }
                        while (inBounds(x, y) && board[x][y] == opponent) {
                            x += di;
                            y += dj;
                            if (inBounds(x, y) && board[x][y] == player) {
                                result.add(new int[]{i, j});
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public boolean isLegalMove(int x, int y) {
        if (legalMoveHistory[moveCount] == null) {
            System.out.println("No legal moves available. Game is probably over.");
            return false;
        }
        for (int[] move : legalMoveHistory[moveCount]) {
            if (move[0] == x && move[1] == y) {
                return true;
            }
        }
        return false;
    }

    private boolean inBounds(int x, int y) {
        return (x >= 0 && y >= 0 && x < 8 && y < 8);
    }

    private void initializeStandardBoard() {
        board = new int[8][8];
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
        board[4][4] = WHITE;
        numberOfPieces = new int[]{2, 2};
    }

    public int[][] getBoard() {
        return board;
    }

    public int[] getNumberOfPieces() {
        return numberOfPieces;
    }

    public int getPlayer() {
        return player;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
