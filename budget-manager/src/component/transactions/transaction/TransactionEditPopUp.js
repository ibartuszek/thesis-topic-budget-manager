import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import TransactionForm from "./TransactionForm";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {updateTransaction} from "../../../actions/transaction/updateTransaction";

class TransactionEditPopUp extends Component {

  state = {
    transaction: null
  };

  constructor(props) {
    super(props);
    this.handleDismissMessage = this.handleDismissMessage.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.refreshTransactions = this.refreshTransactions.bind(this);
    this.showTransactionEdit = this.showTransactionEdit.bind(this);
  }

  componentWillUpdate(nextProps, nextState, nextContext) {

    const {transaction} = this.state;
    if (transaction !== null) {
      this.refreshTransactions();
      this.setState({
        transaction: null
      })
    }
  }

  createMessages(logHolder) {
    return {
      createTransactionSuccessMessage: getMessage(logHolder.messages, "updateTransactionSuccess", true),
      createTransactionErrorMessage: getMessage(logHolder.messages, "updateTransactionError", false),
      uploadPictureSuccessMessage: getMessage(logHolder.messages, "uploadPictureSuccess", true),
      uploadPictureErrorMessage: getMessage(logHolder.messages, "uploadPictureError", false),
      deletePictureSuccessMessage: getMessage(logHolder.messages, "deletePictureSuccess", true),
      deletePictureErrorMessage: getMessage(logHolder.messages, "deletePictureError", false),
      getPictureSuccessMessage: getMessage(logHolder.messages, "getPictureSuccess", true),
      getPictureErrorMessage: getMessage(logHolder.messages, "getPictureError", false)
    }
  }

  handleDismissMessage(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  handleSubmit = (transaction) => {
    const {userHolder, logHolder, transactionType, updateTransaction} = this.props;
    updateTransaction(createTransactionContext(userHolder, logHolder, transactionType), transaction);
    this.setState({
      transaction: transaction
    });
  };

  refreshTransactions() {
    const {transactionHolder, transactionType} = this.props;
    const transactions = transactionHolder[transactionType.toLowerCase() + "s"];
    this.props.refreshTransactions(transactions);
  }

  showTransactionEdit() {
    const {logHolder, showTransactionEdit} = this.props;
    const {handleDismissMessage} = this;
    let messages = this.createMessages(logHolder);
    Object.keys(messages).forEach(function (key, index) {
      let message = messages[key];
      if (message.value !== null) {
        handleDismissMessage(message);
      }
    });
    showTransactionEdit(null);
  }

  render() {
    const {categoryHolder, logHolder, transactionModel, transactionHolder, transactionType} = this.props;

    let mainCategoryList = categoryHolder[transactionType.toLowerCase() + "MainCategories"];

    let messages = this.createMessages(logHolder);
    let loading = messages.createTransactionSuccessMessage.value === null && messages.createTransactionErrorMessage.value === null;

    return (
      <React.Fragment>
        <div className='custom-popup'>
          <div className="card card-body custom-popup-inner mt-3">
            <div className="container overflow-auto">
              <TransactionForm transactionType={transactionType} transactionModel={transactionModel} mainCategoryList={mainCategoryList}
                               formTitle="Update transaction" handleSubmit={this.handleSubmit} popup={true} loading={loading}
                               firstPossibleDay={transactionHolder.firstPossibleDay} closePopUp={this.showTransactionEdit}/>

              <AlertMessageComponent message={messages.createTransactionSuccessMessage} onChange={this.handleDismissMessage}/>
              <AlertMessageComponent message={messages.createTransactionErrorMessage} onChange={this.handleDismissMessage}/>
              <AlertMessageComponent message={messages.uploadPictureSuccessMessage} onChange={this.handleDismissMessage}/>
              <AlertMessageComponent message={messages.uploadPictureErrorMessage} onChange={this.handleDismissMessage}/>
              <AlertMessageComponent message={messages.deletePictureSuccessMessage} onChange={this.handleDismissMessage}/>
              <AlertMessageComponent message={messages.deletePictureErrorMessage} onChange={this.handleDismissMessage}/>
              <AlertMessageComponent message={messages.getPictureSuccessMessage} onChange={this.handleDismissMessage}/>
              <AlertMessageComponent message={messages.getPictureErrorMessage} onChange={this.handleDismissMessage}/>
            </div>
          </div>
        </div>
      </React.Fragment>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    logHolder: state.logHolder,
    categoryHolder: state.categoryHolder,
    pictureHolder: state.pictureHolder,
    transactionHolder: state.transactionHolder,
    userHolder: state.userHolder
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    updateTransaction: (context, model) => dispatch(updateTransaction(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(TransactionEditPopUp);
