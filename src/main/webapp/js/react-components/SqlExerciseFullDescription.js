
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
            this.renderHistory(),
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
            this.renderDisabledTextAreaWithBlackText({width:"1000px"}, this.props.pageData.exercise.schemaDdl),
            this.state.testData
                ?[re('div',{style:{fontWeight: "bold"}},"Data:"),
                    this.renderDisabledTextAreaWithBlackText({width:"1000px"}, this.state.testData)]
                :re(Button,{variant:"contained", color:"primary", onClick: ()=>this.loadTestData(this)}, "Show data")
        )
    }

    renderDisabledTextAreaWithBlackText(style, text) {
        return re(TextField,{className: "black-text", style:style, multiline:true, margin:"normal",
            value: text, disabled: true
        })
    }

    renderHistory() {
        if (!this.props.pageData.exercise.admin) {
            return null;
        }
        return this.state.history
            ? re(VContainer, {},
                re('span', {style: {fontWeight: "bold"}}, "History:"),
                this.renderResultSet(
                    this.state.history,
                    (colName,value) => {
                        if (colName==="ACTUAL_QUERY") {
                            return this.renderDisabledTextAreaWithBlackText({width:"1000px"},value)
                        } else if (colName==="P") {
                            return value
                                ?re('span', {style: {color: "#55ea19", fontWeight: "bold", fontSize: "x-large"}},
                                    "\u2713"
                                ):null
                        } else if (colName==="E") {
                            return value
                                ?re('span', {style: {color: "#ea102a", fontWeight: "bold", fontSize: "x-large"}},
                                    "\u2717"
                                ):null
                        } else if (colName==="R") {
                            return value
                                ?re('span', {style: {color: "#55ea19", fontWeight: "bold", fontSize: "x-large"}},
                                    "\u21BA"
                                ):null
                        } else {
                            return value
                        }
                    }
                )
            )
            : re(Button, {onClick: () => this.loadHistory(this)}, "History")
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

    renderResultSet(resultSet, formatter) {
        const style = {border: "1px solid lightgrey"}
        return re('table', {style:{...style, borderCollapse: "collapse"}},
            re('tbody', {},
                re('tr', {}, _.map(resultSet.colNames, colName=>re('td',{style:style},colName))),
                _.map(resultSet.data, row=>re('tr',{},
                    _.map(resultSet.colNames, colName=>re('td',{style:style},formatter?formatter(colName,row[colName]):row[colName]))
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

    loadHistory(self) {
        doGet({
            url: "/exercise/" + this.props.pageData.exercise.id + "/history",
            success: function (response) {
                self.setState((state,props)=>({
                    history: response
                }))
            }
        })
    }


}
