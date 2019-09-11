import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../../AlertMessageComponent";
import ModelStringValue from "../../layout/form/ModelStringValue";
import SubCategoryList from "../subCategory/SubCategoryList";
import {categoryMessages} from "../../../store/MessageHolder";
import {createTransactionContext} from "../../../actions/common/createContext";
import {getMessage, removeMessage} from "../../../actions/message/messageActions";
import {validateModel} from "../../../actions/validation/validateModel";
import {updateMainCategory} from "../../../actions/category/updateMainCategory";

class EditMainCategory extends Component {

  state = {
    mainCategoryModel: {
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
      },
      subCategoryModelSet: []
    }
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
    this.showCategoryEdit = this.showCategoryEdit.bind(this);
    this.setSubCategoryModelSet = this.setSubCategoryModelSet.bind(this);
  }

  componentDidMount() {
    this.setState({
      mainCategoryModel: this.props.mainCategoryModel
    })
  }

  handleFieldChange(id, value, errorMessage) {
    this.setState(prevState => ({
      mainCategoryModel: {
        ...prevState.mainCategoryModel,
        [id]: {
          ...prevState.mainCategoryModel[id],
          value: value,
          errorMessage: errorMessage
        }
      }
    }));
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const {userHolder, logHolder, transactionType, updateMainCategory, refreshMainCategories} = this.props;
    const {mainCategoryModel} = this.state;
    if (validateModel(mainCategoryModel)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      updateMainCategory(context, mainCategoryModel);
      refreshMainCategories('mainCategory', mainCategoryModel);
    }
  };

  setSubCategoryModelSet(modifiedSubCategoryModelSet) {
    this.setState(prevState => ({
      ...prevState,
      mainCategoryModel: {
        ...prevState.mainCategoryModel,
        subCategoryModelSet: modifiedSubCategoryModelSet
      }
    }));
  }

  showCategoryEdit() {
    this.props.showCategoryEdit(null);
  }

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  render() {
    const {logHolder, transactionType, subCategoryList} = this.props;
    const {name, subCategoryModelSet} = this.state.mainCategoryModel;
    const {categoryNameLabel, categoryNameMessage} = categoryMessages;

    return (
      <div className='custom-popup'>
        <div className='card card-body custom-popup-inner custom-popup-inner-subcategory'>
          <form className="form-group mb-0" onSubmit={this.handleSubmit}>
            <h4 className="mt-3 mx-auto">Update main category:</h4>
            <ModelStringValue onChange={this.handleFieldChange}
                              id="name" model={name}
                              labelTitle={categoryNameLabel} placeHolder={categoryNameMessage} type="text"/>
            <SubCategoryList subCategoriesOfMainCategory={subCategoryModelSet}
                             subCategoryListFromRepository={subCategoryList}
                             transactionType={transactionType}
                             editable={false}
                             setSubCategoryModelSet={this.setSubCategoryModelSet}/>
            <div className="mx-auto mt-3 mb-2">
              <button className="btn btn-outline-success mx-3">
                <span className="fas fa-pencil-alt"/>
                <span> Update main category </span>
              </button>
              <button className="btn btn-outline-danger mx-3" onClick={this.showCategoryEdit}>
                <span>&times;</span>
                <span> Close </span>
              </button>
            </div>
            <AlertMessageComponent message={getMessage(logHolder.messages, "updateMainCategorySuccess", true)} onChange={this.handleDismiss}/>
            <AlertMessageComponent message={getMessage(logHolder.messages, "updateMainCategoryError", false)} onChange={this.handleDismiss}/>
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
    updateMainCategory: (context, model) => dispatch(updateMainCategory(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(EditMainCategory);
