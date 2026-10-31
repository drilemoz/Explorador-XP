#!/usr/bin/env python3
"""Confere o SDK no CI e impede regressão para o pacote obsoleto `tools`.

Verificação estática: a compilação real depende do GitHub Actions.
"""
from pathlib import Path
import re

root = Path(__file__).resolve().parents[1]
workflow_dir = root / '.github/workflows'
for name in ('gerar-apk.yml', 'google-play-aab.yml', 'desempenho.yml'):
    text = (workflow_dir / name).read_text(encoding='utf-8')
    setup = re.findall(r'(?m)^\s*uses:\s*android-actions/setup-android@([^\s#]+)\s*$', text)
    assert setup == ['v4'], f'{name}: setup-android deve usar v4: {setup}'
    # Impede regressão no pacote removido e exige apenas platform-tools no setup.
    assert re.search(r'(?m)^\s*packages:\s*[\'\"]?platform-tools[\'\"]?\s*$', text), f'{name}: packages deve ser platform-tools'
    assert not re.search(r'(?m)^\s*(?:run:|packages:)\s*.*(?:sdkmanager\s+[\'\"]?tools\b|\btools\s+platform-tools\b)', text), f'{name}: pacote tools obsoleto'
    assert 'sdkmanager "platforms;android-36"' in text, f'{name}: plataforma API 36 ausente'
    assert text.index('python3 tools/validate_ci_android_sdk.py') < text.index('uses: android-actions/setup-android@v4'), f'{name}: validação deve ocorrer antes da instalação SDK'
print('OK: APK, AAB e desempenho usam setup-android@v4, platform-tools e API 36, sem tools legado')
