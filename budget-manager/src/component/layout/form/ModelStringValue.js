import React, {Component} from 'react';

class ModelStringValue extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange = (e) => {
    // TODO: validation
    let error = false;
    this.props.onChange([e.target.id], e.target.value, error);
  };

  render() {
    const {id, value, error, labelTitle, placeholder, type} = this.props;
    console.log(this.props);
    return (
      <React.Fragment>
        <div className="input-group mt-3">
          <label className="input-group-addon input-group-text" htmlFor={id}>{labelTitle}</label>
          <input className="form-control" id={id} placeholder={placeholder}
                 onChange={this.handleChange} value={value} type={type}/>
        </div>
        <div className="custom-error-message-container">{error}</div>
      </React.Fragment>
    )
  }

}

export default ModelStringValue;
