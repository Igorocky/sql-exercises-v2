
class SqlExercisesList extends React.Component {
    constructor(props) {
        super(props)
        this.state = {}
        this.renderNodes = this.renderNodes.bind(this)
    }

    render() {
        return re('ul',{},
            _.map(this.props.pageData.exercises,ex=>
                re('li',{},re('a',{href:"exercise/"+ex.id},ex.title))
            )
        )
    }

    renderNodes() {

    }
}
