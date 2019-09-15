import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import TransactionForm from "./TransactionForm";
import {createTransaction} from "../../../actions/transaction/createTransaction";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {transactionMessages} from "../../../store/MessageHolder";
import {validateTransaction} from "../../../actions/validation/validateTransaction";

class TransactionCard extends Component {

  constructor(props) {
    super(props);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit = (transaction) => {
    const {userHolder, logHolder, transactionType, createTransaction} = this.props;
    if (validateTransaction(transaction)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      createTransaction(context, transaction);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {logHolder, mainCategoryList, subCategoryList, target, transactionType} = this.props;
    console.log(this.props);

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <TransactionForm transactionType={transactionType} mainCategoryList={mainCategoryList} subCategoryList={subCategoryList}
                             formTitle={transactionMessages.createTransactionTitle} handleSubmit={this.handleSubmit}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "createTransactionSuccess", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "createTransactionError", false)} onChange={this.handleDismiss}/>
          </div>
        </div>
      </React.Fragment>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    userHolder: state.userHolder,
    logHolder: state.logHolder,
    categoryHolder: state.categoryHolder
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    createTransaction: (context, model) => dispatch(createTransaction(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(TransactionCard);
