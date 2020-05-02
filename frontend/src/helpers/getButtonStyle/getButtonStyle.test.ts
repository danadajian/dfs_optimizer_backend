import {getButtonStyle} from "./getButtonStyle";

describe('getButtonStyle', () => {
    describe('item is current state item', () => {
        let result: any;
        const currentStateItem = 'current'
        const item = 'current'
        beforeEach(() => {
            result = getButtonStyle(currentStateItem, item)
        })

        it('should return expected style', () => {
            expect(result).toEqual({
                backgroundColor: 'dodgerblue'
            })
        });
    })

    describe('site is not current site', () => {
        let result: any;
        const currentStateItem = 'not current'
        const item = 'current'
        beforeEach(() => {
            result = getButtonStyle(currentStateItem, item)
        })

        it('should return expected style', () => {
            expect(result).toEqual({
                backgroundColor: 'white'
            })
        });
    })

})