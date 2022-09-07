 package lexico;

public class Token {
	public TipoToken nome;
	public String lexema;
	
	public Token(TipoToken nome, String lexema) {
		this.nome = nome;
		this.lexema = lexema;
	}
	
	// Método para retornar o lexema lido com o Token atribuido
	@Override
	public String toString() {
		return "<"+nome+","+lexema+">";
	}
}
