package lexico;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Leitor {
	private final static int TAMANHO_BUFFER = 20;
	int [] bufferLeitura;
	int ponteiro;
	int bufferAtual;
	int inicioLexema;
	int numeroLinha = 0;
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
		bufferAtual = 2;
		inicioLexema = 0;
		lexema = "";
		bufferLeitura = new int[TAMANHO_BUFFER * 2];
		ponteiro = 0;
		recarregarBuffer1();
	}
	
	private void incrementarPonteiro() {
		ponteiro++;
		if(ponteiro == TAMANHO_BUFFER) {
			recarregarBuffer2();
		}else if(ponteiro == TAMANHO_BUFFER*2) {
			recarregarBuffer1();
			ponteiro = 0;
		}
	}
	
	private void recarregarBuffer1() {
		if(bufferAtual == 2) {
			bufferAtual = 1;
		
			for(int i=0;i<TAMANHO_BUFFER;i++) {
				try {
					bufferLeitura[i] = is.read();
					if(bufferLeitura[i] == -1) {
						break;
					}
				}catch(IOException e) {
					e.printStackTrace();
				}//catch
			}//for
		}//if
	}//recarregarBuffer1
	
	private void recarregarBuffer2() {
		if(bufferAtual == 1) {
			bufferAtual = 2;
		
			for(int i=TAMANHO_BUFFER;i<TAMANHO_BUFFER*2;i++) {
				try {
					bufferLeitura[i] = is.read();
					if(bufferLeitura[i] == -1) {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}//catch
			}//for
		}//if
	}//recarregarBuffer2
	
	private int lerCaractereBuffer() {
		int ret = bufferLeitura[ponteiro];
		incrementarPonteiro();
		
		// Contagem de linhas
		if((char) ret == '\n') {
			contLinhas();
		
		}
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
			ponteiro = TAMANHO_BUFFER*2 -1;
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
		String buffer = "Buffer:[";
		for(int i: bufferLeitura) {
			char c = (char) i;
		
			if( Character.isWhitespace(c)) {
				buffer += ' ';	
			}else {
				buffer += (char) i;
			}
		}
		buffer += "]\n";
		buffer += "        " ;
		for(int i = 0; i<TAMANHO_BUFFER * 2;i++) {
			if(i==inicioLexema && i == ponteiro) {
				buffer += "%";
			}else if (i==inicioLexema) {
				buffer += "^";
			}else if(i==ponteiro) {
				buffer += "*";
			}else {
				buffer += " ";
			}
		}
		return buffer;
	}
	
	public void contLinhas() {
		this.numeroLinha++;
	//	System.out.println(numeroLinha);
	}
	
	
}
