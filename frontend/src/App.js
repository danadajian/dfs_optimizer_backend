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
import { DfsPlayerBox } from './ts_objects/PlayerPool.tsx'
import { BlackList } from './ts_objects/BlackList.tsx'
import football2 from './resources/football2.svg'
import football from './resources/football.ico'
import search from "./resources/search.ico"
import { apiRoot } from './resources/config.js'

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {isLoading: false, isOptimizing: false, site: '', sport: '', contest: '', gameType: '',
        date: new Date(), fanduelData: {}, dfsData: {}, dfsPlayers: [], contests: [], sports: [],
        lineup: [], salaryCap: 0, playerPool: [], filteredPool: null, searchText: '', whiteList: [], blackList: []};
  }

  getFanduelData = (date) => {
      this.setState({
          isLoading: true,
          site: 'fd',
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
      })
  };

  getDraftKingsData = (sport) => {
      this.setState({site: 'dk'});
      if (sport) {
          this.setState({
              isLoading: true
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
      }
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
                                  playerPool: this.combineData(this.state.dfsPlayers, data)
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

  setSport = (sport) => {
      this.setState({sport: sport});
      this.state.site === 'fd' ? this.filterFanduelDataBySport(sport) : this.getDraftKingsData(sport);
  };

  setContest = (contest) => {
      let {site, sport} = this.state;
      let gameType = contest.includes('@') || contest.includes('vs') ? 'Single Game' : 'Classic';
      this.setState({
          contest: contest,
          gameType: gameType,
          dfsPlayers: this.state.dfsData
              .filter(contestJson => contestJson.contest === contest)[0]['players'],
          lineup: createEmptyLineup(lineupStructures[site][sport][gameType].lineupMatrix,
              lineupStructures[site][sport][gameType].displayMatrix),
          salaryCap: lineupStructures[site][sport][gameType].salaryCap
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
      let {site, sport, gameType} = this.state;
      this.setState({
          lineup: createEmptyLineup(lineupStructures[site][sport][gameType].lineupMatrix,
              lineupStructures[site][sport][gameType].displayMatrix),
          whiteList: [],
          blackList: []
      })
  };

  toggleBlackList = (playerIndex) => {
    let newState = getToggleBlackListState(playerIndex, this.state);
    this.setState(newState);
  };

  render() {
    const {isLoading, isOptimizing, sport, site, contest, date, contests, lineup, salaryCap, playerPool, filteredPool,
      searchText, whiteList, blackList} = this.state;

    let sports = ['mlb', 'nfl', 'nba', 'nhl'];
    let gridSection;

    if (isLoading) {
      gridSection =
          <div className={"Loading"}>
            <div><p className={"Loading-text"}>Loading . . .</p></div>
            <div><img src={football} className={"App-logo"} alt="football"/></div>
          </div>;
    } else if (isOptimizing) {
      gridSection =
          <div className={"Loading"}>
            <div><p className={"Optimizing-text"}>Optimizing . . .</p></div>
            <div><img src={football2} className={"App-logo2"} alt="football2"/></div>
          </div>;
    } else {
      gridSection =
          <div className={"Dfs-grid-section"}>
            <div className={"Player-list-box"}>
              <h2 className={"Dfs-header"}>Blacklist</h2>
              <BlackList blackList={blackList}/>
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
                <DfsPlayerBox playerList={playerPool} filterList={filteredPool}
                              whiteListFunction={this.addToLineup} blackListFunction={this.toggleBlackList}
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
    }

    return (
        <Container fluid={true}>
          <h1 className={"App-header"}>DFS Optimizer</h1>
          <div className={"Dfs-sport"}>
            <h3>Choose a site:</h3>
            <div style={{display: 'flex'}}>
              <button style={{backgroundColor: (site === 'fd') ? 'dodgerblue' : 'white'}}
                      onClick={() => this.getFanduelData(date)}>Fanduel</button>
              <button style={{backgroundColor: (site === 'dk') ? 'dodgerblue' : 'white'}}
                      onClick={() => this.getDraftKingsData(sport)}>Draftkings</button>
            </div>
            {site === 'fd' &&
            <div>
                <DatePicker onChange={(date) => this.getFanduelData(date)} value={date}/>
            </div>}
            {site && <h3>Choose a sport:</h3>}
            {site && <div style={{display: 'flex'}}>
                {sports.map(
                    thisSport => <button
                        style={{backgroundColor: (sport === thisSport) ? 'dodgerblue' : 'white'}}
                        onClick={() => this.setSport(thisSport)}>{thisSport.toUpperCase()}</button>
                )}
            </div>}
            {site && sport && <h3>Choose a game contest:</h3>}
            {site && sport &&
            <div style={{display: 'flex'}}>
                {contests.map(
                    contestName => <button style={{backgroundColor: (contestName === contest) ? 'dodgerblue' : 'white'}}
                                           key={contestName}
                                           onClick={() => this.setContest(contestName)}>{contestName}</button>
                )}
            </div>}
            <div style={{display: 'flex', margin: '2%'}}>
                {sport && contest && site && <button style={{marginTop: '10px'}}
                onClick={() => this.appendProjectionsData(sport)}>Build Lineup</button>}
                {sport && contest && site && <button style={{marginTop: '10px'}}
                >Optimize Lineup</button>}
              {sport && contest && site && <button style={{marginTop: '10px'}}
                                                   onClick={() => this.clearLineup()}>Clear Lineup</button>}
            </div>
          </div>
          {site && gridSection}
        </Container>
    )
  }
}

export default App;
