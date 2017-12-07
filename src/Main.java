import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * The main application class. It also provides methods for communication with
 * the game engine.
 */
public class Main {
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
		int turn = 1;
		try {
			String s;
			Side side = Side.NORTH;
			while (true) {
				System.err.println();
				s = recvMsg();
				System.err.print("Received: " + s);
				try {
					MsgType mt = Protocol.getMessageType(s);
					switch (mt) {
					case START:
						System.err.println("A start.");
						boolean first = Protocol.interpretStartMsg(s);
						System.err.println("Starting player? " + first);

						// If we are on the south side, we're going first.
						if (first) {
							side = Side.SOUTH;

							GameTreeNode node = MoveDecisionMaker.decideMove(new Board(7, 7), true, side);
							int i = node.getMinMaxMove().getHole();
							sendMsg(Protocol.createMoveMsg(i));
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
						// If it's our turn, make the first legal move possible
						if (r.again) {
							GameTreeNode node = MoveDecisionMaker.decideMove(b, canSwap, side);
							if (node.getMinMaxMove() == null)
								sendMsg(Protocol.createSwapMsg());
							else{
								
							
							int i = node.getMinMaxMove().getHole();
							node = null;
							sendMsg(Protocol.createMoveMsg(i));
							}
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
