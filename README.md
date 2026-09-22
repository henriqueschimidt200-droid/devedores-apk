# Devedores - Android Nativo

Aplicativo Android nativo para controle de clientes, empréstimos, parcelas, pagamentos, anotações, calendário e alarmes.

## Gerar o APK pelo GitHub

1. Crie um repositório no GitHub.
2. Envie **o conteúdo desta pasta**, e não a pasta pai.
3. Confirme que `.github/workflows/build-apk.yml` está diretamente na raiz do repositório.
4. Faça um commit na branch `main`.
5. Abra **Actions**.
6. Selecione **Gerar APK - Devedores**.
7. Clique em **Run workflow** e depois em **Run workflow** novamente.
8. Aguarde o job ficar verde ✅.
9. Abra a execução concluída.
10. Na seção **Artifacts**, baixe **Devedores-APK**.
11. Dentro do ZIP estará `app-debug.apk`.

O workflow instala Java 17, Gradle 8.10.2 e Android SDK 35, compila `:app:assembleDebug` e publica o APK como artifact.
