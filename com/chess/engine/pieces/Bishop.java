package com.chess.engine.pieces;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;

public class Bishop extends Piece{
	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -7, 7, 9 };

	Bishop(int piecePosition, Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance);
	}
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();		
		//looping through each vector
		for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES) {
			//we start at our current position
			int candidateDestinationCoordinate = this.piecePosition;
			//is that position valid, have we gone of the edge of the board? 
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)
				   || isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
					break;
				}
				//apply the offset to the current position.
				candidateDestinationCoordinate += candidateCoordinateOffset;
				//is that a valid position?
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					//get the tile of the board of that candidateDestinationCoordinate
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					//destination tile not occupied
					if(!candidateDestinationTile.isTileOccupied()) {
						// add a new non-attacking move 
						//passing board, current knight we are on, destinationLocation
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					//is occupied
					} else {
						final Piece pieceAtDestination = candidateDestinationTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
						//check alliance (is enemy piece ?)
						if(this.pieceAlliance!= pieceAlliance) {
							//add new attacking move
							//passing board, current knight we are on, destinationLocation, and piece atDestination
							legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						}
						//when we find an enemy occupied tile, we break, because there is no more legal moves in that vector. Otherwise we keep sliding. 
						break;
					}
				}
			}
		}		
		return ImmutableList.copyOf(legalMoves);
	}
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
	}
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
	}
}