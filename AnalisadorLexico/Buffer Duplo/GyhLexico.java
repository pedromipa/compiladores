package lexico;

public class GyhLexico {
	Leitor leitor;
	
	public GyhLexico(String arquivo) {
		leitor = new Leitor(arquivo);
	}

	// Verifica qual categoria o lexema pertence
	public Token proximoToken() {
		Token proximo = null;
		
		espacosEComents();
		leitor.confirmar();
		
		proximo = fim();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = palavrasChave();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = operadorAritmetico();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = operadorRelacional();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = booleanos();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = delimitador();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = atribuicao();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = parenteses();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = variaveis();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = numeros();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		proximo = cadeia();
		if(proximo == null) {
			leitor.zerar();
		}else {
			leitor.confirmar();
			return proximo;
		}
		
		// Caso o lexema não pertença a nenhuma categoria, há erro léxico
		System.err.println("Erro Léxico!" ); 
		System.err.println("Linha: "+ leitor.numeroLinha + "\n" + leitor.toString());
		return null;
	}//proximo token
	
	// ======================================================================
	//                   Automatos para cada categoria
	// ======================================================================
	
	private Token operadorAritmetico() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		
		case '*': return new Token(TipoToken.OPAritMult,leitor.getLexema());
		case '/': return new Token(TipoToken.OpAritDiv,leitor.getLexema());
		case '+': return new Token(TipoToken.OpAritSoma,leitor.getLexema());
		case '-': return new Token(TipoToken.OpAritSub,leitor.getLexema());
		default: return null;
		}//switch
	}//operadorAritmetico
	
	private Token delimitador() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		case ':': 
			c = (char) leitor.lerProximoCaractere();
			if(c =='=') {
				return new Token(TipoToken.Atrib,leitor.getLexema());
			}else {
				leitor.retroceder();
				return new Token(TipoToken.Delim,leitor.getLexema());
			}
		default: return null;
		}//switch
	}//delimitador
	
	private Token operadorRelacional() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		
		case '<':
			c = (char) leitor.lerProximoCaractere();
			if(c=='=') {
				return new Token(TipoToken.OpRelMenorIgual,leitor.getLexema());
			}else {
				leitor.retroceder();
				return new Token(TipoToken.OpRelMenor,leitor.getLexema());
			}
		
		case '>':
			c = (char) leitor.lerProximoCaractere();
			if(c=='=') {
				return new Token(TipoToken.OpRelMaiorIgual,leitor.getLexema());
			}else {
				leitor.retroceder();
				return new Token(TipoToken.OpRelMaior,leitor.getLexema());
			}
		
		case '=':
			c = (char) leitor.lerProximoCaractere();
			if(c == '=') {
				return new Token(TipoToken.OpRelIgual,leitor.getLexema());
			}else {
				return null;
			}
		
		case '!':
			c = (char) leitor.lerProximoCaractere();
			if(c=='=') {
				return new Token(TipoToken.OpRelDif,leitor.getLexema());
			}else {
				return null;
			}

		default: return null;

		}//switch
	}//operadorRelacional
	
	
	private Token parenteses() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		case '(': return new Token(TipoToken.AbrePar,leitor.getLexema());
		case ')': return new Token(TipoToken.FechaPar,leitor.getLexema());
		default: return null;
		}//switch
	}//parentesis
	
	private Token atribuicao() {
		int caractereLido = leitor.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		case ':':
			c = (char) leitor.lerProximoCaractere();
			if(c == '=') {
				return new Token(TipoToken.Atrib,leitor.getLexema());
			}else {
				return null;
			}
		default: return null;
		}//switch
	}//atribuicao
	
	
	private Token numeros() {
		int estado = 1;
		
			while(true) {
				char c = (char) leitor.lerProximoCaractere();
				if(estado == 1) {
					if(Character.isDigit(c)) {
						estado = 2;
					}else {
						return null;
					}
				}else if(estado == 2) {
					if(c=='.') {
						c = (char) leitor.lerProximoCaractere();
						if(Character.isDigit(c)) {
						estado = 3;
					}else {
						return null;
					}
				}else if(!Character.isDigit(c)) {
					leitor.retroceder();
					return new Token(TipoToken.NumInt,leitor.getLexema());
					}
				}else if(estado == 3) {
					if(!Character.isDigit(c)) {
					leitor.retroceder();
					return new Token(TipoToken.NumReal,leitor.getLexema());
					}//if interno
				}//if externo
			}//while
	}//numeros
	
	private Token variaveis() {
		int estado = 1;
		int aux = 0;
		
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			aux++;
			
			if(estado == 1) {
				if(Character.isLetter(c) && aux == 1 && Character.isUpperCase(c) ) { 
					return null;
				}else if(Character.isLetter(c)){
					estado = 2;					
				}else {
					return null;
				}
			}else if(estado == 2) {
				if(!Character.isLetterOrDigit(c)) {
					leitor.retroceder();
					return new Token(TipoToken.Var,leitor.getLexema());
				}
			}
		}//while
	}//variaveis
	
	
	private Token cadeia() {
		int estado = 1;
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			if(estado == 1) {
				if(c == '"') {
					estado = 2;
				}else {
					return null;
				}
			}else if(estado == 2) {
				if(c=='\n') {
					return null;
				}
				if(c=='"') {
					return new Token(TipoToken.Cadeia,leitor.getLexema());
				}
			}
		}//while
	}//cadeia
	
	private void espacosEComents() {
		int estado = 1;
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			
			
			if(estado == 1) {
				if(Character.isWhitespace(c)||c== ' ') {
					estado = 2;
				}else if(c == '#') {
					estado = 3;
				}else {
					leitor.retroceder();
					return;
				}
			}else if(estado == 2) {
				if(c == '#') {
					estado = 3;
				}else if(!(Character.isWhitespace(c)||c==' ')) {
					leitor.retroceder();
					return;
				}
			}else if(estado == 3) {
				if(c == '\n') {
					estado = 2;
				}
			}
		}//while
	}//espacosEComents
	
	private Token palavrasChave() {
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			if(!Character.isLetter(c)) {
				leitor.retroceder();
				String lexema = leitor.getLexema();
				switch(lexema) {
				case "DEC": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCDec,lexema);						
					}
				case "PROG": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCProg,lexema);
					}
				case "INT": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCInt,lexema);
					}
				case "REAL": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCReal,lexema);
					}
				case "LER": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCLer,lexema);
					}
				case "IMPRIMIR":
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCImprimir,lexema);
					}
				case "SE":
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCSe,lexema);
					}
				case "SENAO": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCSenao,lexema);
					}
				case "ENTAO": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCEntao,lexema);
					}
				case "ENQTO": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCEnqto,lexema);
					}
				case "INI": 
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCIni,lexema);
					}
				case "FIM":
					if(maiusculas(lexema)==true) {
						return new Token(TipoToken.PCFim,lexema);
					}
				default: return null;
				}
			}
		}//while
	}//palavrasChave
	
	private Token booleanos() {
		while(true) {
			char c = (char) leitor.lerProximoCaractere();
			if(!Character.isLetter(c)) {
				leitor.retroceder();
				String lexema = leitor.getLexema();
				
				switch(lexema) {
				case "E": return new Token(TipoToken.OpBoolE,lexema);
				case "OU": return new Token(TipoToken.OpBoolOu,lexema);
				default: return null;
				}
			}
		}//while
	}//booleanos
	

	// Token para verificar o fim do arquivo
	// A condição de parada do programa depende do retorno deste Token
	private Token fim() {
		int caractereLido = leitor.lerProximoCaractere();
		if(caractereLido == -1) {
			return new Token(TipoToken.Fim,"Fim");
		}
		return null;
	}

	// Método para verificar se todas as letras das palavras chave são maiúsculas
	private boolean maiusculas(String teste) {
		for(int i = 0; i < teste.length(); i++) {
			char c = teste.charAt(i);
			
			if(Character.isLowerCase(c)) {
				return false;
			}
			
		}//for
		
		return true;
	}

}