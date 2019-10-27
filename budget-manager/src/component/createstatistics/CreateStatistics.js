import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";

class CreateStatistics extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  /*
    componentDidUpdate(oldProps) {
      const newProps = this.props;
      if (this.state.standardStatisticsAreLoaded === false && newProps.statisticsHolder['standardStatisticsAreLoaded'] === true) {
        this.setState({
          ...this.state,
          standardStatisticsAreLoaded: true
        })
      }
    }
  */
  handleChange = (id, value) => {
    this.setState({
      [id]: value
    });
  };

  render() {
    const {statisticsHolder} = this.props;

    return (
      <main>
        <div className="card mt-3">
          test
        </div>
      </main>);
  }

}

const mapStateToProps = (state) => {
  return {
    logHolder: state.logHolder,
    statisticsHolder: state.statisticsHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {},
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(CreateStatistics)

