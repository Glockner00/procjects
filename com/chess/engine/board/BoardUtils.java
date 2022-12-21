package com.chess.engine.board;

/**
 * utility class for useful constants and methods 
 */
public class BoardUtils {
	/**
	 * ArrayList of boolean of size 64
	 */
	//Sets first column to trues
	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);
	public static final boolean[] EIGHTH_COLUMN = initColumn(7);

	//TODO: make method for rows.
	//BOOLEAN CONSTANTS
	public static final boolean[] SECOND_ROW = initRow(8);
	public static final boolean[] SEVENTH_ROW = initRow(48);
	
	//INTEGER CONSTANTS
	public static final int NUM_TILES = 64;
	public static final int NUM_TILES_PER_ROW = 8;

	private BoardUtils(){
		throw new RuntimeException("You cannot instantiate this class!");
	}
	private static boolean[] initRow(int rowNumber) {
		final boolean[] row = new boolean[NUM_TILES];
		do {
			row[rowNumber] = true;
			rowNumber++;
		}while(rowNumber%NUM_TILES_PER_ROW !=0);
		return row;
	}
	private static boolean[] initColumn(int columnNumber) {
		final boolean[] column = new boolean[NUM_TILES];
		do {
			column[columnNumber]=true;
			columnNumber+=NUM_TILES_PER_ROW;
		}while(columnNumber<NUM_TILES);
		return column;
	}
	/**
	 * check if coordinate is in or out of bounds of the chess board
	 * @param coordinate
	 * @return
	 */
	public static boolean isValidTileCoordinate(final int coordinate) {
		return coordinate>=0 && coordinate<NUM_TILES;		
	}
}