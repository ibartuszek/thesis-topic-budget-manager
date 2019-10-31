import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import CustomStatisticsPlaceProxy from "./customStatistics/CustomStatisticsPlaceProxy";
import Loading from "../Loading";
import Redirect from "react-router-dom/es/Redirect";
import StandardStatistics from "./standardStatistics/StandardStatistics";
import TransactionTableSearchBar from "../transactions/transaction/table/TransactionTableSearchBar";
import {createContext} from "../../actions/common/createContext";
import {getStandardStatistics} from "../../actions/statistics/getStandardStatistics";
import {statisticsMessages} from "../../store/MessageHolder";

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
    this.handleLock = this.handleLock.bind(this);
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

  handleLock() {
    // TODO:
    console.log("TODO: Lock statistics data!");
  }

  render() {
    const {startDate, endDate, standardStatisticsAreLoaded} = this.state;
    const {statisticsHolder, userHolder} = this.props;

    if (userHolder == null || !userHolder.userIsLoggedIn) {
      return <Redirect to='/login'/>;
    }

    let lockStatisticsButtonEnabled = null;
    let standardStatistics = null;
    let customStatistics = null;
    if (standardStatisticsAreLoaded === false) {
      standardStatistics = <Loading/>;
    } else if (standardStatisticsAreLoaded === true) {
      standardStatistics = <StandardStatistics standardStatistics={statisticsHolder.standardStatistics}/>;
      customStatistics = this.createCustomStatistics(statisticsHolder.customSchemas);
      lockStatisticsButtonEnabled = true;
    }

    return (
      <main>
        <div className="card mt-3">
          <TransactionTableSearchBar startDateId="startDate" startDate={startDate} startDatePlaceHolder={statisticsMessages.startDatePlaceHolder}
                                     endDateId="endDate" endDate={endDate} endDatePlaceHolder={statisticsMessages.endDatePlaceHolder}
                                     buttonName="Get statistics" buttonIcon="fas fa-download" handleDateChange={this.handleChange}
                                     handleSearch={this.handleGetStandardStatistics} lockStatisticsButtonEnabled={lockStatisticsButtonEnabled}
                                     handleLock={this.handleLock}/>
        </div>
        {standardStatistics}
        {customStatistics}
      </main>);
  }

  createCustomStatistics(customSchemas) {
    let statisticsList = null;
    if (customSchemas !== null) {
      statisticsList = customSchemas.map((schema, i) =>
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
