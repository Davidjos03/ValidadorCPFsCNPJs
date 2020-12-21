Tecnologias Utilizadas:
-Maven
-SpringBoot
-Postman
-JPA
-JAVA 8
-Swagger
-PostgreSQL

-------------------------------------------------------

- É necessário criar no banco de dados o DataBase chamado "validador", as tabelas serão carregadas ao executar o projeto.

- A Classe DataConfiguration armazena as informações para comunicação com o Banco de Dados Postgresql, insira o username e password correspondentes ao seu banco de dados.

- Foi desenvolvido alguns testes Unitários, estão presentes na Classe "ValidadorApplicationTests".

- Os testes dos protocolos das Rotas HTTP foram realidados via Postman, abaico será disponibilidado alguns exemplos que podem ser utilizados para execução:

- POST : http://localhost:8080/usuario

{
    "nomeUsuario" : "david",
    "senha" : "12345678"
}

- POST: http://localhost:8080/validador

{
    "cpfouCNPJ" : "85.509.583/0001-34",
    "usuario": 
    {
        "cdUsuario": "",
        "nomeUsuario" : "david",
        "senha" : "12345678"
        }
}

- DELETE: http://localhost:8080/validador
{
    "codigo": 1,
    "cpfouCNPJ" : "85.509.583/0001-34",
    "usuario": 
    {
        "cdUsuario": "1",
        "nomeUsuario" : "david",
        "senha" : "12345678"
        }
}

{
    "cpfouCNPJ" : "85.509.583/0001-33",
    "usuario": 
    {
        "cdUsuario": "1",
        "nomeUsuario" : "david",
        "senha" : "12345678"
        }
}

{
    "cpfouCNPJ" : "85.509.583/0001-335466",
    "usuario": 
    {
        "cdUsuario": "97",
        "nomeUsuario" : "david",
        "senha" : "12345678"
        }
}

{
    "cpfouCNPJ" : "056.790.581-03",
    "usuario": 
    {
        "cdUsuario": "97",
        "nomeUsuario" : "david",
        "senha" : "12345678"
        }
}

{
    "cpfouCNPJ" : "056.790.581-04",
    "usuario": 
    {
        "cdUsuario": "97",
        "nomeUsuario" : "david",
        "senha" : "12345678"
        }
}


- PUT: http://localhost:8080/usuario

{
    "cdUsuario" : 1,
    "nomeUsuario" : "david",
    "senha" : "12345678",
    "valorDividaUsuario" : 2
}

- GET: http://localhost:8080/usuario/1
Retorna o Usuario com Codigo 1

- GET: http://localhost:8080/usuario
Retorna todos os Usuarios

- DELETE: http://localhost:8080/usuario
{
        "cdUsuario": "1",
        "nomeUsuario" : "david",
        "senha" : "12345678"
        
}





