import React, {useState} from 'react'
import './css/App.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-bootstrap-table/dist/react-bootstrap-table.min.css';
import "react-datepicker/dist/react-datepicker.css";
import {NavBar} from "./components/NavBar";
import {ActionButtonSection} from "./components/ActionButtonSection";
import {CsvSection} from "./components/CsvSection";
import {GridSection} from "./components/GridSection";
import {INITIAL_STATE} from "./constants";
import './env'

const App = () => {

    const [state, setState] = useState(INITIAL_STATE);

    return (
        <div>
            <NavBar state={state} setState={setState}/>
            <div className="App-body">
                <h1 className="App-header">DFS Optimizer</h1>
                <ActionButtonSection state={state} setState={setState}/>
                <CsvSection state={state}/>
            </div>
            <GridSection state={state} setState={setState}/>
        </div>
    );
};

export default App;
