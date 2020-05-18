import React from 'react';
import {StateProps} from "../interfaces";
import {LineupGrid} from "./LineupGrid";

export const Lineup = React.forwardRef((props: StateProps, ref: any) =>
    <div ref={ref}>
        <LineupGrid {...props}/>
    </div>
);
