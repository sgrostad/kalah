
public class Heuristic {

    //The following coefficients needs to be found by experimenting
    private static double stonesInStoreCoefficient = 2;
    private static double stonesInHolesCoefficient = 0.1;
    private static double freeTurnMovesCoefficient = 0.5;
    private static double maxStealSeedMoveCoefficient = 0.7;

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

    public static double simpleHeuristic(Board board, Side side){
        return stonesInStoreCoefficient * stonesInStoreDiff(board, side)
                + stonesInHolesCoefficient * stonesInHolesDiff(board, side);
    }

    public static double advancedHeuristic(Board board, Side side){
        int minCoefficientValue = 1;
        int maxCoefficientValue = 5;
        int linearIncreasingCoefficient = calculateLinearIncreasingCoefficient(board, minCoefficientValue, maxCoefficientValue);
        return linearIncreasingCoefficient * stonesInStoreDiff(board, side)
                + stonesInHolesCoefficient * stonesInHolesDiff(board, side)
                + linearIncreasingCoefficient * freeTurnMovesDiff(board, side)
                + maxStealSeedMoveCoefficient * maxStealMoveDiff(board, side);
    }
}
