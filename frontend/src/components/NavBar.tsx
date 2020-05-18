import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import {StateProps} from "../interfaces";
import {SiteSection} from "./SiteSection";
import {SportSection} from "./SportSection";
import {DateSection} from "./DateSection";
import {ContestSection} from "./ContestSection";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";

const logo = require('../icons/logo.ico');

export const NavBar = (props: StateProps) => {
    const isDesktopView = window.innerWidth > 1200;

    return <Navbar sticky="top" bg="light" variant="light" expand="xl">
        <Navbar.Brand href="#home">
            <img alt="logo"
                 src={logo}
                 width="30"
                 height="30"
                 className="d-inline-block align-top mr-2"/>
                 {' '}DFS Optimizer
        </Navbar.Brand>
        <OverlayTrigger
            placement={'auto'}
            defaultShow={!isDesktopView}
            overlay={
                <Tooltip id={'site-tooltip'} placement={'auto'}>
                    Select a site to begin.
                </Tooltip>
            }
        >
        <Navbar.Toggle aria-controls="basic-navbar-nav"/>
        </OverlayTrigger>
        <Navbar.Collapse id="nav-bar">
            <Nav>
                <Nav.Link href="#home" className="ml-2 mr-2 mt-1 mb-1">Home</Nav.Link>
                <Nav.Link href="#about" className="ml-2 mr-2 mt-1 mb-1">About</Nav.Link>
                <DateSection {...props}/>
                <SiteSection {...props} isDesktopView={isDesktopView}/>
                <SportSection {...props}/>
                <ContestSection {...props}/>
            </Nav>
        </Navbar.Collapse>
    </Navbar>
}