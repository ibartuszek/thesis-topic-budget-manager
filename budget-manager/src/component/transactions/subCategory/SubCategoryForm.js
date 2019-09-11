import React, {Component} from 'react';
import ModelStringValue from "../../layout/form/ModelStringValue";
import {categoryMessages} from "../../../store/MessageHolder";

class SubCategoryForm extends Component {

  state = {
    subCategoryModel: {
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
      }
    }
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.showCategoryEdit = this.showCategoryEdit.bind(this);
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
    this.props.handleSubmit(this.state.subCategoryModel);
  };

  showCategoryEdit(message) {
    this.props.showCategoryEdit(message);
  }

  render() {
    const {formTitle, popup} = this.props;
    const {name} = this.state.subCategoryModel;
    const {categoryNameLabel, categoryNameMessage} = categoryMessages;

    let closeButton = popup === undefined ? null :
      (
        <button className="btn btn-outline-danger mx-3" onClick={this.showCategoryEdit}>
          <span>&times;</span>
          <span> Close </span>
        </button>
      );

    return (
      <React.Fragment>
        <form className="form-group mb-0" onSubmit={this.handleSubmit}>
          <h4 className="mt-3 mx-auto">{formTitle}</h4>
          <ModelStringValue onChange={this.handleFieldChange}
                            id="name" model={name}
                            labelTitle={categoryNameLabel} placeHolder={categoryNameMessage} type="text"/>
          <button className="btn btn-outline-success mt-3 mb-2">
            <span className="fas fa-pencil-alt"/>
            <span> Save supplementary category </span>
          </button>
          {closeButton}
        </form>
      </React.Fragment>
    )
  }
}

export default SubCategoryForm;
