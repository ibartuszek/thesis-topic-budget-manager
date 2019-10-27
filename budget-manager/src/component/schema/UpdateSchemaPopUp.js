import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import AlertMessageComponent from "../AlertMessageComponent";
import SchemaForm from "./SchemaForm";
import {createContext} from "../../actions/common/createContext";
import {validateSchema} from "../../actions/validation/validateSchema";
import {updateSchema} from "../../actions/schema/updateSchema";
import {getMessage, removeMessage} from "../../actions/message/messageActions";

class UpdateSchemaPopUp extends Component {

  constructor(props) {
    super(props);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.showSchemaEdit = this.showSchemaEdit.bind(this);
  }

  handleSubmit(schema) {
    const {userHolder, logHolder, updateSchema} = this.props;
    if (validateSchema(schema)) {
      let context = createContext(userHolder, logHolder);
      updateSchema(context, schema);
    }
  }

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  showSchemaEdit() {
    this.props.showSchemaEdit(null);
  }

  render() {
    const {categoryHolder, logHolder, schema} = this.props;
    let outcomeMainCategories = categoryHolder.outcomeMainCategories;
    console.log(schema);

    return (
      <React.Fragment>
        <div className='custom-popup'>
          <div className="card card-body custom-popup-inner">
            <div className="container overflow-auto">
              <SchemaForm formTitle="Update schema" mainCategoryList={outcomeMainCategories}
                          handleSubmit={this.handleSubmit} editableSchema={schema} showSchemaEdit={this.showSchemaEdit}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "updateSchemaSuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "updateSchemaError", false)} onChange={this.handleDismiss}/>
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
