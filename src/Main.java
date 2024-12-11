import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.json.JSONObject;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey = "662a6c2d-3036-4fef-b69b-f687913729d2XXX";

        HttpClient httpClient = HttpClient.newHttpClient();

//        Сделайте GET запрос используя путь: https://api.weather.yandex.ru/v2/forecast.
        System.out.println("Задание 1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.yandex.ru/v2/forecast"))
                .header("X-Yandex-API-Key", apiKey)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            System.err.println("Error making HTTP request: " + e.getMessage());
        }

//        Передайте координаты точки lat и lon, в которой хотите определить погоду,
//        например: https://api.weather.yandex.ru/v2/forecast?lat=55.75&lon=37.62.
        System.out.println("Задание 2");
        double lat = 55.75, lon=37.62;

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.yandex.ru/v2/forecast?lat=" + lat + "&lon=" + lon))
                .header("X-Yandex-API-Key", apiKey)
                .GET()
                .build();

        try {
            HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + response2.statusCode());
            System.out.println("Response Body: " + response2.body());

        } catch (Exception e) {
            System.err.println("Error making HTTP request: " + e.getMessage());
        }

//        Выведите на экран все данные (весь ответ от сервиса в формате json)
//        и отдельно температуру (находится в fact {temp}).
        System.out.println("Задание 3");
        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        if (response2.statusCode() == 200) {
            String body = response2.body();
            System.out.println("Весь ответ от сервиса в формате JSON:");
            System.out.println(body);
            String jsonResponse = body.toString();
            int startIndex = jsonResponse.indexOf("\"temp\":") + "\"temp\":".length();
            int endIndex = jsonResponse.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = jsonResponse.length();
            }
            String temperature = jsonResponse.substring(startIndex, endIndex);
            System.out.println("Текущая температура: " + temperature + "°C");
        } else {
            System.err.println("Произошла ошибка при выполнении запроса. Код ошибки: " + response2.statusCode());
        }

//        Вычислить среднюю температуру за определенный период
//        (передать limit и найти среднее арифметическое температуры).
        System.out.println("Задание 4");

        int limit = 7;
        double[] temperatures = new double[limit];
        double sumTemperatures = 0;

        for (int i = 0; i < limit; i++) {
            LocalDate date = LocalDate.now().plusDays(i);

            String url = String.format("https://api.weather.yandex.ru/v2/forecast?lat=%s&lon=%s&limit=%d&date=%s", lat, lon, i + 1,date);
            HttpRequest requestWeek = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-Yandex-API-Key", apiKey)
                    .build();

            HttpResponse<String> responseWeek = httpClient.send(requestWeek, HttpResponse.BodyHandlers.ofString());

            if (responseWeek.statusCode() == 200) {
                JSONObject json = new JSONObject(responseWeek.body());
                double temperature = json.getJSONObject("fact").getDouble("temp");
                temperatures[i] = temperature;
                sumTemperatures += temperature;

            }
            else {
               System.err.println("Произошла ошибка при выполнении запроса. Код ошибки: " + responseWeek.statusCode());
            }
        }


    double averageTemperature = sumTemperatures / temperatures.length;

        System.out.printf("Средняя температура за %d дней: %.2f°C\n", limit, averageTemperature);

    }
}