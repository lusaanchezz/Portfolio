package compilador;

public class Tokens {

	private String[] token;
	private String[] palRes;

	public Tokens() {
		token = new String[25];
		palRes = new String[11];

		token[0] = "bool"; //boolean
		token[1] = "for";
		token[2] = "func"; //function
		token[3] = "if";
		token[4] = "get";
		token[5] = "int";
		token[6] = "let";
		token[7] = "put";
		token[8] = "return";
		token[9] = "string";
		token[10] = "void";
		token[11] = "ad"; //--
		token[12] = "d"; //numero
		token[13] = "cad"; //cadena
		token[14] = "id"; //identificador
		token[15] = "ig"; //=
		token[16] = "coma";
		token[17] = "pc"; //;
		token[18] = "pa"; //(
		token[19] = "p"; //)
		token[20] = "la"; //{
		token[21] = "lc"; //}	
		token[22] = "res"; //-
		token[23] = "and"; //&
		token[24] = "may"; //>

		for(int i = 0; i < 25; i++) {
			if(i >= 0 && i <= 10) {
				palRes[i] = token[i]; //Palabras reservadas del lenguaje
			}
		}
	}

	public String[] getToken() {
		return token;
	}
}
