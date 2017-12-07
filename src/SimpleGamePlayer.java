import java.util.Scanner;

public class SimpleGamePlayer {

    public static void SimpleGame(){
        Board board = new Board(7,7);
        Side turn = Side.SOUTH;
        Move move;
        Side playerSide = Side.NORTH;
        Side computerSide = playerSide.opposite();
        Scanner reader = new Scanner(System.in);
        boolean firstTurn = false;
        while(!Kalah.gameOver(board)){
            System.out.println(board.toString());
            System.out.println(turn.toString() + " Player's turn:");
            if (turn == playerSide) {
                System.out.println("Current Heuristic: " + Heuristic.advancedHeuristic(board, playerSide));
                System.out.println("Choose hole: ");
                int hole = reader.nextInt();
                if (hole == -1) {
                    move = null;
                } else {
                    move = new Move(playerSide, hole);
                }
            }
            else {
                GameTreeNode root;
                if (firstTurn) {
                    root = MoveDecisionMaker.decideMove(board, true, computerSide);
                    firstTurn = false;
                } else {
                    root = MoveDecisionMaker.decideMove(board, false, computerSide);
                }
                move = root.getMinMaxMove();
                System.out.println("Computer moved from hole: " + move.getHole());
            }
            if (move != null){
                turn = Kalah.makeMove(board, move, false);
            }
            else {
                System.out.println(turn.toString() + " swapped!");
                playerSide = playerSide.opposite();
                computerSide = computerSide.opposite();
            }
        }
        reader.close();
        System.out.println(board.toString());
    }
}
