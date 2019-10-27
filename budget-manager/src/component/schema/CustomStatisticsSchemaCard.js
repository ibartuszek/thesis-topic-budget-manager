import React, {Component} from 'react';
import StatisticsSchemaContainer from "./StatisticsSchemaContainer";

class CustomStatisticsSchemaCard extends Component {

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
          <StatisticsSchemaContainer schema={schema} editable={true} showSchemaEdit={this.props.showSchemaEdit}/>
          <hr/>
        </div>);
    }
    return schemaList;
  }
}

export default CustomStatisticsSchemaCard;
