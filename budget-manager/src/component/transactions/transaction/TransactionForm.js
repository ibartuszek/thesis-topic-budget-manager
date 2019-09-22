import React, {Component} from 'react';
import moment from "moment";
import CurrencySelect from "../CurrencySelect";
import MainCategoryEditPopUp from "../mainCategory/MainCategoryEditPopUp";
import MainCategorySelect from "../mainCategory/MainCategorySelect";
import ModelAmountValue from "../../layout/form/ModelAmountValue";
import ModelDateValue from "../../layout/form/ModelDateValue";
import ModelStringValue from "../../layout/form/ModelStringValue";
import MonthlySelect from "../MonthlySelect";
import SubCategoryEditPopUp from "../subCategory/SubCategoryEditPopUp";
import SubCategorySelect from "../subCategory/selectSubCategory/SubCategorySelect";
import {dateProperties} from "../../../store/Properties";
import {getPossibleFirstDate} from "../../../actions/date/dateActions";
import {transactionMessages} from "../../../store/MessageHolder";

class TransactionForm extends Component {

  state = {
    transactionModel: {
      id: null,
      title: {
        value: '',
        errorMessage: null,
        minimumLength: 2,
        maximumLength: 50,
      },
      amount: {
        value: 1.0,
        errorMessage: null,
        positive: true,
      },
      currency: {
        value: "HUF",
        errorMessage: null,
        possibleEnumValues: [
          "EUR",
          "USD",
          "HUF"
        ]
      },
      transactionType: {
        value: null,
        errorMessage: null,
        possibleEnumValues: [
          "INCOME",
          "OUTCOME"
        ]
      },
      mainCategory: undefined,
      subCategory: undefined,
      monthly: false,
      date: {
        value: null,
        errorMessage: null,
        possibleFirstDay: null
      },
      endDate: {
        value: null,
        errorMessage: null,
        possibleFirstDay: null
      },
      description: {
        value: '',
        errorMessage: null,
        minimumLength: 2,
        maximumLength: 100,
      },
      locked: false
    },
    editAbleMainCategory: null,
    editAbleSubCategory: null,
  };

  constructor(props) {
    super(props);
    this.setFirstPossibleDay = this.setFirstPossibleDay.bind(this);
    this.handleModelValueChange = this.handleModelValueChange.bind(this);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.showTransactionEdit = this.showTransactionEdit.bind(this);
  }

  componentDidMount() {
    const {transactionType, mainCategoryList} = this.props;
    const mainCategory = mainCategoryList.length > 0 ? mainCategoryList[0] : undefined;
    let now = moment().format(dateProperties.dateFormat);
    this.setState(prevState => ({
      transactionModel: {
        ...prevState.transactionModel,
        transactionType: {
          ...prevState.transactionModel.transactionType,
          value: transactionType
        },
        date: {
          ...prevState.transactionModel.date,
          value: now,
        },
        mainCategory: mainCategory
      }
    }));
    this.setFirstPossibleDay();
  }

  componentWillUpdate(nextProps, nextState, nextContext) {
    if (nextProps.transactionModel !== undefined && this.state.transactionModel.id === null) {
      this.setState({
        transactionModel: nextProps.transactionModel
      });
      this.setFirstPossibleDay();
    }
  }

  setFirstPossibleDay() {
    let possibleFirstDay = getPossibleFirstDate();
    this.setState(prevState => ({
      ...prevState,
      transactionModel: {
        ...prevState.transactionModel,
        date: {
          ...prevState.transactionModel.date,
          possibleFirstDay: possibleFirstDay
        },
        endDate: {
          ...prevState.transactionModel.endDate,
          possibleFirstDay: possibleFirstDay
        }
      }
    }));
  }

  handleModelValueChange(id, value, errorMessage) {
    this.setState(prevState => ({
      transactionModel: {
        ...prevState.transactionModel,
        [id]: {
          ...prevState.transactionModel[id],
          value: value,
          errorMessage: errorMessage
        }
      }
    }));
  }

  handleFieldChange(id, value) {
    this.setState(prevState => ({
      transactionModel: {
        ...prevState.transactionModel,
        [id]: value
      }
    }));
  }

  handleSubmit = (e) => {
    e.preventDefault();
    this.props.handleSubmit(this.state.transactionModel);
  };

