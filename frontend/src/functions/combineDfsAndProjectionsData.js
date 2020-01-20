import {teamAbbreviations} from "../resources/teamAbbreviations";
import {injuryAbbreviations} from "../resources/injuryAbbreviations";

export function combineDfsAndProjectionsData(dfsPlayers, projectionsData, site, opponentRanks, injuries) {
  let combinedData = [];
  dfsPlayers.forEach(player => {
    let playerData = projectionsData[player.playerId];
    if (playerData) {
      player.name = playerData.name;
      player.team = playerData.team;
      player.opponent = playerData.opponent;
      player.gameDate = playerData.gameDate;
      player.spread = playerData['spread'];
      player.overUnder = playerData.overUnder;
      player.projection = playerData[site + 'Projection'];
      if (Object.keys(opponentRanks).length > 0) {
        let opposingTeam = playerData.opponent.split(' ')[1];
        let teamRanks = opponentRanks[teamAbbreviations[opposingTeam]];
        let opponentRankPosition = Object.keys(teamRanks)
          .find(position => position.replace('/', '').includes(player.position));
        player.opponentRank = teamRanks[opponentRankPosition];
      }
      let status = injuries[playerData.name] ? injuries[playerData.name].toLowerCase() : '';
      player.status = injuryAbbreviations[status];
      combinedData.push(player);
    }
  });
  return combinedData;
}