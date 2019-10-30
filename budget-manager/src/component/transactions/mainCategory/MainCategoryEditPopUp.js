import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import MainCategoryForm from "./MainCategoryForm";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {updateMainCategory} from "../../../actions/category/updateMainCategory";
import {findElementById, findElementByName} from "../../../actions/common/listActions";

class MainCategoryEditPopUp extends Component {

  state = {
    mainCategory: null
  };

  constructor(props) {
    super(props);
    this.closePopUp = this.closePopUp.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.refreshMainCategories = this.refreshMainCategories.bind(this);
  }

  componentWillUpdate(nextProps, nextState, nextContext) {
    const {categoryHolder, transactionType} = this.props;
    const {mainCategory} = this.state;
    if (mainCategory !== null) {
      this.refreshMainCategories(categoryHolder[transactionType.toLowerCase() + "MainCategories"]);
      this.setState({
        mainCategory: null
      })
    }
  }

  handleSubmit = (mainCategoryModel) => {
    const {logHolder, transactionType, updateMainCategory, userHolder} = this.props;
    updateMainCategory(createTransactionContext(userHolder, logHolder, transactionType), mainCategoryModel);
    this.setState({
      mainCategory: mainCategoryModel
    })
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  closePopUp() {
    this.props.showCategoryEdit(null);
  }

  refreshMainCategories(mainCategoryList) {
    const {mainCategory} = this.state;
    let mainCategoryFromRepoById = findElementById(mainCategoryList, mainCategory.id);
    let mainCategoryFromRepoByName = findElementByName(mainCategoryList, mainCategory.name.value);
    if (mainCategoryFromRepoById !== null
      && (mainCategoryFromRepoByName === null || mainCategoryFromRepoByName.id === mainCategory.id)) {
      this.props.refreshMainCategories("mainCategory", mainCategory);
    }
  }

  render() {
    const {categoryHolder, logHolder, mainCategoryModel, transactionType} = this.props;

    let successMessage = getMessage(logHolder.messages, "updateMainCategorySuccess", true);
    let errorMessage = getMessage(logHolder.messages, "updateMainCategoryError", false);
    let loading = successMessage.value === null && errorMessage.value === null;

    return (
      <div className='custom-popup'>
        <div className='card card-body custom-popup-inner'>
          <MainCategoryForm transactionType={transactionType} subCategoryList={categoryHolder[transactionType.toLowerCase() + "SubCategories"]}
                            mainCategoryModel={mainCategoryModel} formTitle="Update main category" loading={loading}
                            popup={true} handleSubmit={this.handleSubmit} closePopUp={this.closePopUp}/>
          <AlertMessageComponent message={successMessage} onChange={this.handleDismiss}/>
          <AlertMessageComponent message={errorMessage} onChange={this.handleDismiss}/>
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
    updateMainCategory: (context, model) => dispatch(updateMainCategory(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(MainCategoryEditPopUp);
