import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import AlertMessageComponent from "../AlertMessageComponent";
import CustomStatisticsPlaceProxy from "./customStatistics/CustomStatisticsPlaceProxy";
import Loading from "../Loading";
import StandardStatistics from "./standardStatistics/StandardStatistics";
import TransactionTableSearchBar from "../transactions/transaction/table/TransactionTableSearchBar";
import {createContext} from "../../actions/common/createContext";
import {getFirstPossibleDay} from "../../actions/transaction/getFirstPossibleDay";
import {getStandardStatistics} from "../../actions/statistics/getStandardStatistics";
import {getMessage} from "../../actions/message/messageActions";
import {lockTransactions} from "../../actions/transaction/lockTransactions";
import {statisticsMessages} from "../../store/MessageHolder";

class Statistics extends Component {

  state = {
    startDate: undefined,
    endDate: undefined,
    standardStatisticsAreLoaded: undefined,
    locked: false
  };

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
    this.handleDismissMessage = this.handleDismissMessage.bind(this);
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
    if (newProps.transactionHolder.firstPossibleDay === null) {
      this.props.getFirstPossibleDay(createContext(newProps.userHolder, newProps.logHolder));
    }
  }

  handleChange = (id, value) => {
    this.setState({
      [id]: value
    });
  };

  handleDismissMessage(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  handleGetStandardStatistics() {
    const {endDate, startDate} = this.state;
    const {userHolder, logHolder} = this.props;
    if (startDate !== undefined && startDate !== null && endDate !== undefined && endDate !== null) {
      let context = createContext(userHolder, logHolder);
      this.setState({
        standardStatisticsAreLoaded: false,
        locked: false
      });
      this.props.getStandardStatistics(context, startDate, endDate);
    }
  }

  handleLock() {
    const {endDate, locked} = this.state;
    const {userHolder, logHolder} = this.props;
    if (!locked && endDate !== undefined && endDate !== null) {
      let context = createContext(userHolder, logHolder);
      this.setState({
        locked: true
      });
      this.props.lockTransactions(context, endDate);
    }
  }

  render() {
    const {startDate, endDate, standardStatisticsAreLoaded} = this.state;
    const {logHolder, statisticsHolder, userHolder} = this.props;

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

    let lockingSuccessMessage = getMessage(logHolder.messages, "transactionsLockedSuccess", true);
    let lockingErrorMessage = getMessage(logHolder.messages, "transactionsLockedError", false);
    let locking = lockingSuccessMessage.value === null && lockingErrorMessage.value === null;

    return (
      <main>
        <div className="card mt-3">
          <TransactionTableSearchBar startDateId="startDate" startDate={startDate} startDatePlaceHolder={statisticsMessages.startDatePlaceHolder}
                                     endDateId="endDate" endDate={endDate} endDatePlaceHolder={statisticsMessages.endDatePlaceHolder}
                                     buttonName="Get statistics" buttonIcon="fas fa-download" handleDateChange={this.handleChange}
                                     handleSearch={this.handleGetStandardStatistics} lockStatisticsButtonEnabled={lockStatisticsButtonEnabled}
                                     handleLock={this.handleLock} locking={locking}/>
        </div>
        <AlertMessageComponent message={lockingSuccessMessage} onChange={this.handleDismissMessage}/>
        <AlertMessageComponent message={lockingErrorMessage} onChange={this.handleDismissMessage}/>
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
    transactionHolder: state.transactionHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {
        getStandardStatistics: getStandardStatistics,
        lockTransactions: lockTransactions,
        getFirstPossibleDay: getFirstPossibleDay
      },
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(Statistics)
