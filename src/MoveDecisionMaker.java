public class MoveDecisionMaker {

    private static final int searchDepth = 10; //need to be even??? maybe not?

    public MoveDecisionMaker()
    {

    }

    public GameTreeNode decideMove(Board board, boolean swapAvailable, Side maximizingSide, Heuristic givenHeuristic){
        GameTreeNode rootNode = new GameTreeNode(board, swapAvailable , maximizingSide, searchDepth, givenHeuristic);
        MinMaxAlphaBeta(rootNode, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, givenHeuristic);
        return rootNode;
    }

    private double MinMaxAlphaBeta(GameTreeNode gameTreeNode, double alpha, double beta, Heuristic givenHeuristic){
        if (gameTreeNode.getDepth() == 0 || Kalah.gameOver(gameTreeNode.getCurrentBoard())){
            return gameTreeNode.getCurrentHeuristic();
        }
        if (gameTreeNode.getNextPlayerTurn() == gameTreeNode.getMaximizingPlayer()){
            double tempAlpha = Double.NEGATIVE_INFINITY;
            for(GameTreeNode child : gameTreeNode.findChildren(givenHeuristic)){
                if(child == null){
                    break;
                }
                tempAlpha = Math.max(tempAlpha, MinMaxAlphaBeta(child, alpha, beta, givenHeuristic));
                if (tempAlpha > alpha){
                    alpha = tempAlpha;
                    gameTreeNode.setMinMaxMove(child.getParentMove());
                }
                if(beta <= alpha){
                    break;
                }
            }
            gameTreeNode.removeBadChildren();
            return tempAlpha;
        }
        else {
            double tempBeta = Double.POSITIVE_INFINITY;
            for(GameTreeNode child : gameTreeNode.findChildren(givenHeuristic)){
                if(child == null){
                    break;
                }
                tempBeta = Math.min(tempBeta, MinMaxAlphaBeta(child, alpha, beta, givenHeuristic));
                if (tempBeta < beta){
                    beta = tempBeta;
                    gameTreeNode.setMinMaxMove(child.getParentMove());
                }
                if(beta <= alpha){
                    break;
                }
            }
            gameTreeNode.removeBadChildren();
            return beta;
        }
    }



}
