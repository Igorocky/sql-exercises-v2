
class SqlExerciseFullDescription extends React.Component {
    constructor(props) {
        super(props)
        this.state = {expectedResultSet: props.pageData.exercise.expectedResultSet}
        this.renderNodes = this.renderNodes.bind(this)
    }

    render() {
        return re(VContainer,{},
            re('h1',{}, this.props.pageData.exercise.title),
            re('div',{}, this.props.pageData.exercise.description),
            this.renderTestResults(this.state.passed, this.state.expectedResultSet, this.state.actualResultSet)
        )
    }

    renderTestResults(passed, expectedResultSet, actualResultSet) {
        return re(VContainer,{},
            re('div', {}, passed?"Success":"Fail"),
            re(HContainer,{},
                re(VContainer,{},
                    re('span',{},"Expected:"),
                    this.renderResultSet(expectedResultSet)
                ),
                actualResultSet?re(VContainer,{},
                    re('div',{},"Actual:"),
                    this.renderResultSet(actualResultSet)
                ):null
            )
        )
    }

    renderResultSet(resultSet) {
        const style = {border: "1px solid lightgrey"}
        return re('table', {style:{...style, borderCollapse: "collapse"}},
            re('tbody', {},
                re('tr', {}, _.map(resultSet.colNames, colName=>re('td',{style:style},colName))),
                _.map(resultSet.data, row=>re('tr',{},
                    _.map(resultSet.colNames, colName=>re('td',{style:style},row[colName]))
                ))
            )
        )
    }

    renderNodes() {

    }
}
