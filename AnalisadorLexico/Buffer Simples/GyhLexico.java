package teste_analisador;

public class GyhLexico {
	Leitor ldat;
	
	public GyhLexico(String arquivo) {
		ldat = new Leitor(arquivo);
	}
	

	
	public Token proximoToken() {
		Token proximo = null;
		
		espacosEComents();
		ldat.confirmar();
		
		proximo = fim();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = palavrasChave();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = operadorAritmetico();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = operadorRelacional();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = booleanos();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = delimitador();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = atribuicao();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = parentesis();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = variaveis();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = numeros();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		proximo = cadeia();
		if(proximo == null) {
			ldat.zerar();
		}else {
			ldat.confirmar();
			return proximo;
		}
		
		
		System.err.println("Erro Léxico!" ); //imprimir linha interia que deu erro
		
		System.err.println("Linha: "+ ldat.numeroLinha + "\n" + ldat.toString());
		return null;
	}//proximo token
	
	private Token operadorAritmetico() {
		int caractereLido = ldat.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		
		case '*': return new Token(TipoToken.OPAritMult,ldat.getLexema());
		case '/': return new Token(TipoToken.OpAritDiv,ldat.getLexema());
		case '+': return new Token(TipoToken.OpAritSoma,ldat.getLexema());
		case '-': return new Token(TipoToken.OpAritSub,ldat.getLexema());
		default: return null;
		}//switch
	}//operadorAritmetico
	
	private Token delimitador() {
		int caractereLido = ldat.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		case ':': 
			c = (char) ldat.lerProximoCaractere();
			if(c =='=') {
				return new Token(TipoToken.Atrib,ldat.getLexema());
			}else {
				ldat.retroceder();
				return new Token(TipoToken.Delim,ldat.getLexema());
			}
		default: return null;
		}//switch
	}//delimitador
	
	private Token operadorRelacional() {
		int caractereLido = ldat.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		
		case '<':
			c = (char) ldat.lerProximoCaractere();
			if(c=='=') {
				return new Token(TipoToken.OpRelMenorIgual,ldat.getLexema());
			}else {
				ldat.retroceder();
				return new Token(TipoToken.OpRelMenor,ldat.getLexema());
			}
		
		case '>':
			c = (char) ldat.lerProximoCaractere();
			if(c=='=') {
				return new Token(TipoToken.OpRelMaiorIgual,ldat.getLexema());
			}else {
				ldat.retroceder();
				return new Token(TipoToken.OpRelMaior,ldat.getLexema());
			}
		
		case '=':
			c = (char) ldat.lerProximoCaractere();
			if(c == '=') {
				return new Token(TipoToken.OpRelIgual,ldat.getLexema());
			}else {
				return null;
			}
		
		case '!':
			c = (char) ldat.lerProximoCaractere();
			if(c=='=') {
				return new Token(TipoToken.OpRelDif,ldat.getLexema());
			}else {
				return null;
			}

		default: return null;

		}//switch
	}//operadorRelacional
	
	
	private Token parentesis() {
		int caractereLido = ldat.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		case '(': return new Token(TipoToken.AbrePar,ldat.getLexema());
		case ')': return new Token(TipoToken.FechaPar,ldat.getLexema());
		default: return null;
		}//switch
	}//parentesis
	
	private Token atribuicao() {
		int caractereLido = ldat.lerProximoCaractere();
		char c = (char) caractereLido;
		
		switch(c) {
		case ':':
			c = (char) ldat.lerProximoCaractere();
			if(c == '=') {
				return new Token(TipoToken.Atrib,ldat.getLexema());
			}else {
				return null;
			}
		default: return null;
		}//switch
	}//atribuicao
	
	
	private Token numeros() {
		int estado = 1;
		
			while(true) {
				char c = (char) ldat.lerProximoCaractere();
				if(estado == 1) {
					if(Character.isDigit(c)) {
						estado = 2;
					}else {
						return null;
					}
				}else if(estado == 2) {
					if(c=='.') {
						c = (char) ldat.lerProximoCaractere();
						if(Character.isDigit(c)) {
						estado = 3;
					}else {
						return null;
					}
				}else if(!Character.isDigit(c)) {
					ldat.retroceder();
					return new Token(TipoToken.NumInt,ldat.getLexema());
					}
				}else if(estado == 3) {
					if(!Character.isDigit(c)) {
					ldat.retroceder();
					return new Token(TipoToken.NumReal,ldat.getLexema());
					}//if interno
				}//if externo
			}//while
	}//numeros
	
	private Token variaveis() {
		int estado = 1;
		int aux = 0;
		
		while(true) {
			char c = (char) ldat.lerProximoCaractere();
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
					ldat.retroceder();
					return new Token(TipoToken.Var,ldat.getLexema());
				}
			}
		}//while
	}//variaveis
	
	
	private Token cadeia() {
		int estado = 1;
		while(true) {
			char c = (char) ldat.lerProximoCaractere();
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
					return new Token(TipoToken.Cadeia,ldat.getLexema());
				}
			}
		}//while
	}//cadeia
	
	private void espacosEComents() {
		int estado = 1;
		while(true) {
			char c = (char) ldat.lerProximoCaractere();
			
			
			if(estado == 1) {
				if(Character.isWhitespace(c)||c== ' ') {
					estado = 2;
				}else if(c == '#') {
					estado = 3;
				}else {
					ldat.retroceder();
					return;
				}
			}else if(estado == 2) {
				if(c == '#') {
					estado = 3;
				}else if(!(Character.isWhitespace(c)||c==' ')) {
					ldat.retroceder();
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
			char c = (char) ldat.lerProximoCaractere();
			if(!Character.isLetter(c)) {
				ldat.retroceder();
				String lexema = ldat.getLexema();
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
			char c = (char) ldat.lerProximoCaractere();
			if(!Character.isLetter(c)) {
				ldat.retroceder();
				String lexema = ldat.getLexema();
				
				switch(lexema) {
				case "E": return new Token(TipoToken.OpBoolE,lexema);
				case "OU": return new Token(TipoToken.OpBoolOu,lexema);
				default: return null;
				}
			}
		}//while
	}//booleanos
	
	private Token fim() {
		int caractereLido = ldat.lerProximoCaractere();
		if(caractereLido == -1) {
			return new Token(TipoToken.Fim,"Fim");
		}
		return null;
	}

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