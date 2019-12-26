package api;

public class ApiCaller extends HttpRequest {

    @Override
    public int getStatusCode() {
        return super.getStatusCode();
    }

    public String callStatsApi(String baseCall) {
        ApiCredentials apiCredentials = new ApiCredentials();
        String[] credentials = apiCredentials.getPropValues("src/main/resources/config.properties");
        String key = credentials[0];
        String secret = credentials[1];
        String sig = apiCredentials.getSignature(key, secret);
        String url = "http://api.stats.com/v1/" + baseCall + "?accept=json&api_key=" + key + "&sig=" + sig;
        return super.makeHttpRequest(url);
    }

    public String callFanduelApi(String dateString) {
        String url = "https://www.fanduel.com/api/playerprices?date=" + dateString;
        return super.makeHttpRequest(url);
    }

    public String callDraftKingsApi(String sport) {
        return super.makeHttpRequest("https://api.draftkings.com/partners/v1/draftpool/sports/" + sport);
    }

    public String scrapeFantasyProsSite(String sport) {
        if (sport.equals("nfl"))
            return super.makeHttpRequest("https://www.fantasypros.com/nfl/points-allowed.php");
        else
            return "";
    }

    public String scrapeEspnInjuriesSite(String sport) {
        return super.makeHttpRequest("http" + (sport.equals("nfl") ? "s" : "") + "://www.espn.com/" + sport + "/injuries");
    }
}
