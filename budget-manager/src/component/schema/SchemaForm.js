import React, {Component} from 'react';
import CloseButton from "../buttons/CloseButton";
import EnumSelect from "../transactions/EnumSelect";
import ModelStringValue from "../layout/form/ModelStringValue";
import MainCategorySelect from "../transactions/mainCategory/MainCategorySelect";
import SubCategorySelect from "../transactions/subCategory/selectSubCategory/SubCategorySelect";
import {createEmptySchema} from "../../actions/schema/createSchemaMethods";
import SpinButton from "../buttons/SpinButton";
import {validateSchema} from "../../actions/validation/validateSchema";

class SchemaForm extends Component {

  state = {
    schema: createEmptySchema(),
    loading: false
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleModelValueChange = this.handleModelValueChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.showSchemaEdit = this.showSchemaEdit.bind(this);
  }

  componentDidMount() {
    let {editableSchema} = this.props;
    if (editableSchema !== null && editableSchema !== undefined) {
      this.setState({
        schema: editableSchema
      })
    }
  }

  componentWillUpdate(nextProps, nextState, nextContext) {
    if (this.state.loading && !nextProps.loading) {
      this.setState({
        loading: false
      })
    }
  }

  handleFieldChange(id, value) {
    this.setState(prevState => ({
      schema: {
        ...prevState.schema,
        [id]: value
      }
    }));
  }

  handleModelValueChange(id, value, errorMessage) {
    console.log(id, value);
    this.setState(prevState => ({
      schema: {
        ...prevState.schema,
        [id]: {
          ...prevState.schema[id],
          value: value,
          errorMessage: errorMessage
        }
      }
    }));
  }

  handleSubmit = (e) => {
    e.preventDefault();
    if (validateSchema(this.state.schema)) {
      this.setState({
        loading: true
      });
      this.props.handleSubmit(this.state.schema);
    }
  };

  showSchemaEdit() {
    this.props.showSchemaEdit();
  }

  render() {
    const {chartType, currency, mainCategory, subCategory, title, type} = this.state.schema;
    const {loading} = this.state;
    const {formTitle, mainCategoryList, editableSchema} = this.props;

    let subCategoryList = mainCategory !== undefined && mainCategory !== null ? mainCategory.subCategoryModelSet : [];

    let closeButton = editableSchema === null || editableSchema === undefined ? null
      : (<CloseButton buttonLabel="Close" closePopUp={this.showSchemaEdit}/>);

    return (
      <React.Fragment>
        <form className="form-group mb-0" onSubmit={this.handleSubmit}>
          <h4 className="mt-3 mx-auto">{formTitle}</h4>
          <ModelStringValue onChange={this.handleModelValueChange}
                            id="title" model={title}
                            labelTitle="Title" placeHolder="Title of the schema" type="text"/>
          <EnumSelect handleModelValueChange={this.handleModelValueChange} model={type} id="type" label="Type of the schema"
                      placeHolder="Type of the schema"/>
          <EnumSelect handleModelValueChange={this.handleModelValueChange} model={currency} id="currency" label="Currency"
                      placeHolder="Currency of the schema"/>
          <EnumSelect handleModelValueChange={this.handleModelValueChange} model={chartType} id="chartType" label="Type of the chart"
                      placeHolder="Type of the visualization  of the data"/>
          <MainCategorySelect handleModelValueChange={this.handleFieldChange} mainCategory={mainCategory} mainCategoryList={mainCategoryList}
                              withNullList={true}/>
          <SubCategorySelect handleModelValueChange={this.handleFieldChange} subCategory={subCategory} subCategoryList={subCategoryList}/>
          <SpinButton buttonLabel="Save schema" icon="fas fa-pencil-alt" loading={loading}/>
          {closeButton}
        </form>
      </React.Fragment>
    )
  }

}

export default SchemaForm;
