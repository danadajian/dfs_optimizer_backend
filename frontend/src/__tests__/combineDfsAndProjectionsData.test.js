import { combineDfsAndProjectionsData } from '../functions/combineDfsAndProjectionsData'

describe('test', () => {
    test('combines data correctly', () => {
        let dfsPlayers = [
            {"position":"RB","salary":4900,"name":"LeSean McCoy"},
            {"position":"RB","salary":4500,"playerId":400947,"name":"Anthony Sherman"}
        ];
        let projectionsData = {
            "397945":{"overUnder":53,"gameDate":"Sun 3:05PM EST","opponent":"v. Ten",
                "dkProjection":0.8460388191826829,"name":"LeSean McCoy","fdProjection":0.7579393092959834,
                "team":"KC","spread":"-7.0"},
            "400947":{"overUnder":53,"gameDate":"Sun 3:05PM EST","opponent":"v. Ten",
                "dkProjection":0.5435214312075175,"name":"Anthony Sherman","fdProjection":0.4296565759700945,
                "team":"KC","spread":"-7.0"}
        };
        let site = 'fd';
        let opponentRanks = {"Tennessee Titans":{"RB":16,"QB":18,"TE":27,"D/ST":8,"WR":15,"K":6}};
        let injuries = {"LeSean McCoy":"Questionable"};
        expect(combineDfsAndProjectionsData(dfsPlayers, projectionsData, site, opponentRanks, injuries))
            .toEqual([
                {
                    "name":"LeSean McCoy",
                    "team":"KC",
                    "opponent":"v. Ten",
                    "gameDate":"Sun 3:05PM EST",
                    "spread":"-7.0",
                    "overUnder":53,
                    "projection": 0.7579393092959834,
                    "opponentRank":16,
                    "status":"Q",
                    "position":"RB",
                    "salary":4900,
                    "playerId":397945
                },
                {
                    "name":"Anthony Sherman",
                    "team":"KC",
                    "opponent":"v. Ten",
                    "gameDate":"Sun 3:05PM EST",
                    "spread":"-7.0",
                    "overUnder":53,
                    "projection": 0.4296565759700945,
                    "opponentRank":16,
                    "position":"RB",
                    "salary":4500,
                    "playerId":400947
                }
            ])
    });
});