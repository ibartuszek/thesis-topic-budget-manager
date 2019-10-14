import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import Loading from "../Loading";
import TransactionTableSearchBar from "../transactions/transaction/table/TransactionTableSearchBar";
import {connect} from "react-redux";
import {createContext} from "../../actions/common/createContext";
import {getStandardStatistics} from "../../actions/statistics/getStandardStatistics";
import {statisticsMessages} from "../../store/MessageHolder";
import StandardStatistics from "./standardStatistics/StandardStatistics";

class Statistics extends Component {

  state = {
    startDate: undefined,
    endDate: undefined,
    standardStatisticsAreLoaded: undefined,
  };

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
    this.handleGetStandardStatistics = this.handleGetStandardStatistics.bind(this);
  }

  componentDidUpdate(oldProps) {
    const newProps = this.props;

    if (this.state.standardStatisticsAreLoaded === false && newProps.statisticsHolder['standardStatisticsAreLoaded'] === true) {
      this.setState({
        ...this.state,
        standardStatisticsAreLoaded: true
      })
    }
  }

  handleChange = (id, value) => {
    this.setState({
      [id]: value
    });
  };

  handleGetStandardStatistics() {
    const {endDate, startDate} = this.state;
    const {userHolder, logHolder} = this.props;
    if (startDate !== undefined && startDate !== null && endDate !== undefined && endDate !== null) {
      let context = createContext(userHolder, logHolder);
      this.setState({
        standardStatisticsAreLoaded: false
      });
      this.props.getStandardStatistics(context, startDate, endDate);
    }
  }

  render() {
    const {startDate, endDate, standardStatisticsAreLoaded} = this.state;
    const {statisticsHolder} = this.props;

    let standardStatistics = null;
    if (standardStatisticsAreLoaded === false) {
      standardStatistics = <Loading/>;
    } else if (standardStatisticsAreLoaded === true) {
      standardStatistics = <StandardStatistics standardStatistics={statisticsHolder.standardStatistics}/>;
    }

    return (
      <main>
        <div className="card my-3">
          <TransactionTableSearchBar startDateId="startDate" startDate={startDate} startDatePlaceHolder={statisticsMessages.startDatePlaceHolder}
                                     endDateId="endDate" endDate={endDate} endDatePlaceHolder={statisticsMessages.endDatePlaceHolder}
                                     buttonName="Get statistics" buttonIcon="fas fa-download" handleDateChange={this.handleChange}
                                     handleSearch={this.handleGetStandardStatistics}/>
          <div className="card-body">
            {standardStatistics}
          </div>
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
      {
        getStandardStatistics: getStandardStatistics,
      },
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(Statistics)
