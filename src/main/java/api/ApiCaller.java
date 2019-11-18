package api;

public class ApiCaller extends MakeHttpRequest {

    @Override
    public int getStatusCode() {
        return super.getStatusCode();
    }

    public String callStatsApi(String baseCall) {
        String[] credentials = new GetCredentials().getPropValues("src/main/resources/config.properties");
        String key = credentials[0];
        String secret = credentials[1];
        String sig = new StatsApiSignature(key, secret).getSignature();
        String url = "http://api.stats.com/v1/" + baseCall + "?accept=json&api_key=" + key + "&sig=" + sig;
        return super.HttpRequest(url);
    }

    public String callFanduelApi(String dateString) {
        String url = "https://www.fanduel.com/api/playerprices?date=" + dateString;
        return super.HttpRequest(url);
    }

    public String callDraftKingsApi() {
        return super.HttpRequest("https://api.draftkings.com/partners/v1/draftpool/sports/nfl/?format=json");
    }
}
