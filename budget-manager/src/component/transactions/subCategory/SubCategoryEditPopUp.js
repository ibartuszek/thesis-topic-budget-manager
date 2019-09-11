import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import SubCategoryForm from "./SubCategoryForm";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {validateModel} from "../../../actions/validation/validateModel";
import {createTransactionContext} from "../../../actions/common/createContext";
import {categoryMessages} from "../../../store/MessageHolder";
import {updateSubCategory} from "../../../actions/category/updateSubCategory";

class SubCategoryEditPopUp extends Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.showCategoryEdit = this.showCategoryEdit.bind(this);
  }

  handleSubmit = (subCategoryModel) => {
    const {userHolder, logHolder, transactionType, updateSubCategory, refreshSubCategories} = this.props;
    if (validateModel(subCategoryModel)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      updateSubCategory(context, subCategoryModel);
      refreshSubCategories(subCategoryModel);
    }
  };

  showCategoryEdit() {
    this.props.showCategoryEdit(null);
  }

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {transactionType, subCategoryModel, logHolder} = this.props;

    return (
      <div className='custom-popup'>
        <div className='card card-body custom-popup-inner custom-popup-inner-subcategory'>
          <SubCategoryForm transactionType={transactionType}
                           subCategoryModel={subCategoryModel}
                           formTitle={categoryMessages.updateSubCategoryTitle}
                           handleSubmit={this.handleSubmit}
                           popup={true} showCategoryEdit={this.showCategoryEdit}/>
          <AlertMessageComponent message={getMessage(logHolder.messages, "updateSubCategorySuccess", true)} onChange={this.handleDismiss}/>
          <AlertMessageComponent message={getMessage(logHolder.messages, "updateSubCategoryError", false)} onChange={this.handleDismiss}/>
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
