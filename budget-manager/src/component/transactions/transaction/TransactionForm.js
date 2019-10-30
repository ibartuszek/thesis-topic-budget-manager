import React, {Component} from 'react';
import moment from "moment";
import EnumSelect from "../EnumSelect";
import MainCategoryEditPopUp from "../mainCategory/MainCategoryEditPopUp";
import MainCategorySelect from "../mainCategory/MainCategorySelect";
import ModelAmountValue from "../../layout/form/ModelAmountValue";
import ModelDateValue from "../../layout/form/ModelDateValue";
import ModelStringValue from "../../layout/form/ModelStringValue";
import YesNoSelect from "../../layout/form/YesNoSelect";
import SubCategoryEditPopUp from "../subCategory/SubCategoryEditPopUp";
import SubCategorySelect from "../subCategory/selectSubCategory/SubCategorySelect";
import {createEmptyTransaction} from "../../../actions/transaction/createTransactionMethods";
import {dateProperties} from "../../../store/Properties";
import {getPossibleFirstDate} from "../../../actions/date/dateActions";
import {transactionMessages} from "../../../store/MessageHolder";
import UploadPicture from "../../layout/picture/UploadPicture";
import RemovePicture from "../../layout/picture/RemovePicture";
import ShowPicture from "../../layout/picture/ShowPicture";
import GetPicture from "../../layout/picture/GetPicture";

class TransactionForm extends Component {

  state = {
    transactionModel: createEmptyTransaction(),
    editAbleMainCategory: null,
    editAbleSubCategory: null,
    showablePicture: null,
  };

  constructor(props) {
    super(props);
    this.getPictureId = this.getPictureId.bind(this);
    this.handleModelValueChange = this.handleModelValueChange.bind(this);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.setFirstPossibleDay = this.setFirstPossibleDay.bind(this);
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
        mainCategory: mainCategory,
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

    this.setState({
      editAbleMainCategory: editAbleMainCategory,
    });
  };

  showSubCategoryPopUp = (subCategory) => {
    let editAbleSubCategory = subCategory !== null && subCategory !== undefined ? subCategory : null;
    this.setState({
      editAbleSubCategory: editAbleSubCategory,
    });
  };

  showTransactionEdit(message) {
    this.props.showTransactionEdit(message);
  }

  getPictureId(pictureId) {
    this.setState(prevState => ({
      transactionModel: {
        ...prevState.transactionModel,
        pictureId: pictureId
      }
    }));
  }

  showPicture = (picture) => {
    let showablePicture = picture !== null ? picture : null;
    this.setState({
      showablePicture: showablePicture,
    })
  };

  render() {
    const {formTitle, mainCategoryList, popup, transactionType} = this.props;
    const {editAbleMainCategory, editAbleSubCategory, showablePicture} = this.state;
    const {title, amount, currency, mainCategory, subCategory, monthly, date, endDate, description, pictureId} = this.state.transactionModel;
    const {
      transactionTitleLabel, transactionTitleMessage, transactionAmountLabel, transactionAmountMessage, transactionCurrencyLabel,
      transactionCurrencyMessage, transactionDateLabel, transactionDateMessage, transactionMonthlyLabel, transactionMonthlyMessage,
      transactionEndDateLabel, transactionEndDateMessage, transactionDescriptionLabel, transactionDescriptionMessage
    } = transactionMessages;

    let endDateComponent = !monthly ? null : (
      <ModelDateValue onChange={this.handleModelValueChange} id="endDate" model={endDate}
                      labelTitle={transactionEndDateLabel} placeHolder={transactionEndDateMessage}/>);

    let subCategoryList = mainCategory !== undefined && mainCategory !== null ? mainCategory.subCategoryModelSet : [];

    let editMainCategoryPopUp = editAbleMainCategory === null ? null : (
      <MainCategoryEditPopUp mainCategoryModel={editAbleMainCategory} transactionType={transactionType}
                             showCategoryEdit={this.showCategoryEdit} refreshMainCategories={this.handleFieldChange}/>);

    let editSubCategoryPopUp = editAbleSubCategory === null ? null : (
      <SubCategoryEditPopUp subCategoryModel={editAbleSubCategory} transactionType={transactionType}
                            showSubCategoryPopUp={this.showSubCategoryPopUp} refreshSubCategories={this.handleFieldChange}/>);

    let closeButton = popup !== true ? null :
      (
        <button className="btn btn-outline-danger mx-3 mt-3 mb-2" onClick={this.showTransactionEdit}>
          <span>&times;</span>
          <span> Close </span>
        </button>
      );

    let pictureButton = transactionType === 'INCOME' || (pictureId !== null && pictureId !== undefined)
      ? null
      : <UploadPicture getPictureId={this.getPictureId}/>;
    let pictureRemoveButton = transactionType === 'INCOME' || pictureId === undefined || pictureId === null
      ? null
      : <RemovePicture getPictureId={this.getPictureId} pictureId={pictureId}/>;
    let showPicture = showablePicture !== undefined && showablePicture !== null
      ? <ShowPicture showPicture={this.showPicture} picture={showablePicture}/> : null;
    let getPicture = transactionType === 'INCOME' || pictureId === null || pictureId === undefined
      ? null
      : <GetPicture pictureId={pictureId} showPicture={this.showPicture}/>;

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
          <EnumSelect handleModelValueChange={this.handleModelValueChange} model={currency} id="currency" label={transactionCurrencyLabel}
                      placeHolder={transactionCurrencyMessage}/>
          <MainCategorySelect handleModelValueChange={this.handleFieldChange} showCategoryEdit={this.showCategoryEdit}
                              mainCategory={mainCategory} mainCategoryList={mainCategoryList} editable={true}/>
          <SubCategorySelect handleModelValueChange={this.handleFieldChange} showSubCategoryPopUp={this.showSubCategoryPopUp}
                             subCategory={subCategory} subCategoryList={subCategoryList} editable={true}/>
          <YesNoSelect handleFieldChange={this.handleFieldChange} value={monthly} valueName="monthly"
                       valueLabel={transactionMonthlyLabel} valueMessage={transactionMonthlyMessage}/>
          <ModelDateValue onChange={this.handleModelValueChange}
                          id="date" model={date}
                          labelTitle={transactionDateLabel} placeHolder={transactionDateMessage}/>
          {endDateComponent}
          <ModelStringValue onChange={this.handleModelValueChange}
                            id="description" model={description}
                            labelTitle={transactionDescriptionLabel} placeHolder={transactionDescriptionMessage} type="text"/>
          <div>
            {pictureButton}
            {getPicture}
            {pictureRemoveButton}
          </div>
          <button className="btn btn-outline-success mt-3 mb-2">
            <span className="fas fa-pencil-alt"/>
            <span> Save transaction </span>
          </button>
          {closeButton}
        </form>
        {editMainCategoryPopUp}
        {editSubCategoryPopUp}
        {showPicture}
      </React.Fragment>
    )
  }
}

export default TransactionForm;
