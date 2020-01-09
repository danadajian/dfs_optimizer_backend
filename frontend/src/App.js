import React, { Component } from 'react'
import './App.css'
import { Container } from 'react-bootstrap'
import {createEmptyLineup} from './functions/createEmptyLineup'
import { getAddToLineupState } from './functions/getAddToLineupState'
import { getRemoveFromLineupState } from './functions/getRemoveFromLineupState'
import { getToggleBlackListState } from './functions/getToggleBlackListState'
import { getFilterPlayersState } from './functions/getFilterPlayersState'
import {formatDate} from "./functions/formatDate"
import { lineupStructures } from './resources/lineupStructures'
import { apiRoot } from './resources/config.js'
import {teamAbbreviations} from "./resources/teamAbbreviations";
import {GridSection} from "./ts_objects/GridSection";
import {ContestSection} from "./ts_objects/ContestSection";
import {SportSection} from "./ts_objects/SportSection";
import {injuryAbbreviations} from "./resources/injuryAbbreviations";
import { CSVLink } from "react-csv";
import {DateSection} from "./ts_objects/DateSection";

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {isLoading: false, isOptimizing: false, loadingText: '', site: '', sport: '', contest: '',
        date: new Date(), dfsData: {}, projectionsData: {}, contests: [], lineup: [], lineupPositions: [],
        displayMatrix: [], salaryCap: 0, playerPool: [], filteredPool: null, playerPoolData: [],
        sortAttribute: 'salary', sortSign: 1, searchText: '', whiteList: [], blackList: [],
        opponentRanks: {}, injuries: {}};
  }

  setSite = (site) => {
      this.setState({
          site: site,
          sport: '',
          contest: '',
          contests: [],
          playerPool: [],
          filteredPool: null,
          whiteList: [],
          blackList: [],
          lineup: []
      });
  };

  setDate = async (date) => {
      await this.setState({date: date});
      if (this.state.sport)
          await this.setSport(this.state.sport);
  };

  setSport = async (sport) => {
      let {site, date} = this.state;
      this.setState({isLoading: true, sport: sport, contest: '', lineup: []});
      let dfsData;
      if (site === 'fd') {
          this.setState({loadingText: 'Fanduel data'});
          const fanduelData = await fetch(apiRoot + '/fanduel?date=' + formatDate(date))
              .then(response => response.json());
          dfsData = fanduelData.filter(contest => contest.sport === sport.toUpperCase());
      } else {
          this.setState({loadingText: 'Draftkings data'});
          dfsData = await fetch(apiRoot + '/draftkings?sport=' + sport).then(response => response.json());
      }
      this.setState({loadingText: sport.toUpperCase() + ' projections'});
      const projectionsData = await fetch(apiRoot + '/projections?sport=' + sport)
          .then(response => response.json());
      const contests = this.extractContestsFromData(dfsData);
      this.setState({loadingText: sport.toUpperCase() + ' opponent ranks'});
      const opponentRanks = await fetch(apiRoot + '/opponentRanks?sport=' + sport)
          .then(response => response.json());
      this.setState({loadingText: 'injury data'});
      const injuries = await fetch(apiRoot + '/injuries?sport=' + sport)
          .then(response => response.json());
      this.setState({
          isLoading: false,
          projectionsData: projectionsData,
          dfsData: dfsData,
          contests: contests,
          opponentRanks: opponentRanks,
          injuries: injuries,
          playerPool: [],
          filteredPool: null,
          whiteList: [],
          blackList: []
      });
  };

  setContest = (contest) => {
      if (!contest)
          return;
      let {site, sport, dfsData, projectionsData} = this.state;
      if (dfsData.length === 0)
          alert(site + ' data is currently unavailable.');
      if (Object.keys(projectionsData).length === 0 || projectionsData['errorMessage'])
          alert('Player projection data is currently unavailable.');
      let gameType = contest.includes('@') || contest.includes('vs') ? 'Single Game' : 'Classic';
      let lineupPositions = lineupStructures[site][sport][gameType].lineupPositions;
      let displayMatrix = lineupStructures[site][sport][gameType].displayMatrix;
      let salaryCap = lineupStructures[site][sport][gameType].salaryCap;
      let dfsPlayers = dfsData.filter(contestJson => contestJson.contest === contest)[0]['players'];
      let playerPoolData = this.combineData(dfsPlayers, projectionsData);
      let emptyLineup = createEmptyLineup(lineupPositions, displayMatrix);
      this.setState({
          contest: contest,
          playerPool: playerPoolData,
          filteredPool: null,
          playerPoolData: playerPoolData,
          whiteList: [],
          blackList: [],
          lineup: emptyLineup,
          lineupPositions: lineupPositions,
          displayMatrix: displayMatrix,
          salaryCap: salaryCap
      });
  };

  combineData = (dfsPlayers, projectionsData) => {
      let {site, opponentRanks, injuries} = this.state;
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
              if (status !== 'out') {
                  player.status = injuryAbbreviations[status];
                  combinedData.push(player);
              }
          }
      });
      return combinedData;
  };

  extractContestsFromData = (dataArray) => {
      let {site, date} = this.state;
      let contestArray = [];
      if (site === 'fd')
          dataArray.forEach(contestJson => contestArray.push(contestJson.contest));
      else
          dataArray
              .filter(contestJson => {
                  let contestName = contestJson.contest;
                  let contestDate = contestName.split(' ')[contestName.split(' ').length - 1].slice(1, -1);
                  let month = parseInt(contestDate.split('/')[0]);
                  let day = parseInt(contestDate.split('/')[1]);
                  return date.getMonth() + 1 === month && date.getDate() === day;
              })
              .forEach(contestJson => contestArray.push(contestJson.contest));
      return contestArray;
  };

  filterPlayers = (attribute, value) => {
    let newState = getFilterPlayersState(attribute, value, this.state);
    this.setState(newState);
  };

  addToLineup = (playerIndex) => {
    let newState = getAddToLineupState(playerIndex, this.state);
    if (typeof newState === 'string') {
      alert(newState);
    } else {
      this.setState(newState);
    }
  };

  removeFromLineup = (playerIndex) => {
    let newState = getRemoveFromLineupState(playerIndex, this.state);
    this.setState(newState);
  };

  clearLineup = () => {
      let {lineupPositions, displayMatrix} = this.state;
      this.setState({
          lineup: createEmptyLineup(lineupPositions, displayMatrix),
          whiteList: [],
          blackList: []
      })
  };

  toggleBlackList = (playerIndex) => {
    let newState = getToggleBlackListState(playerIndex, this.state);
    this.setState(newState);
  };

  sortAttribute = (a, b) => {
      let {sortAttribute, sortSign} = this.state;
      if (sortAttribute === 'pricePerPoint')
          return sortSign * (b.salary / b.projection - a.salary / a.projection);
      else
          return sortSign*(b[sortAttribute] - a[sortAttribute])
  };
  
  toggleSort = (attribute) => {
      if (attribute === this.state.sortAttribute) {
          this.setState({sortSign: -this.state.sortSign})
      } else {
          this.setState({sortAttribute: attribute})
      }
  };

  generateOptimalLineup = async () => {
      let {lineup, playerPool, blackList, lineupPositions, displayMatrix, salaryCap, playerPoolData} = this.state;
      if (playerPool.length === 0) {
          alert('Insufficient player data to optimize.');
          return
      }
      this.setState({isOptimizing: true});
      const playerIds = await fetch(apiRoot + '/optimize', {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({
              'lineup': lineup,
              'playerPool': playerPool,
              'blackList': blackList,
              'lineupPositions': lineupPositions,
              'salaryCap': salaryCap
          })
      }).then(response => response.json());
      if (!Array.isArray(playerIds) || playerIds.includes(0)) {
          alert('Optimal lineup could not be found.' + playerIds['errorMessage'] ?
              playerIds['errorType'] + '\n' + playerIds['errorMessage'] + '\n' + playerIds['stackTrace'].slice(0, 4) : '');
          this.setState({isOptimizing: false})
      } else {
          let optimalLineup = playerIds.map(playerId => playerPool.find(player => player.playerId === playerId));
          optimalLineup.forEach((player, index) => player.displayPosition = displayMatrix[index]);
          this.setState({
              isOptimizing: false,
              lineup: optimalLineup,
              playerPool: playerPoolData
          })
      }
  };

  render() {
    const {isLoading, isOptimizing, loadingText, sport, site, contest, date, contests, lineup, salaryCap, playerPool,
        filteredPool, sortAttribute, sortSign, searchText, whiteList, blackList, displayMatrix} = this.state;

    const csvData = [
        displayMatrix,
        lineup.map(player => player.name)
    ];

    return (
        <Container fluid={true}>
          <h1 className={"App-header"}>DFS Optimizer</h1>
          <div className={"Dfs-sport"}>
            <h3>Choose a site:</h3>
            <div style={{display: 'flex'}}>
              <button style={{backgroundColor: (site === 'fd') ? 'dodgerblue' : 'white'}}
                      onClick={() => this.setSite('fd')}>Fanduel</button>
              <button style={{backgroundColor: (site === 'dk') ? 'dodgerblue' : 'white'}}
                      onClick={() => this.setSite('dk')}>Draftkings</button>
            </div>
            <DateSection site={site} date={date} setDate={this.setDate}/>
            <SportSection isLoading={isLoading} site={site} sport={sport} setSport={this.setSport}/>
            <ContestSection isLoading={isLoading} site={site} sport={sport} contest={contest} contests={contests}
                            setContest={this.setContest}/>
            <div style={{display: 'flex', margin: '2%'}}>
                {sport && contest && site && <button style={{marginTop: '10px'}}
                                                     onClick={this.generateOptimalLineup}>Optimize Lineup</button>}
              {sport && contest && site && <button style={{marginTop: '10px'}}
                                                   onClick={this.clearLineup}>Clear Lineup</button>}
            </div>
            {lineup.length > 0 && lineup.every(player => player.name.length > 0) &&
            <CSVLink data={csvData} filename={site + '-' + sport + '-lineup.csv'}>Download Lineup CSV</CSVLink>}
          </div>
          <GridSection isLoading={isLoading} isOptimizing={isOptimizing} loadingText={loadingText} site={site}
                       sport={sport} contest={contest} lineup={lineup} playerPool={playerPool}
                       filteredPool={filteredPool} whiteList={whiteList} blackList={blackList} searchText={searchText}
                       filterPlayers={this.filterPlayers} addToLineup={this.addToLineup}
                       removeFromLineup={this.removeFromLineup} toggleBlackList={this.toggleBlackList}
                       sortAttributeFunction={this.sortAttribute} sortAttribute={sortAttribute} sortSign={sortSign}
                       toggleSort={this.toggleSort} salaryCap={salaryCap}/>
        </Container>
    )
  }
}

export default App;
