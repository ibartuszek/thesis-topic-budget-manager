import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import TransactionForm from "./TransactionForm";
import {createTransaction} from "../../../actions/transaction/createTransaction";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {createTrackingCoordinates} from "../../../actions/transaction/createTrackingCoordinates";

class TransactionCard extends Component {

  constructor(props) {
    super(props);
    this.handleDismissMessage = this.handleDismissMessage.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  createMessages(logHolder) {
    return {
      createTransactionSuccessMessage: getMessage(logHolder.messages, "createTransactionSuccess", true),
      createTransactionErrorMessage: getMessage(logHolder.messages, "createTransactionError", false),
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
    const {userHolder, logHolder, transactionType, createTransaction} = this.props;
    createTrackingCoordinates(transactionType, userHolder.userData.tracking)
      .then(function (coordinate) {
        createTransaction(createTransactionContext(userHolder, logHolder, transactionType), transaction, coordinate);
      });
  };

  render() {
    const {categoryHolder, logHolder, target, transactionHolder, transactionType} = this.props;

    let mainCategoryList = categoryHolder[transactionType.toLowerCase() + "MainCategories"];

    let messages = this.createMessages(logHolder);
    let loading = messages.createTransactionSuccessMessage.value === null
      && messages.createTransactionErrorMessage.value === null;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">

            <TransactionForm transactionType={transactionType} mainCategoryList={mainCategoryList} formTitle="Create new transaction"
                             handleSubmit={this.handleSubmit} loading={loading} firstPossibleDay={transactionHolder.firstPossibleDay}/>

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
    createTransaction: (context, model, coordinate) => dispatch(createTransaction(context, model, coordinate)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(TransactionCard);
