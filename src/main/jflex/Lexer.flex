package com.analisadorlexico;

import java.io.IOException;

%%

// ---------------------- CONFIGURAÇÕES----------------------
%class Lexer
%type String
%unicode
%public
%final
%line
%column

// Expressões regulares para RG e CPF.
CPF_MASCARA = [0-9]{3}\.[0-9]{3}\.[0-9]{3}\-[0-9]{2}
CPF_SEM_MASCARA = [0-9]{11}
RG_MASCARA = [0-9]{2}\.[0-9]{3}\.[0-9]{3}\-([0-9]|X|x)
RG_SEM_MASCARA = [0-9]{8}([0-9]|X|x)

// Expressões regulares para outros elementos léxicos do código Java
ID = [a-zA-Z_][a-zA-Z0-9_]*
NUM = [0-9]+
STR = \"[^\r\n\"]*\"
WS = [\ \t\r\n]+

%%
// ---------------------- REGRAS LÉXICAS ----------------------

// Palavras-chave do Java.
"public" { return "KEYWORD(public)"; }
"class" { return "KEYWORD(class)"; }
"static" { return "KEYWORD(static)"; }
"void" { return "KEYWORD(void)"; }
"return" { return "KEYWORD(return)"; }
"import" { return "KEYWORD(import)"; }
"package" { return "KEYWORD(package)"; }
"String" { return "TYPE(String)"; }
"int" { return "TYPE(int)"; }
"if" { return "KEYWORD(if)"; }
"else" { return "KEYWORD(else)"; }
"while" { return "KEYWORD(while)"; }
"true" { return "BOOLEAN(true)"; }
"false" { return "BOOLEANE(false)"; }

// Reconhece CPF e RG primeiro, antes das regras mais genéricas.
{CPF_MASCARA} { return "CPF_VALIDO(" + yytext() + ")"; }
{CPF_SEM_MASCARA} { return "CPF_VALIDO(" + yytext() + ")"; }
{RG_MASCARA} { return "RG_VALIDO(" + yytext() + ")"; }
{RG_SEM_MASCARA} { return "RG_VALIDO(" + yytext() + ")"; }

// Operadores
"+" | "-" | "*" | "/" { return "OPERADOR_ARITMETICO(" + yytext() + ")"; }
"&&" | "||" | "!" { return "OPERADOR_LOGICO(" + yytext() + ")"; }
"==" | "!=" | "<" | ">" { return "OPERADOR_RELACIONAL(" + yytext() + ")"; }
"=" { return "ATRIBUICAO(" + yytext() + ")"; }

// Delimitadores
"{" | "}" | "(" | ")" | ";" | "." | "," { return "DELIMITADOR(" + yytext() + ")"; }

// Ignora espaços e comentários.
"//"[^\n]* { /* ignorado */ }
{WS} { /* ignorado */ }

// Identifica identificadores, números e strings literais.
{ID} { return "ID(" + yytext() + ")"; }
{NUM} { return "NUM(" + yytext() + ")"; }
{STR} { return "STRING_LITERAL(" + yytext() + ")"; }

// Trata strings não finalizadas como um erro.
// Esta regra deve vir depois da regra para STR, devido à prioridade de "maior casamento" (longest match).
\"[^\r\n\"]* { return "ERRO(String não finalizada: " + yytext() + ")"; }

// Trata qualquer outro caractere como um erro léxico.
. { return "ERRO(Caractere não reconhecido: " + yytext() + ")"; }