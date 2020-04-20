import React, { Component } from 'react'
import './App.css'
import { CSVLink } from "react-csv";
import {createEmptyLineup} from './resources/createEmptyLineup/createEmptyLineup'
import { getAddToLineupState } from './resources/getAddToLineupState/getAddToLineupState'
import { getRemoveFromLineupState } from './resources/getRemoveFromLineupState/getRemoveFromLineupState'
import { getToggleBlackListState } from './resources/getToggleBlackListState/getToggleBlackListState'
import { getFilterPlayersState } from './resources/getFilterPlayersState/getFilterPlayersState'
import {invokeLambdaFunction} from "./aws/aws";
import {extractContestsFromData} from "./resources/extractContestsFromData/extractContestsFromData";
import {combineDfsAndProjectionsData} from "./resources/combineDfsAndProjectionsData/combineDfsAndProjectionsData";
import {formatDate} from "./resources/formateDate/formatDate";
import {GridSection} from "./components/GridSection";
import {ContestSection} from "./components/ContestSection";
import {SportSection} from "./components/SportSection";
import {DateSection} from "./components/DateSection";
import {LINEUP_RULES} from "./constants";
import './env'

class App extends Component {

  constructor(props: any) {
    super(props);
    this.state = {isLoading: false, isOptimizing: false, loadingText: '', site: '', sport: '', contest: '',
        date: new Date(), dfsData: {}, projectionsData: {}, contests: [], lineup: [], lineupPositions: [],
        displayMatrix: [], salaryCap: 0, lineupRestrictions: {}, playerPool: [], filteredPool: null,
        sortAttribute: 'salary', sortSign: 1, searchText: '', whiteList: [], blackList: [],
        opponentRanks: {}, injuries: {}, playerStatuses: [], maxCombinations: 10000000};
  }

