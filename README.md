# Sistema de Cardápio Digital - Backend

Sistema backend completo para cardápio digital com pedidos em tempo real, desenvolvido em Java + Spring Boot.

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3.2.0**
    - Spring Web
    - Spring Data JPA
    - Spring Security
    - Spring Validation
- **PostgreSQL** - Banco de dados
- **JWT** - Autenticação e autorização
- **MapStruct** - Mapeamento de DTOs
- **SSE (Server-Sent Events)** - Comunicação em tempo real
- **JUnit + Mockito** - Testes unitários

## 📋 Funcionalidades

### 👥 Perfis de Usuário

1. **Cliente**
    - Visualizar cardápio
    - Realizar pedidos (mesa ou adega)
    - Consultar status do pedido

2. **Administrador**
    - Gerenciar cardápio (CRUD completo)
    - Visualizar todos os pedidos
    - Relatórios e estatísticas

3. **Cozinha**
    - Receber pedidos em tempo real via SSE
    - Atualizar status dos pedidos
    - Visualizar fila de pedidos

4. **Garçom**
    - Receber notificações de pedidos prontos via SSE
    - Marcar pedidos como entregues
    - Consultar pedidos por mesa

## 🏗️ Arquitetura

```
src/main/java/com/cardapio/
├── controller/          # Controladores REST
├── service/            # Regras de negócio
├── repository/         # Interfaces JPA
├── model/              # Entidades JPA
├── dto/                # Objetos de transferência
├── mapper/             # Conversores entidade <-> DTO
├── exception/          # Tratamento de erros
├── security/           # Configurações de segurança
└── config/             # Configurações gerais
```

## 📊 Modelo de Dados

### Entidades Principais

- **Usuario**: Gerenciamento de usuários e autenticação
- **ItemCardapio**: Itens do cardápio com preços e descrições
- **Pedido**: Pedidos realizados pelos clientes
- **PedidoItem**: Itens individuais de cada pedido

### Status do Pedido

- `RECEBIDO` - Pedido criado
- `EM_PREPARO` - Sendo preparado na cozinha
- `PRONTO` - Pronto para entrega
- `ENTREGUE` - Entregue ao cliente

## 🔐 Segurança

- **JWT Tokens** para autenticação
- **Roles baseadas** (ADMIN, COZINHA, GARCOM, CLIENTE)
- **Endpoints protegidos** por role
- **Senhas criptografadas** com BCrypt
- **CORS configurado** para frontend

## 📡 API Endpoints

### Autenticação
```
POST /api/auth/login          # Login
POST /api/auth/registro       # Registro de usuário
```

### Cliente (Público)
```
GET  /api/cardapio            # Listar cardápio
POST /api/pedidos             # Criar pedido
GET  /api/pedidos/{id}        # Consultar pedido
```

### Administrador
```
GET    /api/admin/cardapio           # Listar todos os itens
POST   /api/admin/cardapio           # Adicionar item
PUT    /api/admin/cardapio/{id}      # Atualizar item
DELETE /api/admin/cardapio/{id}      # Remover item
GET    /api/admin/pedidos            # Listar pedidos
GET    /api/admin/relatorios/hoje    # Relatório do dia
```

### Cozinha
```
GET   /api/cozinha/pedidos/stream         # SSE - Novos pedidos
GET   /api/cozinha/pedidos/novos          # Pedidos recebidos
PATCH /api/cozinha/pedidos/{id}/status    # Atualizar status
```

### Garçom
```
GET   /api/garcom/pedidos/stream          # SSE - Pedidos prontos
GET   /api/garcom/pedidos/prontos         # Listar pedidos prontos
PATCH /api/garcom/pedidos/{id}/entregar   # Marcar como entregue
```

## 🔄 Server-Sent Events (SSE)

### Cozinha Stream (`/api/cozinha/pedidos/stream`)
- Recebe novos pedidos automaticamente
- Evento: `novo-pedido`
- Reconexão automática

### Garçom Stream (`/api/garcom/pedidos/stream`)
- Recebe notificações de pedidos prontos
- Evento: `pedido-pronto`
- Reconexão automática

## ⚙️ Configuração

### Banco de Dados (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cardapio_db
    username: cardapio_user
    password: cardapio_pass
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### JWT
```yaml
spring:
  security:
    jwt:
      secret: minha-chave-secreta-super-segura-para-jwt-tokens-2024
      expiration: 86400000 # 24 horas
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- PostgreSQL
- Maven

### Passos

1. **Clone o repositório**
```bash
git clone <repository-url>
cd cardapio-backend
```

2. **Configure o banco de dados**
```sql
CREATE DATABASE cardapio_db;
CREATE USER cardapio_user WITH PASSWORD 'cardapio_pass';
GRANT ALL PRIVILEGES ON DATABASE cardapio_db TO cardapio_user;
```

3. **Execute a aplicação**
```bash
mvn spring-boot:run
```

4. **Acesse a API**
- Base URL: `http://localhost:8080/api`
- Documentação: Swagger UI (se configurado)

## 👤 Usuários Padrão

O sistema vem com usuários pré-configurados (senha: `password123`):

- **Admin**: `admin@cardapio.com`
- **Cozinha**: `cozinha@cardapio.com`
- **Garçom**: `garcom@cardapio.com`
- **Cliente**: `cliente@cardapio.com`

## 🧪 Testes

Execute os testes unitários:
```bash
mvn test
```

Os testes cobrem:
- Services (MenuService, PedidoService)
- Validações de negócio
- Cenários de erro
- Mapeamentos de DTOs

## 📝 Validações

### Pedidos
- Deve ter pelo menos um item
- Quantidade deve ser maior que zero
- Mesa máximo 20 caracteres
- Observações máximo 500 caracteres

### Itens do Cardápio
- Nome obrigatório (2-100 caracteres)
- Preço maior que zero
- Descrição máximo 500 caracteres

### Usuários
- Email válido e único
- Senha mínimo 6 caracteres
- Nome 2-100 caracteres
- Role obrigatório

## 🔧 Tratamento de Erros

- **404**: Recurso não encontrado
- **400**: Dados inválidos ou erro de validação
- **401**: Não autenticado
- **403**: Sem permissão
- **500**: Erro interno do servidor

Todas as respostas de erro seguem o padrão:
```json
{
  "status": 400,
  "message": "Erro de validação",
  "timestamp": "2024-01-01T10:00:00",
  "path": "/api/pedidos",
  "validationErrors": {
    "campo": "mensagem de erro"
  }
}
```

## 📈 Melhorias Futuras

- [ ] Cache com Redis
- [ ] Métricas com Micrometer
- [ ] Documentação com OpenAPI/Swagger
- [ ] Containerização com Docker
- [ ] CI/CD Pipeline
- [ ] Logs estruturados
- [ ] Rate limiting
- [ ] Backup automático

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

