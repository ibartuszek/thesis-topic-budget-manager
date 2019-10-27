import React, {Component} from 'react';
import {createEmptySchema} from "../../actions/schema/createEmptySchema";
import CurrencySelect from "../transactions/CurrencySelect";
import ModelStringValue from "../layout/form/ModelStringValue";

class SchemaForm extends Component {

  state = {
    schema: createEmptySchema()
  };

  constructor(props) {
    super(props);
    this.handleModelValueChange = this.handleModelValueChange.bind(this);
  }

  handleModelValueChange(id, value, errorMessage) {
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

  render() {
    const {currency, mainCategory, title, type} = this.state.schema;
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
          <CurrencySelect handleModelValueChange={this.handleModelValueChange} currency={type}/>
          <CurrencySelect handleModelValueChange={this.handleModelValueChange} currency={currency}/>
        </form>
      </React.Fragment>
    )
  }

}

export default SchemaForm;
