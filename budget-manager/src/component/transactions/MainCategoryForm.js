import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../AlertMessageComponent";
import ModelStringValue from "../layout/form/ModelStringValue";
import {categoryMessages} from "../../store/MessageHolder";
import {createMainCategory} from "../../actions/category/createMainCategory";
import {createTransactionContext} from "../../actions/common/createContext";
import {getMessage, removeMessage} from "../../actions/message/messageActions";
import {validateModel} from "../../actions/validation/validateModel";
import SubCategoryList from "./subCategory/SubCategoryList";
import EditSubCategory from "./subCategory/EditSubCategory";
import {replaceElementAtArray} from "../../actions/common/listActions";

class MainCategoryForm extends Component {

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
    },
    editAbleSubCategory: null
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.setSubCategoryModelSet = this.setSubCategoryModelSet.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  componentDidMount() {
    this.setState(prevState => ({
      mainCategoryModel: {
        ...prevState.mainCategoryModel,
        transactionType: {
          ...prevState.transactionType,
          value: this.props.transactionType
        }
      }
    }))
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

  setSubCategoryModelSet(modifiedSubCategoryModelSet) {
    this.setState(prevState => ({
      ...prevState,
      mainCategoryModel: {
        ...prevState.mainCategoryModel,
        subCategoryModelSet: modifiedSubCategoryModelSet
      }
    }));
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const {userHolder, logHolder, transactionType, createMainCategory} = this.props;
    const {mainCategoryModel} = this.state;
    if (validateModel(mainCategoryModel)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      createMainCategory(context, mainCategoryModel);
    }
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }


  showCategoryEdit = (subCategory) => {
    this.setState({
      editAbleSubCategory: subCategory
    });
  };

  refreshSubCategories = (subCategory) => {
    let newSubCategories = replaceElementAtArray(this.state.mainCategoryModel.subCategoryModelSet, subCategory);
    this.setState(prevState => ({
      mainCategoryModel: {
        ...prevState.mainCategoryModel,
        subCategoryModelSet: newSubCategories
      }
    }))
  };

  render() {
    const {target, subCategoryListName, logHolder, transactionType} = this.props;
    const {editAbleSubCategory} = this.state;
    const {name, subCategoryModelSet} = this.state.mainCategoryModel;
    const {categoryNameLabel, categoryNameMessage} = categoryMessages;

    let editCategory = editAbleSubCategory === null ? null : (
      <EditSubCategory subCategoryModel={editAbleSubCategory} transactionType={transactionType}
                       showCategoryEdit={this.showCategoryEdit} refreshSubCategories={this.refreshSubCategories}/>);


    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <form className="form-group mb-0" onSubmit={this.handleSubmit}>
              <h4 className="mt-3 mx-auto">New main category</h4>
              <ModelStringValue onChange={this.handleFieldChange}
                                id="name" model={name} labelTitle={categoryNameLabel}
                                placeHolder={categoryNameMessage} type="text"/>
              <SubCategoryList subCategoryModelSet={subCategoryModelSet}
                               subCategoryListName={subCategoryListName}
                               transactionType={transactionType}
                               setSubCategoryModelSet={this.setSubCategoryModelSet}
                               showCategoryEdit={this.showCategoryEdit}/>
              <button className="btn btn-outline-success mt-3 mb-2">
                <span className="fas fa-pencil-alt"/>
                <span> Save main category </span>
              </button>
              <AlertMessageComponent message={getMessage(logHolder.messages, "createMainCategorySuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "createMainCategoryError", false)} onChange={this.handleDismiss}/>
            </form>
          </div>
        </div>
        {editCategory}
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

export default connect(mapStateToProps, mapDispatchToProps)(MainCategoryForm);
