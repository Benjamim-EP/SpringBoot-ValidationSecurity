# SpringBoot-ValidationSecurity
Repositorio com um exemplo de implementação de Validação e segurança



Essas classes estão relacionadas à configuração de segurança e autenticação usando OAuth2 em um aplicativo Java Spring. Vamos analisá-las uma por uma e entender como elas se relacionam:

## 1. `AppConfig`:
Essa classe é responsável por configurar alguns beans utilizados na aplicação. Ela define três métodos `@Bean` que instanciam e configuram os seguintes objetos:

- `passwordEncoder()`: Retorna uma instância do `BCryptPasswordEncoder`, que é um encoder de senha utilizado para codificar senhas de usuário antes de armazená-las no banco de dados.
- `accessTokenConverter()`: Retorna uma instância de `JwtAccessTokenConverter`, que é responsável por converter os tokens de acesso para o formato JWT (JSON Web Token). O método também define a chave de assinatura do token com base em uma propriedade de configuração chamada `${jwt.secret}`.
- `tokenStore()`: Retorna uma instância de `JwtTokenStore`, responsável por armazenar os tokens JWT.

## 2. `AuthorizationServerConfig`:
Essa classe é uma configuração específica do Spring para configurar o Authorization Server, que é responsável pelo processo de autenticação e autorização do OAuth2. A classe estende `AuthorizationServerConfigurerAdapter`, que fornece métodos para configurar detalhes do cliente, endpoints do Authorization Server e aspectos de segurança.

- Os métodos `configure(AuthorizationServerSecurityConfigurer security)`, `configure(ClientDetailsServiceConfigurer clients)`, e `configure(AuthorizationServerEndpointsConfigurer endpoints)` configuram a segurança, detalhes do cliente e endpoints do Authorization Server, respectivamente.
- O `AuthorizationServerConfig` usa os beans definidos na classe `AppConfig`, como o `BCryptPasswordEncoder`, o `JwtAccessTokenConverter`, o `JwtTokenStore` e o `JwtTokenEnhancer`, para configurar o comportamento do Authorization Server.

## 3. `ResourceServerConfig`:
Essa classe é uma configuração específica do Spring para configurar o Resource Server, que é responsável por proteger os recursos da aplicação e validar os tokens JWT. A classe estende `ResourceServerConfigurerAdapter`, que fornece métodos para configurar o token store JWT e as regras de autorização para os endpoints.

- O método `configure(ResourceServerSecurityConfigurer resources)` configura o Resource Server para usar o `JwtTokenStore` definido na classe `AppConfig`.
- O método `configure(HttpSecurity http)` define as regras de autorização para os endpoints da aplicação, especificando quais endpoints são públicos, acessíveis por OPERATOR e ADMIN, ou acessíveis apenas por ADMIN.

## 4. `WebSecurityConfig`:
Essa classe é uma configuração específica do Spring para configurar as regras de segurança da aplicação web. A classe estende `WebSecurityConfigurerAdapter`, que fornece métodos para configurar a autenticação e ignorar determinados caminhos durante a verificação de segurança.

- O método `configure(AuthenticationManagerBuilder auth)` configura o `AuthenticationManagerBuilder` para usar o `UserDetailsService` e o `BCryptPasswordEncoder` definidos na classe `AppConfig` para autenticar os usuários.
- O método `configure(WebSecurity web)` é usado para ignorar determinados caminhos durante a verificação de segurança, especificando que o caminho "/actuator/**" será ignorado.
- O método `authenticationManager()` cria um bean para o `AuthenticationManager`, que é usado pelo Spring Security para realizar a autenticação dos usuários.

## 5. `RoleDTO`:

A classe `RoleDTO` é uma classe de transferência de dados (DTO - Data Transfer Object) que representa uma projeção simplificada da entidade `Role` do sistema. Essa classe permite transmitir informações específicas de uma `Role` para outras camadas da aplicação, como a camada de serviço ou a camada de apresentação.

