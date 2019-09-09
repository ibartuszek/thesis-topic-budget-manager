import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import ModelStringValue from "../../layout/form/ModelStringValue";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {validateModel} from "../../../actions/validation/validateModel";
import {createTransactionContext} from "../../../actions/common/createContext";
import {categoryMessages} from "../../../store/MessageHolder";
import {updateSubCategory} from "../../../actions/category/updateSubCategory";

class EditSubCategory extends Component {

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
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.showCategoryEdit = this.showCategoryEdit.bind(this);
  }

  componentDidMount() {
    this.setState({
      subCategoryModel: this.props.subCategoryModel
    })
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
    const {userHolder, logHolder, transactionType, updateSubCategory, refreshSubCategories} = this.props;
    const {subCategoryModel} = this.state;
    if (validateModel(subCategoryModel)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      updateSubCategory(context, subCategoryModel);
      refreshSubCategories(subCategoryModel);
    }
  };

  showCategoryEdit() {
    console.log(this);
    this.props.showCategoryEdit(null);
  }

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {logHolder} = this.props;
    const {subCategoryModel} = this.state;
    const {categoryNameLabel, categoryNameMessage} = categoryMessages;

    return (
      <div className='custom-popup'>
        <div className='card card-body custom-popup-inner custom-popup-inner-subcategory'>
          <form className="form-group mb-0" onSubmit={this.handleSubmit}>
            <h4 className="mt-3 mx-auto">Update supplementary category:</h4>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="name" model={subCategoryModel.name}
                              labelTitle={categoryNameLabel} placeHolder={categoryNameMessage} type="text"/>
            <div className="mx-auto mt-3 mb-2">
              <button className="btn btn-outline-success mx-3">
                <span className="fas fa-pencil-alt"/>
                <span> Save supplementary category </span>
              </button>
              <button className="btn btn-outline-danger mx-3" onClick={this.showCategoryEdit}>
                <span>&times;</span>
                <span> Cancel </span>
              </button>
            </div>
            <AlertMessageComponent message={getMessage(logHolder.messages, "updateSubCategorySuccess", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "updateSubCategoryError", false)} onChange={this.handleDismiss}/>
          </form>
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

export default connect(mapStateToProps, mapDispatchToProps)(EditSubCategory);
