import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import AlertMessageComponent from "../AlertMessageComponent";
import SchemaForm from "./SchemaForm";
import {createContext} from "../../actions/common/createContext";
import {updateSchema} from "../../actions/schema/updateSchema";
import {getMessage, removeMessage} from "../../actions/message/messageActions";

class UpdateSchemaPopUp extends Component {

  constructor(props) {
    super(props);
    this.closePopUp = this.closePopUp.bind(this);
    this.handleDismissMessage = this.handleDismissMessage.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(schema) {
    const {userHolder, logHolder, updateSchema} = this.props;
    updateSchema(createContext(userHolder, logHolder), schema);
  }

  handleDismissMessage(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  closePopUp() {
    const {logHolder} = this.props;
    let successMessage = getMessage(logHolder.messages, "updateSchemaSuccess", true);
    let errorMessage = getMessage(logHolder.messages, "updateSchemaError", false);
    if (successMessage.value !== null) {
      this.handleDismissMessage(successMessage);
    }
    if (errorMessage.value) {
      this.handleDismissMessage(errorMessage);
    }
    this.props.showSchemaEdit(null);
  }

  render() {
    const {categoryHolder, logHolder, schema} = this.props;
    let incomeMainCategories = categoryHolder.incomeMainCategories;
    let outcomeMainCategories = categoryHolder.outcomeMainCategories;

    let successMessage = getMessage(logHolder.messages, "updateSchemaSuccess", true);
    let errorMessage = getMessage(logHolder.messages, "updateSchemaError", false);
    let loading = successMessage.value === null && errorMessage.value === null;

    return (
      <React.Fragment>
        <div className='custom-popup'>
          <div className="card card-body custom-popup-inner">
            <div className="container overflow-auto">
              <SchemaForm formTitle="Update schema" incomeMainCategories={incomeMainCategories} outcomeMainCategoryList={outcomeMainCategories}
                          loading={loading} handleSubmit={this.handleSubmit} editableSchema={schema} showSchemaEdit={this.closePopUp}/>
              <AlertMessageComponent message={successMessage} onChange={this.handleDismissMessage}/>
              <AlertMessageComponent message={errorMessage} onChange={this.handleDismissMessage}/>
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
    categoryHolder: state.categoryHolder,
    statisticsHolder: state.statisticsHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {
        updateSchema: updateSchema,
        removeMessage: removeMessage
      },
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(UpdateSchemaPopUp)
