# Caju Challenge

Este projeto é um desafio técnico da empresa Caju. O objetivo é criar um sistema de autorização de transações, onde o sistema deve processar transações de acordo com regras específicas e garantir que apenas transações válidas sejam autorizadas.

## Funcionalidades

O autorizador deve processar as transações com base nos seguintes critérios:

1. **Autorizador Simples**: Usa o MCC para mapear a transação para uma categoria de benefícios (FOOD, MEAL, CASH) e aprova ou rejeita a transação com base no saldo disponível.

2. **Autorizador com Fallback**: Se o saldo da categoria principal não for suficiente, verifica o saldo da categoria CASH e, se for suficiente, debita esse saldo.

3. **Autorizador Dependente do Comerciante**: Substitui o MCC com base no nome do comerciante, quando disponível.

## Configuração do Projeto

O projeto é uma aplicação Spring Boot que utiliza um banco de dados H2 para persistência. A configuração do banco de dados é realizada através da classe `DatabaseConfig`, que preenche o banco com dados de exemplo para facilitar os testes.

### Entidades Disponíveis

**Account**

| ID  | Meal Amount | Food Amount | Cash Amount |
|-----|-------------|-------------|-------------|
| 100 | 100.00      | 200.00      | 1300.00     |
| 101 | 150.00      | 2250.00     | 350.00      |
| 102 | 1200.00     | 3300.00     | 4400.00     |

**Merchant**

| ID  | Merchant Name                          | MCC  |
|-----|----------------------------------------|------|
| 1   | PADARIA DO ZE SAO PAULO BR             | 5411 |
| 2   | SUPERMERCADO ABC RIO DE JANEIRO RJ     | 5412 |
| 3   | RESTAURANTE XYZ CURITIBA PR            | 5811 |
| 4   | LOJA DE ELETRONICOS SAO PAULO SP       | 5812 |
| 5   | LOJA DE FRUTAS SAO JOSE DOS CAMPOS SP  | 8900 |

## Como Executar

1. **Clone o Repositório**

    ```bash
    git clone https://github.com/seu-usuario/autorizador-caju.git
    cd autorizador-caju
    ```

2. **Compile e Execute o Projeto**

    Use Maven para construir e executar o projeto:

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

3. **Testes**

    Para rodar os testes unitários, use o comando:

    ```bash
    mvn test
    ```

4. **Acessando o Banco de Dados H2**

   Após iniciar a aplicação, você pode acessar o banco de dados H2 através do console web.

    - **URL do Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    - **Username**: `caju`
    - **Senha**: `123`

## Endpoints

O projeto expõe um endpoint HTTP para autorizar transações:

- **POST** `localhost:8080/api/v1/transaction/authorize`
  
  **Request Body**:

    ```json
    {
        "account": "100",
        "totalAmount": 50.00,
        "mcc": "5411",
        "merchant": "PADARIA DO ZE SAO PAULO BR"
    }
    ```

  **Response**:

    ```json
    {
        "code": "00"
    }
    ```

  Os códigos de resposta podem ser:
  - `00` - Transação aprovada
  - `51` - Saldo insuficiente
  - `07` - Outro problema

### Versão do Java

Este projeto utiliza Java 17.

### Discutindo sobre a L4
Bom, sobre a questão de concorrência, uma abordagem para garantir que apenas uma transação seja processada por vez é usar um bloqueio no nível de banco de dados. Isso pode ser feito utilizando a anotação @Transactional com um nível de isolamento adequado. Embora eu tenha utilizado o Transactional padrão na service do projeto, poderia ter utilizado um isolamento mais rigoroso como o "@Transactional(isolation = Isolation.SERIALIZABLE)" para garantir que as transações fossem executadas sequencialmente. Acredito também que outra maneira de fazer isso ainda olhando pro projeto que fiz nesse challenge seria aproveitar o JPA e bloquear a entidade "Account" utilizada para representar a conta de um cliente neste cenário como por exemplo o decorator @Lock com um tipo pessimista como parâmetro, desta forma podemos garantir que somente uma transação possa modificar a conta por vez. Ainda no caso do JPA se tendermos pro lado de heurísticas otimistas, podemos colocar um parametro de versionamento na entidade Account de uma forma que ela sempre verifique se a versão do objeto é a mesma que foi lida antes de escrever novamente na base de dados. Claro que é possível mesclarmos esses tratamentos, porém acredito que dado a necessidade de velocidade é viável escolher apenas uma e adaptar dependendo do contexto que será aplicado.

