package br.uem.din.processaexpressoes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessaExpressoesMatematicas {

	private String expressaoMatematica;
	private Integer indiceFinalDeAvaliacao = 0;
	private Integer indiceInicialDeAvaliacao = 0;
	
	private List<String> tokens;
	
	
	public Long processa(String expressaoMatematica) {
		
		tokens = new ArrayList<>();
		this.expressaoMatematica = expressaoMatematica;
		
		System.out.println("> " + this.expressaoMatematica);
		
		quebrarEmTokens();
		validaEstruturaDeParenteses();
		processaExpressoes();
		
		return Long.parseLong(tokens.get(0));

	}

	private void quebrarEmTokens() {

		String patron = "(?<token>[\\(]|\\d+|[-+\\*/%^]|[\\)])";
		Pattern pattern = Pattern.compile(patron);
		Matcher matcher = pattern.matcher(this.expressaoMatematica);

		String token;

		while (matcher.find()) {
			token = matcher.group("token");
			tokens.add(token);
		}

		//Tratando quando há dois operadores consecutivos
		int contOperator = 0;
		for (int i = 0; i < tokens.size(); i++) {

			if (tokenEhOperador(tokens.get(i))) {
				contOperator++;
			} else {
				contOperator = 0;
			}

			if ((i == 0 || tokens.get((i - 1)).equals("(")) && tokenEhOperador(tokens.get(i))) {
				contOperator = 2;
			}

			if (tokens.get(i).equals("--") || tokens.get(i).equals("+-") || tokens.get(i).equals("-+") || tokens.get(i).equals("++")) {
				contOperator = 2;
			}

			if (contOperator == 2) {
				contOperator = 0;
				String proxToken = tokens.get(i) + tokens.get((i + 1));
				tokens.set((i + 1), proxToken);
				tokens.set(i, "");
			}

		}

	}
	
	private void processaExpressoes() {
		
		Integer qtdeOps = qtdeOperacoesASeremRealizadas();
		
		for (int  i = 0; i < qtdeOps; i++) {
			
			removerCamposEmBranco();

			atualizaIntervaloIndicesASeremAvaliados();
			
			processaDentroDosIndicesDeAvaliacao();

			removerCamposEmBranco();
			
			String expressaoParcial = tokens.toString().replaceAll(",", "");
			
			System.out.println(expressaoParcial.substring(1, expressaoParcial.length() - 1).trim());
			
		}
		
		System.out.println("-------------------------------------------------------------------");
		
	}
	
	private void processaDentroDosIndicesDeAvaliacao() {
		
		Integer indiceDoOperador = procuraExponenciacao();
		
		if (indiceDoOperador.equals(-1)) {
			indiceDoOperador = procuraMultiplicacaoOuDivisaoOuModulo();
		}

		if (indiceDoOperador.equals(-1)) {
			indiceDoOperador = procuraSomaOuSubtracao();
		}
		
		realizaOperacaoEAtualizaExpressao(tokens.get(indiceDoOperador - 1), 
                						  tokens.get(indiceDoOperador), 
                						  tokens.get(indiceDoOperador + 1),
                						  indiceDoOperador);
		
		
	}

	private Integer procuraSomaOuSubtracao() {

		for (int i = indiceInicialDeAvaliacao; i <= indiceFinalDeAvaliacao; i++) {
			
			if (tokens.get(i).equals("+") || tokens.get(i).equals("-")) {
				return i;
			}
			
		}
		
		return -1;
		
	}

	private Integer procuraExponenciacao() {

		for (int i = indiceInicialDeAvaliacao; i <= indiceFinalDeAvaliacao; i++) {
			
			if (tokens.get(i).equals("^")) {
				return i;
			}
			
		}
		
		return -1;
		
	}

	private Integer procuraMultiplicacaoOuDivisaoOuModulo() {

		for (int i = indiceInicialDeAvaliacao; i <= indiceFinalDeAvaliacao; i++) {
			
			if (tokens.get(i).equals("*") || tokens.get(i).equals("/") || tokens.get(i).equals("%")) {
				return i;
			}
			
		}
		
		return -1;
	}

	private void atualizaIntervaloIndicesASeremAvaliados() {

		indiceInicialDeAvaliacao = 0;
		indiceFinalDeAvaliacao = (tokens.size() - 1);
		
		for (int i = 0; i < tokens.size(); i++) {
			
			if (tokens.get(i).equals("(")) {
				indiceInicialDeAvaliacao = i;
				continue;
			}
			
			if (tokens.get(i).equals(")")) {
				indiceFinalDeAvaliacao = i;
				return;
			}
			
		}

	}

	private Integer qtdeOperacoesASeremRealizadas() {
		
		Integer qtdeOps = 0;
		
		for (int i = 0; i < tokens.size(); i++) {
			if (tokenEhOperador(tokens.get(i))) {
				qtdeOps++;
			}
		}
		
		return qtdeOps;
		
	}
	
	private void realizaOperacaoEAtualizaExpressao(String operandoEsquerdo, String operador, String operandoDireito, Integer indiceOperador) {
		
		Long opEsq = Long.parseLong(operandoEsquerdo);
		Long opDir = Long.parseLong(operandoDireito);
		
		Long resultado = 0L;
		
		switch(operador) {
			case "+" : 
				resultado = opEsq + opDir;
				break;
			case "-" : 
				resultado = opEsq - opDir;
				break;
			case "*" : 
				resultado = opEsq * opDir;
				break;
			case "/" : 
				resultado = opEsq / opDir;
				break;
			case "%" : 
				resultado = opEsq % opDir;
				break;
			case "^" : 
				if (opEsq.equals(0)) {
					resultado = 0L;
				} else if (opEsq.equals(1)) {
					resultado = 1L;
				} else {
					resultado = opEsq;
					for (int i = 0; i < (opDir - 1); i++) {
						resultado *= opEsq;
					}
				}
				break;
			default: System.err.println("Não foi possível encontrar o operador '" + operador + "', o resultado da expressão pode estar incorreto!");
		}
		
		tokens.set(indiceOperador, Long.toString(resultado));
		tokens.set((indiceOperador - 1), " ");
		tokens.set((indiceOperador + 1), " ");
		
		validarParentesesARemover();
		validaEstruturaDeParenteses();
		
	}
	
	private void removerCamposEmBranco() {
		
		Integer qtdeRegs = tokens.size();
		
		for (int i = 0; i < qtdeRegs; i++) {
			
			if (tokens.get(i).equals("") || tokens.get(i).equals(" ")) {
				qtdeRegs--;
				tokens.remove(i);
			}
			
		}
		
	}
	
	private void validarParentesesARemover() {

		Boolean temOperadores = Boolean.FALSE;
		
		for (int i = indiceInicialDeAvaliacao; i <= indiceFinalDeAvaliacao; i++) {
			
			if (tokenEhOperador(tokens.get(i))) {
				temOperadores = Boolean.TRUE;
			}
			
		}
		
		if(!temOperadores) {
			tokens.set(indiceInicialDeAvaliacao, " ");
			tokens.set(indiceFinalDeAvaliacao, " ");
		}
		
	}

	private void validaEstruturaDeParenteses() {
		
		Integer qtdeAberturaParenteses = 0;
		Integer qtdeFechamentoParenteses = 0;
		
		for (int i = 0; i < tokens.size(); i++) {
			
			if (tokens.get(i).equals("(")) {
				qtdeAberturaParenteses++;
				continue;
			}
			
			if (tokens.get(i).equals(")")) {
				qtdeFechamentoParenteses++;
			}
			
		}
		
		if (!qtdeAberturaParenteses.equals(qtdeFechamentoParenteses)) {
			System.err.println(tokens.toString());
			throw new RuntimeException("A quantidade de abertura e fechamento de parênteses informado na expressão é inválida!");
		}
		
	}
	
	private Boolean tokenEhOperador(String token) {
		
		return token.equals("+") || 
			   token.equals("-") || 
			   token.equals("*") || 
			   token.equals("/") || 
			   token.equals("%") || 
			   token.equals("^");
		
	}

}