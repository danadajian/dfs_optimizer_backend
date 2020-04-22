import {INITIAL_STATE} from "../constants";
import {State} from "../interfaces";

export const handleSiteChange = (site: string, setState: (state: State) => void) => {
    setState({
        ...INITIAL_STATE,
        site,
    });
};