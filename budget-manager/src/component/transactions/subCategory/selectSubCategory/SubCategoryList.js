import React, {Component} from 'react';
import {connect} from "react-redux";
import SubCategoryListItem from "./SubCategoryListItem";
import {
  addElementToArray,
  createCategoryListWithNullForSelect,
  findElementById,
  findElementByName,
  removeElementFromArray
} from "../../../../actions/common/listActions";
import {categoryMessages} from "../../../../store/MessageHolder";
import ModelSelectValue from "../../../layout/form/ModelSelectValue";

class SubCategoryList extends Component {

  state = {
    subCategoriesOfMainCategory: []
  };

  constructor(props) {
    super(props);
    this.handleAddCategory = this.handleAddCategory.bind(this);
    this.handleRemoveCategory = this.handleRemoveCategory.bind(this);
    this.setSubCategoryModelSet = this.setSubCategoryModelSet.bind(this);
    this.showCategoryEdit = this.showCategoryEdit.bind(this);
  }

  componentDidMount() {
    this.setState({
      subCategoriesOfMainCategory: this.props.subCategoriesOfMainCategory
    })
  }

  handleRemoveCategory(removableCategoryId) {
    const {subCategoryListFromRepository, subCategoriesOfMainCategory} = this.props;
    let removableCategory = findElementById(subCategoryListFromRepository, removableCategoryId);
    let newSubCategories = removeElementFromArray(subCategoriesOfMainCategory, removableCategory);
    this.setSubCategoryModelSet(newSubCategories);
    this.setState({
      subCategoriesOfMainCategory: newSubCategories
    })
  }

  handleAddCategory(newCategoryName) {
    const {subCategoryListFromRepository, subCategoriesOfMainCategory} = this.props;
    let newCategory = findElementByName(subCategoryListFromRepository, newCategoryName);
    this.setSubCategoryModelSet(addElementToArray(subCategoriesOfMainCategory, newCategory));
  }

  setSubCategoryModelSet(modifiedSubCategoryModelSet) {
    this.props.setSubCategoryModelSet(modifiedSubCategoryModelSet);
  }

  showCategoryEdit = (subCategory) => {
    this.props.showCategoryEdit(subCategory);
  };

  render() {
    const {subCategoryLabel, selectNewCategory, addNewSubCategory} = categoryMessages;
    const {subCategoryListFromRepository, subCategoriesOfMainCategory, editable} = this.props;

    const subcategories = subCategoriesOfMainCategory.map((subCategory, index) =>
      <SubCategoryListItem key={index} id={subCategory.id} name={subCategory.name.value} category={subCategory}
                           label={subCategoryLabel} editable={editable} showCategoryEdit={this.showCategoryEdit}
                           onChange={this.handleRemoveCategory}/>);
    const categoryList = createCategoryListWithNullForSelect(subCategoryListFromRepository, subCategoriesOfMainCategory);

    return (
      <React.Fragment>
        {subcategories}
        <ModelSelectValue onChange={this.handleAddCategory}
                          id="newSubCategoryModel" value={undefined} labelTitle={addNewSubCategory}
                          placeHolder={selectNewCategory} elementList={categoryList}/>
      </React.Fragment>
    );
  }

}

const mapStateToProps = (state) => {
  return {
    categoryHolder: state.categoryHolder
  }
};

export default connect(mapStateToProps)(SubCategoryList);
