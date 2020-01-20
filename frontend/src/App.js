import React, { Component } from 'react'
import './App.css'
import { Container } from 'react-bootstrap'
import { CSVLink } from "react-csv";
import {createEmptyLineup} from './functions/createEmptyLineup'
import { getAddToLineupState } from './functions/getAddToLineupState'
import { getRemoveFromLineupState } from './functions/getRemoveFromLineupState'
import { getToggleBlackListState } from './functions/getToggleBlackListState'
import { getFilterPlayersState } from './functions/getFilterPlayersState'
import {invokeLambdaFunction} from "./functions/invokeLambdaFunction";
import {extractContestsFromData} from "./functions/extractContestsFromData";
import {combineDfsAndProjectionsData} from "./functions/combineDfsAndProjectionsData";
import {formatDate} from "./functions/formatDate";
import { lineupRules } from './resources/lineupRules'
import {GridSection} from "./ts_objects/GridSection";
import {ContestSection} from "./ts_objects/ContestSection";
import {SportSection} from "./ts_objects/SportSection";
import {DateSection} from "./ts_objects/DateSection";
require('dotenv').config();
let AWS = require('aws-sdk');
AWS.config.region = 'us-east-2';
AWS.config.credentials = new AWS.Credentials(process.env.REACT_APP_AWS_KEY, process.env.REACT_APP_AWS_SECRET);
const lambda = new AWS.Lambda();

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {isLoading: false, isOptimizing: false, loadingText: '', site: '', sport: '', contest: '',
        date: new Date(), dfsData: {}, projectionsData: {}, contests: [], lineup: [], lineupPositions: [],
        displayMatrix: [], salaryCap: 0, lineupRestrictions: {}, playerPool: [], filteredPool: null,
        sortAttribute: 'salary', sortSign: 1, searchText: '', whiteList: [], blackList: [],
        opponentRanks: {}, injuries: {}, maxCombinations: 5000000};
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
          let fanduelData = await invokeLambdaFunction(lambda, process.env.REACT_APP_FANDUEL_LAMBDA,
              {"date": formatDate(date)});
          dfsData = fanduelData.filter(contest => contest.sport === sport.toUpperCase());
      } else {
          this.setState({loadingText: 'Draftkings data'});
          dfsData = await invokeLambdaFunction(lambda, process.env.REACT_APP_DRAFTKINGS_LAMBDA,
            {"sport": sport});
      }
      const contests = extractContestsFromData(dfsData, site, date);
      this.setState({loadingText: sport.toUpperCase() + ' projections'});
      const projectionsData = await invokeLambdaFunction(lambda, process.env.REACT_APP_PROJECTIONS_LAMBDA,
        {"sport": sport});
      this.setState({loadingText: sport.toUpperCase() + ' opponent ranks'});
      const opponentRanks = await invokeLambdaFunction(lambda, process.env.REACT_APP_OPPONENT_RANKS_LAMBDA,
        {"sport": sport});
      this.setState({loadingText: 'injury data'});
      const injuries = await invokeLambdaFunction(lambda, process.env.REACT_APP_INJURIES_LAMBDA,
        {"sport": sport});
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
      let {site, sport, dfsData, projectionsData, opponentRanks, injuries} = this.state;
      if (dfsData.length === 0) {
        alert(site + ' data is currently unavailable.');
        return
      }
      if (Object.keys(projectionsData).length === 0 || projectionsData['errorMessage']) {
        alert('Player projection data is currently unavailable.');
        return
      }
      let gameType = contest.includes('@') || contest.includes('vs') ? 'Single Game' : 'Classic';
      let contestRules = lineupRules[site][sport][gameType];
      let dfsPlayers = dfsData.filter(contestJson => contestJson.contest === contest)[0]['players'];
      let playerPoolData = combineDfsAndProjectionsData(dfsPlayers, projectionsData, site, opponentRanks, injuries);
      let emptyLineup = createEmptyLineup(contestRules.lineupPositions, contestRules.displayMatrix);
      this.setState({
          contest: contest,
          playerPool: playerPoolData,
          filteredPool: null,
          whiteList: [],
          blackList: [],
          lineup: emptyLineup,
          lineupPositions: contestRules.lineupPositions,
          displayMatrix: contestRules.displayMatrix,
          salaryCap: contestRules.salaryCap,
          lineupRestrictions: contestRules.lineupRestrictions
      });
  };

  filterPlayers = (attribute, value) => {
    let newState = getFilterPlayersState(attribute, value, this.state);
    this.setState(newState);
  };

  addToLineup = (playerIndex) => {
    let newState = getAddToLineupState(playerIndex, this.state);
    if (typeof newState === 'string')
      alert(newState);
    else
      this.setState(newState);
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
  
  toggleSort = (attribute) => {
      if (attribute === this.state.sortAttribute)
        this.setState({sortSign: -this.state.sortSign});
      else
        this.setState({sortAttribute: attribute})
  };

  generateOptimalLineup = async () => {
    this.setState({isOptimizing: true});
    let optimalLineup = this.state.lineup;
    let playerIds = await invokeLambdaFunction(lambda, process.env.REACT_APP_OPTIMAL_LINEUP_LAMBDA, this.state);
    if (playerIds['errorMessage']) {
      alert('An error occurred.' +
      playerIds['errorType'] + '\n' + playerIds['errorMessage'] + '\n' +
      playerIds['stackTrace'] !== undefined ? playerIds['stackTrace'].slice(0, 7) : '');
    } else if (playerIds.includes(0)) {
        alert('The whitelisted salary was too high to optimize around.')
    } else {
        optimalLineup = playerIds.map(playerId => this.state.playerPool.find(player => player.playerId === playerId));
        optimalLineup.forEach((player, index) => player.displayPosition = this.state.displayMatrix[index]);
    }
    this.setState({
        isOptimizing: false,
        lineup: optimalLineup
    })
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
                       sortAttribute={sortAttribute} sortSign={sortSign} toggleSort={this.toggleSort}
                       salaryCap={salaryCap}/>
        </Container>
    )
  }
}

export default App;
