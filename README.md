# 🧠 Analisador Léxico de CPF, RG e Elementos Java com JFlex

Este projeto consiste no desenvolvimento de um **analisador léxico interativo**, desenvolvido em **Java** com uso da ferramenta **JFlex**, capaz de:

- Identificar e validar documentos brasileiros: **CPF** e **RG**
- Reconhecer elementos básicos da linguagem **Java**
- Classificar **tokens léxicos** com base em regras definidas
- Aplicar **validações semânticas matemáticas** para CPF e RG

## 📚 Fundamentos

A **análise léxica** é a primeira fase de um compilador. Ela transforma uma sequência de caracteres em **tokens**, que são unidades léxicas significativas como palavras-chave, identificadores, literais e operadores.

Este analisador foi criado com **JFlex**, que transforma regras de expressões regulares (em um arquivo `.flex`) em uma classe Java (`Lexer.java`) capaz de reconhecer tokens. Utiliza também **autômatos finitos determinísticos** (AFDs) para realizar essas correspondências.

Entre os tokens reconhecidos estão:

- Palavras-chave: `if`, `else`, `public`, `class`, etc.
- Identificadores
- Literais numéricos e strings
- Operadores aritméticos, lógicos e relacionais
- Delimitadores e pontuação
- CPFs e RGs com e sem máscara (validados matematicamente)
- Espaços e comentários (ignorados)

## 🛠️ Ferramentas Utilizadas

- **Java:** Lógica principal e estrutura do programa
- **JFlex:** Geração do analisador léxico a partir de regras (.flex)
- **Maven:** Build automation e integração com o plugin do JFlex

## 🧩 Estrutura do Projeto

- `src/main/java/com/analisadorlexico/Main.java`: Classe principal que executa o analisador e valida CPF/RG.
- `src/main/jflex/Lexer.flex`: Arquivo de definição léxica com expressões regulares e regras.
- `pom.xml`: Configuração Maven com plugin JFlex.

### 📁 Lexer.flex

Dividido em 3 partes:

1. **Configurações:** Define nome da classe (`Lexer`), tipo de retorno (`String`), suporte a Unicode, rastreamento de linha e coluna.
2. **Declarações:** Macros reutilizáveis para CPF, RG, identificadores, números e strings.
3. **Regras:** Cada expressão regular está associada a uma ação (`return "TOKEN(...)"`). A ordem importa.

Inclui regras específicas para validação de **CPF e RG**, além de comentários ignorados e erros tratados.

### 🖥 Main.java

Interface textual interativa. Responsável por:

- Exibir instruções ao usuário
- Receber entradas de texto
- Executar o `Lexer` e imprimir tokens
- Validar CPF/RG com algoritmos oficiais de cálculo de dígito verificador
- Lidar com entradas inválidas (ex: strings malformadas, caracteres não reconhecidos)

## ✅ Testes Realizados

O analisador foi testado com entradas variadas:

- **CPFs e RGs válidos e inválidos**, com e sem máscara
- **Código Java realista**, com palavras-chave, identificadores, operadores, strings e delimitadores
- **Entradas inválidas**, como strings não finalizadas, emojis, símbolos não suportados

O analisador:

- Reconheceu corretamente os tokens válidos
- Ignorou espaços e comentários
- Classificou erros léxicos de forma clara
- Validou semanticamente CPFs e RGs

## 🧾 Conclusão

O projeto cumpriu todos os seus objetivos:

- ✅ Reconhecimento preciso de tokens da linguagem Java
- ✅ Validação matemática de CPF e RG
- ✅ Classificação clara de erros
- ✅ Estrutura modular e escalável com JFlex

A abordagem adotada seguiu as boas práticas da engenharia de compiladores. Este analisador serve como base para futuras etapas como análise sintática e semântica. Possui potencial para ser estendido a outras linguagens ou domínios específicos.

## 📚 Referências

- [JFlex Documentation](https://jflex.de)
- [Receita Federal - Validação de CPF](https://www.gov.br/receitafederal)
- [Documentação Java SE - Oracle](https://docs.oracle.com/en/java/)
- Aho, A. V. et al. **Compilers: Principles, Techniques, and Tools.** Pearson.
