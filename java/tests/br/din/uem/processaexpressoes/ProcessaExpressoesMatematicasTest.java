package br.din.uem.processaexpressoes;

import java.util.ArrayList;
import java.util.List;

import br.uem.din.processaexpressoes.ProcessaExpressoesMatematicas;

public class ProcessaExpressoesMatematicasTest {

	private static List<String> listaExpressoes = new ArrayList<>();
	private static List<Long> listaResultados = new ArrayList<>();
	private static ProcessaExpressoesMatematicas pem = new ProcessaExpressoesMatematicas();
	
	public static void realizaTestes() {
		
		populaListaDeExpressoesEResultados();
		
		for (int i = 0; i < listaExpressoes.size(); i++) {
			
			String expressao = listaExpressoes.get(i);
			Long resultado = pem.processa(expressao);

			assert (listaResultados.get(i).equals(resultado)) : "\n\nPara a expressão '" + expressao + "', o resultado esperado era " + listaResultados.get(i) + " mas veio " + resultado + ".\n\n";
			
		}
		
	}
	
	private static void populaListaDeExpressoesEResultados() {
		
		listaExpressoes.add("1 + 3"); listaResultados.add(4L);
		listaExpressoes.add("1 + 2 * 3"); listaResultados.add(7L);
		listaExpressoes.add("4 / 2 + 7"); listaResultados.add(9L);
		listaExpressoes.add("1 + 2 + 3 * 4"); listaResultados.add(15L);
		listaExpressoes.add("(1 + 2 + 3) * 4"); listaResultados.add(24L);
		listaExpressoes.add("(10 / 3 + 23) * (1 - 4)"); listaResultados.add(-78L);
		listaExpressoes.add("((1 + 3) * 8 + 1) / 3"); listaResultados.add(11L);
		listaExpressoes.add("58 - -8 * (58 + 31) - -14"); listaResultados.add(784L);
		listaExpressoes.add("-71 * (-76 * 91 * (10 - 5 - -82) - -79)"); listaResultados.add(42714523L);
		listaExpressoes.add("10 * 20 + 3 * 7 + 2 * 3 + 10 / 3 * 4"); listaResultados.add(239L);
		listaExpressoes.add("(-13 - -73) * (44 - -78 - 77 + 42 - -32)"); listaResultados.add(7140L);
		listaExpressoes.add("-29 * 49 + 47 - 29 + 74 - -85 - -27 + 4 - 28"); listaResultados.add(-1241L);
		listaExpressoes.add("-74 - -14 + 42 - -4 + -78 + - 50 * -35 * -81 + -41"); listaResultados.add(-141883L);
		listaExpressoes.add("80 * -18 * (85 * (-46 + -71) - 12 + 26 - 59) + 84"); listaResultados.add(14385684L);
		listaExpressoes.add("25 + 38 + 88 + (-6 - -73) * (-83 + (53 + 97) * 14)"); listaResultados.add(135290L);
		listaExpressoes.add("(84 - 90) * (-8 - 75 + -83 * (56 - -77) + 4 + -94)"); listaResultados.add(67272L);
		listaExpressoes.add("(54 - -8 - -35 + -68 - -90) * -39 + -43 + -91 * -30"); listaResultados.add(-1954L);
		listaExpressoes.add("-13 - -74 + (66 + -57) * -93 * -9 * 77 + 79 - 66 + - 53"); listaResultados.add(580062L);
		listaExpressoes.add("(-72 - 50 * -74 + -45) * 92 * 21 * 5 * (-13 - 66 - 18)"); listaResultados.add(-3357342660L);
		listaExpressoes.add("-7 - -37 * (90 + 70) - 30 - -44 + -32 -56 - -48 - -78"); listaResultados.add(5965L);
		listaExpressoes.add("65 * -83 - -3 + -20 + 24 - 85 * (-24 + -32) * (61 - 20)"); listaResultados.add(189772L);
		listaExpressoes.add("55 * 48 * -44 - -32 + 1 * -80 * -94 -74 * -53 + -30 + -61"); listaResultados.add(-104777L);
		listaExpressoes.add("-82 * (25 + 62 + 3) - -72 + -65 * -32 * (77 + 12) - -95 + 51"); listaResultados.add(177958L);
		listaExpressoes.add("(2 - 65 - (-24 + -97) * -5 * -61) * (-41 + 85 * 9 * -92 * (75 - 18))"); listaResultados.add(-147799088242L);
		listaExpressoes.add("-20 + -51 + 20 + -68 * -11 + -35 * -14 -95 - 32 + -52 * -23 - -90 * -42"); listaResultados.add(-1524L);
		listaExpressoes.add("(3 + 4) * 2 - 4"); listaResultados.add(10L);
		listaExpressoes.add("18 / 2 * (1 + 2) ^ 2"); listaResultados.add(81L);
		listaExpressoes.add("18 / 2 ^ 2"); listaResultados.add(4L);
		listaExpressoes.add("14 % 3"); listaResultados.add(2L);
		listaExpressoes.add("10 % 3"); listaResultados.add(1L);
		listaExpressoes.add("32 % (300 % 27)"); listaResultados.add(2L);
		listaExpressoes.add("32129 % (300 % 27) ^ 9"); listaResultados.add(12446L);
		listaExpressoes.add("2 ^ 3"); listaResultados.add(8L);
		
	}
	
}