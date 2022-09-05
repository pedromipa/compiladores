package teste_analisador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Leitor {
	private final static int TAMANHO_BUFFER = 100;
	int[] bufferLeitura;
	int ponteiro;
	int inicioLexema;
	private String lexema;
	InputStream is; 
	
	public Leitor(String arquivo) {
		try {
			is = new FileInputStream(new File(arquivo));
			inicializarBuffer();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void inicializarBuffer() {
		inicioLexema = 0;
		lexema = "";
		bufferLeitura = new int[TAMANHO_BUFFER];
		ponteiro = 0;
		recarregarBuffer();
	}
	
	private void incrementarPonteiro() {
		ponteiro++;
		
		if(ponteiro >= TAMANHO_BUFFER) {
			recarregarBuffer();
			ponteiro=0;
		}
	}
	
	private void recarregarBuffer() {
		for(int i=0;i<TAMANHO_BUFFER;i++) {
			try {
				bufferLeitura[i] = is.read();
				
				if(bufferLeitura[i] == -1) {
					break;
				}
			} catch (IOException e) {
				System.out.checkError();
				e.printStackTrace();
			}//catch
		}//for
		}//recarregarBuffer1
		
	private int lerCaractereBuffer() {
		int ret = bufferLeitura[ponteiro];
		incrementarPonteiro();
		return ret;
	}
	
	public int lerProximoCaractere() {
		int c = lerCaractereBuffer();
		lexema += (char)c;
		return c;
	}
	
	public void retroceder() {
		ponteiro--;
		lexema = lexema.substring(0,lexema.length()-1);
		if(ponteiro<0) {
			ponteiro = 0;
			//ponteiro = TAMANHO_BUFFER -1;
		}
	}
	
	public void zerar() {
		ponteiro = inicioLexema;
		lexema = "";
	}
	
	public void confirmar() {
		inicioLexema = ponteiro;
		lexema = "";
	}

	public String getLexema() {
		return lexema;
	}
		
	@Override
	public String toString() {
		String ret = "Buffer:[";
		for(int i: bufferLeitura) {
			char c = (char) i;
		
			if( Character.isWhitespace(c)) {
				ret += ' ';	
			}else {
				ret += (char) i;
			}
		}
		ret += "]\n";
		ret += "        " ;
		for(int i = 0; i<TAMANHO_BUFFER;i++) {
			if(i==inicioLexema && i == ponteiro) {
				ret += "%";
			}else if (i==inicioLexema) {
				ret += "^";
			}else if(i==ponteiro) {
				ret += "*";
			}else {
				ret += " ";
			}
		}
		return ret;
	}
}
