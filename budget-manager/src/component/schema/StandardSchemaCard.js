import React, {Component} from 'react';
import SchemaContainer from "./SchemaContainer";

class StandardSchemaCard extends Component {

  render() {
    const {schema, target} = this.props;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <SchemaContainer schema={schema}/>
          </div>
        </div>
      </React.Fragment>
    )
  }
}

export default StandardSchemaCard;
