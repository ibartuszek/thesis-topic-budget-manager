import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import MainCategoryForm from "./MainCategoryForm";
import {categoryMessages} from "../../../store/MessageHolder";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {validateModel} from "../../../actions/validation/validateModel";
import {updateMainCategory} from "../../../actions/category/updateMainCategory";

class MainCategoryEditPopUp extends Component {


  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.showMainCategoryEdit = this.showMainCategoryEdit.bind(this);
  }

  handleSubmit = (mainCategoryModel) => {
    const {logHolder, refreshMainCategories, transactionType, updateMainCategory, userHolder} = this.props;
    if (validateModel(mainCategoryModel)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      updateMainCategory(context, mainCategoryModel);
      refreshMainCategories('mainCategory', mainCategoryModel);
    }
  };

  showMainCategoryEdit() {
    this.props.showCategoryEdit(null);
  }

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {logHolder, mainCategoryModel, subCategoryList, transactionType} = this.props;

    return (
      <div className='custom-popup'>
        <div className='card card-body custom-popup-inner'>
          <MainCategoryForm transactionType={transactionType} subCategoryList={subCategoryList} mainCategoryModel={mainCategoryModel}
                            formTitle={categoryMessages.updateMainCategoryTitle} popup={true}
                            handleSubmit={this.handleSubmit} showMainCategoryEdit={this.showMainCategoryEdit}/>
          <AlertMessageComponent message={getMessage(logHolder.messages, "updateMainCategorySuccess", true)} onChange={this.handleDismiss}/>
          <AlertMessageComponent message={getMessage(logHolder.messages, "updateMainCategoryError", false)} onChange={this.handleDismiss}/>
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