  showCategoryEdit = (category) => {
    let editAbleMainCategory = category !== null && category !== undefined && category.subCategoryModelSet !== undefined ? category : null;
    let editAbleSubCategory = category !== null && category !== undefined && category.subCategoryModelSet === undefined ? category : null;
    this.setState({
      editAbleMainCategory: editAbleMainCategory,
      editAbleSubCategory: editAbleSubCategory
    })
  };

  showTransactionEdit(message) {
    this.props.showTransactionEdit(message);
  }

  render() {
    const {formTitle, mainCategoryList, subCategoryListFromRepo, popup, transactionType} = this.props;
    const {editAbleMainCategory, editAbleSubCategory} = this.state;
    const {title, amount, currency, mainCategory, subCategory, monthly, date, endDate, description} = this.state.transactionModel;
    const {
      transactionTitleLabel, transactionTitleMessage, transactionAmountLabel, transactionAmountMessage,
      transactionDateLabel, transactionDateMessage, transactionEndDateLabel, transactionEndDateMessage,
      transactionDescriptionLabel, transactionDescriptionMessage
    } = transactionMessages;

    let editableCategories = popup === undefined;

    let endDateComponent = !monthly ? null :
      (
        <ModelDateValue onChange={this.handleModelValueChange}
                        id="endDate" model={endDate}
                        labelTitle={transactionEndDateLabel} placeHolder={transactionEndDateMessage}/>
      );

    let subCategoryList = mainCategory !== undefined && mainCategory !== null ? mainCategory.subCategoryModelSet : [];

    let editMainCategory = !editableCategories || editAbleMainCategory === null ? null : (
      <MainCategoryEditPopUp mainCategoryModel={editAbleMainCategory} transactionType={transactionType} subCategoryList={subCategoryListFromRepo}
                             showCategoryEdit={this.showCategoryEdit} refreshMainCategories={this.handleFieldChange}/>);

    let editSubCategory = !editableCategories || editAbleSubCategory === null ? null : (
      <SubCategoryEditPopUp subCategoryModel={editAbleSubCategory} transactionType={transactionType}
                            showCategoryEdit={this.showCategoryEdit} refreshSubCategories={this.handleFieldChange}/>);

    let closeButton = editableCategories ? null :
      (
        <button className="btn btn-outline-danger mx-3 mt-3 mb-2" onClick={this.showTransactionEdit}>
          <span>&times;</span>
          <span> Close </span>
        </button>
      );

    return (
      <React.Fragment>
        <form className="form-group mb-0" onSubmit={this.handleSubmit}>
          <h4 className="mt-3 mx-auto">{formTitle}</h4>
          <ModelStringValue onChange={this.handleModelValueChange}
                            id="title" model={title}
                            labelTitle={transactionTitleLabel} placeHolder={transactionTitleMessage} type="text"/>
          <ModelAmountValue onChange={this.handleModelValueChange}
                            id="amount" model={amount}
                            labelTitle={transactionAmountLabel} placeHolder={transactionAmountMessage} type="number"/>
          <CurrencySelect handleModelValueChange={this.handleModelValueChange} currency={currency}/>
          <MainCategorySelect handleFieldChange={this.handleFieldChange} showCategoryEdit={this.showCategoryEdit}
                              mainCategory={mainCategory} mainCategoryList={mainCategoryList} editable={editableCategories}/>
          <SubCategorySelect handleFieldChange={this.handleFieldChange} showCategoryEdit={this.showCategoryEdit}
                             subCategory={subCategory} subCategoryList={subCategoryList} editable={editableCategories}/>
          <MonthlySelect handleFieldChange={this.handleFieldChange} monthly={monthly}/>
          <ModelDateValue onChange={this.handleModelValueChange}
                          id="date" model={date}
                          labelTitle={transactionDateLabel} placeHolder={transactionDateMessage}/>
          {endDateComponent}
          <ModelStringValue onChange={this.handleModelValueChange}
                            id="description" model={description}
                            labelTitle={transactionDescriptionLabel} placeHolder={transactionDescriptionMessage} type="text"/>
          <button className="btn btn-outline-success mt-3 mb-2">
            <span className="fas fa-pencil-alt"/>
            <span> Save transaction </span>
          </button>
          {closeButton}
        </form>
        {editMainCategory}
        {editSubCategory}
      </React.Fragment>
    )
  }
}

export default TransactionForm;
