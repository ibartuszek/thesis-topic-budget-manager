import React, {Component} from 'react';
import {connect} from "react-redux";
import CategoryListItem from "./CategoryListItem";
import ModelSelectValue from "../layout/form/ModelSelectValue";
import ModelStringValue from "../layout/form/ModelStringValue";
import {validateModel} from "../../actions/validation/validateModel";
import {categoryMessages} from "../../store/MessageHolder";
import {addElementToArray, createCategoryListForSelect, removeElementFromArray} from "../../actions/common/listActions";
import {createTransactionContext} from "../../actions/common/createContext";
import {createMainCategory} from "../../actions/category/createMainCategory";

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
    }
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleAddCategory = this.handleAddCategory.bind(this);
    this.handleRemoveCategory = this.handleRemoveCategory.bind(this)
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

  handleRemoveCategory(id) {
    this.setSubCategoryModelSet(removeElementFromArray(this.state.mainCategoryModel.subCategoryModelSet, id));
  }

  handleAddCategory(id, value, newCategory) {
    this.setSubCategoryModelSet(addElementToArray(this.state.mainCategoryModel.subCategoryModelSet, newCategory));
  }

  setSubCategoryModelSet(modifiedSubCategoryModelSet) {
    this.setState(prevState => ({
      mainCategoryModel: {
        ...prevState.mainCategoryModel,
        subCategoryModelSet: modifiedSubCategoryModelSet
      }
    }))
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

  render() {
    const {target, subCategoryListName, categoryHolder} = this.props;
    const {name, subCategoryModelSet} = this.state.mainCategoryModel;
    const {addNewSubCategory, categoryNameLabel, categoryNameMessage, selectNewCategory, subCategoryLabel} = categoryMessages;

    const subcategories = subCategoryModelSet.map((subCategory) =>
      <CategoryListItem key={subCategory.id} id={subCategory.id} name={subCategory.name.value}
                        label={subCategoryLabel} onChange={this.handleRemoveCategory}/>);

    const categoryList = createCategoryListForSelect(categoryHolder[subCategoryListName], subCategoryModelSet);

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <form className="form-group mb-0" onSubmit={this.handleSubmit}>
              <h4 className="mt-3 mx-auto">New main category</h4>
              <ModelStringValue onChange={this.handleFieldChange}
                                id="name" model={name}
                                labelTitle={categoryNameLabel} placeHolder={categoryNameMessage} type="text"/>
              {subcategories}
              <ModelSelectValue onChange={this.handleAddCategory}
                                id="newSubCategoryModel" model={undefined} labelTitle={addNewSubCategory}
                                placeHolder={selectNewCategory} elementList={categoryList}/>
              <button className="btn btn-outline-success mt-3 mb-2">
                <span className="fas fa-pencil-alt"/>
                <span> Save main category </span>
              </button>
            </form>
          </div>
        </div>
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
    createMainCategory: (context, model) => dispatch(createMainCategory(context, model))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(MainCategoryForm);
