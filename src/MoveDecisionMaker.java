public class MoveDecisionMaker {

    //TODO Convert to iterative instead of recursive for speed
    public void decideMove(Board board, boolean swapAvailable, Side maxSide){
        BoardNode rootNode = new BoardNode(board, swapAvailable , maxSide);
        MinMaxAlphaBeta(rootNode, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    private double MinMaxAlphaBeta(BoardNode boardNode, int depth, double alpha, double beta){

    }
}
