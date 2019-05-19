
class SqlExerciseFullDescription extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            expectedResultSet: props.pageData.exercise.expectedResultSet,
            completed: props.pageData.exercise.completed
        }
    }

    render() {
        return re(VContainer,{},
            re('h1',{}, this.props.pageData.exercise.title),
            this.state.completed
                ?re('div',{},
                    re('span',{style: {color: "#55ea19", fontWeight: "bold", fontSize: "x-large"}},"Completed"),
                    re(Button,{onClick: ()=>this.resetProgress(this)}, "Reset"),
                )
                :null,
            re('div',{}, this.props.pageData.exercise.description),
            re(TextField,{style:{width:"1000px"},
                label:"Your query", multiline:true, margin:"normal", variant:"filled", autoFocus: true,
                onChange: e => {
                    const newValue = e.target.value
                    this.setState((state,props)=>({actualQuery: newValue}))
                }
            }),
            re(Button,{variant:"contained", color:"primary", onClick: ()=>this.validateActualQuery(this)}, "Test"),
            this.renderTestResults(),
            re('div',{style:{fontWeight: "bold"}},"Schema:"),
            re(TextField,{style:{width:"1000px", color:"black"},
                multiline:true, margin:"normal",
                value: this.props.pageData.exercise.schemaDdl, disabled: true
            }),
            this.state.testData
                ?[re('div',{style:{fontWeight: "bold"}},"Data:"),
                    re(TextField,{style:{width:"1000px", color:"black"},
                        multiline:true, margin:"normal",
                        value: this.state.testData, disabled: true
                    })]
                :re(Button,{variant:"contained", color:"primary", onClick: ()=>this.loadTestData(this)}, "Show data")
        )
    }

    renderTestResults() {
        return re(VContainer,{},
            typeof this.state.passed === "undefined"?null:this.renderFailSuccess(),
            re(HContainer,{tdStyle: {verticalAlign: "top"}},
                re(VContainer,{},
                    re('span',{style:{fontWeight: "bold"}},"Expected:"),
                    this.renderResultSet(this.state.expectedResultSet)
                ),
                typeof this.state.passed !== "undefined"?re(VContainer,{},
                    re('div',{style:{fontWeight: "bold"}},"Actual:"),
                    this.state.error
                        ?re('div',{},this.state.error)
                        :this.renderResultSet(this.state.actualResultSet)
                ):null
            )
        )
    }

    renderFailSuccess() {
        return re('div', {
            style: {
                color: this.state.passed?"green":"red",
                fontWeight: "bold",
                fontSize: this.state.passed?"xx-large":"inherit"
            }
        }, this.state.passed?"Success":"Fail")
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
                        expectedResultSet: response.expectedResultSet,
                        actualResultSet: response.actualResultSet,
                        passed: response.passed,
                        error: response.error,
                        completed: state.completed || response.passed
                    }))
                }
            }
        })
    }

    resetProgress(self) {
        doPost({
            url: "/exercise/" + this.props.pageData.exercise.id + "/reset",
            success: function (response) {
                self.setState((state,props)=>({
                    completed: false
                }))
            }
        })
    }

    loadTestData(self) {
        doGet({
            url: "/exercise/" + this.props.pageData.exercise.id + "/testdata",
            success: function (response) {
                self.setState((state,props)=>({
                    testData: response
                }))
            }
        })
    }


}
