# Explorador XP 0.1.0-alpha.141

**Versão:** `0.1.0-alpha.141`  
**versionCode:** `141`

## Correção do workflow Gerar APK 131

- A etapa `android-actions/setup-android@v3` tentava executar `sdkmanager tools`, mas o pacote `tools` não é mais distribuído.
- Atualizado para `android-actions/setup-android@v4` e configurado `packages: platform-tools` nos workflows de APK, AAB e desempenho.
- Mantida instalação explícita de `platforms;android-36` e API 33 apenas no workflow que executa emulador.
- Validador Play verifica a versão do action, o pacote e a coerência da API 36.
- Sincroniza `app_identity.json` com `compileSdk = 36` e `targetSdk = 36`, que já estavam corretos no Gradle.

## Validação e pendências

Validação de arquivos, scripts e metadados realizada localmente. O erro relatado ocorreu **antes da compilação Gradle**; não há prova de que APK/AAB compilarão até que os workflows sejam executados no GitHub Actions.
