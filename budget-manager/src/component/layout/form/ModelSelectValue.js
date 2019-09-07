import React, {Component} from 'react';

class ModelSelectValue extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange = (e, elementList) => {
    if (elementList[e.target.value].id !== null) {
      this.props.onChange([e.target.id], e.target.value, elementList[e.target.value]);
    }
  };

  render() {
    const {id, model, labelTitle, placeHolder, elementList} = this.props;

    return (
      <React.Fragment>
        <div className="input-group mt-3">
          <label className="input-group-addon input-group-text" htmlFor={id}>{labelTitle}</label>
          <select className="form-control" id={id}
                  onChange={(e) => this.handleChange(e, elementList)} value={model}>
            {elementList.map(function (element, index) {
              return element.name.value === null
                ? <option key={index} value={index} className="text-muted">{placeHolder}</option>
                : <option key={index} value={index}>{element.name.value}</option>
            })
            }
          </select>
        </div>
      </React.Fragment>
    )
  }
}

export default ModelSelectValue;
