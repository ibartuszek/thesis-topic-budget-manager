import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {validateTransaction} from "../../../actions/validation/validateTransaction";
import {deleteTransaction} from "../../../actions/transaction/deleteTransaction";

class TransactionDeletePopUp extends Component {

  constructor(props) {
    super(props);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.showTransactionDelete = this.showTransactionDelete.bind(this);
  }

  handleSubmit = (transaction) => {
    const {userHolder, logHolder, transactionType, deleteTransaction} = this.props;
    if (validateTransaction(transaction)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      deleteTransaction(context, transaction);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  showTransactionDelete() {
    this.props.showTransactionDelete(null);
  }

  render() {
    const {logHolder, transactionModel} = this.props;

    return (
      <React.Fragment>
        <div className='custom-popup'>
          <div className="card-body custom-popup-inner custom-popup-inner-delete-transaction">
            <div className="row justify-content-md-center">
              <button className="btn btn-outline-danger mt-3 mb-2" onClick={() => this.handleSubmit(transactionModel)}>
                <span className="fas fa-trash-alt"/>
                <span> Delete </span>
              </button>
              <button className="btn btn-outline-secondary mx-3 mt-3 mb-2" onClick={this.showTransactionDelete}>
                <span>&times;</span>
                <span> Cancel </span>
              </button>
              <AlertMessageComponent message={getMessage(logHolder.messages, "deleteTransactionSuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "deleteTransactionError", false)} onChange={this.handleDismiss}/>
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
    transactionHolder: state.transactionHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    deleteTransaction: (context, model) => dispatch(deleteTransaction(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(TransactionDeletePopUp);
