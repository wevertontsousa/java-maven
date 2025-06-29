# Maven

O Maven é uma ferramenta de construção que simplifica o uso de bibliotecas de terceiros (dependências).

A ferramente ajuda a:
- Gerenciar automaticamente as bibliotecas.
- Construir e empacotar projetos.
- Organizar projetos.

Sem uma ferramenta de construção como o Maven (ou Gradle) seria preciso ir manualmente nos sites [MVN REPOSITORY](https://mvnrepository.com/) ou [Maven Central Repository](https://search.maven.org/) e, baixar o arquivo binário do tipo ".jar" da biblioteca dentro do diretório "/lib" e, sempre que quisesse mudar a versão de uma biblioteca seria necessário fazer tudo isso novamente.

O arquivo "pom.xml" é o arquivo de configuração do Maven, onde:
- `?xml`, `project` e `modelVersion`: Boirlerplates, ou seja, código pronto da própria configuração, não modificar.
- `groupId`, `artifactId` e `version`: Representam a identidade do projeto.
- `properties`: Epecifica a versão do Java usada, entre outras propriedades.
- `dependencies`: Onde são inseridas as "identidades" das bibliotecas de terceiros que deseja usar. Assim o Maven automaticamente vai baixar e inserir no diretório "/lib". Procurar as bibliotecas no site [MVN REPOSITORY](https://mvnrepository.com/).


## Comandos Maven

Esses comandos só podem ser usados caso o Maven esteja instalado na máquina local onde está o projeto.

Uma IDE normalmente faz a maioria desses comandos implicitamente.

Para baixar o Maven, basta acessar o site oficial [Apache Maven Project](https://maven.apache.org/download.cgi) e, em Files escolher o arquivo binário "Binary zip archive". 

```Shell
mvn compile
```
- Compila o código fonte.

```Shell
maven clean
```
- Limpa (apaga) o "build" do projeto, ou seja, todos os artefatos de execução do projeto.

```Shell
maven package
```
- Empacota o projeto em um arquivo JAR.
- Manualmente se executa `maven clean package`.
_ `java -jar target/<project-name>-<project-version>-SNAPSHOT.jar` executa o projeto Java empacotado.

```Shell
maven install
```
- Baixa todas as bibliotecas inseridas no arquivo "pom.xml" para o repositório local do usuário.
- Ao instalar o Maven, automaticamente é criado o diretório "~/.m2/repository" (repositório local do usuário).

```Shell
maven dependency:tree
```
- Exibe no Shell todas as bibliotecas e as bibliotecas das bibliotecas (dependências transitivas).


## Fontes

- [YouTube - Giuliana Bezerra](https://www.youtube.com/watch?v=N7AyIfUQxGc)
