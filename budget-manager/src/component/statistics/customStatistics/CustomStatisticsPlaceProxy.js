import React, {Component} from 'react';
import CustomStatistics from "./CustomStatistics";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {createContext} from "../../../actions/common/createContext";
import {getCustomStatistics} from "../../../actions/statistics/getCustomStatistics";

class CustomStatisticsPlaceProxy extends Component {

  state = {
    customStatisticsAreLoaded: undefined,
  };

  constructor(props) {
    super(props);
    this.handleGetStatistics = this.handleGetStatistics.bind(this)
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;
    if (this.state.customStatisticsAreLoaded === false
      && newProps.statisticsHolder.customStatistics.length > oldProps.statisticsHolder.customStatistics.length) {
      this.setState({
        ...this.state,
        customStatisticsAreLoaded: true
      })
    }
  }

  handleGetStatistics() {
    const {endDate, logHolder, schema, startDate, userHolder} = this.props;
    if (startDate !== undefined && startDate !== null && endDate !== undefined && endDate !== null) {
      let context = createContext(userHolder, logHolder);
      this.setState({
        customStatisticsAreLoaded: false
      });
      this.props.getCustomStatistics(context, startDate, endDate, schema.id);
    }
  }

  render() {
    const {schema, statisticsHolder} = this.props;
    const {customStatisticsAreLoaded} = this.state;

    let content = null;
    if (customStatisticsAreLoaded === undefined) {
      content = (
        <div className="card card-body my-3 mx-auto">
          <button className="btn btn-outline-success my-2" onClick={this.handleGetStatistics}>
            <span className="fas fa-download"/>
            <span>{" Get " + schema.title}</span>
          </button>
        </div>);
    } else if (customStatisticsAreLoaded === false) {
      content = (
        <div className="card card-body my-3 mx-auto">
          <button className="btn btn-outline-success my-2">
            <div className="spinner-border spinner-border-sm" role="status">
              <span className="sr-only">Loading...</span>
            </div>
            <span>{" Get " + schema.title}</span>
          </button>
        </div>);
    } else {
      let statistics = this.getStatistics(statisticsHolder.customStatistics, schema.id);
      content = statistics !== null ? (<CustomStatistics customStatistics={statistics}/>) : null;
    }

    return (
      <React.Fragment>
        {content}
      </React.Fragment>)
  }

  getStatistics(customStatistics, schemaId) {
    let result = null;
    for (let index = 0; index < customStatistics.length && result === null; index++) {
      let current = customStatistics[index];
      if (current.schema.id === schemaId) {
        result = current;
      }
    }
    return result;
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
      {
        getCustomStatistics: getCustomStatistics,
      },
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(CustomStatisticsPlaceProxy)
