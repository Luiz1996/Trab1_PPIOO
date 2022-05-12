extern crate regex;
use regex::Regex;

fn main() {

    let expressoes: Vec<String> = vec!["1 + 3", 
                                       "1 + 2 * 3", 
                                       "4 / 2 + 7",
                                       "1 + 2 + 3 * 4",
                                       "(1 + 2 + 3) * 4",
                                       "(10 / 3 + 23) * (1 - 4)",
                                       "((1 + 3) * 8 + 1) / 3",
                                       "58 - -8 * (58 + 31) - -14",
                                       "-71 * (-76 * 91 * (10 - 5 - -82) - -79)",
                                       "10 * 20 + 3 * 7 + 2 * 3 + 10 / 3 * 4",
                                       "(-13 - -73) * (44 - -78 - 77 + 42 - -32)",
                                       "-29 * 49 + 47 - 29 + 74 - -85 - -27 + 4 - 28",
                                       "-74 - -14 + 42 - -4 + -78 + - 50 * -35 * -81 + -41",
                                       "80 * -18 * (85 * (-46 + -71) - 12 + 26 - 59) + 84",
                                       "25 + 38 + 88 + (-6 - -73) * (-83 + (53 + 97) * 14)",
                                       "(84 - 90) * (-8 - 75 + -83 * (56 - -77) + 4 + -94)",
                                       "(54 - -8 - -35 + -68 - -90) * -39 + -43 + -91 * -30",
                                       "-13 - -74 + (66 + -57) * -93 * -9 * 77 + 79 - 66 + - 53",
                                       "(-72 - 50 * -74 + -45) * 92 * 21 * 5 * (-13 - 66 - 18)",
                                       "-7 - -37 * (90 + 70) - 30 - -44 + -32 -56 - -48 - -78",
                                       "65 * -83 - -3 + -20 + 24 - 85 * (-24 + -32) * (61 - 20)",
                                       "55 * 48 * -44 - -32 + 1 * -80 * -94 -74 * -53 + -30 + -61",
                                       "-82 * (25 + 62 + 3) - -72 + -65 * -32 * (77 + 12) - -95 + 51",
                                       "(2 - 65 - (-24 + -97) * -5 * -61) * (-41 + 85 * 9 * -92 * (75 - 18))",
                                       "-20 + -51 + 20 + -68 * -11 + -35 * -14 -95 - 32 + -52 * -23 - -90 * -42",
                                       "(3 + 4) * 2 - 4",
                                       "18 / 2 * (1 + 2) ^ 2",
                                       "18 / 2 ^ 2",
                                       "14 % 3",
                                       "10 % 3",
                                       "32 % (300 % 27)",
                                       "32129 % (300 % 27) ^ 9",
                                       "2 ^ 3"].into_iter()
                                               .map(|s| s.to_owned())
                                               .collect();

    let resultados: Vec<isize> = vec![4, 
                                      7, 
                                      9,
                                      15,
                                      24,
                                      -78,
                                      11,
                                      784,
                                      42714523,
                                      239,
                                      7140,
                                      -1241,
                                      -141883,
                                      14385684,
                                      135290,
                                      67272,
                                      -1954,
                                      580062,
                                      -3357342660,
                                      5965,
                                      189772,
                                      -104777,
                                      177958,
                                      -147799088242,
                                      -1524,
                                      10,
                                      81,
                                      4,
                                      2,
                                      1,
                                      2,
                                      12446,
                                      8].into_iter()
                                        .map(|s| s.to_owned())
                                        .collect();

    println!("---------------------------------------------------------------------------------------\n\n");

    for i in 0..expressoes.len() {
        println!("> {}", expressoes[i]);

        let mut tokens : Vec<String> = quebrar_em_tokens(&expressoes[i]);
    
        valida_estrutura_de_parenteses(&mut tokens);
    
        processa_expressoes(&mut tokens);

        assert_eq!(resultados[i], tokens[0].parse::<isize>().unwrap()); 

    }

}

