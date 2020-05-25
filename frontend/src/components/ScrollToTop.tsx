import { Component } from "react";
import { withRouter } from 'react-router-dom';

class ScrollToTop extends Component {
    componentDidUpdate(prevProps: any) {
        const props: any = this.props;
        if (props.location !== prevProps.location) {
            window.scrollTo(0, 0);
        }
    }

    render() {
        return this.props.children
    }
}

// @ts-ignore
export default withRouter(ScrollToTop)