A relação da classe `RoleDTO` com as classes anteriores pode ocorrer principalmente na camada de apresentação, onde os controladores podem utilizar objetos `RoleDTO` para representar e transmitir as informações das roles para os clientes (por exemplo, uma interface web ou API REST).

Aqui estão algumas formas de relação com as classes anteriores:

1. **Camada de serviço e controladores**:
    
    - Quando os controladores da aplicação (classe de controle da API REST ou controlador de interface web) recebem solicitações do cliente para acessar ou manipular informações de roles, eles podem interagir com os serviços da aplicação.
    - Os serviços podem retornar informações sobre roles na forma de `RoleDTO` quando necessário, simplificando as informações transmitidas para os clientes.
    - Por exemplo, quando uma solicitação para listar todas as roles é feita, o serviço pode obter uma lista de entidades `Role` do banco de dados e, em seguida, converter essas entidades em uma lista de `RoleDTO` para retornar apenas as informações relevantes para a apresentação.
2. **Processo de autenticação e autorização**:
    
    - As classes de configuração, como `AuthorizationServerConfig` e `ResourceServerConfig`, lidam com o processo de autenticação e autorização da aplicação.
    - Quando um usuário é autenticado com sucesso e autorizado a acessar recursos protegidos, o sistema pode retornar detalhes sobre as `Roles` do usuário.
    - Nesse cenário, a classe `RoleDTO` pode ser usada para representar as informações das `Roles` em vez de usar a entidade completa `Role`. Isso ajuda a evitar informações desnecessárias e a transmitir apenas os dados relevantes do usuário.
3. **Respostas da API**:
    
    - Quando a API REST da aplicação retorna informações sobre roles como parte de uma resposta HTTP, ela pode usar objetos `RoleDTO` para representar essas informações.
    - Isso garante que apenas as informações necessárias sejam enviadas para o cliente, evitando dados sensíveis ou detalhes internos da entidade `Role` que não precisam ser expostos.

## 6. `UseResource`:


A classe `UserResource` representa os endpoints RESTful da API para gerenciamento de usuários. Ela possui mapeamentos para as operações CRUD (Create, Read, Update, Delete) de usuários e se relaciona com as classes anteriores de configuração de segurança e autenticação da seguinte forma:

1. `UserResource` utiliza o serviço `UserService`:

```java
@Autowired
private UserService service;
```

O serviço `UserService` é responsável por fornecer a lógica de negócio relacionada aos usuários, como a recuperação de usuários, inserção, atualização e exclusão. É uma classe que não está incluída no código fornecido, mas sua funcionalidade pode ser entendida como sendo um intermediário entre o controlador `UserResource` e a camada de acesso a dados (repositório).

2. `UserResource` realiza operações com DTOs:

```java
@PostMapping
public ResponseEntity<UserDTO> insert(@RequestBody @Valid UserInsertDTO dto) {
    // ...
}
```

Os métodos do `UserResource` recebem e retornam objetos DTO (`UserDTO`, `UserInsertDTO`, `UserUpdateDTO`). Os DTOs (Data Transfer Objects) são objetos utilizados para transferir dados entre a camada de controle (controlador) e a camada de serviço. Eles são uma forma de evitar a exposição direta das entidades do domínio e fornecer uma visão simplificada dos dados que serão manipulados pela API.

3. `UserResource` realiza operações de CRUD em usuários:

Através dos mapeamentos de endpoints, o `UserResource` é capaz de executar operações de CRUD:

- `findAll`: Retorna uma lista paginada de usuários.
- `findById`: Retorna um usuário específico com base no ID fornecido.
- `insert`: Cria um novo usuário com base nas informações fornecidas.
- `update`: Atualiza um usuário existente com as informações fornecidas.
- `delete`: Exclui um usuário com o ID especificado.

Essas operações são chamadas pelos clientes da API através dos endpoints correspondentes e, quando necessário, os métodos do serviço `UserService` são utilizados para executar a lógica de negócio associada.

4. Segurança e Autorização:

