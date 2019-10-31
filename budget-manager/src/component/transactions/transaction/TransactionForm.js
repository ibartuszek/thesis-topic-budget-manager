import React, {Component} from 'react';
import moment from "moment";
import CloseButton from "../../buttons/CloseButton";
import EnumSelect from "../EnumSelect";
import MainCategoryEditPopUp from "../mainCategory/MainCategoryEditPopUp";
import MainCategorySelect from "../mainCategory/MainCategorySelect";
import ModelAmountValue from "../../layout/form/ModelAmountValue";
import ModelDateValue from "../../layout/form/ModelDateValue";
import ModelStringValue from "../../layout/form/ModelStringValue";
import SpinButton from "../../buttons/SpinButton";
import SubCategoryEditPopUp from "../subCategory/SubCategoryEditPopUp";
import SubCategorySelect from "../subCategory/selectSubCategory/SubCategorySelect";
import YesNoSelect from "../../layout/form/YesNoSelect";
import {createEmptyTransaction} from "../../../actions/transaction/createTransactionMethods";
import {dateProperties} from "../../../store/Properties";
import {getPossibleFirstDate} from "../../../actions/date/dateActions";
import {validateTransaction} from "../../../actions/validation/validateTransaction";
import GetPicture from "../../layout/picture/GetPicture";
import RemovePicture from "../../layout/picture/RemovePicture";
import ShowPicture from "../../layout/picture/ShowPicture";
import UploadPicture from "../../layout/picture/UploadPicture";
import {findElementById} from "../../../actions/common/listActions";

class TransactionForm extends Component {

  state = {
    transactionModel: createEmptyTransaction(),
    editAbleMainCategory: null,
    editAbleSubCategory: null,
    showablePicture: null,
    loading: false
  };

  constructor(props) {
    super(props);
    this.closePopUp = this.closePopUp.bind(this);
    this.getPictureId = this.getPictureId.bind(this);
    this.handleModelValueChange = this.handleModelValueChange.bind(this);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.setFirstPossibleDay = this.setFirstPossibleDay.bind(this);
    this.showPicture = this.showPicture.bind(this);
  }

