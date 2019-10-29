import React, {Component} from 'react';
import CloseButton from "../../buttons/CloseButton";
import ModelStringValue from "../../layout/form/ModelStringValue";
import SpinButton from "../../buttons/SpinButton";
import {categoryMessages} from "../../../store/MessageHolder";
import {createEmptySubCategory} from "../../../actions/category/createSubCategoryMethods";
import {validateModel} from "../../../actions/validation/validateModel";

class SubCategoryForm extends Component {

  state = {
    subCategoryModel: createEmptySubCategory(),
    loading: false
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.closePopUp = this.closePopUp.bind(this);
  }

  componentDidMount() {
    this.setState(prevState => ({
      subCategoryModel: {
        ...prevState.subCategoryModel,
        transactionType: {
          ...prevState.transactionType,
          value: this.props.transactionType
        }
      }
    }))
  }

  componentWillUpdate(nextProps, nextState, nextContext) {
    if (nextProps.subCategoryModel !== undefined && this.state.subCategoryModel.id === null) {
      this.setState({
        subCategoryModel: nextProps.subCategoryModel
      });
    }
    if (this.state.loading && !nextProps.loading) {
      this.setState({
        loading: false
      })
    }
  }

  handleFieldChange(id, value, errorMessage) {
    this.setState(prevState => ({
      subCategoryModel: {
        ...prevState.subCategoryModel,
        [id]: {
          ...prevState.subCategoryModel[id],
          value: value,
          errorMessage: errorMessage
        }
      }
    }));
  }

  handleSubmit = (e) => {
    e.preventDefault();
    if (validateModel(this.state.subCategoryModel)) {
      this.setState({
        loading: true
      });
      this.props.handleSubmit(this.state.subCategoryModel);
    }
  };

  closePopUp() {
    this.setState({
      subCategoryModel: createEmptySubCategory(),
      loading: false
    });
    this.props.closePopUp();
  }

  render() {
    const {formTitle, popup} = this.props;
    const {subCategoryModel, loading} = this.state;
    const {categoryNameLabel, categoryNameMessage} = categoryMessages;

    let closeButton = popup === undefined ? null : (<CloseButton buttonLabel="Close" closePopUp={this.closePopUp}/>);

    return (
      <React.Fragment>
        <form className="form-group mb-0" onSubmit={this.handleSubmit}>
          <h4 className="mt-3 mx-auto">{formTitle}</h4>
          <ModelStringValue onChange={this.handleFieldChange}
                            id="name" model={subCategoryModel.name}
                            labelTitle={categoryNameLabel} placeHolder={categoryNameMessage} type="text"/>
          <SpinButton buttonLabel="Save supplementary category" icon="fas fa-pencil-alt" loading={loading}/>
          {closeButton}
        </form>
      </React.Fragment>
    )
  }
}

export default SubCategoryForm;
