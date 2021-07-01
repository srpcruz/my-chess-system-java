package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	public ChessMatch() {
		board = new Board(8,8);
		initialSetup();
	}
	
	public ChessPiece[][] getPieces() {
		// criando matriz ChessPiece com a quantidade de linhas e colunad definida no ChessMatch
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		
		// percorrer a matriz para cada item visando Downcast
		// fazendo Downcast de Board(tabuleiro) para ChessPiece que é a partida em si 
		// (tabuleiro e peças do jogo)
		for (int i=0;i<board.getRows();i++) {
			for (int j=0;j<board.getColumns();j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		
		return mat;
	}
	
	private void initialSetup() {
		board.PlacePiece(new Rook(board, Color.WHITE), new Position(2,1));
		board.PlacePiece(new King(board, Color.BLACK), new Position(7,4));

	}
}
