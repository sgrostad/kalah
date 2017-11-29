public class MoveDecisionMaker {
    private static final int searchDepth = 7;

    //TODO Convert to iterative instead of recursive for speed
    public static Move decideMove(Board board, boolean swapAvailable, Side maximizingSide){
        BoardNode rootNode = new BoardNode(board, swapAvailable , maximizingSide, searchDepth);
        MinMaxAlphaBeta(rootNode, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        return rootNode.getMinMaxMove();
    }

    private static double MinMaxAlphaBeta(BoardNode boardNode, double alpha, double beta){
        if (boardNode.getDepth() == 0 || Kalah.gameOver(boardNode.getCurrentBoard())){
            // return Heuristic.advancedHeuristic(boardNode.getCurrentBoard(), boardNode.getMaximizingPlayer());
        }
        if (boardNode.getNextPlayerTurn() == BoardNode.getMaximizingPlayer()){
            double tempAlpha = Double.NEGATIVE_INFINITY;
            for(BoardNode child : boardNode.findChildren()){
                if(child == null){
                    break;
                }
                tempAlpha = Math.max(tempAlpha, MinMaxAlphaBeta(child, alpha, beta) );
                if (tempAlpha > alpha){
                    alpha = tempAlpha;
                    boardNode.setMinMaxMove(child.getParentMove());
                }
                if(beta <= alpha){
                    break;
                }
            }

            return tempAlpha;
        }
        else {
            double tempBeta = Double.POSITIVE_INFINITY;
            for(BoardNode child : boardNode.findChildren()){
                if(child == null){
                    break;
                }
                tempBeta = Math.min(tempBeta, MinMaxAlphaBeta(child, alpha, beta));
                if (tempBeta < beta){
                    beta = tempBeta;
                    boardNode.setMinMaxMove(child.getParentMove());
                }
                if(beta <= alpha){
                    break;
                }
            }
            return beta;
        }
    }
}
