import java.awt.*;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.util.Arrays;

public class PlayOnline {

    Robot robot = new Robot();
//
//    //region Settings for vint.ee open in window in right side of screen (16" MacBook Pro)
//    int spaceBetweenPieces = 75;
//    int firstPieceXInCapture = 25; // 30 instead of 37 because of red dot
//    int firstPieceYInCapture = 25;
//    int firstPieceXInOwnBoard = 35;
//    int firstPieceYInOwnBoard = 245;
//    int TILE_SIZE = 70;
//    //Rectangle rectangle = new Rectangle(1775, 650, 8*spaceBetweenPieces, 8*spaceBetweenPieces);
//    Rectangle rectangle = new Rectangle(890, 350, 8*spaceBetweenPieces, 8*spaceBetweenPieces);
////1480 x end
//    int whiteRGB = -2236963;
//    int blackRGB = -16777216;
//int[] boardBackroundRGBs = new int[]{-13866187}; // some websites have highlighted possible moves
//
//    //endregion
//
//





//    //region Settings for eothello.com open in window in right side of screen (16" MacBook Pro)
//    int spaceBetweenPieces = 70;
//    int firstPieceXInCapture = 25; // 30 instead of 37 because of red dot
//    int firstPieceYInCapture = 25;
//    int firstPieceXInOwnBoard = 35;
//    int firstPieceYInOwnBoard = 245;
//    int TILE_SIZE = 70;
//    //Rectangle rectangle = new Rectangle(1775, 650, 8*spaceBetweenPieces, 8*spaceBetweenPieces);
//    Rectangle rectangle = new Rectangle(1015, 400, 8*spaceBetweenPieces, 8*spaceBetweenPieces);
//int[] boardBackroundRGBs = new int[]{-16674713}; // some websites have highlighted possible moves
//
//
//    //endregion





//
//    //region Settings for hewgill.com open in window in right side of screen (16" MacBook Pro)
//    int spaceBetweenPieces = 49;
//    int firstPieceXInCapture = 15; // 30 instead of 37 because of red dot
//    int firstPieceYInCapture = 15;
//    int firstPieceXInOwnBoard = 35;
//    int firstPieceYInOwnBoard = 245;
//    int TILE_SIZE = 70;
//    //Rectangle rectangle = new Rectangle(1775, 650, 8*spaceBetweenPieces, 8*spaceBetweenPieces);
//    Rectangle rectangle = new Rectangle(885, 250, 8*spaceBetweenPieces, 8*spaceBetweenPieces);
//int[] boardBackroundRGBs = new int[]{-16673792}; // some websites have highlighted possible moves

//
//    //endregion






    //region Settings for https://www.egaroucid.nyanyan.dev/en/web/ open in window in right side of screen (16" MacBook Pro)
    int spaceBetweenPieces = 63;
    int firstPieceXInCapture = 25; // 30 instead of 37 because of red dot
    int firstPieceYInCapture = 25;
    int firstPieceXInOwnBoard = 35;
    int firstPieceYInOwnBoard = 245;
    int TILE_SIZE = 70;
    //Rectangle rectangle = new Rectangle(1775, 650, 8*spaceBetweenPieces, 8*spaceBetweenPieces);
    Rectangle rectangle = new Rectangle(1045, 500, 8*spaceBetweenPieces, 8*spaceBetweenPieces);
    int[] boardBackroundRGBs = new int[]{-14444174, -9389922, -15045808}; // some websites have highlighted possible moves

    //endregion




    boolean weAreWhite = true;

    int[][] previousBoard = new int[8][8];

    public PlayOnline() throws AWTException {
//        for(int i=0; i<8; i++) {
//            for (int k = 0; k < 8; k++) {
//                previousBoard[i][k] = boardRGB;
//            }
//        }
//        previousBoard[3][3] = whiteRGB; // white
//        previousBoard[4][4] = whiteRGB;
//        previousBoard[3][4] = blackRGB; // black
//        previousBoard[4][3] = blackRGB;
        robot.mouseMove(rectangle.x - 10, rectangle.y - 10); // change to webbrowser
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        for(int i=0; i<8; i++) {
            for (int k = 0; k < 8; k++) {
                previousBoard[i][k] = 0;
            }
        }
        previousBoard[3][3] = 1;
        previousBoard[4][4] = 1;
        previousBoard[3][4] = 1;
        previousBoard[4][3] = 1;
    }

