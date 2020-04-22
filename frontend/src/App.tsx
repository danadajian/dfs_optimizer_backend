import React, {useState} from 'react'
import './App.css'
import {CSVLink} from "react-csv";
import {createEmptyLineup} from './resources/createEmptyLineup/createEmptyLineup'
import {GridSection} from "./components/GridSection";
import {ContestSection} from "./components/ContestSection";
import {SportSection} from "./components/SportSection";
import {DateSection} from "./components/DateSection";
import {INITIAL_STATE} from "./constants";
import './env'
import {handleContestChange} from "./handleContestChange";
import {handleGenerateOptimalLineup} from "./handleGenerateOptimalLineup";
import {loadAllDataAndGetNewState} from "./handleSportChange";

const App = () => {

    const [state, setState] = useState(INITIAL_STATE);

    const {isLoading, site, sport, date, contest, lineup, lineupPositions, displayMatrix} = state;

    const handleSiteChange = (site: string) => {
        setState({
            ...state,
            site,
            sport: '',
            contest: '',
            contests: [],
            playerPool: [],
            filteredPool: [],
            whiteList: [],
            blackList: [],
            lineup: []
        });
    };

    const handleSportChange = async (sport: string) => {
        await setState({
            ...state,
            isLoading: true,
            loadingText: `${state.site} data`,
            sport,
            contest: '',
            lineup: []
        });
        const newState = await loadAllDataAndGetNewState(state.site, sport, state.date);
        await setState({
            ...state,
            ...newState
        });
    };

    const handleDateChange = (date: Date) => {
        setState({
            ...state,
            date
        })
    };

    const handleClearLineup = () => {
        const emptyLineup: any = createEmptyLineup(lineupPositions, displayMatrix);
        setState({
            ...state,
            lineup: emptyLineup,
            whiteList: [],
            blackList: []
        });
    };

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
                    <button style={{backgroundColor: (site === 'Fanduel') ? 'dodgerblue' : 'white'}}
                            onClick={() => handleSiteChange('Fanduel')}>Fanduel
                    </button>
                    <button style={{backgroundColor: (site === 'DraftKings') ? 'dodgerblue' : 'white'}}
                            onClick={() => handleSiteChange('DraftKings')}>Draftkings
                    </button>
                </div>
                {site && <DateSection date={date} handleDateChange={handleDateChange}/>}
                {!isLoading && site &&
                <SportSection sport={sport} handleSportChange={handleSportChange}/>}
                {!isLoading && site && sport &&
                <ContestSection state={state} setState={setState} handleContestChange={handleContestChange}/>}
                <div style={{display: 'flex', margin: '2%'}}>
                    {sport && contest && site && <button style={{marginTop: '10px'}}
                                                         onClick={() => handleGenerateOptimalLineup(state, setState)}>Optimize
                        Lineup</button>}
                    {sport && contest && site && <button style={{marginTop: '10px'}}
                                                         onClick={handleClearLineup}>Clear Lineup</button>}
                </div>
                {lineup.length > 0 && lineup.every((player: any) => player.name.length > 0) &&
                <CSVLink data={csvData} filename={site + '-' + sport + '-lineup.csv'}>Download Lineup CSV</CSVLink>}
            </div>
            <GridSection state={state} setState={setState}/>
        </div>
    );
};

export default App;
