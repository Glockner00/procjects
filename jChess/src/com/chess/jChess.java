package com.chess;
import com.chess.engine.board.Board;
import com.chess.gui.Table;
/**
 * MAIN
 * @author 
 */
public class jChess {
	public static void main(String[] args) {
		Board board = Board.createStandardBoard();
		System.out.println(board);
		Table table = new Table();
	}
}