fn quebrar_em_tokens(expressao:&str) -> Vec<String>{

    let re = Regex::new("([\\(]|\\d+|[-+\\*/%^]|[\\)])").unwrap();

    let mut tokens: Vec<String> = re.find_iter(expressao)
                                    .map(|m| m.as_str().to_string())
                                    .collect();

    let mut contador_operadores = 0;                                
    for i in 0..tokens.len() {

        if token_eh_operador(&mut tokens[i].to_string()) {
            contador_operadores += 1;
        } else {
            contador_operadores = 0;
        }

        if (i == 0 || tokens[(i - 1)] == "(") && {token_eh_operador(&mut tokens[i].to_string())} {
            contador_operadores = 2;
        }

        if tokens[i] == "--" || tokens[i] == "+-" || tokens[i] == "-+" || tokens[i] == "++" {
            contador_operadores = 2;
        }

        if contador_operadores == 2 {
            contador_operadores = 0;
            let mut prox_token = tokens[i].to_string();
            prox_token.push_str(&tokens[(i + 1)]);
            tokens[i + 1] = prox_token;
            tokens[i] = String::new();
        }

    }

    return tokens;

}

fn valida_estrutura_de_parenteses(tokens: &mut Vec<String>) {

    let mut qtde_abertura_parenteses = 0;
	let mut qtde_fechamento_parenteses = 0;
		
	for i in 0..tokens.len() {

		if tokens[i] == "(" {
			qtde_abertura_parenteses += 1;
			continue;
		}
			
		if tokens[i] == ")" {
			qtde_fechamento_parenteses += 1;
		}
			
	}
		
	if qtde_abertura_parenteses != qtde_fechamento_parenteses {
		println!("A quantidade de aberturas e fechamento de parênteses não batem, aplicação encerrada!");
        std::process::exit(0);
	}

}

fn processa_expressoes(tokens: &mut Vec<String>){

    let mut qtde_operacoes = qtde_operacoes_a_serem_realizadas(tokens);

    while qtde_operacoes > 0 {

        remover_campos_em_branco(tokens);

        atualiza_intervalo_indices_a_serem_avaliados(tokens);

        remover_campos_em_branco(tokens);
        
        print_expressao_temporaria(tokens);
        
        qtde_operacoes -= 1;

    }

    println!("--------------------------------------------------------------------------------------");

}

fn print_expressao_temporaria(tokens: &mut Vec<String>){
    
    for tk in tokens {
        print!("{} ", tk);
    }

    print!("\n");

}

fn atualiza_intervalo_indices_a_serem_avaliados(tokens:&mut Vec<String>)  {

    let mut indice_inicial_de_avaliacao = 0;
    let mut indice_final_de_avaliacao = tokens.len() - 1;

    for i in 0..tokens.len() {
        
        if tokens[i] == "(" {
            indice_inicial_de_avaliacao = i;
            continue;
        }
        
        if tokens[i] == ")" {
            indice_final_de_avaliacao = i;
            break;
        }
        
    }

    processa_dentro_dos_indices_de_avaliacao(indice_inicial_de_avaliacao, indice_final_de_avaliacao, tokens);

}

fn processa_dentro_dos_indices_de_avaliacao(indice_inicial_de_avaliacao:usize, indice_final_de_avaliacao:usize, tokens:&mut Vec<String>) {

    let mut indice_do_operador :isize = procura_exponenciacao(indice_inicial_de_avaliacao, indice_final_de_avaliacao, tokens);
		
    if indice_do_operador == -1 {
        indice_do_operador = procura_multiplicacao_ou_divisao_modulo(indice_inicial_de_avaliacao, indice_final_de_avaliacao, tokens);
    }

    if indice_do_operador == -1 {
        indice_do_operador = procura_adicao_ou_subtracao(indice_inicial_de_avaliacao, indice_final_de_avaliacao, tokens);
    }

    realiza_operacao_e_atualiza_expressao(indice_do_operador,
                                          tokens,
                                          indice_inicial_de_avaliacao, 
                                          indice_final_de_avaliacao);


}

