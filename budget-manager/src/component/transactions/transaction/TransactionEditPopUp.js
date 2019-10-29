import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import TransactionForm from "./TransactionForm";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {transactionMessages} from "../../../store/MessageHolder";
import {updateTransaction} from "../../../actions/transaction/updateTransaction";
import {validateTransaction} from "../../../actions/validation/validateTransaction";

class TransactionEditPopUp extends Component {

  constructor(props) {
    super(props);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.showTransactionEdit = this.showTransactionEdit.bind(this);
  }

  handleSubmit = (transaction) => {
    const {userHolder, logHolder, transactionType, updateTransaction} = this.props;
    if (validateTransaction(transaction)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      updateTransaction(context, transaction);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  showTransactionEdit() {
    this.props.showTransactionEdit(null);
  }

  render() {
    const {logHolder, mainCategoryList, subCategoryList, transactionModel, transactionType} = this.props;

    console.log(this.props);
    return (
      <React.Fragment>
        <div className='custom-popup'>
          <div className="card card-body custom-popup-inner">
            <div className="container overflow-auto">
              <TransactionForm transactionType={transactionType} transactionModel={transactionModel} mainCategoryList={mainCategoryList}
                               subCategoryList={subCategoryList} formTitle={transactionMessages.updateTransactionTitle} handleSubmit={this.handleSubmit}
                               popup={true} showTransactionEdit={this.showTransactionEdit}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "updateTransactionSuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "updateTransactionError", false)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "uploadPictureSuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "uploadPictureError", false)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "deletePictureSuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "deletePictureError", false)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "getPictureSuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "getPictureError", false)} onChange={this.handleDismiss}/>
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
