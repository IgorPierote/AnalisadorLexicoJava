package com.analisadorlexico;

import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

    /**
     * Realiza a validação matemática de um número de CPF.
     */
    private static boolean validarCPF(String cpf) {
        cpf = cpf.replace(".", "").replace("-", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48);
            }

            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));

        } catch (InputMismatchException erro) {
            return false;
        }
    }

    /**
     * Realiza a validação matemática de um número de RG (Padrão SP).
     */
    private static boolean validarRG(String rg) {
        // 1. Remove a máscara e coloca o 'X' em maiúsculo para consistência
        rg = rg.replace(".", "").replace("-", "").toUpperCase();

        // 2. Verifica se o RG tem 9 caracteres
        if (rg.length() != 9) {
            return false;
        }

        try {
            // 3. Cálculo do dígito verificador
            int soma = 0;
            // Multiplica os 8 primeiros dígitos pelos pesos (2, 3, 4, 5, 6, 7, 8, 9)
            for (int i = 0; i < 8; i++) {
                soma += Character.getNumericValue(rg.charAt(i)) * (i + 2);
            }

            // O dígito verificador é 11 menos o resto da divisão da soma por 11
            int resultado = 11 - (soma % 11);
            char digitoCalculado;
            
            if (resultado == 10) {
                digitoCalculado = 'X';
            } else if (resultado == 11) {
                digitoCalculado = '0';
            } else {
                digitoCalculado = (char) (resultado + '0'); // Converte o número para char
            }

            // 5. Compara o dígito calculado com o dígito real do RG
            return rg.charAt(8) == digitoCalculado;

        } catch (Exception e) {
            // Se ocorrer qualquer erro (ex: char não numérico), a validação falha
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Bem-vindo ao Analisador Léxico Interativo ---");
        System.out.println("\n[INFO] O que é um Analisador Léxico?");
        System.out.println("É a primeira fase de um compilador. Ele lê o código-fonte como uma sequência de caracteres");
        System.out.println("e o converte em uma sequência de 'tokens' (as 'palavras' da linguagem), descartando o que não é essencial, como espaços e comentários.");
        System.out.println("\n[INFO] Como funciona?");
        System.out.println("1. Regras: Usamos um arquivo (.flex) para definir padrões com expressões regulares (ex: um número é uma sequência de dígitos).");
        System.out.println("2. Geração: Uma ferramenta (JFlex) lê essas regras e gera um programa em Java (Lexer.java) que sabe como reconhecer esses padrões.");
        System.out.println("3. Execução: Este programa que você está usando chama o analisador gerado para dividir sua entrada em tokens.");
        System.out.println("\n-------------------------------------------------");
        System.out.println("\nDigite um código para analisar. Para testar, insira um CPF ou RG (com ou sem máscara).");
        System.out.println("Para sair, digite 'sair'.");

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine();

            if ("sair".equalsIgnoreCase(input)) {
                System.out.println("👋 Encerrando o analisador.");
                break;
            }

            if (input.trim().isEmpty()) {
                continue;
            }

            System.out.println("\n🔎 Análise Léxica da entrada: '" + input + "'");
            Lexer lexer = new Lexer(new StringReader(input));
            String token;
            boolean hasTokens = false;

            while ((token = lexer.yylex()) != null) {
                hasTokens = true;
                System.out.println("  -> Token reconhecido: " + token);

                // Validação Semântica para CPF
                if (token.startsWith("CPF_VALIDO(")) {
                    String cpf = token.substring(11, token.length() - 1);
                    System.out.println("    [VALIDAÇÃO SEMÂNTICA] O token parece um CPF. Validando matematicamente...");
                    if (validarCPF(cpf)) {
                        System.out.println("    [RESULTADO] ✅ O CPF " + cpf + " é VÁLIDO!");
                    } else {
                        System.out.println("    [RESULTADO] ❌ O CPF " + cpf + " é INVÁLIDO!");
                    }
                }

                // Validação Semântica para RG
                if (token.startsWith("RG_VALIDO(")) {
                    String rg = token.substring(10, token.length() - 1);
                    System.out.println("    [VALIDAÇÃO SEMÂNTICA] O token parece um RG. Validando matematicamente (padrão SP)...");
                    if (validarRG(rg)) {
                        System.out.println("    [RESULTADO] ✅ O RG " + rg + " é VÁLIDO!");
                    } else {
                        System.out.println("    [RESULTADO] ❌ O RG " + rg + " é INVÁLIDO!");
                    }
                }
            }

            if (!hasTokens) {
                System.out.println("  Nenhum token foi reconhecido (a entrada pode conter apenas espaços ou comentários).");
            }

            System.out.println("✅ Análise concluída.");
        }

        scanner.close();
    }
}
