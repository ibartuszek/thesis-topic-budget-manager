import React, {Component} from 'react';
import ModelSelectValue from "../../layout/form/ModelSelectValue";
import {createCategoryListForSelect, createCategoryListWithNullForSelect, findElementByName} from "../../../actions/common/listActions";
import {transactionMessages} from "../../../store/MessageHolder";

class MainCategorySelect extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(newMainCategory) {
    const {mainCategoryList} = this.props;
    let newCategory = findElementByName(mainCategoryList, newMainCategory);
    this.props.handleCategoryChange("mainCategory", newCategory);
  }

  showCategoryEdit = (category) => {
    this.props.showCategoryEdit(category);
  };

  render() {
    const {mainCategory, mainCategoryList} = this.props;
    const {transactionMainCategoryLabel, transactionMainCategoryMessage} = transactionMessages;

    let mainCategorySelectList = mainCategoryList.length === 0
      ? createCategoryListWithNullForSelect(mainCategoryList)
      : createCategoryListForSelect(mainCategoryList);

    let model = mainCategory !== undefined ? mainCategory.name.value : undefined;

    return (
      <React.Fragment>
        <ModelSelectValue onChange={this.handleChange}
                          id="mainCategory" model={model} value={model} elementList={mainCategorySelectList}
                          editableObject={mainCategory} showCategoryEdit={this.showCategoryEdit}
                          labelTitle={transactionMainCategoryLabel} placeHolder={transactionMainCategoryMessage} type="text"/>
      </React.Fragment>)
  }

}

export default MainCategorySelect;
