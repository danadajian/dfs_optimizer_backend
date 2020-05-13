import React, {useState} from 'react'
import Jumbotron from 'react-bootstrap/Jumbotron'
import './css/App.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import "react-datepicker/dist/react-datepicker.css";
import {NavBar} from "./components/NavBar";
import {GridSection} from "./components/GridSection";
import {INITIAL_STATE} from "./constants";
import './env'

const App = () => {

    const [state, setState] = useState(INITIAL_STATE);

    return (
        <div>
            <NavBar state={state} setState={setState}/>
            <section id="home">
                <Jumbotron className="mh-100">
                    {!state.site && !state.sport && <div>
                        <h1>DFS Optimizer</h1>
                        <p>
                            A better way to find the winning lineup.
                        </p>
                    </div>}
                    <GridSection state={state} setState={setState}/>
                </Jumbotron>
            </section>
            <section id="about">
                <Jumbotron className="mh-100">
                    <h1>About</h1>
                    <p>
                        Coming soon.
                    </p>
                </Jumbotron>
            </section>
        </div>
    );
};

export default App;
