import React, { Component } from 'react';
import './App.css';
import { Container } from 'react-bootstrap'
import DatePicker from 'react-date-picker';
import { getAddToLineupState } from './functions/getAddToLineupState'
import { getRemoveFromLineupState } from './functions/getRemoveFromLineupState'
import { getToggleBlackListState } from './functions/getToggleBlackListState'
import { getFilterPlayersState } from './functions/getFilterPlayersState'
import {formatDate} from "./functions/formatDate";
import { sumAttribute } from './functions/sumAttribute'
import { DfsGrid } from './DfsGrid.tsx';
import { DfsPlayerBox } from './DfsPlayerListBox.tsx'
import { DfsBlackListBox } from './DfsBlackListBox.tsx'
import football2 from './icons/football2.svg';
import football from './icons/football.ico';
import search from "./icons/search.ico";
import { apiRoot } from './config.js';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {isLoading: false, isOptimizing: false, site: '', sport: '', contest: '',
        date: new Date(), dfsData: {}, projectionsData: {}, contests: [], sports: [],
        lineup: [], cap: 0, playerPool: [], filteredPool: null, searchText: '', whiteList: [], blackList: []};
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
                        console.log(data);
                        this.setState({
                            isLoading: false,
                            dfsData: data,
                            contests: this.extractContestsFromData(data)
                        });
                    });
            }
        });
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
                              console.log(data);
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

  extractContestsFromData = (dataArray) => {
      let contestArray = [];
      dataArray.forEach(contestJson => contestArray.push(contestJson.contest));
      return contestArray;
  };

  setSport = (sport) => {
      this.setState({sport: sport});
      if (this.state.site === 'dk')
          this.getDraftKingsData(sport);
  };

  setContest = (contest) => {
      let playerPool = this.state.dfsData
          .filter(contestJson => contestJson.contest === contest)[0]['players'];
      this.setState({
          contest: contest,
          playerPool: playerPool
      });
  };

  generateLineup = (sport, site, contest) => {
    let {lineup, blackList} = this.state;
    let whiteListNames = lineup.filter((player) => player.Name).map((player) => (player.Name));
    let blackListNames = blackList.filter((player) => player.Name).map((player) => (player.Name));
    this.setState({
      isOptimizing: true,
      sport: sport,
      site: site,
      contest: contest,
      whiteList: lineup.filter((player) => player.Name)
    });
    fetch(window.location.origin + '/optimize/generate/' + sport + '/' + site + '/' + contest, {
      method: 'POST',
      body: whiteListNames.toString() + '|' + blackListNames.toString()
    }).then(response => {
      if (response.status !== 200) {
        alert('Error removing player.');
      } else {
        response.json()
            .then((lineupJson) => {
              this.ingestDfsLineup(lineupJson);
            });
      }
    });
  };

  ingestDfsLineup = (lineupJson) => {
    if (typeof lineupJson === "string") {
      this.setState({isOptimizing: false});
      alert(lineupJson);
      return
    }
    let lineup = (typeof lineupJson === "string") ? [] : lineupJson;
    this.setState({
      isOptimizing: false,
      lineup: lineup
    });
  };

  clearLineup = (sport, site, contest) => {
    if (!sport) {
      alert('Please select a sport.');
    } else if (sport === 'nba') {
      alert('Warning: \nThis sport is currently unavailable.');
    } else if (!site || !contest) {
      this.setState({sport: sport, site: site, contest: contest});
    } else {
      this.setState({
        isLoading: true,
        sport: sport,
        site: site,
        contest: contest
      });
      fetch(window.location.origin + '/optimize/clear/' + sport + '/' + site + '/' + contest)
          .then(response => {
            if (response.status !== 200) {
              alert('An error occurred.');
            } else {
              response.json()
                  .then((data) => {
                    this.setState({
                      isLoading: false,
                      playerPool: data.playerPool,
                      lineup: data.lineup,
                      cap: data.cap,
                      whiteList: [],
                      blackList: []
                    });
                  });
            }
          });
    }
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

  toggleBlackList = (playerIndex) => {
    let newState = getToggleBlackListState(playerIndex, this.state);
    this.setState(newState);
  };

  render() {
    const {isLoading, isOptimizing, sport, site, contest, date, contests, lineup, cap, playerPool, filteredPool,
      searchText, whiteList, blackList} = this.state;

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
              <DfsBlackListBox blackList={blackList}/>
            </div>
            <div>
              <h2 className={"Dfs-header"}>Players</h2>
              <div style={{display: 'flex', flexDirection: 'column'}}>
                {!filteredPool &&
                <img src={search} style={{height: '3vmin', position: 'absolute'}}
                     alt="search"/>}
                <input type="text" style={{height: '25px', width: '90%'}}
                       value={searchText}
                       onChange={(event) =>
                           this.filterPlayers('Name', event.target.value)}>{null}</input>
              </div>
              <div style={{display: 'flex'}}>
                <button onClick={() => this.filterPlayers('Position', 'All')}>All</button>
                {
                  [...new Set(playerPool.map((player) => (player.position === 'D/ST') ?
                      player.position : player.position.split('/')[0]))]
                      .map((position) =>
                          <button onClick={() => this.filterPlayers('Position', position)}>{position}</button>
                      )
                }
                <select onChange={(event) => this.filterPlayers('Team', event.target.value)}>
                  <option selected={"selected"} value={'All'}>All</option>
                  {[...new Set(playerPool.map((player) => player.Team))].sort().map((team) =>
                      <option value={team}>{team}</option>
                  )}
                </select>
              </div>
              <div className={"Player-list-box"}>
                <DfsPlayerBox playerList={playerPool} filterList={filteredPool}
                              whiteListFunction={this.addToLineup} blackListFunction={this.toggleBlackList}
                              whiteList={whiteList} blackList={blackList} salarySum={sumAttribute(lineup, 'Price')}
                              cap={cap}/>
              </div>
            </div>
            <div>
              <h2 className={"Dfs-header"}>Lineup</h2>
              <DfsGrid dfsLineup={lineup} removePlayer={this.removeFromLineup} site={site}
                       whiteList={whiteList} pointSum={sumAttribute(lineup, 'Projected')}
                       salarySum={sumAttribute(lineup, 'Price')} cap={cap}/>
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
          {site === 'fd' && <div>
              <DatePicker
                  onChange={(date) => this.getFanduelData(date)}
                  value={date}
              />
          </div>}
          {site && <h3>Choose a sport:</h3>}
          {site && <div style={{display: 'flex'}}>
              <button style={{backgroundColor: (sport === 'mlb') ? 'dodgerblue' : 'white'}}
                      onClick={() => this.setSport('mlb')}>MLB</button>
              <button style={{backgroundColor: (sport === 'nfl') ? 'dodgerblue' : 'white'}}
                      onClick={() => this.setSport('nfl')}>NFL</button>
              <button style={{backgroundColor: (sport === 'nba') ? 'dodgerblue' : 'white'}}
                      onClick={() => this.setSport('nba')}>NBA</button>
              <button style={{backgroundColor: (sport === 'nhl') ? 'dodgerblue' : 'white'}}
                      onClick={() => this.setSport('nhl')}>NHL</button>
            </div>}
            {site && sport && <h3>Choose a game contest:</h3>}
            {site && sport &&
            <div style={{display: 'flex'}}>
                {contests.map(
                    contestName =>
                        <button
                            style={{backgroundColor: (contestName === contest) ? 'dodgerblue' : 'white'}}
                            key={contestName}
                            onClick={() => this.setContest(contestName)}
                        >{contestName}</button>
                )}
            </div>}

            <div style={{display: 'flex', margin: '2%'}}>
              {sport && contest && site && <button style={{marginTop: '10px'}}
                                                 onClick={() => this.generateLineup(sport, site, contest)}>Optimize Lineup</button>}
              {sport && contest && site && <button style={{marginTop: '10px'}}
                                                 onClick={() => this.clearLineup(sport, site, contest)}>Clear Lineup</button>}
            </div>
          </div>
          {site && gridSection}
        </Container>
    )
  }
}

export default App;
