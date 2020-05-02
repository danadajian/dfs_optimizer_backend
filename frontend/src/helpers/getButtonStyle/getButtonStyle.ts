export const getButtonStyle = (currentStateItem: string, item: string) => ({
    backgroundColor: (currentStateItem === item) ? 'dodgerblue' : 'white'
});