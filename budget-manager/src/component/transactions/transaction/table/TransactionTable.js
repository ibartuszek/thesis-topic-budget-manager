import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import Loading from "../../../Loading";
import TransactionTableBody from "./TransactionTableBody";
import TransactionTableSearchBar from "./TransactionTableSearchBar";
import {createContextParameters, createFetchTransactionsContext} from "../../../../actions/common/createContext";
import {fetchTransactions} from "../../../../actions/transaction/fetchTransactions";
import {getPossibleFirstDate} from "../../../../actions/date/dateActions";

class TransactionTable extends Component {

  state = {
    startDate: undefined,
    endDate: undefined,
    transactionsAreLoaded: undefined,
  };

  constructor(props) {
    super(props);
    this.handleDateChange = this.handleDateChange.bind(this);
    this.handleSearch = this.handleSearch.bind(this);
  }

  componentDidMount() {
    let possibleFirstDay = getPossibleFirstDate();
    this.setState({
      startDate: possibleFirstDay
    })
  }

  componentDidUpdate(oldProps) {
    const {transactionType} = this.props;
    const newProps = this.props;

    let transactionsAreLoadedName = transactionType.toLowerCase() + 'sAreLoaded';

    if (!this.state.transactionsAreLoaded && newProps.transactionHolder[transactionsAreLoadedName]) {
      this.setState({
        ...this.state,
        transactionsAreLoaded: true,
      })
    }
  }

  handleDateChange = (id, value) => {
    this.setState({
      [id]: value
    });
  };

  handleSearch() {
    const {endDate, startDate} = this.state;
    const {userHolder, logHolder, transactionType} = this.props;
    if (startDate !== undefined && startDate !== null) {
      let parameters = createContextParameters(endDate, startDate, transactionType);
      let context = createFetchTransactionsContext(userHolder, logHolder, parameters);
      this.setState({
        transactionsAreLoaded: false
      });
      this.props.fetchTransactions(context);
    }
  }

  render() {
    const {endDate, startDate, transactionsAreLoaded} = this.state;
    const {transactionType} = this.props;

    let transactionTableBody;
    if (transactionsAreLoaded === undefined || transactionsAreLoaded === null) {
      transactionTableBody = null;
    } else if (transactionsAreLoaded === false) {
      transactionTableBody = <Loading/>
    } else {
      transactionTableBody = <TransactionTableBody transactionType={transactionType}/>;
    }


    return (
      <div className="card my-3">
        <TransactionTableSearchBar startDateId="startDate" startDate={startDate} startDatePlaceHolder="Select start date"
                                   endDateId="endDate" endDate={endDate} endDatePlaceHolder="Select end date"
                                   handleDateChange={this.handleDateChange} handleSearch={this.handleSearch}/>
        <div className="card-body pt-0 mb-3 mr-0">
          {transactionTableBody}
        </div>
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    categoryHolder: state.categoryHolder,
    logHolder: state.logHolder,
    transactionHolder: state.transactionHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {
        fetchTransactions: fetchTransactions,
      },
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(TransactionTable)
