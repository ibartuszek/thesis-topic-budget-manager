import React, {Component} from 'react';
import {connect} from "react-redux";
import ModelStringValue from "../layout/form/ModelStringValue";
import {createMainCategory} from "../../actions/category/createMainCategory";
import {getMessage, removeMessage} from "../../actions/message/messageActions";
import AlertMessageComponent from "../AlertMessageComponent";
import {convertDate} from "../../actions/date/dateActions";
import {transactionMessages} from "../../store/MessageHolder";
import moment from "moment";
import ModelAmountValue from "../layout/form/ModelAmountValue";
import ModelSelectValue from "../layout/form/ModelSelectValue";

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
      mainCategory: null,
      subCategory: null,
      monthly: false,
      date: {
        value: null,
        errorMessage: null,
        possibleFirstDay: null
      },
      endDate: null,
      description: null,
      locked: false
    },
    editAbleMainCategory: null,
    editAbleSubCategory: null,
    dateFormat: "YYYY-MM-DD"
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleDismiss = this.handleDismiss.bind(this);
  }

  componentDidMount() {
    let firstPossibleDay = convertDate(moment().subtract(30, 'days'), this.state.dateFormat);
    this.setState(prevState => ({
      transactionModel: {
        ...prevState.transactionModel,
        transactionType: {
          ...prevState.transactionType,
          value: this.props.transactionType
        },
        date: {
          ...prevState.date,
          possibleFirstDay: firstPossibleDay
        }
      }
    }))
  }

  handleFieldChange(id, value, errorMessage) {
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

  showCategoryEdit = (subCategory) => {
    console.log("ShowCategoryEdit" + subCategory);
    /* this.setState({
      editAbleSubCategory: subCategory
    });*/
  };

  refreshSubCategories = (subCategory) => {
    console.log("refreshSubCategories" + subCategory);
    /*let newSubCategories = replaceElementAtArray(this.state.mainCategoryModel.subCategoryModelSet, subCategory);
    this.setState(prevState => ({
      mainCategoryModel: {
        ...prevState.mainCategoryModel,
        subCategoryModelSet: newSubCategories
      }
    }))*/
  };

  render() {
    const {target, logHolder, transactionType} = this.props;
    const {title, amount, currency} = this.state.transactionModel;
    const {
      transactionTitleLabel, transactionTitleMessage, transactionAmountLabel, transactionAmountMessage,
      transactionCurrencyLabel, transactionCurrencyMessage
    } = transactionMessages;
    /*
        let editCategory = editAbleSubCategory === null ? null : (
          <EditSubCategory subCategoryModel={editAbleSubCategory} transactionType={transactionType}
                           showCategoryEdit={this.showCategoryEdit} refreshSubCategories={this.refreshSubCategories}/>);
    */

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <form className="form-group mb-0" onSubmit={this.handleSubmit}>
              <h4 className="mt-3 mx-auto">New transaction</h4>
              <ModelStringValue onChange={this.handleFieldChange}
                                id="title" model={title}
                                labelTitle={transactionTitleLabel} placeHolder={transactionTitleMessage} type="text"/>
              <ModelAmountValue onChange={this.handleFieldChange}
                                id="amount" model={amount}
                                labelTitle={transactionAmountLabel} placeHolder={transactionAmountMessage} type="number"/>
              <ModelSelectValue onChange={this.handleFieldChange}
                                id="currency" model={currency} value={currency.value} elementList={currency.possibleEnumValues}
                                labelTitle={transactionCurrencyLabel} placeHolder={transactionCurrencyMessage} type="text"/>
              <button className="btn btn-outline-success mt-3 mb-2">
                <span className="fas fa-pencil-alt"/>
                <span> Save transaction </span>
              </button>
              <AlertMessageComponent message={getMessage(logHolder.messages, "createTransactionSuccess", true)} onChange={this.handleDismiss}/>
              <AlertMessageComponent message={getMessage(logHolder.messages, "createTransactionError", false)} onChange={this.handleDismiss}/>
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
    createMainCategory: (context, model) => dispatch(createMainCategory(context, model)),
    removeMessage: (messages, message) => dispatch(removeMessage(messages, message))
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(TransactionForm);
