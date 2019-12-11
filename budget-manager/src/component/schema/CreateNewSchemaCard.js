import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import AlertMessageComponent from "../AlertMessageComponent";
import SchemaForm from "./SchemaForm";
import {createContext} from "../../actions/common/createContext";
import {createSchema} from "../../actions/schema/createSchema";
import {getMessage, removeMessage} from "../../actions/message/messageActions";

class CreateNewSchemaCard extends Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  handleSubmit(schema) {
    const {userHolder, logHolder, createSchema} = this.props;
    createSchema(createContext(userHolder, logHolder), schema);
  }

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {categoryHolder, logHolder, target} = this.props;
    let incomeMainCategories = categoryHolder.incomeMainCategories;
    let outcomeMainCategories = categoryHolder.outcomeMainCategories;

    let successMessage = getMessage(logHolder.messages, "createSchemaSuccess", true);
    let errorMessage = getMessage(logHolder.messages, "createSchemaError", false);
    let loading = successMessage.value === null && errorMessage.value === null;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <SchemaForm formTitle="Create schema" incomeMainCategories={incomeMainCategories} outcomeMainCategoryList={outcomeMainCategories}
                        handleSubmit={this.handleSubmit} loading={loading}/>
            <AlertMessageComponent message={successMessage} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={errorMessage} onChange={this.handleDismiss}/>
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
        createSchema: createSchema,
        removeMessage: removeMessage
      },
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(CreateNewSchemaCard)
