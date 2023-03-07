package Engine;

import java.util.ArrayList;
import java.util.List;

import static Engine.Bitboard.*;

public class BitboardGameState {
    public static final int BLACK = 0;
    public static final int WHITE = 1;

    private final long[] pieces = new long[2];

    private int playerToMove = BLACK;
    private int inversePlayerToMove = WHITE;

    private int moveCount = 0;
    private final int[] moveHistory = new int[256];
    private final long[] flippedHistory = new long[256];
    private final long[] legalMoveHistory = new long[256];

    private int[] numberOfPieces;
    private boolean gameOver = false;

    public BitboardGameState() {
        initializeStandardBoard();
        legalMoveHistory[moveCount] = computeLegalMoves();
    }

    public void makeMove(int move) {
        long moveMask = setBit(0L, move);
        long flipped = getFlippedPieces(moveMask);
        flippedHistory[moveCount] = flipped;

        //Place piece
        pieces[playerToMove] |= moveMask;

        //Flip pieces
        pieces[playerToMove] ^= flipped;
        pieces[inversePlayerToMove] ^= flipped;

        int piecesFlipped = Long.bitCount(flipped);
        numberOfPieces[playerToMove] += piecesFlipped + 1;
        numberOfPieces[inversePlayerToMove] -= piecesFlipped;

        moveHistory[moveCount++] = move;
        changePlayerToMove();
        legalMoveHistory[moveCount] = computeLegalMoves();

        //Change turn if no available moves
        if (legalMoveHistory[moveCount] == 0L) {
            changePlayerToMove();
            legalMoveHistory[moveCount] = computeLegalMoves();

            //End game if no player has any legal moves
            if (legalMoveHistory[moveCount] == 0L) {
                gameOver = true;
            }
        }
    }

    public void undoMove() {
        if (moveCount == 0) {
            System.out.println("Cannot undo any more moves");
            return;
        }

        legalMoveHistory[moveCount] = 0L;
        moveCount--;

        //Go back one more move if no available moves
        if (legalMoveHistory[moveCount] == 0L) {
            changePlayerToMove();
        }

        int move = moveHistory[moveCount];
        moveHistory[moveCount] = -1;

        //Flip pieces back
        long flipped = flippedHistory[moveCount];
        pieces[playerToMove] ^= flipped;
        pieces[inversePlayerToMove] ^= flipped;

        int piecesFlipped = Long.bitCount(flipped);
        numberOfPieces[playerToMove] += piecesFlipped;
        numberOfPieces[inversePlayerToMove] -= piecesFlipped + 1;

        changePlayerToMove();

        long moveMask = 1L << move;
        pieces[playerToMove] ^= moveMask;
        gameOver = false;
    }

    private long computeLegalMoves() {
        long result = 0L;

        long emptySquares = ~(pieces[playerToMove] | pieces[inversePlayerToMove]);

        long northCaptures = north(pieces[playerToMove]) & pieces[inversePlayerToMove];
        long southCaptures = south(pieces[playerToMove]) & pieces[inversePlayerToMove];
        long eastCaptures = east(pieces[playerToMove]) & pieces[inversePlayerToMove];
        long westCaptures = west(pieces[playerToMove]) & pieces[inversePlayerToMove];
        long northEastCaptures = northeast(pieces[playerToMove]) & pieces[inversePlayerToMove];
        long northWestCaptures = northwest(pieces[playerToMove]) & pieces[inversePlayerToMove];
        long southEastCaptures = southeast(pieces[playerToMove]) & pieces[inversePlayerToMove];
        long southWestCaptures = southwest(pieces[playerToMove]) & pieces[inversePlayerToMove];

        for (int i = 0; i < 5; i++) {
            northCaptures |= north(northCaptures) & pieces[inversePlayerToMove];
            southCaptures |= south(southCaptures) & pieces[inversePlayerToMove];
            eastCaptures |= east(eastCaptures) & pieces[inversePlayerToMove];
            westCaptures |= west(westCaptures) & pieces[inversePlayerToMove];
            northEastCaptures |= northeast(northEastCaptures) & pieces[inversePlayerToMove];
            northWestCaptures |= northwest(northWestCaptures) & pieces[inversePlayerToMove];
            southEastCaptures |= southeast(southEastCaptures) & pieces[inversePlayerToMove];
            southWestCaptures |= southwest(southWestCaptures) & pieces[inversePlayerToMove];
        }

        result |= (north(northCaptures) | south(southCaptures)
                | east(eastCaptures) | west(westCaptures)
                | northeast(northEastCaptures) | northwest(northWestCaptures)
                | southeast(southEastCaptures) | southwest(southWestCaptures))
                & emptySquares;

        return result;
    }

