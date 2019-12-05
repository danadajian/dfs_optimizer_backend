import React, { Component } from 'react'
import './App.css'
import { Container } from 'react-bootstrap'
import DatePicker from 'react-date-picker'
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

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {isLoading: false, isOptimizing: false, loadingText: '', site: '', sport: '', contest: '',
        date: new Date(), fanduelData: {}, dfsData: {}, projectionsData: {}, contests: [], sports: [],
        lineup: [], lineupMatrix: [], displayMatrix: [], salaryCap: 0, playerPool: [], filteredPool: null,
        playerPoolData: [], sortAttribute: 'salary', sortSign: 1, searchText: '', whiteList: [], blackList: [],
        opponentRanks: {}, injuries: {}};
  }

  getFanduelData = async (date) => {
      this.setState({isLoading: true, loadingText: 'Fanduel data'});
      const fanduelData = await fetch(apiRoot + '/fanduel?date=' + formatDate(date))
          .then(response => response.json());
      this.setState({
          isLoading: false,
          date: date,
          fanduelData: fanduelData,
          sport: ''
      });
  };

  setSite = async (site) => {
      this.setState({
          site: site,
          sport: '',
          contest: '',
          contests: [],
          playerPool: [],
          filteredPool: null,
          whiteList: [],
          blackList: []
      });
      if (site === 'fd')
          await this.getFanduelData(this.state.date);
  };

  setSport = async (sport) => {
      let {site, fanduelData} = this.state;
      this.setState({isLoading: true, sport: sport, contest: ''});
      let dfsData;
      if (site === 'fd') {
          dfsData = fanduelData.filter(contest => contest.sport === sport.toUpperCase());
      } else {
          this.setState({loadingText: 'Draftkings data'});
          dfsData = await fetch(apiRoot + '/draftkings?sport=' + sport).then(response => response.json());
      }
      this.setState({loadingText: sport.toUpperCase() + ' projections'});
      const projectionsData = await fetch(apiRoot + '/projections?sport=' + sport)
          .then(response => response.json());
      const contests = dfsData.length > 0 ? this.extractContestsFromData(dfsData) : [];
      this.setState({loadingText: sport.toUpperCase() + ' opponent ranks'});
      const opponentRanks = sport === 'nfl' ?
          await fetch(apiRoot + '/opponentRanks').then(response => response.json()) : {};
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
      let {site, sport, dfsData, projectionsData} = this.state;
      let gameType = contest.includes('@') || contest.includes('vs') ? 'Single Game' : 'Classic';
      let lineupMatrix = lineupStructures[site][sport][gameType].lineupMatrix;
      let displayMatrix = lineupStructures[site][sport][gameType].displayMatrix;
      let salaryCap = lineupStructures[site][sport][gameType].salaryCap;
      let dfsPlayers = dfsData.filter(contestJson => contestJson.contest === contest)[0]['players'];
      let playerPoolData = this.combineData(dfsPlayers, projectionsData);
      let emptyLineup = createEmptyLineup(lineupMatrix, displayMatrix);
      this.setState({
          contest: contest,
          playerPool: playerPoolData,
          filteredPool: null,
          playerPoolData: playerPoolData,
          whiteList: [],
          blackList: [],
          lineup: emptyLineup,
          lineupMatrix: lineupMatrix,
          displayMatrix: displayMatrix,
          salaryCap: salaryCap
      });
  };

  combineData = (dfsPlayers, projectionsData) => {
      let {site, sport, opponentRanks, injuries} = this.state;
      let combinedData = [];
      dfsPlayers.forEach(player => {
          let playerData = projectionsData[player.playerId];
          if (playerData) {
              player.name = playerData.name;
              player.team = playerData.team;
              player.opponent = playerData.opponent;
              player.gameDate = playerData.gameDate;
              player.projection = playerData[site + 'Projection'];
              if (sport === 'nfl') {
                  let opposingTeam = playerData.opponent.split(' ')[1];
                  let teamRanks = opponentRanks[teamAbbreviations[opposingTeam]];
                  let opponentRankPosition = Object.keys(teamRanks).find(position => position.includes(player.position));
                  player.opponentRank = teamRanks[opponentRankPosition];
              }
              let status = injuries[playerData.name] ? injuries[playerData.name].toLowerCase() : '';
              player.status = injuryAbbreviations[status];
              combinedData.push(player);
          }
      });
      return combinedData;
  };

  extractContestsFromData = (dataArray) => {
      let contestArray = [];
      dataArray.forEach(contestJson => contestArray.push(contestJson.contest));
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
      let {lineupMatrix, displayMatrix} = this.state;
      this.setState({
          lineup: createEmptyLineup(lineupMatrix, displayMatrix),
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
      let {playerPool, whiteList, blackList, lineupMatrix, displayMatrix, salaryCap, playerPoolData} = this.state;
      this.setState({isOptimizing: true});
      const playerIds = await fetch(apiRoot + '/optimize', {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({
              'players': playerPool,
              'whiteList': whiteList,
              'blackList': blackList,
              'lineupMatrix': lineupMatrix,
              'salaryCap': salaryCap
          })
      }).then(response => response.json());
      if (!Array.isArray(playerIds) || playerIds.includes(0)) {
          let positionsWithIssue = [];
          playerIds.forEach((id, index) => {
              if (id === 0)
                  positionsWithIssue.push(displayMatrix[index]);
          });
          alert('Optimal lineup could not be found.\nThe optimizer had a problem finding the following positions: ' +
              JSON.stringify(positionsWithIssue));
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
        filteredPool, sortAttribute, sortSign, searchText, whiteList, blackList} = this.state;

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
            {site === 'fd' && <DatePicker onChange={(date) => this.getFanduelData(date)} value={date}/>}
            <SportSection isLoading={isLoading} site={site} sport={sport} setSport={this.setSport}/>
            <ContestSection isLoading={isLoading} site={site} sport={sport} contest={contest} contests={contests}
                            setContest={this.setContest}/>
            <div style={{display: 'flex', margin: '2%'}}>
                {sport && contest && site && <button style={{marginTop: '10px'}}
                                                     onClick={this.generateOptimalLineup}>Generate Lineup</button>}
              {sport && contest && site && <button style={{marginTop: '10px'}}
                                                   onClick={this.clearLineup}>Clear Lineup</button>}
            </div>
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
