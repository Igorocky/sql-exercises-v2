
class SqlExerciseFullDescription extends React.Component {
    constructor(props) {
        super(props)
        this.state = {}
        this.renderNodes = this.renderNodes.bind(this)
    }

    render() {
        return re(VContainer,{},
            re('h1',{}, this.props.pageData.exercise.title),
            re('div',{}, this.props.pageData.exercise.description),
            this.renderTestResults(this.state.passed, this.state.expected, this.state.actual)
        )
    }

    renderTestResults(passed, expected, actual) {
        return re(VContainer,{},
            re('div', {}, passed?"Success":"Fail"),
            re(HContainer,{},
                re(VContainer,{},
                    re('span',{},"Expected:")
                ),
                re(VContainer,{},
                    re('div',{},"Actual:")
                )
            )
        )
    }

    renderNodes() {

    }
}
