
class SqlExerciseFullDescription extends React.Component {
    constructor(props) {
        super(props)
        this.state = {expectedResultSet: props.pageData.exercise.expectedResultSet}
    }

    render() {
        return re(VContainer,{},
            re('h1',{}, this.props.pageData.exercise.title),
            re('div',{}, this.props.pageData.exercise.description),
            re(TextField,{
                label:"Your query", multiline:true, margin:"normal", variant:"filled",
                autoFocus: true, onChange: e => {
                    const newValue = e.target.value
                    this.setState((state,props)=>({actualQuery: newValue}))
                }
            }),
            re(Button,{variant:"contained", color:"primary", onClick: ()=>this.validateActualQuery(this)}, "Test"),
            this.renderTestResults()
        )
    }

    renderTestResults() {
        return re(VContainer,{},
            typeof this.state.passed === "undefined"?null:re('div', {}, this.state.passed?"Success":"Fail"),
            re(HContainer,{},
                re(VContainer,{},
                    re('span',{},"Expected:"),
                    this.renderResultSet(this.state.expectedResultSet)
                ),
                typeof this.state.passed !== "undefined"?re(VContainer,{},
                    re('div',{},"Actual:"),
                    this.state.error
                        ?re('div',{},this.state.error)
                        :this.renderResultSet(this.state.actualResultSet)
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

    validateActualQuery(self) {
        doPost({
            url: "/exercise/" + this.props.pageData.exercise.id + "/validate",
            data: {actualQuery:this.state.actualQuery},
            success: function (response) {
                if (response.status == "ok") {
                    self.setState((state,props)=>({
                        actualResultSet: response.actualResultSet,
                        passed: response.passed,
                        error: response.error
                    }))
                }
            }
        })
    }


}
