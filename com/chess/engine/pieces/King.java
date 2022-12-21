package com.chess.engine.pieces;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

public class King extends Piece{
	public King(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.KING, piecePosition, pieceAlliance);
	}
	private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9};
	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES ) {
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
			if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || 
			   isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
				continue;
			}
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
 					//not the same alliance (enemy piece)
					if(this.pieceAlliance!= pieceAlliance) {
						//add new attacking move
						//passing board, current knight we are on, destinationLocation, and piece atDestination
						legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	@Override
	public String toString() {
		return pieceType.KING.toString();
	}
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		//if current position is in the first column and has a valid move ( valid offset) -> return true.
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 
				|| candidateOffset == 7);	 
	}
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		//if current position is in the second column and has a valid move ( valid offset) -> return true.
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1
				|| candidateOffset == 9);	 
	}
}	