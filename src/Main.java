import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

/**
 * The main application class. It also provides methods for communication with
 * the game engine.
 */
public class Main {
	
	
	private static long FAST_MOVE_TIME = 10000;
	private static long SLOW_MOVE_TIME = 60000;
	private static long HURRY_UP_TIME = 3300000;
	
	
	/**
	 * Input from the game engine.
	 */
	private static Reader input = new BufferedReader(new InputStreamReader(
			System.in));

	/**
	 * Sends a message to the game engine.
	 * 
	 * @param msg
	 *            The message.
	 */
	public static void sendMsg(String msg) {
		System.out.print(msg);
		System.out.flush();
	}

	/**
	 * Receives a message from the game engine. Messages are terminated by a
	 * '\n' character.
	 * 
	 * @return The message.
	 * @throws IOException
	 *             if there has been an I/O error.
	 */
	public static String recvMsg() throws IOException {
		StringBuilder message = new StringBuilder();
		int newCharacter;

		do {
			newCharacter = input.read();
			if (newCharacter == -1)
				throw new EOFException("Input ended unexpectedly.");
			message.append((char) newCharacter);
		} while ((char) newCharacter != '\n');

		return message.toString();
	}

	/**
	 * The main method, invoked when the program is started.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		// TODO: implement
		// System.out.println("MOVE;2");
		long startTime = new Date().getTime();
		System.err.println(startTime);
		
		long totalTime = 0;
		long[] fiveTimes = new long[5];
		
		
		int turn = 1;
		int j = 0;
		
		long timeSum = 0;
		
		try {
			
			
			String s;
			Side side = Side.NORTH;
			while (true) {
				System.err.println();
				s = recvMsg();
				System.err.print("Received: " + s);
				try {
					
					j = 0;
					timeSum = 0;
					while (j < 5)
					{
						timeSum += fiveTimes[j];
						System.err.println(fiveTimes[j]);
						j++;
						
					}
					timeSum = timeSum /5;
					
					// if we get to 58 minutes then we need to get a move on or risk forfeiting
					if (totalTime > HURRY_UP_TIME)
					{
						System.err.println("Less than two minutes left, lowering depth!");
						MoveDecisionMaker.setSearchDepth(8);
					}
					else if (turn > 10)
					{
						// if our last 5 moves took &20 seconds& then we're taking too long
						if (timeSum > SLOW_MOVE_TIME)
						{
							System.err.println("Decision making is taking too long, lowering search depth...");
							MoveDecisionMaker.setSearchDepth(MoveDecisionMaker.getSearchDepth() - 1);
						}
						// if our last 5 moves took less than &0.5 seconds& we can look a little deeper, probably
						if (timeSum < FAST_MOVE_TIME)
						{
							System.err.println("Decision making is happening quickly, increasing search depth...");
							MoveDecisionMaker.setSearchDepth(MoveDecisionMaker.getSearchDepth() + 1);
						}
					}
					
					MsgType mt = Protocol.getMessageType(s);
					switch (mt) {
					case START:
						System.err.println("A start.");
						boolean first = Protocol.interpretStartMsg(s);
						System.err.println("Starting player? " + first);

						// If we are on the south side, we're going first.
						if (first) {
							long moveStartTime = new Date().getTime();
							
							
							side = Side.SOUTH;
							//GameTreeNode node = MoveDecisionMaker.decideMove(new Board(7, 7), true, side);
							//int i = node.getMinMaxMove().getHole();
							
							long moveEndTime = new Date().getTime();
							long moveTime = moveEndTime - moveStartTime;
							System.err.println(moveTime);
							totalTime += moveTime;
							fiveTimes[0] = fiveTimes[1];
							fiveTimes[1] = fiveTimes[2];
							fiveTimes[2] = fiveTimes[3];
							fiveTimes[3] = fiveTimes[4];
							fiveTimes[4] = moveTime;
							
							// hole 1 does not provide a significant advantage or disadvantage to us according to our heuristics
							// as such it cannot be exploited by the opponent using the pie rule
							sendMsg(Protocol.createMoveMsg(1));
						}
						break;
					case STATE:
						turn++;
						System.err.println("Turn " + turn);
						System.err.println("A state.");
						Board b = new Board(7, 7);
						System.err.println(s);
						Protocol.MoveTurn r = Protocol.interpretStateMsg(s, b);
						System.err.println("This was the move: " + r.move);
						
						if (r.move == -1)
						{
							if (side == Side.SOUTH)
									{
								side = Side.NORTH;
									}
									else side = Side.SOUTH;
						}
						System.err.println("Is the game over? " + r.end);
						if (!r.end)
							System.err.println("Is it our turn again? "
									+ r.again);
						System.err.print("The board:\n" + b);

						
						boolean canSwap = false;
						//if (turn < 3) canSwap = true;
						if (r.again) {

							
							
							long moveStartTime = new Date().getTime();
							
							//if (node.getMinMaxMove() == null)
							
							// if the opponent chooses 2, 3, or 4, as their first move going first
							// it is advantageous to them according to our heuristics, so we should swap
							if (turn == 2 && r.move > 1 && r.move < 5)
							{
								sendMsg(Protocol.createSwapMsg());
							side = Side.SOUTH;
							}
							else{
								
							GameTreeNode node = MoveDecisionMaker.decideMove(b, canSwap, side);
							int i = node.getMinMaxMove().getHole();
							node = null;
							sendMsg(Protocol.createMoveMsg(i));
							}
							
							long moveEndTime = new Date().getTime();
							long moveTime = moveEndTime - moveStartTime;
							System.err.println(moveTime);
							totalTime += moveTime;
							fiveTimes[0] = fiveTimes[1];
							fiveTimes[1] = fiveTimes[2];
							fiveTimes[2] = fiveTimes[3];
							fiveTimes[3] = fiveTimes[4];
							fiveTimes[4] = moveTime;
							
						}
						break;
					case END:
						System.err.println("An end. Bye bye!");
						return;
					}

				} catch (InvalidMessageException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("This shouldn't happen: " + e.getMessage());
		}
	}
}
