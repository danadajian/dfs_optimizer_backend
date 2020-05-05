import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {State} from "../interfaces";
import {SiteSection} from "./SiteSection";
import {SportSection} from "./SportSection";
import {DateSection} from "./DateSection";
import {ContestSection} from "./ContestSection";

const logo = require('../icons/logo.ico');

export const NavBar = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    return <Navbar sticky="top" bg="light" variant="light" expand="xl">
        <Navbar.Brand href="#home">
            <img alt="logo"
                 src={logo}
                 width="30"
                 height="30"
                 className="d-inline-block align-top mr-2"/>
                 {' '}DFS Optimizer
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav"/>
        <Navbar.Collapse id="nav-bar">
            <Nav>
                <Nav.Link href="#home" className="ml-2 mr-2 mt-1 mb-1">Home</Nav.Link>
                <Nav.Link href="#about" className="ml-2 mr-2 mt-1 mb-1">About</Nav.Link>
                <DateSection state={props.state} setState={props.setState}/>
                <SiteSection state={props.state} setState={props.setState}/>
                <SportSection state={props.state} setState={props.setState}/>
                <ContestSection state={props.state} setState={props.setState}/>
            </Nav>
        </Navbar.Collapse>
    </Navbar>
}