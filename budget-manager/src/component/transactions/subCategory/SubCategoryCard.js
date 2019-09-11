import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import SubCategoryForm from "./SubCategoryForm";
import {categoryMessages} from "../../../store/MessageHolder";
import {createSubCategory} from "../../../actions/category/createSubCategory";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {validateModel} from "../../../actions/validation/validateModel";

class SubCategoryCard extends Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  handleSubmit = (subCategoryModel) => {
    const {userHolder, logHolder, transactionType, createSubCategory} = this.props;
    if (validateModel(subCategoryModel)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      createSubCategory(context, subCategoryModel);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {target, transactionType, logHolder} = this.props;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <SubCategoryForm transactionType={transactionType}
                             formTitle={categoryMessages.createSubCategoryTitle}
                             handleSubmit={this.handleSubmit}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "createSubCategorySuccess", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "createSubCategoryError", false)} onChange={this.handleDismiss}/>
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
