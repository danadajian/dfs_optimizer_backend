import {INJURY_ABBREVIATIONS, TEAM_ABBREVIATIONS} from "../../constants";

export const combineDfsAndProjectionsData = (dfsPlayers: any[], projectionsData: any, site: string,
                                                   opponentRanks: any, injuries: any, playerStatuses: any) => {
    let combinedData: any[] = [];
    dfsPlayers.forEach((player: any) => {
        if (!player.playerId)
            player.playerId = parseInt(
                Object.keys(projectionsData)
                    .filter(playerId => projectionsData[playerId].name === player.name)[0]
            );
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
                const opposingTeam = playerData.opponent.split(' ')[1];
                const teamRanks = opponentRanks[TEAM_ABBREVIATIONS[opposingTeam]];
                const opponentRankPosition: any = Object.keys(teamRanks)
                    .find(position => position.replace('/', '').includes(player.position));
                player.opponentRank = teamRanks[opponentRankPosition];
            }
            let playerStatus = playerStatuses.find((player: any) => player.name === playerData.name) ?
                playerStatuses.find((player: any) => player.name === playerData.name).status : '';
            let injuryStatus = injuries[playerData.name] ? injuries[playerData.name].toLowerCase() : '';
            if (playerStatus || injuryStatus)
                player.status = `${INJURY_ABBREVIATIONS[injuryStatus] || ''}${playerStatus ? ' ' : ''}${playerStatus}`;
            combinedData.push(player);
        }
    });
    return combinedData;
};