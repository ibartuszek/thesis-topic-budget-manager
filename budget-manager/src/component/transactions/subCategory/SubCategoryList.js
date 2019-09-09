import React, {Component} from 'react';
import {connect} from "react-redux";
import ModelSelectValue from "../../layout/form/ModelSelectValue";
import {addElementToArray, createCategoryListForSelect, removeElementFromArray} from "../../../actions/common/listActions";
import {categoryMessages} from "../../../store/MessageHolder";
import SubCategoryListItem from "./SubCategoryListItem";

class SubCategoryList extends Component {

  state = {
    subCategoryModelSet: [],
  };

  constructor(props) {
    super(props);
    this.handleAddCategory = this.handleAddCategory.bind(this);
    this.handleRemoveCategory = this.handleRemoveCategory.bind(this);
    this.setSubCategoryModelSet = this.setSubCategoryModelSet.bind(this);
    this.showCategoryEdit = this.showCategoryEdit.bind(this);
  }

  handleRemoveCategory(id) {
    this.setSubCategoryModelSet(removeElementFromArray(this.props.subCategoryModelSet, id));
  }

  handleAddCategory(id, value, newCategory) {
    this.setSubCategoryModelSet(addElementToArray(this.props.subCategoryModelSet, newCategory));
  }

  setSubCategoryModelSet(modifiedSubCategoryModelSet) {
    this.setState({
      subCategoryModelSet: modifiedSubCategoryModelSet
    });
    this.props.setSubCategoryModelSet(modifiedSubCategoryModelSet);
  }

  showCategoryEdit = (subCategory) => {
    console.log("showCategoryEdit");
    this.props.showCategoryEdit(subCategory);
  };

  render() {
    const {subCategoryLabel, selectNewCategory, addNewSubCategory} = categoryMessages;
    const {categoryHolder, subCategoryListName, subCategoryModelSet} = this.props;

    const subcategories = subCategoryModelSet.map((subCategory) =>
      <SubCategoryListItem key={subCategory.id} id={subCategory.id} name={subCategory.name.value} category={subCategory}
                           label={subCategoryLabel} showCategoryEdit={this.showCategoryEdit}
                           onChange={this.handleRemoveCategory}/>);

    const categoryList = createCategoryListForSelect(categoryHolder[subCategoryListName], subCategoryModelSet);

    return (
      <React.Fragment>
        {subcategories}
        <ModelSelectValue onChange={this.handleAddCategory}
                          id="newSubCategoryModel" model={undefined} labelTitle={addNewSubCategory}
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
