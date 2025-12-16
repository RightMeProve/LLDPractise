public class WeatherStation {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();

        // 1. Register Observers
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);

        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);

        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData);

        // 2. Simulate Updates
        System.out.println("\n--- Update 1 (All Active) ---");
        weatherData.setMeasurements(80, 65, 30.4f);

        System.out.println("\n--- Update 2 (Forecast Unsubscribes) ---");
        weatherData.removeObserver(forecastDisplay);
        weatherData.setMeasurements(82, 70, 29.2f);

        System.out.println("\n--- Update 3 (Forecast Re-subscribes) ---");
        weatherData.registerObserver(forecastDisplay);
        weatherData.setMeasurements(78, 90, 29.2f);

        System.out.println("\n--- Update 4 (Pressure Rises) ---");
        weatherData.setMeasurements(76, 50, 30.1f);

        System.out.println("\n--- Update 5 (Statistics Unsubscribes) ---");
        weatherData.removeObserver(statisticsDisplay);
        weatherData.setMeasurements(70, 45, 29.8f);

        System.out.println("\n--- Update 6 (Heatwave) ---");
        weatherData.setMeasurements(95, 30, 29.8f);
    }
}