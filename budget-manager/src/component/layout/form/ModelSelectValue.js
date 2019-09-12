import React, {Component} from 'react';

class ModelSelectValue extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange = (e, elementList) => {
    this.props.onChange(elementList[e.target.value]);
  };

  showCategoryEdit = (editableObject) => {
    this.props.showCategoryEdit(editableObject);
  };

  render() {
    const {id, model, labelTitle, placeHolder, elementList, editableObject} = this.props;
    let optionClass = model === undefined ? "form-control text-muted" : "form-control";

    let editButton = editableObject !== undefined ? (
      <button type="button" className="close ml-3" onClick={() => this.showCategoryEdit(editableObject)}>
        <span className="btn btn-warning btn-sm fas fa-edit"/>
      </button>
    ) : null;

    return (
      <React.Fragment>
        <div className="input-group mt-3">
          <label className="input-group-addon input-group-text" htmlFor={id}>{labelTitle}</label>
          <select className={optionClass} id={id} value={model === undefined ? 0 : elementList.indexOf(model)}
                  onChange={(e) => this.handleChange(e, elementList)}>
            {elementList.map(function (element, index) {
              return element === null
                ? <option key={index} value={index} className="text-muted">{placeHolder}</option>
                : <option key={index} value={index} className="text-unmuted">{element}</option>
            })
            }
          </select>
          {editButton}
        </div>
      </React.Fragment>
    )
  }
}

export default ModelSelectValue;
