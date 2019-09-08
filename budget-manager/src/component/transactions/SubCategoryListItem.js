import React, {Component} from 'react';

class EnumListItem extends Component {

  constructor(props) {
    super(props);
    this.handleRemoveCategory = this.handleRemoveCategory.bind(this);
  }

  handleRemoveCategory = (id) => {
    this.props.onChange(id);
  };

  showCategoryEdit = (category) => {
    this.props.showCategoryEdit(category);
  };

  render() {
    const {id, name, label, category} = this.props;

    return (
      <div className="input-group mt-3">
        <label className="input-group-addon input-group-text">{label}</label>
        <div className="form-control">
          <span>{name}</span>
          <button type="button" className="close" onClick={() => this.handleRemoveCategory(id)}>
            <span>&times;</span>
          </button>
        </div>
        <button type="button" className="close ml-3" onClick={() => this.showCategoryEdit(category)}>
          <span className="btn btn-warning btn-sm fas fa-edit"/>
        </button>
      </div>)
  }

}

export default EnumListItem;