    public int getAndWaitForPlayerMove(){
        int move = -1;
        while(move == -1){
            move = getPlayerMove(false);
        }

        return move;
    }

    private boolean isMovedPiece(int row, int column, int[][] currentBoard){
        var rgbInt = currentBoard[row][column];
        if(previousBoard[row][column] == 1) return false; // already placed a piece here

        var isBackground = false;
        for(int i=0; i<boardBackroundRGBs.length; i++){
            if(boardBackroundRGBs[i] - 250000 < rgbInt && boardBackroundRGBs[i] + 250000 > rgbInt){
                isBackground = true;
            }else{
                var test = 0;
            }
        }

        return !isBackground;
    }

    public int getPlayerMove(boolean playingWithUI){
//        writePreviousBoard();
        var currentBoard = getCurrentBoard();

        int piece = -1;
        for(int i=0; i<8; i++) {
            for (int k = 0; k < 8; k++) {
                if(isMovedPiece(i, k, currentBoard)){
                    System.out.println("piece row: " + i);
                    System.out.println("piece column: " + k);
                    piece = i * 8 + k;
                    System.out.println("Piece color: " + currentBoard[i][k]);
                    previousBoard[i][k] = 1; // mark as placed
                }
            }
        }

        System.out.println("Opponent moved piece: " + piece);

        if(playingWithUI && piece != -1) {
            moveAndClickOwnBoard(piece);
        }

        return piece;
    }


    private int[][] getCurrentBoard(){

        var currentBoardImage = robot.createScreenCapture(rectangle);

        var test = currentBoardImage.createGraphics();
        var currentBoard = new int[8][8];
//        JFrame frame = new JFrame();
//        frame.getContentPane().setLayout(new FlowLayout());
//        frame.getContentPane().add(new JLabel(new ImageIcon(currentBoardImage)));
//        frame.pack();
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // if you want the X button to close the app

        for(int i=0; i<8; i++) {
            System.out.println("row" + i);
            var tileY = firstPieceYInCapture + i * spaceBetweenPieces;
            for (int k = 0; k < 8; k++) {
                var tileX = firstPieceXInCapture + k * spaceBetweenPieces;
                currentBoard[i][k] = currentBoardImage.getRGB(tileX, tileY);
                System.out.print(currentBoard[i][k] + " ");
//                System.out.print(tileX + " " + tileY);
            }
            System.out.println();
        }

        return currentBoard;
    }

    public void moveAndClickOnline(int piece, boolean playingWithUI) throws InterruptedException {
        var row = piece / 8;
        var column = piece % 8;

        var x = rectangle.x + firstPieceXInCapture + spaceBetweenPieces * column;
        var y = rectangle.y + firstPieceYInCapture + spaceBetweenPieces * row;

        System.out.println("AI move piece: " + piece );
        System.out.println("Click online x: " + x + " y: " + y);

        robot.mouseMove(x, y);

        Thread.sleep(153);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(182);

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        if(playingWithUI){
            // needs to be done twice because we change windows
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            // back to java program
            robot.mouseMove(80,80);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }

    }


    public void moveAndClickOwnBoard(int piece){
        var row = piece / 8;
        var column = piece % 8;

        var x = firstPieceXInOwnBoard + TILE_SIZE * column;
        var y = firstPieceYInOwnBoard + TILE_SIZE * row;

        System.out.println("Own board move piece: " + piece );
        System.out.println("Click own board x: " + x + " y: " + y);


        robot.mouseMove(x, y);

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void updateBoard(){
        previousBoard = getCurrentBoard();
    }

    public void updateBoard(int piece){
//        var board = getCurrentBoard();
        var row = piece / 8;
        var column = piece % 8;
        previousBoard[row][column] = 1;
//        printCurrentBoard();
//        System.out.println("new color for (row,colum): (" + row + "," + column + ") " + previousBoard[row][column]);
        System.out.println("marked as placed (" + row + "," + column + ")");
    }

    public void printCurrentBoard(){
        var currentBoardImage = robot.createScreenCapture(rectangle);

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(currentBoardImage)));
        frame.pack();
        frame.setVisible(true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // if you want the X button to close the app

    }

    public void writePreviousBoard(){
        System.out.println("previous board");

        for(int i=0; i<8; i++) {
            for (int k = 0; k < 8; k++) {
                System.out.print(previousBoard[i][k] + " ");
//                System.out.print(tileX + " " + tileY);
            }
            System.out.println();
        }
    }

//    public int getColorToPlace(){
//        your mom
//    }
}
