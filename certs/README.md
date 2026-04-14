# Certificados — Ambiente Local / Dev

## Arquivos gerados

| Arquivo | Tipo | Usado por | Conteúdo |
|---|---|---|---|
| `ispb.p12` | PKCS12 (KeyStore) | `XmlDsigSigner` + `WebClientConfig` | Chave privada RSA + certificado X.509 do PSP |
| `rsfn-trust.jks` | JKS (TrustStore) | `WebClientConfig` | Certificado da CA — diz "confio nessa CA" |
| `ispb-cert.pem` | PEM | Referência / debug | Certificado X.509 em texto legível |
| `gerar-certificados.sh` | Script | Desenvolvedor | Regera tudo do zero |

---

## O que cada arquivo representa em produção

### `ispb.p12` — o certificado ISPB

Em produção este arquivo vem da **ICP-Brasil** (Infraestrutura de Chaves Públicas Brasileira),
emitido especificamente para o ISPB do PSP. O processo é:

1. O PSP gera um par de chaves RSA (chave privada fica no HSM, pública vira CSR)
2. O PSP envia o CSR (Certificate Signing Request) ao BACEN
3. O BACEN assina o certificado vinculando o ISPB à chave pública
4. O PSP recebe o certificado assinado e monta o `.p12`

Usado para **duas finalidades**:
- **mTLS**: autenticar o PSP ao conectar na RSFN (handshake TLS mútuo)
- **XMLDSig**: assinar digitalmente o XML pacs.008 antes de enviar ao SPI

### `rsfn-trust.jks` — o TrustStore

Contém os certificados das CAs que o PSP confia. Em produção contém:
- CA raiz do BACEN (para confiar no certificado do servidor SPI/DICT)
- CA da RSFN (Rede do Sistema Financeiro Nacional)

Disponível para download no site do BACEN em:
`https://www.bcb.gov.br/estabilidadefinanceira/comunicacaodados`

---

## Como usar no Docker

```bash
# Copiar para dentro do container em execução
docker cp certs/ispb.p12       pix-payment-service:/certs/
docker cp certs/rsfn-trust.jks pix-payment-service:/certs/

# Ou via docker-compose (já configurado em docker-compose.yml):
# volumes:
#   - ./certs:/certs:ro
```

---

## Senhas

| Arquivo | Senha |
|---|---|
| `ispb.p12` | `changeit` |
| `rsfn-trust.jks` | `changeit` |

Configuradas em `application-local.yml`:
```yaml
pix:
  dict:
    ssl:
      key-store-pass:   changeit
      trust-store-pass: changeit
```

---

## Regerar os certificados

```bash
cd certs/
bash gerar-certificados.sh
```

Os certificados gerados aqui expiram em 2036 — suficiente para dev.

---

## AVISO

Os arquivos `.p12` e `.jks` desta pasta são **apenas para desenvolvimento local**.
Nunca commitar certificados de produção no repositório.
Adicione ao `.gitignore`:

```
certs/*.p12
certs/*.jks
certs/*.key
certs/*.pem
!certs/gerar-certificados.sh
!certs/README.md
```
