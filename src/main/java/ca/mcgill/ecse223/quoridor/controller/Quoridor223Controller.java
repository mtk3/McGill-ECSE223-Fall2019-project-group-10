package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;

import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Time;
import java.security.InvalidAlgorithmParameterException;

public class Quoridor223Controller {

	private enum side {
		up, down, left, right
	};

	// under feature 1
	public static void createGame() {

	}

	// under feature 2
	public static void selectUser(String playerName1, String playerName2) {

	}

	// under feature 2
	public static void createUser(String name) {

	}

	/**
	 * helper method to get current player
	 * 
	 * @author Andrew Ta
	 * @param playerName
	 * @return
	 */
	public static Player getCurrentPlayer(String playerName) {
		// get current game

		Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
		Player currentPlayer;
		// get currentPlayer
		if (currentGame.getBlackPlayer().getUser().getName().equals(playerName)) {
			currentPlayer = currentGame.getBlackPlayer();
		} else {
			currentPlayer = currentGame.getWhitePlayer();
		}

		return currentPlayer;
	}

	/**
	 * Feature 3: set total thinking time
	 * 
	 * @author Andrew Ta
	 * @param thinkingTime
	 * @param playerName
	 * @throws UnsupportedOperationException
	 */
	public static void setThinkingTime(Time thinkingTime, String playerName) throws UnsupportedOperationException{
		if(!isRunning()) return; //if the game is not running, return
		
		// get current player
		Player currentPlayer = getCurrentPlayer(playerName);

		// set thinking time of that player
		currentPlayer.setRemainingTime(thinkingTime);
	}

	/**
	 * get remaining time
	 * 
	 * @author Andrew Ta
	 * @param playerName
	 * @return Time
	 * @throws UnsupportedOperationException
	 */
	public static Time getThinkingTime(String playerName) throws UnsupportedOperationException{
		if(!isRunning()) return null; //if the game is not running, return
		
		// get current player
		Player currentPlayer = getCurrentPlayer(playerName);

		return currentPlayer.getRemainingTime();
	}

