# alpha.141: diagnóstico do workflow Gerar APK 131

## Falha comprovada (31/10/2026)

O workflow terminou antes do Gradle no passo `android-actions/setup-android@v3`:

```text
[command].../sdkmanager tools
Warning: Failed to find package 'tools'
Error: .../sdkmanager failed with exit code 1
```

O pacote legado `tools` não está disponível nos repositórios atuais do Android SDK. Por isso, o log **não** demonstra erro no código Kotlin, lint ou testes unitários.

## Alteração

- Substituído `android-actions/setup-android@v3` por `@v4` nos workflows **Gerar APK**, **Gerar AAB para Google Play** e **Desempenho**.
- Declarado `packages: platform-tools`, evitando solicitações implícitas ao pacote removido.
- Preservadas as instalações `sdkmanager "platforms;android-36"` e, para emulador de desempenho, `"platforms;android-33"`.
- `tools/validate_ci_android_sdk.py` é chamado **antes** do setup SDK em cada workflow, para detectar regressões antes de instalar pacotes.
- Corrigido `app_identity.json` para SDK 36, coerente com o Gradle. `tools/validate_play_readiness.py` agora verifica o SDK e não fixa a versão alpha.140.

## Validação e pendências

Validadores estáticos locais, YAML/JSON/XML e integridade do arquivo-fonte devem passar. Apenas um novo workflow do GitHub Actions pode confirmar o build de APK/AAB em ambiente Android completo. A assinatura do APK também requer os Secrets configurados no repositório.

Fonte técnica: https://github.com/android-actions/setup-android/blob/main/README.md
