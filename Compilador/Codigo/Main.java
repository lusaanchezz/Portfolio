package compilador;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		//Prueba 1 ----> para ver si se guardan los tokens en el array
		//Tokens token = new Tokens();
		//String[] prueba = token.getToken();
		//System.out.println(prueba[5]);

		//Prueba 2 ----> Prueba de lectura de fichero
		//EntradaSalida entrada = new EntradaSalida();
		//System.out.print(entrada.leer());
		//System.out.print(entrada.leer());ç

		//Prueba 3 ----> Prueba de escritura en fichero
		//File ficheroSalida = new File("C:\\users\\manuel martínez cano\\OneDrive\\Escritorio\\Salida.txt");
		//EntradaSalida salida = new EntradaSalida();
		//salida.escribir(ficheroSalida, 'a');
		//salida.cerrarDescriptor();

		//PPrueba 4 ----> Prueba correspondiente
		Lexico leeTodo = new Lexico();
		leeTodo.correspondiente();


		//Prueba 5 ----> Longitud archivo
		//File ficheroEntrada = new File("C:\\users\\manuel martínez cano\\OneDrive\\Escritorio\\Entrada.txt");

		//Prueba 6 ----> caracteresSeparados
		//  System.out.println("nocabe " + entrada.caracteresSeparados(' '));
		
		//Prueba 7 ----> Consecuente.
		//Sintactico sintactico = new Sintactico();
		//System.out.println(sintactico.consecuente("P ~ D P"));
		
		//Prueba 8 ------>
		/*TablaSimbolos TS = new TablaSimbolos();
		TS.crearTS();
		TS.insertarLexema("Prueba1");
		System.out.println(TS.buscaPosicion("Prueba1"));
		TS.insertarLexema("Prueba2");
		System.out.println(TS.buscaPosicion("Prueba2"));
		TS.insertarLexema("Prueba3");
		System.out.println(TS.buscaPosicion("Prueba3"));
		TS.insertarTipo("ent");
		System.out.println(TS.buscaTipo(TS.buscaPosicion("Prueba1")));
		TS.insertarTipo("logico");
		System.out.println(TS.buscaTipo(TS.buscaPosicion("Prueba2")));
		TS.insertarTipo("cad");
		System.out.println(TS.buscaTipo(TS.buscaPosicion("Prueba3")));
		TS.insertarDesplazamiento(1);
		System.out.println(TS.buscarDesplazamiento(TS.buscaPosicion("Prueba1")));
		TS.insertarDesplazamiento(1);
		System.out.println(TS.buscarDesplazamiento(TS.buscaPosicion("Prueba2")));
		TS.insertarDesplazamiento(64);
		System.out.println(TS.buscarDesplazamiento(TS.buscaPosicion("Prueba3")));
		TS.insertarNumParam(3);
		System.out.println(TS.buscarNumeroParametros(TS.buscaPosicion("Prueba1")));
		TS.insertarNumParam(2);
		System.out.println(TS.buscarNumeroParametros(TS.buscaPosicion("Prueba2")));
		TS.insertarNumParam(1);
		System.out.println(TS.buscarNumeroParametros(TS.buscaPosicion("Prueba3")));
		TS.insertarTipoParametros("ent, logico, cad");
		System.out.println(TS.buscarTipoParametros(TS.buscaPosicion("Prueba1")));
		TS.insertarTipoParametros("ent, cad");
		System.out.println(TS.buscarTipoParametros(TS.buscaPosicion("Prueba2")));
		TS.insertarTipoParametros("logico");
		System.out.println(TS.buscarTipoParametros(TS.buscaPosicion("Prueba3")));
		TS.insertarTipoRet("cad");
		System.out.println(TS.buscarTipoRetorno(TS.buscaPosicion("Prueba1")));
		TS.insertarTipoRet("ent");
		System.out.println(TS.buscarTipoRetorno(TS.buscaPosicion("Prueba2")));
		TS.insertarTipoRet("logico");
		System.out.println(TS.buscarTipoRetorno(TS.buscaPosicion("Prueba3")));
		TS.crearTS("Tablilla");
		TS.insertarLexema("Prueba4");
		System.out.println("Prueba 4 Local ---->" +  TS.buscaPosicion("Prueba4"));		
		TS.destruirTS("Tablilla");
		TS.insertarLexema("Prueba4");
		System.out.println("Prueba 4 Global ---->" +  TS.buscaPosicion("Prueba4"));*/

		/*Sintactico s = new Sintactico();
		s.consecuente("45 F ~ function id H 45.1 ( G ) 45.2 { I } 45.3 45.4", "logico");*/
	}
}