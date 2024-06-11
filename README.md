# Curso Ûdemy

## Java com Spring Boot API REST do 0 ao deploy na AWS

[![Docker Hub Repo](https://img.shields.io/docker/pulls/bambatera/java-rest-by-erudio.svg)](https://hub.docker.com/repository/docker/bambatera/java-rest-by-erudio)

O curso ensina os fundamentos centrais de **Webservices API's REST** e **RESTful** tanto na **teoria(10%)**, quanto na **prática(90%)**. Esse conhecimento será aplicado na implementação de uma **API RESTful** com **SpringBoot 3** e **Java 18**.
Iniciaremos com uma aplicação simples aprendendo como funciona as principais annotations e aprendendo a tratar exceções sempre avançando gradualmente para cenários mais complexos. Abordaremos, noções de HTTP, verbos/métodos, tipos de parâmetros, paginação, upload e download de arquivos, versionamento, HATEOAS, Swagger, autenticação e muito mais.

Ao final iremos adicionar o suporte ao **Docker** e implantar nossa **API** em um cluster **Amazon ECS** de forma manual. E pra fechar o treinamento com chave de ouro iremos implementar um **pipeline de integração contínua** envolvendo o **Github**, o **Github Actions** e um **Cluster ECS** na **Amazon AWS**.
Vai ser incrível, **você commita o código** e envia pro **Github**, o **Github Actions** constrói as Docker Images e **implanta na Amazon "Automágicamente"**.

### Tecnologias abordadas

Nesse treinamento foram abordadas tecnologias como:
- Spring Boot 3;
- Java 18;
- Conceitos arquiteturais do REST/RESTful;
- Migrations com Flyway;
- Postman;
- Integração com o banco de dados MySQL;
- Content Negotiation;
- Versionamento de API’s;
- HATEOAS;
- Swagger Open API;
- Autenticação com JWT e Spring Security;
- Testes unitários e de integração com:
    - JUnit 5;
    - Mockito;
    - REST Assured;
    - Testcontainers.
- Upload e download de arquivos;
- [EXTRA 1] Dockerização da Aplicação
- [EXTRA 2] Implantação na Amazon AWS
- [EXTRA 3] Integração Contínua na Amazon AWS com o Github Actions
- [EXTRA 4] Como consumir a API com React JS
- Padrões de Projetos e muito mais!

## Docker

### Configuração do Docker Compose

Criar o arquivo docker-compose.yml fora da pasta do projeto e incluir o conteúdo abaixo:

```YAML
version: '3.9'
services:
  db:
    image: mysql:8.0.37
    command: mysqld --default-authentication-plugin=mysql_native_password
    restart: always
    environment:
      TZ: America/Sao_Paulo
      MYSQL_ROOT_PASSWORD: L34ndr0.$1lv@
      MYSQL_ROOT_HOST: '%'
      MYSQL_USER: docker
      MYSQL_PASSWORD: admin123
      MYSQL_DATABASE: java-rest-by-erudio
      MYSQL_TCP_PORT: 3308
    ports:
      - 3308:3308
    expose:
      - 3308
    networks:
      - erudio-network
  java-rest-by-erudio:
    image: bambatera/java-rest-by-erudio
    restart: always
    build: ./rest-with-spring-boot-and-java-erudio
    working_dir: /rest-with-spring-boot-and-java-erudio
    environment:
      TZ: America/Sao_Paulo
      SPRING.DATASOURCE.URL: jdbc:mysql://db:3308/java-rest-by-erudio?useTimezone=true&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true
      SPRING.DATASOURCE.USERNAME: root
      SPRING.DATASOURCE.PASSWORD: L34ndr0.$1lv@
    ports:
      - 80:80
    expose:
      - 80
    command: mvn spring-boot:run
    depends_on:
      - db
    networks:
      - erudio-network
networks:
  erudio-network:
    driver: bridge
```