  setSite = (site: string) => {
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

  setDate = async (date: Date) => {
      await this.setState({date: date});
      const {sport}: any = this.state;
      if (sport)
          await this.setSport(sport);
  };

  setSport = async (sport: string) => {
      let {site, date}: any = this.state;
      this.setState({isLoading: true, sport: sport, contest: '', lineup: []});
      let dfsData;
      if (site === 'fd') {
          this.setState({loadingText: 'Fanduel data'});
          let fanduelData = await invokeLambdaFunction(process.env.REACT_APP_FANDUEL_LAMBDA, {"date": formatDate(date)});
          dfsData = fanduelData.filter((contest: any) => contest.sport === sport.toUpperCase());
      } else {
          this.setState({loadingText: 'Draftkings data'});
          dfsData = await invokeLambdaFunction(process.env.REACT_APP_DRAFTKINGS_LAMBDA, {"sport": sport});
      }
      const contests = extractContestsFromData(dfsData, site, date);
      this.setState({loadingText: sport.toUpperCase() + ' projections'});
      const projectionsData = await invokeLambdaFunction(process.env.REACT_APP_PROJECTIONS_LAMBDA, {"sport": sport});
      this.setState({loadingText: sport.toUpperCase() + ' opponent ranks'});
      const opponentRanks = await invokeLambdaFunction(process.env.REACT_APP_OPPONENT_RANKS_LAMBDA, {"sport": sport});
      this.setState({loadingText: 'injury data'});
      const injuries = await invokeLambdaFunction(process.env.REACT_APP_INJURIES_LAMBDA, {"sport": sport});
      let playerStatuses = [];
      if (sport === 'nhl') {
          this.setState({loadingText: 'player statuses'});
          playerStatuses = await invokeLambdaFunction(process.env.REACT_APP_GOALIE_SCRAPER_LAMBDA);
      }
      this.setState({
          isLoading: false,
          projectionsData: projectionsData.body,
          dfsData,
          contests,
          opponentRanks,
          injuries,
          playerStatuses,
          playerPool: [],
          filteredPool: null,
          whiteList: [],
          blackList: []
      });
  };

  setContest = (contest: string) => {
      if (!contest)
        return;
      let {site, sport, dfsData, projectionsData, opponentRanks, injuries, playerStatuses}: any = this.state;
      if (dfsData.length === 0) {
        alert(site + ' data is currently unavailable.');
        return
      }
      if (Object.keys(projectionsData).length === 0 || projectionsData['errorMessage']) {
        alert('Player projection data is currently unavailable.');
        return
      }
      let gameType = contest.includes('@') || contest.includes('vs') ? 'Single Game' : 'Classic';
      let contestRules = LINEUP_RULES[site][sport][gameType];
      let dfsPlayers = dfsData.filter((contestJson: any) => contestJson.contest === contest)[0]['players'];
      let playerPoolData = combineDfsAndProjectionsData(dfsPlayers, projectionsData, site, opponentRanks, injuries,
          playerStatuses);
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

  filterPlayers = (attribute: string, value: string) => {
    let newState = getFilterPlayersState(attribute, value, this.state);
    this.setState(newState);
  };

  addToLineup = (playerIndex: number) => {
    let newState = getAddToLineupState(playerIndex, this.state);
    if (typeof newState === 'string')
      alert(newState);
    else
      this.setState(newState);
  };

  removeFromLineup = (playerIndex: number) => {
    let newState = getRemoveFromLineupState(playerIndex, this.state);
    this.setState(newState);
  };

  clearLineup = () => {
      let {lineupPositions, displayMatrix}: any = this.state;
      this.setState({
          lineup: createEmptyLineup(lineupPositions, displayMatrix),
          whiteList: [],
          blackList: []
      })
  };

  toggleBlackList = (playerIndex: number) => {
    let newState = getToggleBlackListState(playerIndex, this.state);
    this.setState(newState);
  };

  toggleSort = (attribute: string) => {
      const {sortAttribute, sortSign}: any = this.state;
      if (attribute === sortAttribute)
        this.setState({sortSign: -sortSign});
      else
        this.setState({sortAttribute: attribute})
  };

  generateOptimalLineup = async () => {
    this.setState({isOptimizing: true});
    let {lineup, playerPool, displayMatrix}: any = this.state;
    let playerIds = await invokeLambdaFunction(process.env.REACT_APP_OPTIMAL_LINEUP_LAMBDA, this.state);
    if (playerIds['errorMessage']) {
      alert('An error occurred.' +
      playerIds['errorType'] + '\n' + playerIds['errorMessage'] + '\n' +
      playerIds['stackTrace'] !== undefined ? playerIds['stackTrace'].slice(0, 7) : '');
    } else if (playerIds.length === 0) {
        alert('Failed to generate optimal lineup.')
    } else {
        lineup = playerIds.map((playerId: number) => playerPool.find((player: any) => player.playerId === playerId));
        lineup.forEach((player: any, index: number) => player.displayPosition = displayMatrix[index]);
    }
    this.setState({
        isOptimizing: false,
        lineup: lineup
    })
  };

  render() {
    const {isLoading, isOptimizing, loadingText, sport, site, contest, date, contests, lineup, salaryCap, playerPool,
        filteredPool, sortAttribute, sortSign, searchText, whiteList, blackList, displayMatrix}: any = this.state;

    const csvData = [
        displayMatrix,
        lineup.map((player: any) => player.name)
    ];

    return (
        <div>
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
            {lineup.length > 0 && lineup.every((player: any) => player.name.length > 0) &&
            <CSVLink data={csvData} filename={site + '-' + sport + '-lineup.csv'}>Download Lineup CSV</CSVLink>}
          </div>
          <GridSection isLoading={isLoading} isOptimizing={isOptimizing} loadingText={loadingText} site={site}
                       sport={sport} contest={contest} lineup={lineup} playerPool={playerPool}
                       filteredPool={filteredPool} whiteList={whiteList} blackList={blackList} searchText={searchText}
                       filterPlayers={this.filterPlayers} addToLineup={this.addToLineup}
                       removeFromLineup={this.removeFromLineup} toggleBlackList={this.toggleBlackList}
                       sortAttribute={sortAttribute} sortSign={sortSign} toggleSort={this.toggleSort}
                       salaryCap={salaryCap}/>
        </div>
    )
  }
}

export default App;
