package chess;

import boardgame.Board;

public class ChessMatch {

	private Board board;
	
	public ChessMatch() {
		board = new Board(8,8);
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
}
