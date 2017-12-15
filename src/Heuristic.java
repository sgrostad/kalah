
public class Heuristic {

    //The following coefficients needs to be found by experimenting
    private static int stonesInStoreMaxCoefficient = 7;
    private static int stonesInStoreMinCoefficient = 7;
    private static int stonesInHolesMaxCoefficient = 3;
    private static int stonesInHolesMinCoefficient = 1;
    private static int freeTurnMovesMaxCoefficient = 5;
    private static int freeTurnMovesMinCoefficient = 5;
    private static int maxStealSeedMoveMaxCoefficient = 9;
    private static int maxStealSeedMoveMinCoefficient = 1;

    // Every function ending with "Diff" are used to make heuristics. Actual heuristic in the bottom
    private static int stonesInStoreDiff(Board board, Side side){
        int stonesInOwnStore = board.getSeedsInStore(side);
        int stonesInOppStore = board.getSeedsInStore(side.opposite());

        return (stonesInOwnStore - stonesInOppStore);
    }

    private static int stonesInHolesDiff(Board board, Side side) {
        int stonesInOwnHoles = numOfStonesInHoles(board, side);
        int stonesInOppHoles = numOfStonesInHoles(board, side.opposite());
        return (stonesInOwnHoles - stonesInOppHoles);
    }

    private static int numOfStonesInHoles(Board board, Side side){
        int stones = 0;
        for(int hole = 1; hole <= board.getNoOfHoles(); hole++){
            stones += board.getSeeds(side, hole);
        }
        return stones;
    }

    private static int freeTurnMovesDiff(Board board, Side side){
        int ownFreeTurnMoves = countFreeMoves(board, side);
        int oppFreeTurnMoves = countFreeMoves(board, side.opposite());
        return ownFreeTurnMoves - oppFreeTurnMoves;
    }

    private static int countFreeMoves(Board board, Side side){
        int count = 0;
        for(int hole = 1; hole <= board.getNoOfHoles(); hole++){
            Move move = new Move(side,hole);
            if(MoveClassifier.isFreeTurnMove(board, move)){
                count++;
            }
        }
        return count;
    }

    private static int maxStealMoveDiff(Board board, Side side){
        int ownMaxStealMove = maxStealMove(board, side);
        int oppMaxStealMove = maxStealMove(board, side.opposite());
        return ownMaxStealMove - oppMaxStealMove;
    }

    private static int maxStealMove(Board board, Side side){
        int maxSteal = 0;
        for(int hole = 1; hole <= board.getNoOfHoles(); hole++){
            Move move = new Move(side,hole);
            if(MoveClassifier.isStealSeedsMove(board, move)){
                int endHole = MoveClassifier.findEndHole(board, move);
                int stealValue = board.getSeedsOp(side, endHole);
                if (stealValue > maxSteal){
                    maxSteal = stealValue;
                }
            }
        }
        return maxSteal;
    }

    private static int calculateLinearIncreasingCoefficient(Board board, int minCoefficientValue, int maxCoefficientValue){
        int maxSeedsInStore = Math.max(board.getSeedsInStore(Side.NORTH), board.getSeedsInStore(Side.SOUTH));
        int seedsNeededToWin = board.getNoSeedsInHolesAtStart() * board.getNoOfHoles() + 1;
        return (int) ( ( (double)maxSeedsInStore / seedsNeededToWin) * (maxCoefficientValue + 1 - minCoefficientValue) + minCoefficientValue );
    }

    public static double advancedHeuristic(Board board, Side side){
        return calculateLinearIncreasingCoefficient(board,stonesInStoreMinCoefficient,stonesInStoreMaxCoefficient) * stonesInStoreDiff(board, side)
                + calculateLinearIncreasingCoefficient(board,stonesInHolesMinCoefficient,stonesInHolesMaxCoefficient) * stonesInHolesDiff(board, side)
                + calculateLinearIncreasingCoefficient(board,freeTurnMovesMinCoefficient,freeTurnMovesMaxCoefficient) * freeTurnMovesDiff(board, side)
                + calculateLinearIncreasingCoefficient(board,maxStealSeedMoveMinCoefficient,maxStealSeedMoveMaxCoefficient) * maxStealMoveDiff(board, side);
    }
}
