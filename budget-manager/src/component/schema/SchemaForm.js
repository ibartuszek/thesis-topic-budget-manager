import React, {Component} from 'react';
import EnumSelect from "../transactions/EnumSelect";
import ModelStringValue from "../layout/form/ModelStringValue";
import SubCategorySelect from "../transactions/subCategory/selectSubCategory/SubCategorySelect";
import MainCategorySelect from "../transactions/mainCategory/MainCategorySelect";
import {createEmptySchema} from "../../actions/schema/createSchemaMethods";

class SchemaForm extends Component {

  state = {
    schema: createEmptySchema()
  };

  constructor(props) {
    super(props);
    this.handleFieldChange = this.handleFieldChange.bind(this);
    this.handleModelValueChange = this.handleModelValueChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
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
    this.props.handleSubmit(this.state.schema);
  };

  render() {
    const {chartType, currency, mainCategory, subCategory, title, type} = this.state.schema;
    const {formTitle, mainCategoryList} = this.props;

    console.log(this.state);

    let subCategoryList = mainCategory !== undefined && mainCategory !== null ? mainCategory.subCategoryModelSet : [];

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
          <MainCategorySelect handleModelValueChange={this.handleFieldChange} mainCategory={mainCategory} mainCategoryList={mainCategoryList}/>
          <SubCategorySelect handleModelValueChange={this.handleFieldChange} subCategory={subCategory} subCategoryList={subCategoryList}/>
          <button className="btn btn-outline-success mt-3 mb-2">
            <span className="fas fa-pencil-alt"/>
            <span> Save schema </span>
          </button>
        </form>
      </React.Fragment>
    )
  }

}

export default SchemaForm;