fn realiza_operacao_e_atualiza_expressao(indice_operador:isize, tokens:&mut Vec<String>, indice_inicial_de_avaliacao:usize, indice_final_de_avaliacao:usize) {

    let op_esq :isize = tokens[(indice_operador as usize) - 1].parse::<isize>().unwrap();
    
    let mut op_dir :isize = tokens[(indice_operador as usize) + 1].parse::<isize>().unwrap();
    
    let operador :String = tokens[indice_operador as usize].to_string();
    
    let mut resultado :isize = 0;

    match operador.as_str() {
        "+" => {
            resultado = op_esq + op_dir;
        },
        "-" => {
            resultado = op_esq - op_dir;
        },
        "*" => {
            resultado = op_esq * op_dir;
        },
        "/" => {
            resultado = op_esq / op_dir;
        },
        "%" => {
            resultado = op_esq % op_dir;
        },
        "^" => {
            if op_esq == 0 {
                resultado = 0;
            } else if op_esq == 1 {
                resultado = 1;
            } else {
                resultado = op_esq;
                while (op_dir - 1) > 0 {
                    resultado *= op_esq;
                    op_dir -= 1;
                }
            }
        },
        _ => {
            println!("Não foi possível encontrar o operador '{}', portanto, o resultado da expressão pode estar incorreto!", operador);
        },
    }

    tokens[indice_operador as usize] = resultado.to_string();
    tokens[((indice_operador as usize) - 1)] =  " ".to_string();
    tokens[((indice_operador as usize) + 1) ] =" ".to_string();
    
    validar_parenteses_a_remover(indice_inicial_de_avaliacao, indice_final_de_avaliacao, tokens);
    valida_estrutura_de_parenteses(tokens);

}

fn validar_parenteses_a_remover(indice_inicial_de_avaliacao:usize, indice_final_de_avaliacao:usize, tokens:&mut Vec<String>) {

    let mut tem_operadores :bool = false;
		
    for i in indice_inicial_de_avaliacao..indice_final_de_avaliacao {
        
        if token_eh_operador(&mut tokens[i].to_string()) {
            tem_operadores = true;
        }
        
    }
    
    if!tem_operadores {
        tokens[indice_inicial_de_avaliacao] = " ".to_string();
        tokens[indice_final_de_avaliacao] = " ".to_string();
    }

}

fn procura_exponenciacao(indice_inicial_de_avaliacao:usize, indice_final_de_avaliacao:usize, tokens:&mut Vec<String>) -> isize {

    for i in indice_inicial_de_avaliacao..indice_final_de_avaliacao {
        
        if tokens[i] == "^" {
            return i as isize;
        }
        
    }
    
    return -1;
    
}

fn procura_multiplicacao_ou_divisao_modulo(indice_inicial_de_avaliacao:usize, indice_final_de_avaliacao:usize, tokens:&mut Vec<String>) -> isize {

    for i in indice_inicial_de_avaliacao..indice_final_de_avaliacao {
        
        if tokens[i] == "*" || { tokens[i] == "/" || tokens[i] == "%" } {
            return i as isize;
        }
        
    }
    
    return -1;    
    
}

fn procura_adicao_ou_subtracao(indice_inicial_de_avaliacao:usize, indice_final_de_avaliacao:usize, tokens:&mut Vec<String>) -> isize {

    for i in indice_inicial_de_avaliacao..indice_final_de_avaliacao {
        
        if tokens[i] == "+" || { tokens[i] == "-" } {
            return i as isize;
        }
        
    }
    
    return -1;
    
}

fn remover_campos_em_branco(tokens:&mut Vec<String>) {

    let str_vazia = "";
    let espaco_em_branco = " ";

    tokens.retain(|x| x != str_vazia);
    tokens.retain(|x| x != espaco_em_branco); 

}

fn qtde_operacoes_a_serem_realizadas(tokens:&mut Vec<String>) -> u32 {
    
    let mut qtde_operacoes = 0;
		
	for token in tokens {
		if token_eh_operador(token) {
			qtde_operacoes += 1;
		}
	}
		
	return qtde_operacoes;

}

fn token_eh_operador(token:&mut String) -> bool {	
    return token == "+" || token == "-" || token == "*" || token == "/" || token == "%" || token == "^";
}