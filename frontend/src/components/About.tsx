import React from "react";
import Jumbotron from "react-bootstrap/Jumbotron";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {NavLink} from "react-router-dom";

const logo = require('../icons/logo.ico');

export const About = () => {
    return (
        <>
            <Navbar sticky="top" bg="dark" variant="dark" expand="xl">
                <Navbar.Brand as={NavLink} to="/">
                    <img alt="logo"
                         src={logo}
                         width="30"
                         height="30"
                         className="d-inline-block align-top mr-2"/>
                    {' '}DFS Optimizer
                </Navbar.Brand>
                <Navbar.Toggle/>
                <Navbar.Collapse id="nav-bar">
                    <Nav>
                        <Nav.Link as={NavLink} to="/" className="ml-2 mr-2 mt-1 mb-1">Home</Nav.Link>
                        <Nav.Link as={NavLink} to="/about" className="ml-2 mr-2 mt-1 mb-1">About</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
            <Jumbotron className="mh-100">
                <h1>About</h1>
                <p>
                    Coming soon.
                </p>
            </Jumbotron>
        </>
    )
}