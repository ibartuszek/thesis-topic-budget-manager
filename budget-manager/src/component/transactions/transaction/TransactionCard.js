import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import TransactionForm from "./TransactionForm";
import {createTransaction} from "../../../actions/transaction/createTransaction";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {transactionMessages} from "../../../store/MessageHolder";
import {validateTransaction} from "../../../actions/validation/validateTransaction";
import {createTrackingCoordinates} from "../../../actions/transaction/createTrackingCoordinates";

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
      createTrackingCoordinates(transactionType, userHolder.userData.tracking).then(function (coordinate) {
        createTransaction(context, transaction, coordinate);
      });

    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {logHolder, mainCategoryList, subCategoryList, target, transactionType} = this.props;
    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <TransactionForm transactionType={transactionType} mainCategoryList={mainCategoryList} subCategoryListFromRepo={subCategoryList}
                             formTitle={transactionMessages.createTransactionTitle} handleSubmit={this.handleSubmit}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "createTransactionSuccess", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "createTransactionError", false)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "uploadPictureSuccess", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "uploadPictureError", false)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "deletePictureSuccess", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "deletePictureError", false)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "getPictureSuccess", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "getPictureError", false)} onChange={this.handleDismiss}/>
          </div>
        </div>
      </React.Fragment>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    logHolder: state.logHolder,
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
