import engine.BitboardGameState;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCTS {
    public static int iterations = 100000;
    private static int playerToMove;
    private static int originalMoveCount;
    public static int MCTS(BitboardGameState game) {
        playerToMove = game.getPlayerToMove();
        originalMoveCount = game.getNumberOfBlackPieces() + game.getNumberOfWhitePieces();
        Node root = new Node(-1);
        int count = 0;
        while (count < iterations) {
            count++;
            Node leaf = traverse(game, root);
            if (leaf == null) {
                continue;
            }
            int simulationResult = rollout(game, leaf);
            backpropagate(game, leaf, simulationResult);
            while (game.getNumberOfWhitePieces() + game.getNumberOfBlackPieces() > originalMoveCount) {
                game.undoMove();
            }
        }
        return bestChild(game, root);
    }

    private static int bestChild(BitboardGameState game, Node root) {
        Node bestNode = null;
        for (Node nd : root.children) {
            if (bestNode == null || nd.n > bestNode.n) {
                bestNode = nd;
            }
        }
        System.out.println(bestNode.r/(double)bestNode.n);
        return bestNode.move;
    }

    private static void backpropagate(BitboardGameState game, Node leaf, int simulationResult) {
        if (leaf.move == -1) {
            leaf.n++;
            leaf.r += simulationResult;
            return;
        }
        if (leaf.parent.move >= 0) {
            game.undoMove();
        }

        leaf.n++;
        leaf.r += simulationResult;
        backpropagate(game, leaf.parent, simulationResult);
    }

    public static int rollout(BitboardGameState game, Node leaf) {
        int counter = 0;
        while (!game.isGameOver()) {
            List<Integer> legalMoves = game.getLegalMoves();
            counter++;
            game.makeMove(legalMoves.get(new Random().nextInt(0,legalMoves.size())));
        }
        int score = game.getNumberOfBlackPieces() > game.getNumberOfWhitePieces() ? 1 : 0;
        if (playerToMove == BitboardGameState.WHITE) {
            score = score == 1 ? 0 : 1;
        }
        for (int i = 0; i < counter; i++) {
            game.undoMove();
        }
        return score;
    }

    public static Node traverse(BitboardGameState game, Node node) {
        int count = 0;
        while (node != null && node.fullyExpanded) {
            node = bestUCB(node);
            if (node != null && node.move >= 0) {
                game.makeMove(node.move);
                count++;
            }
        }
        if (node == null) {
            for (int i = 0; i < count; i++) {
                game.undoMove();
            }
            return null;
        }
        int nextMove = -1;
        count = 0;
        List<Integer> legalMoves = game.getLegalMoves();
        for (int move : legalMoves) {
            boolean match = false;
            for (Node child : node.children) {
                if (move == child.move) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                nextMove = move;
                count++;
            }
        }
        if (nextMove == -1) {
            return node;
        }
        Node newNode = new Node(nextMove);
        newNode.parent = node;
        node.children.add(newNode);
        if (count <= 1) {
            node.fullyExpanded = true;
        }
        return newNode;
    }

    public static Node bestUCB(Node node) {
        Node bestNode = null;
        double bestUCB = -1;
        for (Node nd : node.children) {
            double UCBScore = nd.r / (double)nd.n + Math.sqrt(2 * Math.log(nd.parent.n) / (double)nd.n);
            if (UCBScore > bestUCB) {
                bestUCB = UCBScore;
                bestNode = nd;
            }
        }
        return bestNode;
    }

    private static class Node {
        public int n;
        public int r;
        public int move;
        public Node parent;
        public List<Node> children = new ArrayList<>();
        public boolean fullyExpanded = false;

        public Node(int move) {
            this.move = move;
        }
    }
}
