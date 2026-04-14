#!/bin/bash
# ============================================================
# gerar-certificados.sh
#
# Gera os certificados necessários para o ambiente local:
#
#  ispb.p12      — KeyStore PKCS12 com chave privada RSA + cert X.509
#                  (simula o certificado ISPB emitido pela ICP-Brasil)
#
#  rsfn-trust.jks — TrustStore JKS com o certificado da CA
#                   (simula a CA raiz da RSFN que o BACEN usa)
#
# Em produção esses arquivos vêm da ICP-Brasil / BACEN.
# Aqui usamos certificados autoassinados apenas para dev/local.
# ============================================================

set -e

DIR="$(cd "$(dirname "$0")" && pwd)"
DAYS=3650          # 10 anos — não queremos expiração no dev
ISPB="00000000"    # ISPB fictício do PSP dev
KEY_PASS="changeit"
TRUST_PASS="changeit"

echo "Gerando certificados em: $DIR"

# -----------------------------------------------
# 1. Gera a chave privada RSA 2048 + certificado
#    X.509 autoassinado (representa o ISPB do PSP)
# -----------------------------------------------
echo "1/3 Gerando chave privada RSA e certificado X.509..."
openssl req -newkey rsa:2048 -nodes \
  -keyout "$DIR/ispb-private.key" \
  -x509 -days $DAYS \
  -out "$DIR/ispb-cert.pem" \
  -subj "/CN=PSP-DEV-ISPB-$ISPB/O=PSP Development/OU=ISPB-$ISPB/C=BR" \
  -extensions v3_ca \
  -addext "basicConstraints=CA:FALSE" \
  -addext "keyUsage=digitalSignature,nonRepudiation,keyEncipherment" \
  -addext "extendedKeyUsage=clientAuth"

echo "   Certificado gerado: ispb-cert.pem"

# -----------------------------------------------
# 2. Empacota chave + certificado no PKCS12 (.p12)
#    Este é o arquivo que o XmlDsigSigner carrega
# -----------------------------------------------
echo "2/3 Criando KeyStore PKCS12 (ispb.p12)..."
openssl pkcs12 -export \
  -in "$DIR/ispb-cert.pem" \
  -inkey "$DIR/ispb-private.key" \
  -out "$DIR/ispb.p12" \
  -name "ispb" \
  -passout "pass:$KEY_PASS"

echo "   KeyStore criado: ispb.p12 (senha: $KEY_PASS)"

# -----------------------------------------------
# 3. Cria o TrustStore JKS com o certificado da CA
#    O WebClient usa para confiar no servidor RSFN
#    Em dev: confiamos no próprio cert autoassinado
# -----------------------------------------------
echo "3/3 Criando TrustStore JKS (rsfn-trust.jks)..."

# Remove arquivo anterior se existir
rm -f "$DIR/rsfn-trust.jks"

keytool -importcert \
  -noprompt \
  -alias "rsfn-ca" \
  -file "$DIR/ispb-cert.pem" \
  -keystore "$DIR/rsfn-trust.jks" \
  -storetype JKS \
  -storepass "$TRUST_PASS"

echo "   TrustStore criado: rsfn-trust.jks (senha: $TRUST_PASS)"

# -----------------------------------------------
# Limpeza de arquivos intermediários
# -----------------------------------------------
rm -f "$DIR/ispb-private.key"

echo ""
echo "Arquivos gerados:"
ls -lh "$DIR"/*.p12 "$DIR"/*.jks "$DIR"/*.pem

echo ""
echo "Verificando o conteudo do KeyStore:"
keytool -list -v \
  -keystore "$DIR/ispb.p12" \
  -storetype PKCS12 \
  -storepass "$KEY_PASS" 2>/dev/null | grep -E "Alias|Owner|Issuer|Serial|Valid"

echo ""
echo "Pronto! Copie os arquivos para o volume do Docker:"
echo "  docker cp certs/ispb.p12      pix-payment-service:/certs/"
echo "  docker cp certs/rsfn-trust.jks pix-payment-service:/certs/"
