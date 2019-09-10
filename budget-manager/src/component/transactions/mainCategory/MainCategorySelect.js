import React, {Component} from 'react';
import {getIndexOfElementById} from "../../../actions/common/listActions";

class MainCategorySelect extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange = (e, elementList) => {
    this.props.onChange(e.target.id, elementList[e.target.value]);
  };

  showCategoryEdit = (category) => {
    this.props.showCategoryEdit(category);
  };

  render() {
    const {id, model, labelTitle, placeHolder, elementList} = this.props;

    return (
      <React.Fragment>
        <div className="input-group mt-3">
          <label className="input-group-addon input-group-text" htmlFor={id}>{labelTitle}</label>
          <select className="form-control" id={id} value={model === undefined ? 0 : getIndexOfElementById(elementList, model.id)}
                  onChange={(e) => this.handleChange(e, elementList)}>
            {elementList.map(function (element, index) {
              return model === undefined
                ? <option key={index} value={index} className="text-muted">{placeHolder}</option>
                : <option key={index} value={index} className="text-unmuted">{element.name.value}</option>
            })
            }
          </select>
          <button type="button" className="close ml-3" onClick={() => this.showCategoryEdit(model)}>
            <span className="btn btn-warning btn-sm fas fa-edit"/>
          </button>
        </div>
      </React.Fragment>
    )
  }
}

export default MainCategorySelect;
