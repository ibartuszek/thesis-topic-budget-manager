import React, {Component} from 'react';

class CreateNewSchemaCard extends Component {

  render() {
    const {target} = this.props;

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            test
          </div>
        </div>
      </React.Fragment>
    )
  }

}

export default CreateNewSchemaCard;
