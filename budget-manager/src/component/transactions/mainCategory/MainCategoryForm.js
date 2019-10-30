import React, {Component} from 'react';
import CloseButton from "../../buttons/CloseButton";
import ModelStringValue from "../../layout/form/ModelStringValue";
import SubCategoryEditPopUp from "../subCategory/SubCategoryEditPopUp";
import SubCategoryList from "../subCategory/selectSubCategory/SubCategoryList";
import {createEmptyMainCategory} from "../../../actions/category/createMainCategoryMethods";
import {replaceElementAtArray} from "../../../actions/common/listActions";
import {validateModel} from "../../../actions/validation/validateModel";
import SpinButton from "../../buttons/SpinButton";

class MainCategoryForm extends Component {

  state = {
    mainCategoryModel: createEmptyMainCategory(),
    editAbleSubCategory: null,
    loading: false
  };

  constructor(props) {
    super(props);
    this.closePopUp = this.closePopUp.bind(this);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.refreshSubCategories = this.refreshSubCategories.bind(this);
    this.setSubCategoryModelSet = this.setSubCategoryModelSet.bind(this);
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
    if (this.state.loading && !nextProps.loading) {
      this.setState({
        loading: false
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

  handleSubmit = (e) => {
    e.preventDefault();
    if (validateModel(this.state.mainCategoryModel)) {
      this.setState({
        loading: true
      });
      this.props.handleSubmit(this.state.mainCategoryModel);
    }
  };

  refreshSubCategories = (id, subCategory) => {
    let newSubCategories = replaceElementAtArray(this.state.mainCategoryModel.subCategoryModelSet, subCategory);
    this.setState(prevState => ({
      mainCategoryModel: {
        ...prevState.mainCategoryModel,
        subCategoryModelSet: newSubCategories
      }
    }));
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

  closePopUp() {
    this.setState({
      mainCategoryModel: createEmptyMainCategory(),
      loading: false
    });
    this.props.closePopUp();
  }

  showSubCategoryPopUp = (subCategory) => {
    this.setState({
      editAbleSubCategory: subCategory
    });
  };

  render() {
    const {subCategoryList, transactionType, formTitle, popup} = this.props;
    const {name, subCategoryModelSet} = this.state.mainCategoryModel;
    const {editAbleSubCategory, loading} = this.state;

    let editableSubcategories = popup === undefined;

    let subCategoryEditContainer = editAbleSubCategory === null ? null : (
      <SubCategoryEditPopUp subCategoryModel={editAbleSubCategory} transactionType={transactionType}
                            showSubCategoryPopUp={this.showSubCategoryPopUp}
                            refreshSubCategories={this.refreshSubCategories}/>);

    let closeButton = popup === undefined ? null : (<CloseButton buttonLabel="Close" closePopUp={this.closePopUp}/>);

    return (
      <React.Fragment>
        <form className="form-group mb-0" onSubmit={this.handleSubmit}>
          <h4 className="mt-3 mx-auto">{formTitle}</h4>
          <ModelStringValue onChange={this.handleFieldChange} id="name" model={name} labelTitle="Name of the category"
                            placeHolder="Please write the name of the new category." type="text"/>
          <SubCategoryList subCategoriesOfMainCategory={subCategoryModelSet} subCategoryListFromRepository={subCategoryList}
                           transactionType={transactionType} editable={editableSubcategories}
                           setSubCategoryModelSet={this.setSubCategoryModelSet} showCategoryEdit={this.showSubCategoryPopUp}/>
          <SpinButton buttonLabel="Save main category" icon="fas fa-pencil-alt" loading={loading}/>
          {closeButton}
        </form>
        {subCategoryEditContainer}
      </React.Fragment>
    )
  }
}

export default MainCategoryForm;
