import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import MainCategoryForm from "./MainCategoryForm";
import {categoryMessages} from "../../../store/MessageHolder";
import {createMainCategory} from "../../../actions/category/createMainCategory";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {validateModel} from "../../../actions/validation/validateModel";

class MainCategoryCard extends Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  handleSubmit = (mainCategoryModel) => {
    const {userHolder, logHolder, transactionType, createMainCategory} = this.props;
    if (validateModel(mainCategoryModel)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      createMainCategory(context, mainCategoryModel);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {target, transactionType, logHolder, subCategoryList} = this.props;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <MainCategoryForm transactionType={transactionType} subCategoryList={subCategoryList}
                              formTitle={categoryMessages.createMainCategoryTitle}
                              handleSubmit={this.handleSubmit}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "createMainCategorySuccess", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "createMainCategoryError", false)} onChange={this.handleDismiss}/>
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
