import React, {Component} from 'react';
import ModelSelectValue from "../../../layout/form/ModelSelectValue";
import {createCategoryListWithNullForSelect, findElementByName} from "../../../../actions/common/listActions";
import {transactionMessages} from "../../../../store/MessageHolder";

class SubCategorySelect extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(newSubCategory) {
    const {subCategoryList} = this.props;
    let newCategory = findElementByName(subCategoryList, newSubCategory);
    this.props.handleModelValueChange("subCategory", newCategory);
  }

  showSubCategoryPopUp = (category) => {
    this.props.showSubCategoryPopUp(category);
  };

  render() {
    const {subCategory, subCategoryList, editable} = this.props;
    const {transactionSubCategoryLabel, transactionSubCategoryMessage} = transactionMessages;

    let subCategorySelectList = createCategoryListWithNullForSelect(subCategoryList);

    let model = subCategory === undefined || subCategory === null ? undefined : subCategory.name.value;

    let editableSubCategory = editable && subCategory !== undefined && subCategory !== null;

    return (
      <React.Fragment>
        <ModelSelectValue onChange={this.handleChange}
                          id="subCategory" model={model} value={model} elementList={subCategorySelectList}
                          editableObject={subCategory} showCategoryEdit={this.showSubCategoryPopUp} editable={editableSubCategory}
                          labelTitle={transactionSubCategoryLabel} placeHolder={transactionSubCategoryMessage} type="text"/>
      </React.Fragment>)
  }

}

export default SubCategorySelect;
