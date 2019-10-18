import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import Loading from "../Loading";
import StandardStatistics from "./standardStatistics/StandardStatistics";
import TransactionTableSearchBar from "../transactions/transaction/table/TransactionTableSearchBar";
import {connect} from "react-redux";
import {createContext} from "../../actions/common/createContext";
import {getStandardStatistics} from "../../actions/statistics/getStandardStatistics";
import {statisticsMessages} from "../../store/MessageHolder";
import CustomStatisticsPlaceProxy from "./customStatistics/CustomStatisticsPlaceProxy";

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
    let customStatistics = null;
    if (standardStatisticsAreLoaded === false) {
      standardStatistics = <Loading/>;
    } else if (standardStatisticsAreLoaded === true) {
      standardStatistics = <StandardStatistics standardStatistics={statisticsHolder.standardStatistics}/>;
      customStatistics = this.createCustomStatistics(statisticsHolder.customStatisticsIds);
    }

    return (
      <main>
        <div className="card mt-3">
          <TransactionTableSearchBar startDateId="startDate" startDate={startDate} startDatePlaceHolder={statisticsMessages.startDatePlaceHolder}
                                     endDateId="endDate" endDate={endDate} endDatePlaceHolder={statisticsMessages.endDatePlaceHolder}
                                     buttonName="Get statistics" buttonIcon="fas fa-download" handleDateChange={this.handleChange}
                                     handleSearch={this.handleGetStandardStatistics}/>
        </div>
        {standardStatistics}
        {customStatistics}
      </main>);
  }

  createCustomStatistics(customStatisticsIds) {
    let statisticsList = null;
    if (customStatisticsIds !== null) {
      statisticsList = customStatisticsIds.map((schema, i) =>
        <div className="mt-3" key={i}>
          <CustomStatisticsPlaceProxy schema={schema} startDate={this.state.startDate} endDate={this.state.endDate}/>
        </div>);
    }
    return statisticsList;
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
