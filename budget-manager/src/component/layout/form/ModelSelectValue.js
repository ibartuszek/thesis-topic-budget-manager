import React, {Component} from 'react';

class ModelSelectValue extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange = (e, elementList, isCategory) => {
    if (isCategory && elementList[e.target.value].id !== null) {
      this.props.onChange([e.target.id], e.target.value, elementList[e.target.value]);
    } else if (!isCategory) {
      this.props.onChange(e.target.id, elementList[e.target.value]);
    }
  };

  render() {
    const {id, model, labelTitle, placeHolder, elementList} = this.props;
    let isCategory = elementList[0].name !== undefined;
    let optionClass = isCategory ? "form-control text-muted" : "form-control";

    return (
      <React.Fragment>
        <div className="input-group mt-3">
          <label className="input-group-addon input-group-text" htmlFor={id}>{labelTitle}</label>
          <select className={optionClass} id={id} value={model === undefined ? 0 : elementList.indexOf(model.value)}
                  onChange={(e) => this.handleChange(e, elementList, isCategory)}>
            {elementList.map(function (element, index) {
              let currentValue = isCategory ? element.name.value : element;
              return currentValue === null
                ? <option key={index} value={index} className="text-muted">{placeHolder}</option>
                : <option key={index} value={index} className="text-unmuted">{currentValue}</option>
            })
            }
          </select>
        </div>
      </React.Fragment>
    )
  }
}

export default ModelSelectValue;
