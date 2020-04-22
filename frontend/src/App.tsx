import React, {useState} from 'react'
import './App.css'
import {SiteSection} from "./components/SiteSection";
import {DateSection} from "./components/DateSection";
import {SportSection} from "./components/SportSection";
import {ContestSection} from "./components/ContestSection";
import {ActionButtonSection} from "./components/ActionButtonSection";
import {CsvSection} from "./components/CsvSection";
import {GridSection} from "./components/GridSection";
import {INITIAL_STATE} from "./constants";
import './env'

const App = () => {

    const [state, setState] = useState(INITIAL_STATE);

    return (
        <div>
            <h1 className={"App-header"}>DFS Optimizer</h1>
            <div className={"Dfs-sport"}>
                <h3>Choose a site:</h3>
                <SiteSection state={state} setState={setState}/>
                <DateSection state={state} setState={setState}/>
                <SportSection state={state} setState={setState}/>
                <ContestSection state={state} setState={setState}/>
                <ActionButtonSection state={state} setState={setState}/>
                <CsvSection state={state}/>
            </div>
            <GridSection state={state} setState={setState}/>
        </div>
    );
};

export default App;