	/**
	 * Feature 4: initialize board
	 * 
	 * @author Andrew Ta
	 * @throws UnsupportedOperationException
	 */
	public static void initializeBoard() throws UnsupportedOperationException{
		// get quoridor object
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Board board = new Board(quoridor);
		
		// add tiles
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
		
		// add user and create players
		User user1 = quoridor.addUser("user1");
		User user2 = quoridor.addUser("user2");
		Player player1 = new Player(new Time(100), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(100), user2, 1, Direction.Horizontal);
		
		// create walls
		for (int i = 0; i < 10; i++) {
			new Wall(0 * 10 + i, player1);
		}
		for (int i = 0; i < 10; i++) {
			new Wall(1 * 10 + i, player2);
		}
		
		// create players' positions
		Tile player1StartPos = quoridor.getBoard().getTile(44);
		Tile player2StartPos = quoridor.getBoard().getTile(36);
		
		// create a game
		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, player1, player2, quoridor);
		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);
		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, player1, game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}

		game.setCurrentPosition(gamePosition);
	}

	/**
	 * return board
	 * 
	 * @author Andrew Ta
	 * @return
	 * @throws UnsupportedOperationException
	 */
	public static Board getBoard() throws UnsupportedOperationException{
		if(!isRunning()) return null;
		
		// get quoridor object
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		
		return quoridor.getBoard();
	}

	// under feature 5
	public static void rotateWall() {

	}

	//under feature 6
	public void grabWall()  throws UnsupportedOperationException{
		//check if the Game is running if not throw exception
		//check if the it is player's turn if not throw exception
		//check if there is no wall in my hand if not throw exception
		//check if there is wall leftover
		//if Player has more wall
		//move the wall into my hand
		//Remove wall from stock
		//create a wallmoveCandidate in the game model.
		//else if player have no wall
		//Notify the user that they don't have any wall.	
	}

	/**
	 * Perform a move operation on a currently selected wall
	 * @author Le-Li Mao
	 * @param side
	 * @throws GameNotRunningException
	 * @throws InvalidOperationException
	 */
	public static void moveWall(String side) throws GameNotRunningException, InvalidOperationException {
		//check if the Game is running if not throw exception
		if(!isRunning())throw new GameNotRunningException("Game not running");
		Game curGame = QuoridorApplication.getQuoridor().getCurrentGame();
		//check if the it is player's turn if not throw exception

		//check if there is wall in my hand if not throw exception
		if(curGame.getWallMoveCandidate()==null)throw new InvalidOperationException("No wall Selected");
		WallMove candidate =  curGame.getWallMoveCandidate();
		//check if newRow and newCol are within the board if not throw exception
		int newRow = candidate.getTargetTile().getRow()+ (side.equalsIgnoreCase("up")?-1:side.equalsIgnoreCase("down")?1:0);
		int newCol = candidate.getTargetTile().getColumn()+ (side.equalsIgnoreCase("left")?-1:side.equalsIgnoreCase("right")?1:0);
		if(!isWallPositionValid(newRow,newCol))throw new InvalidOperationException("Move invalid");
		//update the move candidate according to the change.
		candidate.setTargetTile(getTile(newRow,newCol));
	}

	/**
	 * Perform a drop wall Operation that drop the currently held wall
	 * @author Le-Li Mao
	 * @throws UnsupportedOperationException
	 */
	public static void dropWall() throws GameNotRunningException, InvalidOperationException {
		//check if the Game is running if not throw exception
		if(!isRunning())throw new GameNotRunningException("Game not running");
		Game curGame = QuoridorApplication.getQuoridor().getCurrentGame();
		//check if the it is player's turn if not throw exception
		//HOW to do this??
		//check if there is wall in my hand if not throw exception
		if(curGame.getWallMoveCandidate()==null)throw new InvalidOperationException("No wall Selected");
		//finalize drop by putting the move into the movelist and update the gamePosition TODO.
		curGame.addMove(curGame.getWallMoveCandidate());
		curGame.setWallMoveCandidate(null);
		
		//set my hand as empty and switch turn TODO
	}
	
	// under feature 9
	public void savePosition(String filename) throws UnsupportedOperationException, IOException {

		// check if the Game is running, if not, throw exception
		Game curGame = QuoridorApplication.getCurrentGame();
		if (!isRunning()) {
			throw new UnsupportedOperationException("Game is not running");
		}

		// create a new File instance
		File saveFile = new File(filename);

		// check if the File <filename> exists and is a normal file
		if (!saveFile.exists() && saveFile.isFile()) {
			// make the File writable and then create the new file
			saveFile.setWritable(true);
			if (saveFile.createNewFile() == false)
				throw new IOException("File cannot be created");
		}
		// if the file exists
		else if (saveFile.exists() && saveFile.isFile()) {
			// prompt user to overwrite the current file
			boolean overwrite = userOverwritePrompt(filename);
			if (overwrite == false) {
				return;
			}
		}
		// if file is invalid
		else {
			throw new UnsupportedOperationException("Invalid File");
		}

		// check if the file is writable
		// if(saveFile.canWrite() == false) throw new SecurityException("Cannot modify
		// File");
		GamePosition currentGamePosition = QuoridorApplication.getCurrentGamePosition();

		// if the file is valid and writable, write the current game position to the file
		try {

			FileWriter fileWriter = new FileWriter(filename,false);
			PrintWriter printWriter = new PrintWriter(fileWriter);
 
			// get the player information strings to write to the file
			String WhitePlayer = "W: " + currentGamePosition.getWhitePosition().getTile().getColumn() +
					currentGamePosition.getWhitePosition().getTile().getRow() + ", ";
			// continue with wall list converted to string 
			String BlackPlayer = "B: "; // continue as above

			// if the next player to move is the White Player
			if(currentGamePosition.getPlayerToMove().equals(currentGamePosition.getGame().getWhitePlayer())){
				printWriter.printf("%s\n", WhitePlayer);
				printWriter.printf("%s", BlackPlayer);
			}else {
				printWriter.printf("%s\n",BlackPlayer);
				printWriter.printf("%s", WhitePlayer);
			} 
			printWriter.close();

		} catch (Exception E) {
			E.printStackTrace();
		}
		 

		throw new UnsupportedOperationException("Saving not yet implemented");
	}

	// under feature 10
	public boolean loadPosition(String filename) {
		// check if the Game is running, if it is, throw exception
		Game curGame = QuoridorApplication.getCurrentGame();
		if (isRunning()) {
			throw new UnsupportedOperationException("Game is currently running");
		}

		File loadFile = new File(filename);
		if (!loadFile.isFile() || !loadFile.canRead()) {
			// throw new IOException("Invalid File");
			return false;
		}

		// get the new GamePosition from the loadFile
		GamePosition loadGamePosition = getLoadGamePosition(loadFile);

		// validate the tile position for the current player
		
		/*for(PlayerPosition playerPosition : loadGamePosition.getPlayerPositions()) {
			for(Tile tilePosition : playerPosition) {
			  
				boolean isPositionValid = validatePosition(tilePosition);
				if (isPositionValid == false) {
					//throw new UnsupportedOperationException("Invalid load File");
				}
				return false;  
				
				if(tilePosition.isPlayerWhite()) {
					//set player White to tile Position
				}else if(tilePosition.isPlayerBlack()) {
					//set player Black to tile Position 
				}else if(tilePosition.isPlayerBlackWall()) {
					//set wall in tile position 
				}
						  
				//decrement player Black's wall reserve by 1 } else
				if(tilePosition.isPlayerWhiteWall()) { //set wall in tile position
				//decrement player White's wall reserve by 1
				}
			} 
		}*/
		 

		// load the new GamePosition
		QuoridorApplication.loadNewGamePosition(loadGamePosition);
		return true;
	}

	// under feature 11
	public static void validatePosition(Tile tile) {
		// need parameter to know whether it is a pawn or a wall ?

		// validate pawn position
		// validate wall position
	}

	// under feature 12
	public static void UpdateBoard() {

	}

	/**
	 * @author Le-Li Mao
	 * @return gameIsRunning
	 */
	private static boolean isRunning() {
		Game current = QuoridorApplication.getQuoridor().getCurrentGame();
		if(current == null || current.getGameStatus()!=Game.GameStatus.Running)return false;
		return true;
	}

	/**
	 * Check if wall is valid
	 * @author Le-Li Mao
	 * @param row
	 * @param col
	 * @return
	 */
	private static boolean isWallPositionValid(int row, int col) {
		return (row>0 && col>0 && row<9 && col <9);
	}
	private static Tile getTile(int row, int col){
		Board board = QuoridorApplication.getQuoridor().getBoard();
		return board.getTile((row-1)*9+(col-1));
	}

	private boolean userOverwritePrompt(String filename) {
		// use UI to prompt user to overwrite the existing file filename
		boolean overwriteApproved = false;

		// overwriteApproved = result of user input using the ui;

		return overwriteApproved;
	}

	private GamePosition getLoadGamePosition(File filename) {

		// initialize the new GamePosition
		GamePosition newGamePosition = new GamePosition(0, null, null, null, null);

		try {
			// create a file reader
			// BufferedReader fileReader = new BufferedReader(new FileReader(filename));

			// read the file to load the GamePosition
			// newGamePosition.nextPlayer = fileReader.readLine();
			// newGamePosition.CurrentPlayer = fileReader.readLine();

			// set the current player color by the first entry in the Current player
			// set the next player as the other color

		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}

		return newGamePosition;
	}

}
