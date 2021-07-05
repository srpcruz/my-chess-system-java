package boardgame;

public abstract class Piece {

	// "protected" fará que metodos e parametros sejam
	// visiveis somente neste metodo, suas subclasses e 
	// pelo package ao qual pertence - boardgame 
	
	protected Position position;
	private Board board;

	// posição inicial de uma peça será NULL
	public Piece(Board board) {
		this.position = null;
		this.board = board;
	}

	protected Board getBoard() {
		return board;
	}
	
	public abstract boolean[][] possibleMoves();
	
	// rook method - metodo concreto executando um metodo abstrato....
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for (int i=0;i<mat.length;i++) {
			for (int j=0;j<mat.length;j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
}
