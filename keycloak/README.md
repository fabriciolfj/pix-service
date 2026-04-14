# Keycloak — Ambiente Local

## Acesso

- **Admin Console**: http://localhost:8180
- **Usuário**: `admin`
- **Senha**: `admin`
- **Realm**: `pix`

## Obter token JWT (client_credentials)

```bash
curl -s -X POST \
  http://localhost:8180/realms/pix/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=pix-client" \
  -d "client_secret=pix-secret" | jq .
```

### Resposta esperada

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIi...",
  "expires_in": 300,
  "token_type": "Bearer",
  "scope": "profile email"
}
```

### Usar o token para chamar a API Pix

```bash
# 1. Obter o token
TOKEN=$(curl -s -X POST \
  http://localhost:8180/realms/pix/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=pix-client" \
  -d "client_secret=pix-secret" | jq -r '.access_token')

# 2. Criar pagamento Pix
curl -s -X POST http://localhost:8080/api/v1/pix/pagamentos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "payerKey":    { "key": "52998224725",    "keyType": "CPF"  },
    "receiverKey": { "key": "11222333000181", "keyType": "CNPJ" },
    "amount":      100.00,
    "description": "Pagamento de teste",
    "type":        "PAYMENT"
  }' | jq .
```

## Configuração do realm

O realm `pix` é importado automaticamente pelo Keycloak na inicialização
a partir de `keycloak/pix-realm.json`.

Contém:
- **Client** `pix-client` (client_credentials flow)
- **Roles** `pix-merchant` e `pix-admin`
