package boardgame;

public class Piece {

	// "protected" far� que metodos e parametros sejam
	// visiveis somente neste metodo, suas subclasses e 
	// pelo package ao qual pertence - boardgame 
	
	protected Position position;
	private Board board;

	// posi��o inicial de uma pe�a ser� NULL
	public Piece(Board board) {
		this.position = null;
		this.board = board;
	}

	protected Board getBoard() {
		return board;
	}
	
	
}
