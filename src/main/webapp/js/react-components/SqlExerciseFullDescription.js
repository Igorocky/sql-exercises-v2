
class SqlExerciseFullDescription extends React.Component {
    constructor(props) {
        super(props)
        this.state = {}
        this.renderNodes = this.renderNodes.bind(this)
    }

    render() {
        return re('div',{},
            re('h1',{}, this.props.pageData.exercise.title),
            re('div',{}, this.props.pageData.exercise.description),
            re('h3',{}, "Example output:")
        )
    }

    renderNodes() {

    }
}
