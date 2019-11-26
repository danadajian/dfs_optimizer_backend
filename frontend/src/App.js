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
import { sumAttribute } from './functions/sumAttribute'
import { lineupStructures } from './resources/lineupStructures'
import { Lineup } from './ts_objects/Lineup.tsx'
import {PlayerPool} from "./ts_objects/PlayerPool";
import { BlackList } from './ts_objects/BlackList.tsx'
import search from "./resources/search.ico"
import { apiRoot } from './resources/config.js'
import {Loading} from "./Loading";
import {Optimizing} from "./Optimizing";

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {isLoading: false, isOptimizing: false, site: '', sport: '', contest: '', gameType: '',
        date: new Date(), fanduelData: {}, dfsData: {}, projectionsData: {}, contests: [], sports: [],
        lineup: [], lineupMatrix: [], displayMatrix: [], salaryCap: 0, playerPool: [], filteredPool: null,
        playerPoolData: [], sortAttribute: 'salary', sortSign: 1, searchText: '', whiteList: [], blackList: []};
  }

  getFanduelData = (date) => {
      this.setState({
          isLoading: true,
          site: 'fd',
          sport: '',
          contest: '',
          date: date
      });
    fetch(apiRoot + '/fanduel?date=' + formatDate(date))
        .then(response => {
            if (response.status !== 200) {
                alert('An error occurred.');
            } else {
                response.json()
                    .then((data) => {
                        this.setState({
                            isLoading: false,
                            fanduelData: data
                        });
                    });
            }
        });
  };

  filterFanduelDataBySport = (sport) => {
      let filteredDfsData = this.state.fanduelData.filter(contest => contest.sport === sport.toUpperCase());
      this.setState({
          dfsData: filteredDfsData,
          contests: this.extractContestsFromData(filteredDfsData)
      });
  };

  getDraftKingsData = (sport) => {
      this.setState({
          isLoading: true,
          sport: '',
          contest: ''
      });
      fetch(apiRoot + '/draftkings?sport=' + sport)
          .then(response => {
              if (response.status !== 200) {
                  alert('An error occurred.');
              } else {
                  response.json()
                      .then((data) => {
                          if (data.length === 0) {
                              alert('There are no upcoming ' + sport.toUpperCase() + ' games at this time.');
                          } else {
                              this.setState({
                                  isLoading: false,
                                  sport: sport,
                                  dfsData: data,
                                  contests: this.extractContestsFromData(data)
                              });
                          }
                      });
              }
          });
  };

  appendProjectionsData = (sport) => {
      this.setState({isLoading: true});
      fetch(apiRoot + '/projections?sport=' + sport)
          .then(response => {
              if (response.status !== 200) {
                  alert('An error occurred.');
              } else {
                  response.json()
                      .then((data) => {
                          if (data.length === 0) {
                              alert('No ' + sport.toUpperCase() + ' data is available at this time.');
                          } else {
                              this.setState({
                                  isLoading: false,
                                  projectionsData: data
                              });
                          }
                      });
              }
          });
  };

  combineData = (dfsPlayers, projectionsData) => {
      let combinedData = [];
      dfsPlayers.forEach(player => {
          let playerData = projectionsData[player.playerId];
          if (playerData) {
              player.name = playerData.name;
              player.team = playerData.team;
              player.opponent = playerData.opponent;
              player.gameDate = playerData.gameDate;
              player.projection = playerData[this.state.site + 'Projection'];
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

  setSite = (site) => {
      this.setState({site: site});
      if (site === 'fd')
          this.getFanduelData(this.state.date);
  };

  setSport = async (sport) => {
      this.setState({
          sport: sport,
          contest: '',
          playerPool: [],
          filteredPool: null,
          whiteList: [],
          blackList: [],
      });
      await this.appendProjectionsData(sport);
      this.state.site === 'fd' ? this.filterFanduelDataBySport(sport) : this.getDraftKingsData(sport);
  };

  setContest = (contest) => {
      let {site, sport, projectionsData} = this.state;
      let gameType = contest.includes('@') || contest.includes('vs') ? 'Single Game' : 'Classic';
      let lineupMatrix = lineupStructures[site][sport][gameType].lineupMatrix;
      let displayMatrix = lineupStructures[site][sport][gameType].displayMatrix;
      let salaryCap = lineupStructures[site][sport][gameType].salaryCap;
      let dfsPlayers = this.state.dfsData.filter(contestJson => contestJson.contest === contest)[0]['players'];
      let playerPoolData = this.combineData(dfsPlayers, projectionsData);
      let emptyLineup = createEmptyLineup(lineupMatrix, displayMatrix);
      this.setState({
          contest: contest,
          gameType: gameType,
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

  generateOptimalLineup = () => {
      let {playerPool, whiteList, blackList, lineupMatrix, displayMatrix, salaryCap, playerPoolData} = this.state;
      this.setState({
          isOptimizing: true
      });
      fetch(apiRoot + '/optimize', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify({
              'players': playerPool,
              'whiteList': whiteList,
              'blackList': blackList,
              'lineupMatrix': lineupMatrix,
              'salaryCap': salaryCap
          })
      }).then(response => {
          if (response.status !== 200) {
              alert('An error occurred.');
          } else {
              response.json()
                  .then((playerIds) => {
                      if (!Array.isArray(playerIds) || playerIds.every(playerId => playerId === 0)) {
                          alert('Optimal lineup could not be found.\n' + JSON.stringify(playerIds));
                          this.setState({
                              isOptimizing: false
                          })
                      } else {
                          let optimalLineup = playerIds
                              .map(playerId => playerPool.find(player => player.playerId === playerId));
                          optimalLineup.forEach((player, index) => player.displayPosition = displayMatrix[index]);
                          this.setState({
                              isOptimizing: false,
                              lineup: optimalLineup,
                              playerPool: playerPoolData,
                          })
                      }
                  });
          }
      });
  };

  render() {
    const {isLoading, isOptimizing, sport, site, contest, date, contests, lineup, salaryCap, playerPool, filteredPool,
      sortAttribute, sortSign, searchText, whiteList, blackList} = this.state;

    let sports = ['mlb', 'nfl', 'nba', 'nhl'];
    let sportSection = isLoading || !site ? null : <h3>Choose a sport:</h3>;
    let sportButtons = isLoading || !site ? null :
      <div style={{display: 'flex'}}>
          {sports.map(
              thisSport => <button
                  key={thisSport}
                  style={{backgroundColor: (sport === thisSport) ? 'dodgerblue' : 'white'}}
                  onClick={() => this.setSport(thisSport)}>{thisSport.toUpperCase()}</button>
          )}
      </div>;

      let contestSection = isLoading || (!site || !sport) ? null : <h3>Choose a game contest:</h3>;
      let contestButtons = isLoading || (!site || !sport) ? null :
      <div style={{display: 'flex'}}>
          {contests.map(
              contestName => <button style={{backgroundColor: (contestName === contest) ? 'dodgerblue' : 'white'}}
                                     key={contestName}
                                     onClick={() => this.setContest(contestName)}>{contestName}</button>
          )}
      </div>;

    let gridSection = isLoading ? Loading : isOptimizing ? Optimizing :
        <div className={"Dfs-grid-section"}>
            <div className={"Player-list-box"}>
                <h2 className={"Dfs-header"}>Blacklist</h2>
                <BlackList blackList={blackList} playerPool={playerPool}/>
            </div>
            <div>
              <h2 className={"Dfs-header"}>Players</h2>
              <div style={{display: 'flex', flexDirection: 'column'}}>
                {!filteredPool && <img src={search} style={{height: '3vmin', position: 'absolute'}} alt="search"/>}
                <input type="text" style={{height: '25px', width: '90%'}}
                       value={searchText}
                       onChange={(event) => this.filterPlayers('name', event.target.value)}>{null}</input>
              </div>
              <div style={{display: 'flex'}}>
                <button onClick={() => this.filterPlayers('position', 'All')}>All</button>
                {
                  [...new Set(playerPool.map((player) => player.position.split('/')[0]))]
                      .map((position) =>
                          <button onClick={() => this.filterPlayers('position', position)}>{position}</button>
                      )
                }
                <select onChange={(event) => this.filterPlayers('team', event.target.value)}>
                  <option defaultValue={'All'}>All</option>
                  {[...new Set(playerPool.map((player) => player.team))].sort().map((team) =>
                      <option value={team}>{team}</option>
                  )}
                </select>
              </div>
              <div className={"Player-list-box"}>
                <PlayerPool playerList={playerPool} filterList={filteredPool}
                              whiteListFunction={this.addToLineup} blackListFunction={this.toggleBlackList}
                              sortFunction={this.sortAttribute} toggleSort={this.toggleSort}
                              sortAttribute={sortAttribute} sortSign={sortSign}
                              whiteList={whiteList} blackList={blackList}
                              salarySum={sumAttribute(lineup, 'salary')} cap={salaryCap}/>
              </div>
            </div>
            <div>
              <h2 className={"Dfs-header"}>Lineup</h2>
              <Lineup dfsLineup={lineup} removePlayer={this.removeFromLineup} site={site}
                      whiteList={whiteList} pointSum={sumAttribute(lineup, 'projection')}
                      salarySum={sumAttribute(lineup, 'salary')} cap={salaryCap}/>
            </div>
        </div>;

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
            {site === 'fd' &&
            <div>
                <DatePicker onChange={(date) => this.getFanduelData(date)} value={date}/>
            </div>}
            {sportSection}
            {sportButtons}
            {contestSection}
            {contestButtons}
            <div style={{display: 'flex', margin: '2%'}}>
                {sport && contest && site && <button style={{marginTop: '10px'}}
                                                     onClick={this.generateOptimalLineup}>Optimize Lineup</button>}
              {sport && contest && site && <button style={{marginTop: '10px'}}
                                                   onClick={this.clearLineup}>Clear Lineup</button>}
            </div>
          </div>
          {isLoading ? Loading : (site && sport && contest && gridSection)}
        </Container>
    )
  }
}

export default App;
