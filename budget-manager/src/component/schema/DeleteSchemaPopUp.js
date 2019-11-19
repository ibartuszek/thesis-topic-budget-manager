import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../AlertMessageComponent";
import {createContext} from "../../actions/common/createContext";
import {deleteSchema} from "../../actions/schema/deleteSchema";
import {getMessage, removeMessage} from "../../actions/message/messageActions";
import {validateSchema} from "../../actions/validation/validateSchema";

class DeleteSchemaPopUp extends Component {

  constructor(props) {
    super(props);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.showSchemaDelete = this.showSchemaDelete.bind(this);
  }

  handleSubmit = (schema) => {
    const {userHolder, logHolder, deleteSchema} = this.props;
    if (validateSchema(schema)) {
      let context = createContext(userHolder, logHolder);
      deleteSchema(context, schema);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  showSchemaDelete() {
    this.props.showSchemaDelete(null);
  }

  render() {
    const {logHolder, schema} = this.props;

    return (
      <React.Fragment>
        <div className='custom-popup'>
          <div className="card-body custom-popup-inner custom-popup-inner-delete-transaction">
            <div className="row justify-content-md-center">
              <button className="btn btn-outline-danger mt-3 mb-2" onClick={() => this.handleSubmit(schema)}>
                <span className="fas fa-trash-alt"/>
                <span> Delete </span>
              </button>
              <button className="btn btn-outline-secondary mx-3 mt-3 mb-2" onClick={this.showSchemaDelete}>
                <span>&times;</span>
                <span> Cancel </span>
              </button>
              <AlertMessageComponent message={getMessage(logHolder.messages, "deleteSchemaSuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "deleteSchemaError", false)} onChange={this.handleDismiss}/>
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
    statisticsHolder: state.statisticsHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    deleteSchema: (context, model) => dispatch(deleteSchema(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(DeleteSchemaPopUp);
