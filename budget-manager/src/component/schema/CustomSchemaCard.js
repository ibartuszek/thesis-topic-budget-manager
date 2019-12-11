import React, {Component} from 'react';
import SchemaContainer from "./SchemaContainer";

class CustomSchemaCard extends Component {

  render() {
    const {target, schemas} = this.props;

    let schemaList = this.createSchemas(schemas);

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            {schemaList}
          </div>
        </div>
      </React.Fragment>
    )
  }

  createSchemas(schemas) {
    let schemaList = null;
    if (schemas !== null) {
      schemaList = schemas.map((schema, i) =>
        <div key={i}>
          <SchemaContainer schema={schema} editable={true} showSchemaEdit={this.props.showSchemaEdit}
                           showSchemaDelete={this.props.showSchemaDelete}/>
          <hr/>
        </div>);
    }
    return schemaList;
  }
}

export default CustomSchemaCard;
