import React, {Component} from 'react';
import StatisticsSchemaContainer from "./StatisticsSchemaContainer";

class StandardStatisticsSchemaCard extends Component {

  render() {
    const {target, schema} = this.props;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <StatisticsSchemaContainer schema={schema}/>
          </div>
        </div>
      </React.Fragment>
    )
  }
}

export default StandardStatisticsSchemaCard;
