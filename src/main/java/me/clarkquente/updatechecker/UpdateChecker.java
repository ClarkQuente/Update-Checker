package me.clarkquente.updatechecker;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiredArgsConstructor
public final class UpdateChecker {

    private String LATEST_RELEASE = "https://api.github.com/repos/%s/%s/releases/latest";

    private final String path, version, user, repository;
    private final boolean downloadAutomatically;

    public boolean checkVersion() {
        try {
            final URL url = new URL(String.format(LATEST_RELEASE, user, repository));
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            final InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            final StringBuilder builder = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                builder.append(inputLine);
            }
            inputStreamReader.close();
            bufferedReader.close();

            final JsonObject jsonObject = JsonParser.parseString(builder.toString()).getAsJsonObject();
            final String latestVersion = jsonObject.get("tag_name").getAsString();
            if(latestVersion.equalsIgnoreCase(version)) {
                System.out.println("[UpdateChecker] "+repository+" está atualizado. Aproveite!");
                urlConnection.disconnect();
                return false;
            }

            System.out.println("[UpdateChecker] "+repository+" está desatualizado! Versão atual: "+version+". Nova versão: "+latestVersion+".\n");
            System.out.println("[UpdateChecker] O download automático está " + (downloadAutomatically ? "ativado. Iniciando download." : "desativado. Baixe a nova versão manualmente."));
            if(!downloadAutomatically) return false;

            final JsonArray assets = jsonObject.getAsJsonArray("assets");
            if(assets == null) {
                System.out.println("[UpdateChecker] "+repository+" está desatualizado, mas a última versão não possui arquivo para ser baixado!");
                urlConnection.disconnect();
                return false;
            }

            final JsonElement element = assets.get(0);
            if(element == null) {
                System.out.println("[UpdateChecker] Houve uma falha na tentativa de baixar a última versão de "+repository+"!");
                urlConnection.disconnect();
                return false;
            }

            final JsonObject latestAsset = element.getAsJsonObject();

            final String assetfileName = latestAsset.get("name").getAsString();
            final String assetURI = latestAsset.get("browser_download_url").getAsString();

            final URL assetURL = new URL(assetURI);
            downloadFile(assetfileName, assetURL);

            System.out.println("[UpdateChecker] Uma nova versão do projeto foi baixada de "+repository+"!");
            urlConnection.disconnect();
            return true;
        } catch(IOException exception) {
            System.out.println("Houve um erro na tentativa de acessar a URL. Verifique se o nome de usuário e o repositório ou o path do arquivo estão inseridos corretamente.");
            return false;
        }
    }

    private void downloadFile(String fileName, URL url) throws IOException {
        final File file = new File(path + "\\" + fileName);

        try(
                InputStream inputStream = url.openStream();
                FileOutputStream fileOutputStream = new FileOutputStream(file)
        ) {
            int bytes = 0;

            while ((bytes = inputStream.read()) != -1) {
                fileOutputStream.write(bytes);
            }
        }
    }
}