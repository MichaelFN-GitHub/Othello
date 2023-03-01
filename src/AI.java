import Engine.BitboardGameState;
import static Engine.BitboardGameState.BLACK;
import static Engine.BitboardGameState.WHITE;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class AI {
    public static final int BLACK_WIN = 999999;
    public static final int WHITE_WIN = -999999;
    public static final int MAX_DEPTH_WHITE = 11;
    public static final int MAX_DEPTH_BLACK = 11;

    private static BitboardGameState gameState;
    private static final int BLACK_TYPE = 2; //Version of the AI to test different different versions against each other.
    private static final int WHITE_TYPE = 1;

    private static int evaluatedStates;
    private static int currentType;

    public static int findNextMove(BitboardGameState game) {
        if (game.isGameOver()) {
            return -1;
        }
        System.out.println("Finding best move...");

        int maxDepth = game.getPlayerToMove() == BLACK ? MAX_DEPTH_BLACK : MAX_DEPTH_WHITE;
        currentType = game.getPlayerToMove() == BLACK ? BLACK_TYPE : WHITE_TYPE;
        evaluatedStates = 0;
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(true);

        long timeBeforeSearch = new Date().getTime();
        int[] moveAndScore = miniMax(game, maxDepth);  //Search
        long timeAfterSearch = new Date().getTime();
        long searchTime = timeAfterSearch - timeBeforeSearch + 1;

        System.out.println(
                "Depth: \t\t\t\t" + maxDepth
                + "\nBest move: \t\t\t" + moveAndScore[0]
                + "\nScore: \t\t\t\t" + moveAndScore[1]
                + "\nEvaluated states: \t" + format.format(evaluatedStates) + " evaluated states"
                + "\nSearch time: \t\t" + searchTime + " ms"
                + "\nNodes per second: \t" + format.format((long)evaluatedStates*1000/searchTime) + " nodes/s"
                + "\n"
        );

        return moveAndScore[0];
    }

    public static int[] miniMax(BitboardGameState game, int depth) {
        gameState = game;
        return miniMax(depth, game.getPlayerToMove() == BLACK, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int[] miniMax(int depth, boolean max, int alpha, int beta) {
        evaluatedStates++;

        if (depth == 0 || gameState.isGameOver()) {
            return new int[] { -1, evaluation(gameState) };
        }

        List<Integer> moves = gameState.getLegalMoves();
        int[] bestScore = new int[2];
        bestScore[1] = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int move : moves) {
            gameState.makeMove(move);
            int[] score = miniMax(depth - 1, !max, alpha, beta);

            //Update best move if score > best score
            if ((max && score[1] > bestScore[1]) || (!max && score[1] < bestScore[1])) {
                bestScore[0] = move;
                bestScore[1] = score[1];
            }

            gameState.undoMove();

            //Alpha-beta pruning
            if (max) {
                if (bestScore[1] >= beta) {
                    break;
                }
                alpha = Math.max(alpha, bestScore[1]);
            } else {
                if (bestScore[1] <= alpha) {
                    break;
                }
                beta = Math.min(beta, bestScore[1]);
            }
        }
        return bestScore;
    }

    private static int evaluation(BitboardGameState gameState) {
        int eval = 0;
        int player = gameState.getPlayerToMove();
        int numberOfBlackPieces = gameState.getNumberOfBlackPieces();
        int numberOfWhitePieces = gameState.getNumberOfWhitePieces();

        if (gameState.isGameOver()) {
            if (numberOfBlackPieces + numberOfWhitePieces == 64) {
                eval += numberOfBlackPieces > numberOfWhitePieces ? BLACK_WIN : WHITE_WIN;
            } else {
                eval += player == BLACK ? BLACK_WIN : WHITE_WIN;
            }
        }

        //Evaluate board score
        int boardScore = numberOfBlackPieces - numberOfWhitePieces;

        //Evaluate disk placement
        if (currentType > 1) {
            long[] pieces = gameState.getPieces().clone();
            int placementScore = 0;

            while(pieces[BLACK] != 0L) {
                int index = Long.numberOfTrailingZeros(pieces[BLACK]);
                placementScore += BLACK_DISK_PLACEMENT_TABLE[index];
                pieces[BLACK] &= pieces[BLACK] - 1;
            }

            while(pieces[WHITE] != 0L) {
                int index = Long.numberOfTrailingZeros(pieces[WHITE]);
                placementScore += WHITE_DISK_PLACEMENT_TABLE[index];
                pieces[WHITE] &= pieces[WHITE] - 1;
            }
            eval += placementScore;
        }

        //TODO: Evaluate mobility
        //int mobility = (player == BLACK) ? gameState.getNumberOfLegalMoves() : -gameState.getNumberOfLegalMoves();



        //TODO: Weigh scores based on game state (early, mid, late)
        eval += boardScore;
        //eval += mobility;

        return eval;
    }

    public static int getEvaluation(BitboardGameState gameState) {
        return evaluation(gameState);
    }

    private static final int[] BLACK_DISK_PLACEMENT_TABLE =
            {
             4, -3,  2,  2,  2,  2, -3,  4,
            -3, -4, -1, -1, -1, -1, -4, -3,
             2, -1,  1,  0,  0,  1, -1,  2,
             2, -1,  0,  1,  1,  0, -1,  2,
             2, -1,  0,  1,  1,  0, -1,  2,
             2, -1,  1,  0,  0,  1, -1,  2,
            -3, -4, -1, -1, -1, -1, -4, -3,
             4, -3,  2,  2,  2,  2, -3,  4,
            };

    private static final int[] WHITE_DISK_PLACEMENT_TABLE = new int[64];
    static {
        for (int i = 0; i < 64; i++) {
            WHITE_DISK_PLACEMENT_TABLE[i] = -BLACK_DISK_PLACEMENT_TABLE[i];
        }
    }
}
