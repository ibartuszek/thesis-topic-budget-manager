import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import MainCategoryForm from "./MainCategoryForm";
import {createMainCategory} from "../../../actions/category/createMainCategory";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";

class MainCategoryCard extends Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismissMessage = this.handleDismissMessage.bind(this);
  }

  handleSubmit = (mainCategoryModel) => {
    const {userHolder, logHolder, transactionType, createMainCategory} = this.props;
    createMainCategory(createTransactionContext(userHolder, logHolder, transactionType), mainCategoryModel);
  };

  handleDismissMessage(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    // TODO: check subCategoryList...
    const {target, transactionType, logHolder, subCategoryList} = this.props;

    let successMessage = getMessage(logHolder.messages, "createMainCategorySuccess", true);
    let errorMessage = getMessage(logHolder.messages, "createMainCategoryError", false);
    let loading = successMessage.value === null && errorMessage.value === null;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <MainCategoryForm transactionType={transactionType} subCategoryList={subCategoryList} handleSubmit={this.handleSubmit}
                              formTitle="Create new main category" loading={loading}/>
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
    createMainCategory: (context, model) => dispatch(createMainCategory(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(MainCategoryCard);
