package api;

public class CallApi {
    public static String callStatsApi(String baseCall, String params) {
        String[] credentials = new GetCredentials().getPropValues("resources/config.properties");
        String key = credentials[0];
        String secret = credentials[1];
        String sig = new StatsApiSignature(key, secret).getSignature();
        String url = "http://api.stats.com/v1/" + baseCall + "?accept=json&api_key=" + key + "&sig=" + sig + params;
        return new MakeHttpRequest().HttpRequest(url);
    }

    public static String callFanduelApi(int dayOfWeek) {
        String dateString = new DateOperations().getDfsDateString(dayOfWeek);
        String url = "https://www.fanduel.com/api/playerprices?date=" + dateString;
        return new MakeHttpRequest().HttpRequest(url);
    }

    public static String callDraftKingsApi() {
        return new MakeHttpRequest()
                .HttpRequest("https://api.draftkings.com/partners/v1/draftpool/sports/nfl/?format=json");
    }
}
