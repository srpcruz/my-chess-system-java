package boardgame;

public class Piece {

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
	
	
}
