import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import SubCategoryForm from "./SubCategoryForm";
import {categoryMessages} from "../../../store/MessageHolder";
import {createTransactionContext} from "../../../actions/common/createContext";
import {findElementById, findElementByName} from "../../../actions/common/listActions";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {updateSubCategory} from "../../../actions/category/updateSubCategory";

class SubCategoryEditPopUp extends Component {

  state = {
    subCategory: null
  };

  constructor(props) {
    super(props);
    this.closePopUp = this.closePopUp.bind(this);
    this.handleDismissMessage = this.handleDismissMessage.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.refreshSubCategories = this.refreshSubCategories.bind(this);
  }

  closePopUp() {
    this.props.showSubCategoryPopUp(null);
  }

  componentWillUpdate(nextProps, nextState, nextContext) {
    const {categoryHolder, transactionType} = this.props;
    const {subCategory} = this.state;
    if (subCategory !== null) {
      this.refreshSubCategories(categoryHolder[transactionType.toLowerCase() + "SubCategories"]);
      this.setState({
        subCategory: null
      })
    }
  }

  handleDismissMessage(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  handleSubmit = (subCategoryModel) => {
    const {logHolder, transactionType, updateSubCategory, userHolder} = this.props;
    updateSubCategory(createTransactionContext(userHolder, logHolder, transactionType), subCategoryModel);
    this.setState({
      subCategory: subCategoryModel
    })
  };

  refreshSubCategories(subCategoryList) {
    const {subCategory} = this.state;
    let subCategoryFromRepoById = findElementById(subCategoryList, subCategory.id);
    let subCategoryFromRepoByName = findElementByName(subCategoryList, subCategory.name.value);
    if (subCategoryFromRepoById !== null
      && (subCategoryFromRepoByName === null || subCategoryFromRepoByName.id === subCategory.id)) {
      this.props.refreshSubCategories("subCategory", subCategory);
    }
  }

  render() {
    const {logHolder, subCategoryModel, transactionType} = this.props;


    let successMessage = getMessage(logHolder.messages, "updateSubCategorySuccess", true);
    let errorMessage = getMessage(logHolder.messages, "updateSubCategoryError", false);
    let loading = successMessage.value === null && errorMessage.value === null;

    return (
      <div className='custom-popup'>
        <div className='card card-body custom-popup-inner'>
          <SubCategoryForm transactionType={transactionType}
                           subCategoryModel={subCategoryModel}
                           formTitle={categoryMessages.updateSubCategoryTitle}
                           handleSubmit={this.handleSubmit}
                           loading={loading}
                           popup={true} closePopUp={this.closePopUp}/>
          <AlertMessageComponent message={successMessage} onChange={this.handleDismissMessage}/>
          <AlertMessageComponent message={errorMessage} onChange={this.handleDismissMessage}/>
        </div>
      </div>
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
    updateSubCategory: (context, model) => dispatch(updateSubCategory(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(SubCategoryEditPopUp);
