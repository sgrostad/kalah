public class MoveDecisionMaker {

    private static final int searchDepth = 12; //need to be even??? maybe not?

    public MoveDecisionMaker()
    {

    }

    public static GameTreeNode decideMove(Board board, boolean swapAvailable, Side maximizingSide, Heuristic givenHeurstic){
        GameTreeNode rootNode = new GameTreeNode(board, swapAvailable , maximizingSide, searchDepth, givenHeurstic);
        MinMaxAlphaBeta(rootNode, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, givenHeurstic);
        return rootNode;
    }

    public static GameTreeNode decideMove(Board board, boolean swapAvailable, Side maximizingSide, int depth, Heuristic givenHeurstic){
        GameTreeNode rootNode = new GameTreeNode(board, swapAvailable , maximizingSide, depth, givenHeurstic);
        MinMaxAlphaBeta(rootNode, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, givenHeurstic);
        return rootNode;
    }
    

    private static double MinMaxAlphaBeta(GameTreeNode gameTreeNode, double alpha, double beta, Heuristic givenHeurstic){
        if (gameTreeNode.getDepth() == 0 || Kalah.gameOver(gameTreeNode.getCurrentBoard())){
            return gameTreeNode.getCurrentHeuristic();
        }
        if (gameTreeNode.getNextPlayerTurn() == gameTreeNode.getMaximizingPlayer()){
            double tempAlpha = Double.NEGATIVE_INFINITY;
            for(GameTreeNode child : gameTreeNode.findChildren(givenHeurstic)){
                if(child == null){
                    break;
                }
                tempAlpha = Math.max(tempAlpha, MinMaxAlphaBeta(child, alpha, beta, givenHeurstic) );
                if (tempAlpha > alpha){
                    alpha = tempAlpha;
                    gameTreeNode.setBestChildGameTreeNode(child);
                }
                if(beta <= alpha){
                    break;
                }
            }
            return tempAlpha;
        }
        else {
            double tempBeta = Double.POSITIVE_INFINITY;
            for(GameTreeNode child : gameTreeNode.findChildren(givenHeurstic)){
                if(child == null){
                    break;
                }
                tempBeta = Math.min(tempBeta, MinMaxAlphaBeta(child, alpha, beta, givenHeurstic));
                if (tempBeta < beta){
                    beta = tempBeta;
                    gameTreeNode.setBestChildGameTreeNode(child);
                }
                if(beta <= alpha){
                    break;
                }
            }
            return beta;
        }
    }

    public static int getSearchDepth(){return searchDepth;}
}