    private long getFlippedPieces(long moveMask) {
        long result = 0L;

        long northCaptures = north(moveMask) & pieces[inversePlayerToMove];
        long southCaptures = south(moveMask) & pieces[inversePlayerToMove];
        long eastCaptures = east(moveMask) & pieces[inversePlayerToMove];
        long westCaptures = west(moveMask) & pieces[inversePlayerToMove];
        long northEastCaptures = northeast(moveMask) & pieces[inversePlayerToMove];
        long northWestCaptures = northwest(moveMask) & pieces[inversePlayerToMove];
        long southEastCaptures = southeast(moveMask) & pieces[inversePlayerToMove];
        long southWestCaptures = southwest(moveMask) & pieces[inversePlayerToMove];

        for (int i = 0; i < 5; i++) {
            northCaptures |= north(northCaptures) & pieces[inversePlayerToMove];
            southCaptures |= south(southCaptures) & pieces[inversePlayerToMove];
            eastCaptures |= east(eastCaptures) & pieces[inversePlayerToMove];
            westCaptures |= west(westCaptures) & pieces[inversePlayerToMove];
            northEastCaptures |= northeast(northEastCaptures) & pieces[inversePlayerToMove];
            northWestCaptures |= northwest(northWestCaptures) & pieces[inversePlayerToMove];
            southEastCaptures |= southeast(southEastCaptures) & pieces[inversePlayerToMove];
            southWestCaptures |= southwest(southWestCaptures) & pieces[inversePlayerToMove];
        }

        if ((north(northCaptures) & pieces[playerToMove]) != 0L) {
            result |= northCaptures;
        }
        if ((south(southCaptures) & pieces[playerToMove]) != 0L) {
            result |= southCaptures;
        }
        if ((east(eastCaptures) & pieces[playerToMove]) != 0L) {
            result |= eastCaptures;
        }
        if ((west(westCaptures) & pieces[playerToMove]) != 0L) {
            result |= westCaptures;
        }
        if ((northeast(northEastCaptures) & pieces[playerToMove]) != 0L) {
            result |= northEastCaptures;
        }
        if ((northwest(northWestCaptures) & pieces[playerToMove]) != 0L) {
            result |= northWestCaptures;
        }
        if ((southeast(southEastCaptures) & pieces[playerToMove]) != 0L) {
            result |= southEastCaptures;
        }
        if ((southwest(southWestCaptures) & pieces[playerToMove]) != 0L) {
            result |= southWestCaptures;
        }

        return result;
    }

    private void changePlayerToMove() {
        playerToMove = 1 - playerToMove;
        inversePlayerToMove = 1 - inversePlayerToMove;
    }

    private void initializeStandardBoard() {
        pieces[BLACK] = E4 | D5;
        pieces[WHITE] = D4 | E5;
        numberOfPieces = new int[] {2,2};
    }

    public List<Integer> getLegalMoves() {
        ArrayList<Integer> result = new ArrayList<>();
        long moves = legalMoveHistory[moveCount];
        while (moves != 0L) {
            int move = Long.numberOfTrailingZeros(moves);
            result.add(move);
            moves &= moves - 1;
        }
        return result;
    }

    public int getNumberOfLegalMoves() {
        return Long.bitCount(legalMoveHistory[moveCount]);
    }

    public int getNumberOfBlackPieces() {
        return numberOfPieces[BLACK];
    }

    public int getNumberOfWhitePieces() {
        return numberOfPieces[WHITE];
    }

    public int getPlayerToMove() {
        return playerToMove;
    }

    public long[] getPieces() {
        return pieces;
    }

    public int getPiece(int index) {
        if (getBit(pieces[BLACK], index) != 0) {
            return BLACK;
        } else if (getBit(pieces[WHITE], index) != 0) {
            return WHITE;
        } else {
            return -1;
        }
    }

    public int getPiece(int x, int y) {
        return getPiece(x*8+y);
    }

    public int getMoveCount() {
        return moveCount;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isLegalMove(int move) {
        for (int legalMove : getLegalMoves()) {
            if (move == legalMove) {
                return true;
            }
        }
        return false;
    }

    //--------------HELPER FUNCTIONS--------------
    private long north(long bitboard) {
        return (bitboard & ~RANK_1) << 8;
    }

    private long south(long bitboard) {
        return (bitboard & ~RANK_8) >>> 8;
    }

    private long east(long bitboard) {
        return (bitboard & ~FILE_H) >>> 1;
    }

    private long west(long bitboard) {
        return (bitboard & ~FILE_A) << 1;
    }

    private long northeast(long bitboard) {
        return north(east(bitboard));
    }

    private long northwest(long bitboard) {
        return north(west(bitboard));
    }

    private long southeast(long bitboard) {
        return south(east(bitboard));
    }

    private long southwest(long bitboard) {
        return south(west(bitboard));
    }

    private long getBit(long bitboard, int index) {
        return (bitboard >> index) & 1L;
    }

    private long setBit(long bitboard, int index) {
        return bitboard | (1L << index);
    }

    private long clearBit(long bitboard, int index) {
        if (getBit(bitboard, index) > 0) {
            return bitboard ^ (1L << index);
        }
        return bitboard;
    }

    private void printBitboard(long bitboard) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int square = (i * 8 + j);
                System.out.print((getBit(bitboard, square) > 0 ? 1 : 0) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
