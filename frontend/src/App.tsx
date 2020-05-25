import React, {useState} from 'react'
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import './css/App.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import "react-datepicker/dist/react-datepicker.css";
import './env'
import {Home} from "./components/Home";
import {About} from "./components/About";
import ScrollToTop from "./components/ScrollToTop";
import {INITIAL_STATE} from "./constants";

const App = () => {
    const [state, setState] = useState(INITIAL_STATE);

    return (
        <BrowserRouter>
            <ScrollToTop>
                <Switch>
                    <Route path="/" render={() => <Home state={state} setState={setState}/>} exact/>
                    <Route path="/about" render={() => <About/>}/>
                </Switch>
            </ScrollToTop>
        </BrowserRouter>
    );
};

export default App;
