
class SqlExercisesList extends React.Component {
    constructor(props) {
        super(props)
        this.state = {}
        this.renderNodes = this.renderNodes.bind(this)
    }

    render() {
        return re('ul',{},
            _.map(this.props.pageData.exercises,ex=>
                re('li',{style:{padding:"5px"}},
                    ex.completed
                        ? re('span', {style: {color: "#55ea19", fontWeight: "bold", fontSize: "x-large"}}, "\u2713")
                        : null,
                    re('a',{href:"exercise/"+ex.id},ex.title)
                )
            )
        )
    }

    renderNodes() {

    }
}
