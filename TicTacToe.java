import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToe {

	//Initial Game State 
	private char[] gameState = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}; 
	
	/*
	 * Facilitates the Tic Tac Toe game by receiving the user's moves as keyboard inputs and using miniMaxDecision() to choose the computer's moves 
	 * The state of the game board is output after each move and the result of the game is output at the end. 
	 */
	public void miniMax() {
		char p = player(gameState); 
		printState(gameState);
		while (!terminalTest(gameState)) {
			//User's Turn 
			p = player(gameState); 
			if (p == 'X') {
				char[] newState = userMove(gameState);  
				//Only updates state with user's input if they enter a valid empty index
				if (gameState != newState) gameState = newState; 
			}
			//Computer's Turn 
			else {
				gameState = result(gameState, miniMaxDecision(gameState)); 
			}
			//Outputs new state 
			printState(gameState);
		}
		//Outputs result of the game 
		if (utility(gameState) == 0) System.out.println("X wins!");
		else if (utility(gameState) == 2) System.out.println("O wins!");
		else System.out.println("It's a tie!");
	}
	
	/*
	 * Decides which action the computer should take by aiming to minimize the user's chance of winning and maximizing the computer's chance of winning 
	 * @param – state, a given game state 
	 * @return – the action the computer should take 
	 */
	public int miniMaxDecision(char[] state) {
		ArrayList<Integer> actions = actions(state); 
		int max = 0; 
		int action = 0; 
		for (int i = 0; i < actions.size(); i++) {
			int minVal = minValue(result(state, actions.get(i)));
			if (minVal >= max) {
				max = minVal; 
				action = actions.get(i); 
			}
		}
		return action; 
	}
	
	/*
	 * Determines the best possible computer's move 
	 * @param – state, a given game state 
	 * @return – integer utility function value of the state created by the best possible computer's move 
	 */
	public int maxValue(char[] state) {
		if (terminalTest(state)) {
			return utility(state); 
		}
		int max = 0; 
		ArrayList<Integer> actions = actions(state); 
		for (int i = 0; i < actions.size(); i++) {
			int v = minValue(result(state, actions.get(i))); 
			if (v > max) max = v; 
		}
		return max; 
	}
	
	/*
	 * Determines the best possible user's move 
	 * @param – state, a given game state 
	 * @return – integer utility function value of the state created by the best possible user's move 
	 */
	public int minValue(char[] state) {
		if (terminalTest(state)) {
			return utility(state); 
		}
		int min = 2;  
		ArrayList<Integer> actions = actions(state); 
		for (int i = 0; i < actions.size(); i++) {
			int v = maxValue(result(state, actions.get(i))); 
			if (v < min) min = v; 
		}
		return min; 
	}
	
	/*
	 * Creates list of possible actions (to which numbered squares a variable could be added)
	 * @param – state, a given game state 
	 * @return – list of integer array indices of which squares a variable could be added to 
	 */
	public ArrayList<Integer> actions(char state[]) {
		ArrayList<Integer> actions = new ArrayList<Integer>();   
		for (int i = 0; i < 9; i++) {
			if (state[i] == ' ') actions.add(i); 
		}
		return actions; 
	}
	
	/*
	 * The transition model, which determines a resulting state when given a state and the computer's desired action 
	 * @param – state, a given game state 
	 * @param – index, the index where the computer wishes to place a 0
	 * @return – the resulting state  
	 */
	public char[] result(char[] state, int index) {
		char[] resultState = duplicateState(state);
		char move; 
		//Determines whether to add an X or an O to the game
		if (player(state) == 'O') move = 'O';
		else move = 'X';
		//Creates resulting state
		resultState[index] = move; 
		return resultState; 
	}
	
	/*
	 * Determines the resulting state when given a state and the user's desired action 
	 * @param – state, a given game state 
	 * @param – index, the index where the user wishes to place an X
	 * @return – the resulting state  
	 */
	public char[] userMove(char[] state) {
		char[] userState = duplicateState(state); 
		int index = userSpaceChoice();
		//Returns same state if user's move is invalid
		if ((index < 0) || (index > 8)) {
			System.out.println("Invalid move. Must be a number 1-9");
			return userState; 
		}
		//Returns same state if user's move is invalid
		else if (state[index] != ' ') {
			System.out.println("This spot is already filled. Pick another spot's number!");
			return userState; 
		}
		userState[index] = 'X'; //user's move 
		return userState; 
	}
	
	/*
	 * Determines which player has the move in a state
	 * @param – state, a given game state 
	 * @return – a character X or O, the character of the player whose move it is 
	 */
	public char player(char[] state) {
		char player; 
		if (numXs(state) > numOs(state)) player = 'O';
		else player = 'X'; 
		return player; 
	}
	
	/*
	 * Determines if the state is a terminal state, i.e the game has ended 
	 * @param – state, a given game state 
	 * @return – true if the state is a terminal state, and false otherwise 
	 */
	public Boolean terminalTest(char[] state) {
		if (win(state, 'X')) return true; 
		else if (win(state, 'O')) return true; 
		else if (numEmptySpots(state) == 0) return true; 
		else return false; 
	}
	
	/*
	 * Defines the final numeric value for a game that ends in a given terminal state for a particular player 
	 * @param – state, a given game state 
	 * @param – player, the player for whom a numeric value should be determined 
	 * @return – a double, the numeric value for a given player and terminal state 
	 */
	public int utility(char[] state) {
		Boolean resultX = win(state, 'X'); 
		Boolean resultO = win(state, 'O');
		//Case of Tie 
		if (resultX == resultO) return 1; 
		//Utility of O
		else {
			if (resultO) return 2; 
			else return 0; 
		}
	}
	
	/*
	 * Outputs a game state in a row-major order line
	 * @param state – game state to be output
	 */
	public void printState(char[] state) {
		for (int i = 0; i < 9; i++) {
			if ((i == 3) || (i == 6)) System.out.println();
			if (state[i] == ' ') System.out.print("_ "); 
			else System.out.print(state[i] + " ");
		}
		System.out.println("\n");
	}
	
	/*
	 * Determines how many spots within the tic tac toe game state are blank
	 * @param – state, a given game state 
	 * @return – integer number of empty spots in the game state  
	 */
	public int numEmptySpots(char[] state) {
		int numEmpty = 0; 
		for (int i = 0; i < 9; i++) {
			if (state[i] == ' ') numEmpty++; 
		}
		return numEmpty; 
	}
	
	/*
	 * Determines how many Xs are in the tic tac toe game state
	 * @param – state, a given game state 
	 * @return – integer number of Xs in the game state  
	 */
	public int numXs(char[] state) {
		int numXs = 0; 
		for (int i = 0; i < 9; i++) {
			if (state[i] == 'X') numXs++; 
		}
		return numXs; 
	}
	
	/*
	 * Determines how many Os are in the tic tac toe game state
	 * @param – state, a given game state 
	 * @return – integer number of Os in the game state  
	 */
	public int numOs(char[] state) {
		int numOs = 0; 
		for (int i = 0; i < 9; i++) {
			if (state[i] == 'O') numOs++; 
		}
		return numOs; 
	}
	
	/*
	 * Determines indices of Xs in a given state
	 * @param – state, a given game state 
	 * @return – list of all X indices 
	 */
	public ArrayList<Integer> indexXs(char[] state) {
		ArrayList<Integer> indices = new ArrayList<Integer>();   
		for (int i = 0; i < 9; i++) {
			if (state[i] == 'X') indices.add(i); 
		}
		return indices; 
	}
	
	/*
	 * Determines indices of Os in a given state
	 * @param – state, a given game state 
	 * @return – list of all O indices 
	 */
	public ArrayList<Integer> indexOs(char[] state) {
		ArrayList<Integer> indices = new ArrayList<Integer>();   
		for (int i = 0; i < 9; i++) {
			if (state[i] == 'O') indices.add(i); 
		}
		return indices; 
	}

	/*
	 * Determines if a given state contains a winning combination for a particular player 
	 * @param – state, a given game state
	 * @param – player, which player should be tested for wins (indicated by X or O)  
	 * @return – true if the state contains a winning combination for the given player, false otherwise
	 */
	public Boolean win(char[] state, char player) {
		ArrayList<Integer> indices; 
		if (player == 'X') indices = indexXs(state); 
		else indices = indexOs(state); 
		//Horizontal Wins
		if (indices.contains(0) && indices.contains(1) && indices.contains(2)) return true; 
		else if (indices.contains(3) && indices.contains(4) && indices.contains(5)) return true; 
		else if (indices.contains(6) && indices.contains(7) && indices.contains(8)) return true; 
		//Vertical Wins
		else if (indices.contains(0) && indices.contains(3) && indices.contains(6)) return true; 
		else if (indices.contains(1) && indices.contains(4) && indices.contains(7)) return true; 
		else if (indices.contains(2) && indices.contains(5) && indices.contains(8)) return true; 
		//Diagonal Wins
		else if (indices.contains(0) && indices.contains(4) && indices.contains(8)) return true; 
		else if (indices.contains(2) && indices.contains(4) && indices.contains(6)) return true; 
		//Loss or Not Win
		else return false; 
	}
	
	/*
	 * Gets user's choice of which square to put a variable in 
	 * @return – the integer of the square in the game state
	 */
	public int userSpaceChoice() {
		Scanner s = new Scanner(System.in); 
		int userChoice = s.nextInt();
		return userChoice - 1; //accounts for user labeling first square as 1, not 0
	}
	
	/*
	 * Duplicates a state 
	 * @param state – state to be duplicated
	 * @return – new, duplicate state 
	 */
	public char[] duplicateState(char[] state) {
		char[] newState = new char[9];
		for (int i = 0; i < 9; i++) {
			newState[i] = state[i];
		}
		return newState; 
	}

}
