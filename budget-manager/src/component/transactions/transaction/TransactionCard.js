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
    const {categoryHolder, logHolder, target, transactionType} = this.props;

    let mainCategoryList = categoryHolder[transactionType.toLowerCase() + "MainCategories"];

    let createTransactionSuccessMessage = getMessage(logHolder.messages, "createTransactionSuccess", true);
    let createTransactionErrorMessage = getMessage(logHolder.messages, "createTransactionError", false);
    let uploadPictureSuccessMessage = getMessage(logHolder.messages, "uploadPictureSuccess", true);
    let uploadPictureErrorMessage = getMessage(logHolder.messages, "uploadPictureError", false);
    let deletePictureSuccessMessage = getMessage(logHolder.messages, "deletePictureSuccess", true);
    let deletePictureErrorMessage = getMessage(logHolder.messages, "deletePictureError", false);
    let getPictureSuccessMessage = getMessage(logHolder.messages, "getPictureSuccess", true);
    let getPictureErrorMessage = getMessage(logHolder.messages, "getPictureError", false);
    let loading = createTransactionSuccessMessage.value === null && createTransactionErrorMessage.value === null
      && uploadPictureSuccessMessage.value === null && uploadPictureErrorMessage.value === null
      && deletePictureSuccessMessage.value === null && deletePictureErrorMessage.value === null
      && getPictureSuccessMessage.value === null && getPictureErrorMessage.value === null;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">

            <TransactionForm transactionType={transactionType} mainCategoryList={mainCategoryList} formTitle="Create new transaction"
                             handleSubmit={this.handleSubmit} loading={loading}/>

            <AlertMessageComponent message={createTransactionSuccessMessage} onChange={this.handleDismissMessage}/>
            <AlertMessageComponent message={createTransactionErrorMessage} onChange={this.handleDismissMessage}/>
            <AlertMessageComponent message={uploadPictureSuccessMessage} onChange={this.handleDismissMessage}/>
            <AlertMessageComponent message={uploadPictureErrorMessage} onChange={this.handleDismissMessage}/>
            <AlertMessageComponent message={deletePictureSuccessMessage} onChange={this.handleDismissMessage}/>
            <AlertMessageComponent message={deletePictureErrorMessage} onChange={this.handleDismissMessage}/>
            <AlertMessageComponent message={getPictureSuccessMessage} onChange={this.handleDismissMessage}/>
            <AlertMessageComponent message={getPictureErrorMessage} onChange={this.handleDismissMessage}/>
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