Embora a classe `UserResource` não esteja diretamente relacionada às classes de configuração de segurança (`WebSecurityConfig`, `AuthorizationServerConfig`, `ResourceServerConfig`), a proteção dos endpoints é alcançada através da configuração realizada em tais classes.

A anotação `@EnableWebSecurity` em `WebSecurityConfig` habilita a segurança na aplicação e configura as regras de segurança. Por exemplo, os endpoints `/users/**` só podem ser acessados por usuários autenticados com o papel "OPERATOR" ou "ADMIN", conforme especificado na configuração `configure(HttpSecurity http)`.

O processo de autenticação e autorização OAuth2 é tratado nas classes `AuthorizationServerConfig` e `ResourceServerConfig`. Essas configurações são responsáveis por lidar com a geração e validação dos tokens JWT, além de definir as regras de autorização de acesso aos recursos da API. Através dessas configurações, a API se torna segura, restringindo o acesso apenas a usuários autenticados e autorizados.

Em resumo, a classe `UserResource` é a camada de controle da API que gerencia as operações relacionadas a usuários, utilizando o serviço `UserService` como intermediário para a camada de acesso a dados. A segurança da API é configurada nas classes `WebSecurityConfig`, `AuthorizationServerConfig` e `ResourceServerConfig`, garantindo que os endpoints só sejam acessíveis por usuários autenticados e autorizados.



## Erros

### unauthorized

	<oauth>
	<error_description>Full authentication is required to access this resource</error_description>
	<error>unauthorized</error>
	</oauth>

O erro "Full authentication is required to access this resource" indica que a autenticação completa é necessária para acessar o recurso, ou seja, você precisa estar autenticado para acessar a URL `http://localhost:8080/users/1`.

Isso acontece porque a API está protegida por segurança OAuth2, conforme configurado nas classes `AuthorizationServerConfig` e `ResourceServerConfig`. Portanto, antes de acessar qualquer endpoint protegido, é necessário obter um token de acesso válido através do fluxo de autenticação OAuth2.

Para resolver o problema e obter um token de acesso válido, você precisa seguir os seguintes passos:

1. Certifique-se de que o servidor de autorização (Authorization Server) esteja em execução. Geralmente, isso envolve iniciar o aplicativo Spring Boot que contém as configurações relacionadas ao OAuth2.

2. Realize o fluxo de autenticação OAuth2 para obter um token de acesso. Isso pode ser feito através de uma solicitação HTTP POST para a URL de autorização do servidor OAuth2 com as credenciais de cliente (client_id e client_secret) e o escopo de autorização (scope). Por exemplo:

```bash
curl -X POST http://localhost:8080/oauth/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&username=seu_usuario&password=sua_senha&client_id=seu_client_id&client_secret=seu_client_secret&scope=read write"
```

Substitua `seu_usuario`, `sua_senha`, `seu_client_id` e `seu_client_secret` pelos valores corretos das credenciais de autenticação do cliente definidas no método `configure(ClientDetailsServiceConfigurer clients)` da classe `AuthorizationServerConfig`.

3. O servidor de autorização responderá com um token de acesso. Ele geralmente será um token JWT que você pode usar para autenticar suas solicitações.

4. Inclua o token de acesso nas suas solicitações para acessar os endpoints protegidos. Faça isso adicionando o cabeçalho `Authorization: Bearer <token>` na solicitação HTTP. Por exemplo:

```bash
curl -X GET http://localhost:8080/users/1 \
  -H "Authorization: Bearer seu_token_de_acesso"
```

Substitua `seu_token_de_acesso` pelo token JWT obtido na etapa anterior.

Ao seguir esses passos, você deve ser capaz de obter um token de acesso válido e usar esse token para acessar a URL `http://localhost:8080/users/1` e outros endpoints protegidos da API. Lembre-se de que, se você estiver desenvolvendo um aplicativo cliente, existem bibliotecas e ferramentas para facilitar o fluxo de autenticação OAuth2 e a obtenção de tokens de acesso.
