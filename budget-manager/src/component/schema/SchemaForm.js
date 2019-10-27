import React, {Component} from 'react';
import {createEmptySchema} from "../../actions/schema/createEmptySchema";

class SchemaForm extends Component {

  state = {
    schema: createEmptySchema()
  };

  render() {
    const {formTitle} = this.props;

    return (
      <React.Fragment>
        <form className="form-group mb-0" onSubmit={this.handleSubmit}>
          <h4 className="mt-3 mx-auto">{formTitle}</h4>
        </form>
      </React.Fragment>
    )
  }

}

export default SchemaForm;
