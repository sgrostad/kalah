import java.util.Scanner;

public class SimpleGamePlayer {

    public static void SimpleGame(){
        Board board = new Board(7,7);
        Side turn = Side.SOUTH;
        Move move = new Move(Side.NORTH,1);
        Scanner reader = new Scanner(System.in);
        while(!Kalah.gameOver(board)){
            System.out.println(board.toString());
            System.out.println(turn.toString() + " Player's turn:");
            switch (turn){
                case NORTH:
                    System.out.println("Current Heuristic: " + Heuristic.advancedHeuristic(board, Side.NORTH));
                    System.out.println("Choose hole: ");
                    int hole = reader.nextInt();
                    move = new Move(Side.NORTH, hole);
                    break;
                case SOUTH:
                    BoardNode root = MoveDecisionMaker.decideMove(board, false, Side.SOUTH);
                    move = root.getMinMaxMove();
                    System.out.println("Computer moved from hole: " + move.getHole());
                    break;
            }
            turn = Kalah.makeMove(board, move, false);
        }
        reader.close();
        System.out.println(board.toString());
    }
}
