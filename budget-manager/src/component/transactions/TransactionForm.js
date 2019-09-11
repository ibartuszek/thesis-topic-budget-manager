import React, {Component} from 'react';
import {connect} from "react-redux";
import AlertMessageComponent from "../AlertMessageComponent";
import CurrencySelect from "./CurrencySelect";
import EditMainCategory from "./mainCategory/EditMainCategory";
import EditSubCategory from "./subCategory/EditSubCategory";
import MainCategorySelect from "./mainCategory/MainCategorySelect";
import ModelAmountValue from "../layout/form/ModelAmountValue";
import ModelStringValue from "../layout/form/ModelStringValue";
import SubCategorySelect from "./subCategory/SubCategorySelect";
import {createMainCategory} from "../../actions/category/createMainCategory";
import {getMessage, removeMessage} from "../../actions/message/messageActions";
import {transactionMessages} from "../../store/MessageHolder";
import MonthlySelect from "./MonthlySelect";
import ModelDateValue from "../layout/form/ModelDateValue";
import {convertDate} from "../../actions/date/dateActions";
import moment from "moment";
import {dateProperties} from "../../store/Properties";

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
      endDate: null,
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
    this.handleModelValueChange = this.handleModelValueChange.bind(this);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  componentDidMount() {
    const {transactionType, mainCategoryList} = this.props;
    const mainCategory = mainCategoryList.length > 0 ? mainCategoryList[0] : undefined;
    let now = moment().format(dateProperties.dateFormat);
    let possibleFirstDay = convertDate(moment().subtract(dateProperties.subtractToFirstDate, dateProperties.unit), dateProperties.dateFormat);
    this.setState(prevState => ({
      transactionModel: {
        ...prevState.transactionModel,
        transactionType: {
          ...prevState.transactionType,
          value: transactionType
        },
        date: {
          ...prevState.date,
          value: now,
          possibleFirstDay: possibleFirstDay
        },
        mainCategory: mainCategory
      }
    }))
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
    console.log("SUBMIT")
    /*const {userHolder, logHolder, transactionType, createMainCategory} = this.props;
    const {mainCategoryModel} = this.state;
    if (validateModel(mainCategoryModel)) {
      let context = createTransactionContext(userHolder, logHolder, transactionType);
      createMainCategory(context, mainCategoryModel);
    }*/
  };

  handleDismiss(message) {
    this.props.removeMessage(this.props.logHolder.messages, message);
  }

  showCategoryEdit = (category) => {
    let editAbleMainCategory = category !== null && category.subCategoryModelSet !== undefined ? category : null;
    let editAbleSubCategory = category !== null && category.subCategoryModelSet === undefined ? category : null;
    this.setState({
      editAbleMainCategory: editAbleMainCategory,
      editAbleSubCategory: editAbleSubCategory
    })
  };

  render() {
    const {target, logHolder, transactionType, mainCategoryList, subCategoryList} = this.props;
    const {editAbleMainCategory, editAbleSubCategory} = this.state;
    const {title, amount, currency, mainCategory, subCategory, monthly, date, endDate, description} = this.state.transactionModel;
    const {
      transactionTitleLabel, transactionTitleMessage, transactionAmountLabel, transactionAmountMessage,
      transactionDateLabel, transactionDateMessage, transactionEndDateLabel, transactionEndDateMessage,
      transactionDescriptionLabel, transactionDescriptionMessage
    } = transactionMessages;

    let endDateComponent = !monthly ? null :
      (
        <ModelDateValue onChange={this.handleModelValueChange}
                        id="endDate" model={endDate}
                        labelTitle={transactionEndDateLabel} placeHolder={transactionEndDateMessage}/>
      );

    let editMainCategory = editAbleMainCategory === null ? null : (
      <EditMainCategory mainCategoryModel={editAbleMainCategory} transactionType={transactionType} subCategoryList={subCategoryList}
                        showCategoryEdit={this.showCategoryEdit} refreshMainCategories={this.handleFieldChange}/>);

    let editSubCategory = editAbleSubCategory === null ? null : (
      <EditSubCategory subCategoryModel={editAbleSubCategory} transactionType={transactionType}
                       showCategoryEdit={this.showCategoryEdit} refreshSubCategories={this.handleFieldChange}/>);

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <form className="form-group mb-0" onSubmit={this.handleSubmit}>
              <h4 className="mt-3 mx-auto">New transaction</h4>
              <ModelStringValue onChange={this.handleModelValueChange}
                                id="title" model={title}
                                labelTitle={transactionTitleLabel} placeHolder={transactionTitleMessage} type="text"/>
              <ModelAmountValue onChange={this.handleModelValueChange}
                                id="amount" model={amount}
                                labelTitle={transactionAmountLabel} placeHolder={transactionAmountMessage} type="number"/>
              <CurrencySelect handleFieldChange={this.handleModelValueChange} currency={currency}/>
              <MainCategorySelect handleFieldChange={this.handleFieldChange} showCategoryEdit={this.showCategoryEdit}
                                  mainCategory={mainCategory} mainCategoryList={mainCategoryList}/>
              <SubCategorySelect handleFieldChange={this.handleFieldChange} showCategoryEdit={this.showCategoryEdit}
                                 subCategory={subCategory} subCategoryList={subCategoryList}/>
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
              <AlertMessageComponent message={getMessage(logHolder.messages, "createTransactionSuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "createTransactionError", false)} onChange={this.handleDismiss}/>
            </form>
          </div>
        </div>
        {editMainCategory}
        {editSubCategory}
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

export default connect(mapStateToProps, mapDispatchToProps)(TransactionForm);
