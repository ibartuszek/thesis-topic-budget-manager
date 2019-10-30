import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import SubCategoryForm from "./SubCategoryForm";
import {createSubCategory} from "../../../actions/category/createSubCategory";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";

class SubCategoryCard extends Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismissMessage = this.handleDismissMessage.bind(this);
  }

  handleDismissMessage(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  handleSubmit = (subCategoryModel) => {
    const {userHolder, logHolder, transactionType, createSubCategory} = this.props;
    createSubCategory(createTransactionContext(userHolder, logHolder, transactionType), subCategoryModel);
  };

  render() {
    const {target, transactionType, logHolder} = this.props;

    let successMessage = getMessage(logHolder.messages, "createSubCategorySuccess", true);
    let errorMessage = getMessage(logHolder.messages, "createSubCategoryError", false);
    let loading = successMessage.value === null && errorMessage.value === null;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <SubCategoryForm transactionType={transactionType} formTitle="Create new subcategory"
                             handleSubmit={this.handleSubmit} loading={loading}/>
            <AlertMessageComponent message={successMessage} onChange={this.handleDismissMessage}/>
            <AlertMessageComponent message={errorMessage} onChange={this.handleDismissMessage}/>
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
    createSubCategory: (context, model) => dispatch(createSubCategory(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(SubCategoryCard);
