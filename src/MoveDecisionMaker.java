public class MoveDecisionMaker {

	private static final int MAX_DEPTH = 18;
	
    private static int searchDepth = 12;

    public static GameTreeNode decideMove(Board board, boolean swapAvailable, Side maximizingSide){
        GameTreeNode rootNode = new GameTreeNode(board, swapAvailable , maximizingSide, searchDepth);
        MinMaxAlphaBeta(rootNode, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		System.err.println(searchDepth);
        return rootNode;
    }

    public static GameTreeNode decideMove(Board board, boolean swapAvailable, Side maximizingSide, int depth){
        GameTreeNode rootNode = new GameTreeNode(board, swapAvailable , maximizingSide, depth);
        MinMaxAlphaBeta(rootNode, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        return rootNode;
    }

    private static double MinMaxAlphaBeta(GameTreeNode gameTreeNode, double alpha, double beta){
        if (gameTreeNode.getDepth() == 0 || Kalah.gameOver(gameTreeNode.getCurrentBoard())){
            return gameTreeNode.getCurrentHeuristic();
        }
        if (gameTreeNode.getNextPlayerTurn() == gameTreeNode.getMaximizingPlayer()){
            double tempAlpha = Double.NEGATIVE_INFINITY;
            for(GameTreeNode child : gameTreeNode.findChildren()){
                if(child == null){
                    break;
                }
                tempAlpha = Math.max(tempAlpha, MinMaxAlphaBeta(child, alpha, beta) );
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
            for(GameTreeNode child : gameTreeNode.findChildren()){
                if(child == null){
                    break;
                }
                tempBeta = Math.min(tempBeta, MinMaxAlphaBeta(child, alpha, beta));
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
    
    public static void setSearchDepth(int depth){
    	if (depth > 0 && depth <= MAX_DEPTH)
    		{
    		searchDepth = depth;
    		}
    	}
}
