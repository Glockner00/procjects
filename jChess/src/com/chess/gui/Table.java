package com.chess.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;
public class Table {
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;
	private boolean highLightLegalMoves;
	
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
	private static final String defaultPieceImagesPath = "art1/";
	
	private final Color lightTileColor = Color.decode("#FFFACD");
	private final Color darkTileColor = Color.decode("#593E1A");
	public Table() {
		this.gameFrame = new JFrame("JChess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar); 
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.chessBoard = Board.createStandardBoard();
		this.boardDirection = BoardDirection.NORMAL;
		this.highLightLegalMoves = false;
		
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
	}
	private JMenuBar createTableMenuBar() { 
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		return tableMenuBar;
	}
	private JMenu createFileMenu() {

		final JMenu fileMenu = new JMenu("File");
		//open a game from a PGN file, someone from the internet, for loading prior games.
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Open PGN File");
			}
		});
		
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		
		
		fileMenu.add(openPGN);
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}
	
	private JMenu createPreferencesMenu() {
		final JMenu preferenceMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(chessBoard);
			}
		});
		preferenceMenu.add(flipBoardMenuItem);
		preferenceMenu.addSeparator();
		
		final JCheckBoxMenuItem legalMoveHighLighterCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
		legalMoveHighLighterCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				highLightLegalMoves = legalMoveHighLighterCheckBox.isSelected();
				
			}
			
		});
		
		preferenceMenu.add(legalMoveHighLighterCheckBox);
		return preferenceMenu;
	}
	
	public enum BoardDirection{
		NORMAL{
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles){
				return boardTiles;
			}
			
			@Override
			BoardDirection opposite() {
				return FLIPPED;
			}
		},
		FLIPPED{
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles){
				return Lists.reverse(boardTiles);
			}
			@Override
			BoardDirection opposite() {
				return NORMAL;
			}
		};
		
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		abstract BoardDirection opposite();
		
		
		}

	private class BoardPanel extends JPanel {

		final List<TilePanel> boardTiles;
		BoardPanel(){
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			for(int i=0; i<BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
		public void drawBoard(final Board board) {
			removeAll();
			for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();
			
		}
	}
	
	public static class MoveLog{
		private final List<Move> moves;
		MoveLog(){
			moves = new ArrayList<>();
		}
		public List<Move> getMoves(){
			return this.moves;
		}
		
		public void addMove(final Move move) {
			this.moves.add(move);
		}
		
		public int size() {
			return this.moves.size();
		}
		
		public void clear() {
			this.moves.clear();
		}
		
		public Move removeMove(int index) {
			return this.moves.remove(index);
		}
		
		public boolean removeMove(final Move move) {
			 return this.moves.remove(move);
		}
	}
	private class TilePanel extends JPanel{

		private final int tileId;	
		TilePanel(final BoardPanel boardPanel,
				 final int tileId){	
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilPieceIcon(chessBoard);
			addMouseListener(new MouseListener() {

				
				//click -> get that tileId and the tile from that chess board and assign to our sourceTile. 
				@Override
				public void mouseClicked(final MouseEvent e) {
					if(javax.swing.SwingUtilities.isRightMouseButton(e)) {
						//undo all selections.
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
					}else if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
						//no prior selection of a sourcetile.
						if(sourceTile == null) {
							sourceTile = chessBoard.getTile(tileId);
							humanMovedPiece = sourceTile.getPiece();
							//if there is not a piece on the selected tile.
							if(humanMovedPiece==null) {
								sourceTile = null;
							}
						}else {
							destinationTile = chessBoard.getTile(tileId);
							//get a legal move
							final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
							final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
							if(transition.getMoveStatus().isDone()) {
								chessBoard = transition.getTransitionBoard();
								//TODO moveLog.
							}
							//blank everything out.
							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
						}
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								boardPanel.drawBoard(chessBoard);
							}
						});
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			validate();
		}
		
		public void drawTile(final Board board) {
			assignTileColor();
			assignTilPieceIcon(board);
			highLightLegals(board);
			validate();
			repaint();
		}
		
		public void assignTilPieceIcon(final Board board) {
			this.removeAll();
			if(board.getTile(tileId).isTileOccupied()) {
				try {
					//example: white bishop -> W + B + .gif -> WB.gif.
					final BufferedImage image = 
					ImageIO.read(new File(defaultPieceImagesPath  + board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0,1) +
							board.getTile(this.tileId).toString() + ".png"));
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		private void highLightLegals(final Board board) {
			if(highLightLegalMoves) {
				for(final Move move : pieceLegalMoves(board)) {
					if(move.getDestinationCoordinate() == this.tileId) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("art/green_dot.png")))));
							
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		private Collection<Move> pieceLegalMoves(final Board board){
			if(humanMovedPiece!=null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
				return humanMovedPiece.calculateLegalMoves(board);
			}
			return Collections.emptyList();
		}
		
		
		private void assignTileColor() {
			if(BoardUtils.EIGHTH_RANK[this.tileId]||
			   BoardUtils.SIXTH_RANK[this.tileId]||
			   BoardUtils.FOURTH_RANK[this.tileId]||
			   BoardUtils.SECOND_RANK[this.tileId]) {
			   setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
			}else if(BoardUtils.SEVENTH_RANK[this.tileId]||
					 BoardUtils.FIFTH_RANK[this.tileId]||
				 	 BoardUtils.THIRD_RANK[this.tileId]||
					 BoardUtils.FIRST_RANK[this.tileId]) {
					 setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
			}
		}
	}
}