import React from "react";
import ButtonGroup from 'react-bootstrap/ButtonGroup';
import Button from 'react-bootstrap/Button';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from "react-bootstrap/Dropdown";
import '../css/FilterBar.css'
import {playerPoolAttributes, StateProps} from "../interfaces";
import {getSetFromArray} from "../helpers/getSetFromArray/getSetFromArray";
import {handleFilterPlayers} from "../handlers/handleFilterPlayers/handleFilterPlayers";

export const FilterBar: any = (props: StateProps) => {
    const {playerPool, sortValue} = props.state;

    return (
        <div className="Filter-bar">
            <ButtonGroup>
                <Button variant={"dark"}
                        active={sortValue === 'All'}
                        onClick={() =>
                            handleFilterPlayers('position', 'All', props.state, props.setState)}>All
                </Button>
                {
                    getSetFromArray(playerPool.map((player: playerPoolAttributes) => player.position))
                        .map((position, index: number) =>
                            <Button key={index}
                                    variant={"dark"}
                                    active={sortValue === position}
                                    onClick={() =>
                                        handleFilterPlayers('position', position, props.state, props.setState)}>
                                {position}
                            </Button>
                        )
                }
            </ButtonGroup>
            <DropdownButton id={'team-filter'}
                            title={'Team'}
                            onSelect={(eventKey: any) =>
                                handleFilterPlayers('team', eventKey, props.state, props.setState)}>
                <Dropdown.Item eventKey={'All'}
                               active={sortValue === 'All'}>All</Dropdown.Item>
                {getSetFromArray(playerPool.map((player: playerPoolAttributes) => player.team))
                    .sort()
                    .map((team, index: number) =>
                        <Dropdown.Item key={index}
                                       eventKey={team}
                                       active={sortValue === team}>{team}</Dropdown.Item>
                    )}
            </DropdownButton>
        </div>
    )
};