import React, {Component} from 'react';
import ModelStringValue from "../../layout/form/ModelStringValue";
import SubCategoryEditPopUp from "../subCategory/SubCategoryEditPopUp";
import SubCategoryList from "../subCategory/selectSubCategory/SubCategoryList";
import {categoryMessages} from "../../../store/MessageHolder";
import {createEmptyMainCategory} from "../../../actions/category/createMainCategoryMethods";
import {replaceElementAtArray} from "../../../actions/common/listActions";

class MainCategoryForm extends Component {

  state = {
    mainCategoryModel: createEmptyMainCategory(),
    editAbleSubCategory: null
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.setSubCategoryModelSet = this.setSubCategoryModelSet.bind(this);
    this.refreshSubCategories = this.refreshSubCategories.bind(this);
    this.showMainCategoryEdit = this.showMainCategoryEdit.bind(this);
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

  componentWillUpdate(nextProps, nextState, nextContext) {
    if (nextProps.mainCategoryModel !== undefined && this.state.mainCategoryModel.id === null) {
      this.setState({
        mainCategoryModel: nextProps.mainCategoryModel
      })
    }
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
    this.props.handleSubmit(this.state.mainCategoryModel);
  };

  refreshSubCategories = (subCategory) => {
    let newSubCategories = replaceElementAtArray(this.state.mainCategoryModel.subCategoryModelSet, subCategory);
    this.setState(prevState => ({
      mainCategoryModel: {
        ...prevState.mainCategoryModel,
        subCategoryModelSet: newSubCategories
      }
    }));
  };

  showMainCategoryEdit(message) {
    this.props.showMainCategoryEdit(message);
  }

  showCategoryEdit = (subCategory) => {
    this.setState({
      editAbleSubCategory: subCategory
    });
  };

  render() {
    const {subCategoryList, transactionType, formTitle, popup} = this.props;
    const {name, subCategoryModelSet} = this.state.mainCategoryModel;
    const {editAbleSubCategory} = this.state;
    const {categoryNameLabel, categoryNameMessage} = categoryMessages;

    let editableSubcategories = popup === undefined;

    let editCategory = editAbleSubCategory === null ? null : (
      <SubCategoryEditPopUp subCategoryModel={editAbleSubCategory} transactionType={transactionType}
                            showCategoryEdit={this.showCategoryEdit} refreshSubCategories={this.refreshSubCategories}/>);

    let closeButton = editableSubcategories ? null :
      (
        <button className="btn btn-outline-danger mx-3 mt-3 mb-2" onClick={this.showMainCategoryEdit}>
          <span>&times;</span>
          <span> Close </span>
        </button>
      );

    return (
      <React.Fragment>
        <form className="form-group mb-0" onSubmit={this.handleSubmit}>
          <h4 className="mt-3 mx-auto">{formTitle}</h4>
          <ModelStringValue onChange={this.handleFieldChange}
                            id="name" model={name} labelTitle={categoryNameLabel}
                            placeHolder={categoryNameMessage} type="text"/>
          <SubCategoryList subCategoriesOfMainCategory={subCategoryModelSet}
                           subCategoryListFromRepository={subCategoryList}
                           transactionType={transactionType}
                           editable={editableSubcategories}
                           setSubCategoryModelSet={this.setSubCategoryModelSet}
                           showCategoryEdit={this.showCategoryEdit}/>
          <button className="btn btn-outline-success mt-3 mb-2">
            <span className="fas fa-pencil-alt"/>
            <span> Save main category </span>
          </button>
          {closeButton}
        </form>
        {editCategory}
      </React.Fragment>
    )
  }
}

export default MainCategoryForm;
