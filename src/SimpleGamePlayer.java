import java.util.Scanner;

public class SimpleGamePlayer {

    public static void main(String[] args){
        Board board = new Board(6,6);
        Side turn = Side.SOUTH;
        Move move;
        Side botSide = Side.NORTH;
        Side computerSide = botSide.opposite();
        //Scanner reader = new Scanner(System.in);
        boolean firstTurn = false;
        Heuristic botHeuristic = new Heuristic(Double.parseDouble(args[0]), 
                                               Double.parseDouble(args[1]),
                                               Double.parseDouble(args[2]));
        
        Heuristic computerHeuristic = new Heuristic(0.5,0.5,0.5);

        MoveDecisionMaker computerDecisionMaker = new MoveDecisionMaker();
        MoveDecisionMaker botDecisionMaker = new MoveDecisionMaker();

        GameTreeNode root;
        int turnNumber = 0;

        
        while(!Kalah.gameOver(board)){
    //        System.out.println(board.toString());
            
    //        System.out.println(turn.toString() + " Bot's turn:");
            
            if (turn == botSide) {
                //System.out.println("Current bot Heuristic: " + Heuristic.advancedHeuristic(board, playerSide));
                //System.out.println("Choose hole: ");
                //int hole = reader.nextInt();
                //if (hole == -1) {
                //    move = null;
                //} else {
                //    move = new Move(playerSide, hole);
            //    root = botDecisionMaker.decideMove(board, false, botSide, botHeuristic);
               
                if (firstTurn) {
                    root = computerDecisionMaker.decideMove(board, false, botSide, botHeuristic);
                    firstTurn = false;
                } else {
                    root = computerDecisionMaker.decideMove(board, false, botSide, botHeuristic);
                }
                move = root.getMinMaxMove();
                turnNumber++;
    //            System.out.println("Turn number is "+ turnNumber);

            }
            else {
                if (firstTurn) {
                    root = computerDecisionMaker.decideMove(board, true, computerSide, computerHeuristic);
                    firstTurn = false;
                } else {
                    root = computerDecisionMaker.decideMove(board, false, computerSide, computerHeuristic);
                }
                move = root.getMinMaxMove();

                
                //System.out.println("Computer moved from hole: " + move.getHole());
                turnNumber++;
    //            System.out.println("Turn number is "+ turnNumber);
            }
            
            if (move != null){
                turn = Kalah.makeMove(board, move, false);
            }
            else {
                System.out.println(turn.toString() + " swapped!");
                botSide = botSide.opposite();
                computerSide = computerSide.opposite();
            }
        }//while
        //reader.close();
        System.out.println(board.toString());
        System.out.println("GAME " + args[3] +" IS OVER ON TURN " + turnNumber);
        System.out.println();

    }
}