import React from "react";
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Button from 'react-bootstrap/Button'
import NavDropdown from "react-bootstrap/NavDropdown";
import {State} from "../interfaces";
import {handleContestChange} from "../handlers/handleContestChange/handleContestChange";

export const ContestSection = (props: {
    state: State,
    setState: (state: State) => void
}) => {
    const {site, sport, contest, contests} = props.state;
    const shouldRenderElement = site && sport;

    if (!shouldRenderElement) {
        return null
    } else if (contests.length === 0) {
        return <Button className="ml-2 mr-2 mt-1 mb-1"
                       disabled variant={"outline-danger"}>No contests are available.</Button>
    } else if (contests.length <= 4) {
        return (
            <ButtonGroup className="ml-2 mr-2 mt-1 mb-1">
                {contests.map(
                    contestName =>
                        <Button key={contestName}
                                variant={site === 'Fanduel' ? "outline-primary" : "outline-dark"}
                                active={contest === contestName}
                                onClick={() => handleContestChange(contestName, props.state, props.setState)}>
                            {contestName}
                        </Button>
                )}
            </ButtonGroup>
        )
    } else {
        return (
            <NavDropdown id="basic-nav-dropdown"
                         title="Contest"
                         onSelect={(eventKey: any) => handleContestChange(eventKey, props.state, props.setState)}>
                {contests.map(
                    contestName =>
                        <NavDropdown.Item eventKey={contestName}
                                          active={contest === contestName}>
                            {contestName}
                        </NavDropdown.Item>
                )}
            </NavDropdown>
        )
    }
};
