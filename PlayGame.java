public class PlayGame {

	//Main Method
		public static void main(String argv[]) {
			
			//Initializes Tic Tac Toe Game
			TicTacToe game = new TicTacToe();
			//Game Instructions for User
			System.out.println("TIC TAC TOE");
			System.out.println("You play with Xs, the computer plays with Os");
			System.out.println("Type where you wish to place an X. The spots are numbered 1-9 in row-major order");
			System.out.println("Make your move: ");
			System.out.println();
			//Begins Game
			game.miniMax();		
			
		}
}
