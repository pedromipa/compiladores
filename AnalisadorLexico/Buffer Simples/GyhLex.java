package teste_analisador;

public class GyhLex {

	public static void main(String[] args) {
		GyhLexico lex = new GyhLexico(args[0]);
		Token t = null;
		
		while((t = lex.proximoToken()).nome != TipoToken.Fim) {
			System.out.println(t);
		}

	}

}  