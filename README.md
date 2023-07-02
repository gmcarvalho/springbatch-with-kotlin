# Criando uma Job simples com Kotlin e Spring Boot

Você precisa criar uma aplicação Kotlin com Spring Boot, no final tem uma documentação de referência 
para que você crie a sua, caso nunca tenha criado uma. 

Eu criei o meu projeto pelo intellij IDEA e utilizei uma conexão local com o mysql, 
mas você pode usar a IDE e banco da sua preferência. Vou te mostrar o passo abaixo aqui embaixo.

### Criando o projeto

No intellij basta ir em `File --> New --> Projects` como na imagem abaixo:

![](../../../Desktop/new_project.png)

Vai abrir a tela a seguir, onde você vai configurar o diretório, artifact, a tecnologia e versões. 
Por default o nome vem como demo, e você pode modificar pro nome que quer dar ao seu aplicativo. 

![](../../../Desktop/configs.png)

Na próxima aba de dependências você já pode escolher a opção `Spring Batch` e no meu caso importei a dependência do `mysql`
mas como falei, se não quiser configurar um usuário local, pode usar o H2 database, ou se preferir também pode usar 
o banco da sua preferência. O banco é necessário pra criação das tabelas de metadados do Spring Batch. 

![](../../../Desktop/dependencias.png)

Caso você pule essa etapa, sem escolher as dependências, não tem problema, é só ir no `pom.xml` da sua aplicação que 
acabou de criar, e adicionar as dependências manualmente. 

Perceba que se você tive pulado a parte das dependências, no seu arquivo `pom.xml`, vai encontrar a seguinte dependência:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>
```

No `<artifactId>` você irá trocar o apontamento de `spring-boot-starter` para `spring-boot-starter-batch`, ficando dessa forma: 

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-batch</artifactId>
</dependency>
```

A dependência do `mysql` é a seguinte: 

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
    <version>8.0.33</version>
</dependency>
```

No meu caso precisei informar a versão, mas vi outras pessoas que criaram
sem indicar a versão.

### Criando a configuração da Job

Existem duas formas de processar os ítens em um Job, uma utilizando `Tasklet` e outra que é utilizando `chunk`. 

Como a ideia aqui é só fazer a configuração inicial da Job, estou utilizando a `Tasklet`. Tasklet é usado pra processamentos 
simples, uma tarefa única que é executada dentro do contexto da Job.  

![](../../../Downloads/Documento Ficha de Dados do Aluno Escola Azul e Branco.png)

Qual cenário se aplicaria o uso da Tasklet então, já que no nosso exemplo só imprimimos um texto na tela? Vamos lá!

Imagine que um secretário de uma escolinha de criança adicione em um arquivo dados de alunos, pais, e pessoas que estão 
autorizados a buscá-los na escola. Suponha que esse processo é manual e cada linha do arquivo são os dados de um aluno,
e espera-se que a partir de agora esses dados sejam salvos em uma base de dados, mas a escolhinha ainda não tem um painel administrativo 
para esses registros de forma automatizada e em tempo real.

Um exemplo de uso de Tasklet se enquadraria aqui, pois o Job vai executar um processamento simples, sem a necessidade 
de um processamento em lotes de leitura e escrita, vai fazer a leitura do arquivo em um determinado horário, 
vai processar as informações e em seguida registrar no banco de dados. 

Agora que você já conhece um pouco da Tasklet, vamos voltar ao exemplo. 

Para a configuração da Job você vai precisar das seguintes anotations na sua `classe de configuração` da Job 

```
@EnableBatchProcessing
@Configuration
```

No construtor importamos
```
private val jobBuilderFactory: JobBuilderFactory,
private val stepBuilderFactory: StepBuilderFactory
```

```kotlin
@Bean
fun myJob(): Job{
    return jobBuilderFactory
        .get(MY_JOB_NAME)
        .start(printWelcomeText())
        .incrementer(RunIdIncrementer())
        .build()
}
```

Explicando essa parte:
O `JobBuilderFactory` e `StepBuilderFactory` são classes fornecidas pelo Spring Batch e facilitam a criação e configuração 
de objetos de de `Job` e `Step` respectivamente por meio do padrão (fluent builder Pattern)

* No `.get` é o nome dado a sua Job 
* o `.start` indica qual step vai iniciar, para adicionar mais steps você pode abaixo do start adicionar um `.next` e dentro referenciar o p
* O `.incrementer()` é porque quando inicializamos a Job localmente mais vezes, acontece um erro dizendo que a instância da sua Job já está sendo executada, e para que não precisemos ficar finalizando a instância nos metadados, podemos ir testando com esse incrementer. 
* E por fim o `.build()` para finalizar a construção do job e obter uma instância de `Job`. 

O `printWelcomeText()` referencia instancia o Step que vai ser chamado primeiro quando a Job inicializar, e perceba 
que aqui referenciamos o processamento com a `Tasklet`

```kotlin
@Bean
fun printWelcomeText(): Step{
    return stepBuilderFactory.get(TASKLET_NAME)
        .tasklet(welcomeTasklet)
        .build()
}
```

O que essa tasklet faz é simples, só imprimimos no console um texto, mas como expliquei lá em cima no nosso cenário da escolinha,
poderiamos aqui a leitura de um arquivo e gravação dos dados no banco. 

```kotlin
@Component
class WelcomeTasklet: Tasklet {
    override fun execute(p0: StepContribution, p1: ChunkContext): RepeatStatus {
        println("\n*******************************")
        println("Welcome!")
        println("This is a Job with Kotlin!")
        println("*******************************\n")
        return RepeatStatus.FINISHED
    }
}
```

E prontinho \0/

Nossa primeira configuração de um Spring Batch já foi!

### Documentação de Referência

* [Criando aplicação kotlin com Spring Boot](https://kotlinlang.org/docs/jvm-create-project-with-spring-boot.html#create-a-controller)
* [Documentação do Spring Batch](https://spring.io/projects/spring-batch)
* [Acessando dados com MySQL](https://spring.io/guides/gs/accessing-data-mysql/)