# Jakarta


## persistence.xml

Esse arquivo tem que estar dentro do diretório "src/main/resources/META-INF" e, no "Class Path".


### `<persistence-unit>`

Configuração para um banco de dados específico.

Podem ter mais de um banco de dados configurado no mesmo "persistence.xml".


### `<provider>`

Configura todas as classes que são entidades.

Sem ela seria necessário declarar explicitamente cada classe, exemplo:

```XML
<class>br.com.wevertontsousa.orm.unidirectional.AuthorJpaEntity</class>
<class>br.com.wevertontsousa.orm.unidirectional.BookJpaEntity</class>
<class>br.com.wevertontsousa.orm.unidirectional.PublisherJpaEntity</class>
<class>br.com.wevertontsousa.orm.unidirectional.ReviewJpaEntity</class>
```


### `EntityManagerFactory`

Carrega do arquivo "persistence.xml", que fica no diretório "resources", as configurações de um banco de dados específico.

Pode usar a biblioteca "log4j" para trabalahar com exceções nessa etapa do código.


### `EntityManager`

Faz a interação com o banco de dados (Java <-> SQL), ou seja, cria uma conexão com banco de dados.


### Estado Gerenciado

Se o obejto tiver sido recuperado do banco de dados através do `EntityManager`, e sofrer alterações, essas modificações vão ser salvas no banco implicitamente, então não é necessário fazer um `persistenceObject` ou `mergeObject`.