  componentDidMount() {
    const {mainCategoryList, transactionModel, transactionType} = this.props;
    let mainCategory;
    let subCategory = undefined;
    if (transactionModel === undefined) {
      mainCategory = mainCategoryList.length > 0 ? mainCategoryList[0] : undefined;
    } else {
      mainCategory = transactionModel.mainCategory;
      if (transactionModel.subCategory !== undefined) {
        subCategory = findElementById(mainCategory.subCategoryModelSet, transactionModel.subCategory.id);
      }
    }
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
        subCategory: subCategory
      }
    }));
    this.setFirstPossibleDay();
  }

  componentWillUpdate(nextProps, nextState, nextContext) {
    const {loading, transactionModel} = this.state;
    if (nextProps.transactionModel !== undefined && transactionModel.id === null) {
      this.setState({
        transactionModel: this.createNewTransactionModel(nextProps, transactionModel.mainCategory, transactionModel.subCategory),
      });
      this.setFirstPossibleDay();
    }
    if (loading && !nextProps.loading) {
      this.setState({
        loading: false
      })
    }
  }

  createNewTransactionModel(props, originalMainCategory, originalSubCategory) {
    let newTransactionModel = props.transactionModel !== undefined ? props.transactionModel : this.state.transactionModel;
    if (originalMainCategory !== undefined) {
      let mainCategory = findElementById(props.mainCategoryList, originalMainCategory.id);
      newTransactionModel.mainCategory = mainCategory;
      if (originalSubCategory !== undefined) {
        newTransactionModel.subCategory = findElementById(mainCategory.subCategoryModelSet, originalSubCategory.id);
      }
    }
    return newTransactionModel;
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
    const {transactionModel} = this.state;
    e.preventDefault();
    if (validateTransaction(transactionModel)) {
      this.setState({
        loading: true
      });
      this.props.handleSubmit(transactionModel);
    }
  };

  showMainCategoryPopUp = (mainCategory) => {
    const {transactionModel} = this.state;
    let editAbleMainCategory = mainCategory !== null && mainCategory !== undefined ? mainCategory : null;
    this.setState({
      editAbleMainCategory: editAbleMainCategory,
    });
    if (editAbleMainCategory === null) {
      this.setState({
        transactionModel: this.createNewTransactionModel(this.props, transactionModel.mainCategory, transactionModel.subCategory),
      });
    }
  };

  showSubCategoryPopUp = (subCategory) => {
    let editAbleSubCategory = subCategory !== null && subCategory !== undefined ? subCategory : null;
    this.setState({
      editAbleSubCategory: editAbleSubCategory,
    });
  };

  closePopUp() {
    this.setState({
      transactionModel: createEmptyTransaction(),
      loading: false
    });
    this.props.closePopUp();
  }

  getPictureId(pictureId) {
    this.setState(prevState => ({
      transactionModel: {
        ...prevState.transactionModel,
        pictureId: pictureId
      }
    }));
  }

  showPicture(picture) {
    let showablePicture = picture !== null ? picture : null;
    this.setState({
      showablePicture: showablePicture,
    })
  }

  render() {
    const {formTitle, mainCategoryList, popup, transactionType} = this.props;
    const {editAbleMainCategory, editAbleSubCategory, loading, showablePicture} = this.state;
    const {title, amount, currency, mainCategory, subCategory, monthly, date, endDate, description, pictureId} = this.state.transactionModel;

    let subCategoryListOfMainCategory = mainCategory !== null && mainCategory !== undefined
      ? mainCategory.subCategoryModelSet : [];

    let endDateContainer = !monthly ? null : (
      <ModelDateValue onChange={this.handleModelValueChange} id="endDate" model={endDate}
                      labelTitle="End date" placeHolder="Click to select the end of monthly transaction."/>);

    let editMainCategoryPopUp = editAbleMainCategory === null ? null : (
      <MainCategoryEditPopUp mainCategoryModel={editAbleMainCategory} transactionType={transactionType}
                             showMainCategoryPopUp={this.showMainCategoryPopUp} refreshMainCategories={this.handleFieldChange}/>);

    let editSubCategoryPopUp = editAbleSubCategory === null ? null : (
      <SubCategoryEditPopUp subCategoryModel={editAbleSubCategory} transactionType={transactionType}
                            showSubCategoryPopUp={this.showSubCategoryPopUp} refreshSubCategories={this.handleFieldChange}/>);

    let closeButton = popup === undefined ? null : (<CloseButton buttonLabel="Close" closePopUp={this.closePopUp}/>);

    let uploadPictureButton = transactionType === 'INCOME' || (pictureId !== null && pictureId !== undefined)
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
          <ModelStringValue onChange={this.handleModelValueChange} id="title" model={title}
                            labelTitle="Title" placeHolder="The title of your new transaction." type="text"/>

          <ModelAmountValue onChange={this.handleModelValueChange} id="amount" model={amount}
                            labelTitle="Amount" placeHolder="The amount of your new transaction." type="number"/>

          <EnumSelect handleModelValueChange={this.handleModelValueChange} model={currency} id="currency"
                      label="Currency" placeHolder="The currency of your new transaction."/>

          <MainCategorySelect handleModelValueChange={this.handleFieldChange} showMainCategoryPopUp={this.showMainCategoryPopUp}
                              mainCategory={mainCategory} mainCategoryList={mainCategoryList} editable={true}/>

          <SubCategorySelect handleModelValueChange={this.handleFieldChange} showSubCategoryPopUp={this.showSubCategoryPopUp}
                             subCategory={subCategory} subCategoryList={subCategoryListOfMainCategory} editable={true}/>

          <YesNoSelect handleFieldChange={this.handleFieldChange} value={monthly} valueName="monthly"
                       valueLabel="Monthly transaction" valueMessage=""/>

          <ModelDateValue onChange={this.handleModelValueChange} id="date" model={date}
                          labelTitle="Date" placeHolder="Click to select the date of transaction."/>

          {endDateContainer}

          <ModelStringValue onChange={this.handleModelValueChange} id="description" model={description}
                            labelTitle="Description" placeHolder="Description of the transaction." type="text"/>
          <div>
            {uploadPictureButton}
            {getPicture}
            {pictureRemoveButton}
          </div>
          <SpinButton buttonLabel="Save transaction" icon="fas fa-pencil-alt" loading={loading}/>
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
