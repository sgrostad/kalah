
public class Heuristic {

    //The following coefficients needs to be found by experimenting
    private static double stonesInStoreCoefficient = 2;
    private static double stonesInHolesCoefficient = 0.5;
    private static double freeTurnMovesCoefficient = 0.5;
    private static double stealSeedMovesCoefficient = 0.5;
    private static double maxStealSeedMoveCoefficient = 0.5;

    // Every function ending with "Diff" are used to make heuristics. Actual heuristic in the bottom
    private static int stonesInStoreDiff(Board board, Side side){
        int stonesInOwnStore = board.getSeedsInStore(side);
        int stonesInOppStore = board.getSeedsInStore(side.opposite());

        return (stonesInOwnStore - stonesInOppStore);
    }

    private static int stonesInHolesDiff(Board board, Side side) {
        int stonesInOwnHoles = numberOfStonesInHoles(board, side);
        int stonesInOppHoles = numberOfStonesInHoles(board, side.opposite());
        return (stonesInOwnHoles - stonesInOppHoles);
    }

    private static int numberOfStonesInHoles(Board board, Side side){
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
            if(isFreeTurnMove(board, side, hole)){
                count++;
            }
        }
        return count;
    }

    private static boolean isFreeTurnMove(Board board, Side side, int hole){
        int stonesInHole = board.getSeeds(side, hole);
        int noOfHoles = board.getNoOfHoles();
        if(stonesInHole % (noOfHoles*2 + 1) == noHolesToStore(board, side, hole)){
            return true;
        }
        return false;
    }

    private static int noHolesToStore(Board board, Side side, int hole){
        if (side == Side.NORTH){
            return hole;
        }
        else{
            return board.getNoOfHoles() - hole + 1;
        }
    }
    /* Do not think this is a good heuristic:*/
    private static int noStealMovesDiff(Board board, Side side){
        int ownStealMoves = countStealMoves(board, side);
        int oppStealMoves = countStealMoves(board, side.opposite());
        return ownStealMoves - oppStealMoves;
    }

    private static int maxStealMoveDiff(Board board, Side side){
        int ownMaxStealMove = maxStealMove(board, side);
        int oppMaxStealMove = maxStealMove(board, side.opposite());
        return ownMaxStealMove - oppMaxStealMove;
    }

    private static int maxStealMove(Board board, Side side){
        int maxSteal = 0;
        for(int hole = 1; hole <= board.getNoOfHoles(); hole++){
            if(isStealSeedsMove(board, side, hole)){
                int endHole = findEndHole(board, side, hole);
                int stealValue = board.getSeeds(side.opposite(), endHole);
                if (stealValue > maxSteal){
                    maxSteal = stealValue;
                }
            }
        }
        return maxSteal;
    }

    private static int countStealMoves(Board board, Side side){
        int count = 0;
        for(int hole = 1; hole <= board.getNoOfHoles(); hole++){
            if(isStealSeedsMove(board, side, hole)){
                count++;
            }
        }
        return count;
    }

    private static boolean isStealSeedsMove(Board board, Side side, int hole){
        int stealFromHole = findEndHole(board, side, hole);
        if (!enoughSeedsForOverFullRound(board, side, hole)
                && board.getSeeds(side, hole) > 0
                && stealFromHole > 0
                && board.getSeedsOp(side, stealFromHole) > 0){
            return true;
        }
        return false;
    }

    private static boolean enoughSeedsForOverFullRound(Board board, Side side, int hole){
        int seeds = board.getSeeds(side, hole);
        int noHoles = board.getNoOfHoles();
        if(seeds > noHoles * 2 + 1){
            return true;
        }
        return false;
    }

    private static int findEndHole(Board board, Side side, int hole){
        int seeds = board.getSeeds(side, hole);
        int distanceToStore = noHolesToStore(board, side, hole);
        int noHoles = board.getNoOfHoles();
        seeds = seeds % (noHoles * 2 + 1);
        if(side == Side.NORTH){
            if(seeds < distanceToStore){
                return hole - seeds;
            }
            else if(seeds > distanceToStore + noHoles){ // to many seeds already handled
                return noHoles - (seeds - distanceToStore - noHoles);
            }
        }
        else{ //Side.South
            if (seeds < distanceToStore){
                return hole + seeds;
            }
            else if(seeds > distanceToStore + noHoles){// to many seeds already handled
                return seeds - distanceToStore - noHoles;
            }
        }
        return 0;
    }

    public static double simpleHeuristic(Board board, Side side){
        return stonesInStoreCoefficient * stonesInStoreDiff(board, side)
                + stonesInHolesCoefficient * stonesInHolesDiff(board, side);
    }

    public static double advancedHeuristic(Board board, Side side){
        return stonesInStoreCoefficient * stonesInStoreDiff(board, side)
                + stonesInHolesCoefficient * stonesInHolesDiff(board, side)
                + freeTurnMovesCoefficient * freeTurnMovesDiff(board, side)
                + stealSeedMovesCoefficient * noStealMovesDiff(board, side) /* Do not think this is a good heuristic*/
                + maxStealSeedMoveCoefficient * maxStealMoveDiff(board, side);
    }
}
