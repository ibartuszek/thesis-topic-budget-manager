import React, {Component} from 'react';
import ModelSelectValue from "../../layout/form/ModelSelectValue";
import {createCategoryListForSelect, createCategoryListWithNullForSelect, findElementByName} from "../../../actions/common/listActions";

class MainCategorySelect extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(newMainCategory) {
    const {mainCategoryList} = this.props;
    let newCategory = findElementByName(mainCategoryList, newMainCategory);
    this.props.handleModelValueChange("mainCategory", newCategory);
  }

  showCategoryEdit = (category) => {
    this.props.showMainCategoryPopUp(category);
  };

  render() {
    const {mainCategory, mainCategoryList, editable} = this.props;

    let mainCategorySelectList = mainCategoryList.length === 0
      ? createCategoryListWithNullForSelect(mainCategoryList)
      : createCategoryListForSelect(mainCategoryList);

    let model = mainCategory !== undefined ? mainCategory.name.value : undefined;

    return (
      <React.Fragment>
        <ModelSelectValue onChange={this.handleChange}
                          id="mainCategory" model={model} value={model} elementList={mainCategorySelectList}
                          editableObject={mainCategory} editable={editable} showCategoryEdit={this.showCategoryEdit}
                          labelTitle="Main category" placeHolder="The main category of the transaction" type="text"/>
      </React.Fragment>)
  }

}

export default MainCategorySelect;
