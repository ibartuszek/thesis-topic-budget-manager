import React, {Component} from 'react';
import {connect} from "react-redux";
import ModelStringValue from "../layout/form/ModelStringValue";
import {validateModel} from "../../actions/validation/validateModel";
import {categoryMessages} from "../../store/MessageHolder";
import {createTransactionContext} from "../../actions/common/createContext";
import {createSubCategory} from "../../actions/category/createSubCategory";
import {getMessage, removeMessage} from "../../actions/message/messageActions";
import AlertMessageComponent from "../AlertMessageComponent";

class SubCategoryForm extends Component {

  state = {
    subCategoryModel: {
      id: null,
      name: {
        value: '',
        errorMessage: null,
        minimumLength: 2,
        maximumLength: 50,
      },
      transactionType: {
        value: null,
        errorMessage: null,
        possibleEnumValues: [
          "INCOME",
          "OUTCOME"
        ]
      }
    }
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  componentDidMount() {
    this.setState(prevState => ({
      subCategoryModel: {
        ...prevState.subCategoryModel,
        transactionType: {
          ...prevState.transactionType,
          value: this.props.transactionType
        }
      }
    }))
  }

  handleFieldChange(id, value, errorMessage) {
    this.setState(prevState => ({
      subCategoryModel: {
        ...prevState.subCategoryModel,
        [id]: {
          ...prevState.subCategoryModel[id],
          value: value,
          errorMessage: errorMessage
        }
      }
    }));
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const {userHolder, logHolder, transactionType, createSubCategory} = this.props;
    const {subCategoryModel} = this.state;
    if (validateModel(subCategoryModel)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      createSubCategory(context, subCategoryModel);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {target, logHolder} = this.props;
    const {name} = this.state.subCategoryModel;
    const {categoryNameLabel, categoryNameMessage} = categoryMessages;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <form className="form-group mb-0" onSubmit={this.handleSubmit}>
              <h4 className="mt-3 mx-auto">New supplementary category</h4>
              <ModelStringValue onChange={this.handleFieldChange}
                                id="name" model={name}
                                labelTitle={categoryNameLabel} placeHolder={categoryNameMessage} type="text"/>
              <button className="btn btn-outline-success mt-3 mb-2">
                <span className="fas fa-pencil-alt"/>
                <span> Save supplementary category </span>
              </button>
              <AlertMessageComponent message={getMessage(logHolder.messages, "createSubCategorySuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "createSubCategoryError", false)} onChange={this.handleDismiss}/>
            </form>
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

export default connect(mapStateToProps, mapDispatchToProps)(SubCategoryForm);
