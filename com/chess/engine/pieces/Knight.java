package com.chess.engine.pieces;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Knight extends Piece {
	/*
	 * all legal moves for a knight
	 */
	private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10 ,-6, 6, 10, 15, 17};
	Knight(final int piecePosition, final Alliance pieceAlliance) {
		super(piecePosition, pieceAlliance);
	}
		@Override
		public Collection<Move> calculateLegalMoves(final Board board) {
			final List<Move> legalMoves = new ArrayList<>();
			for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
				//Applying the offset to the current position
				final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || 
							isSecondColumnExclusion(this.piecePosition, currentCandidateOffset)|| 
							isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)|| 
							isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
						continue;
					}
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
		private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
			//if current position is in the first column and has a valid move ( valid offset) -> return true.
			return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 
					|| candidateOffset == 6 || candidateOffset == 15);	 
		}
		private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
			//if current position is in the second column and has a valid move ( valid offset) -> return true.
			return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);	 
		}
		private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
			//if current position is in the seventh column and has a valid move ( valid offset) -> return true.
			return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);	 
		}
		private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
			//if current position is in the eighth column and has a valid move ( valid offset) -> return true.
			return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 || 
					candidateOffset == 10 || candidateOffset == 17);	 	 
		}
	}