# Explorador XP 0.1.0-alpha.140

**Versão:** `0.1.0-alpha.140`  
**versionCode:** `140`

## Preparação para a Play Store

- `compileSdk` e `targetSdk` atualizados para **36** (Android 16), preservando `minSdk 26`.
- Android Gradle Plugin `8.13.2` e Gradle `8.13` configurados para os workflows.
- Workflow independente `Gerar AAB para Google Play` com testes, lint, assinatura via Secrets e Artifact.
- Política de privacidade explicativa dentro de **Sobre**; minuta pública e declarações de permissões documentadas.
- Não altera assinatura existente, ID do pacote ou os recursos centrais.

## Antes de publicar

- Fazer build **real** e testes Android 16 (edge-to-edge, Back, armazenamento, arquivos e APKs).
- Confirmar e publicar política de privacidade em URL HTTPS pública.
- Preencher Data safety e as declarações de permissões de alto risco na Play Console.
- Confirmar a chave de upload e aderir ao Play App Signing.
- Criar ficha, capturas e formulário de classificação indicativa.

Esta entrega é código-fonte, não um bundle já compilado ou publicação aprovada.
