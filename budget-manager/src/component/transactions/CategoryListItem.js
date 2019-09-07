import React, {Component} from 'react';

class CategoryListItem extends Component {

  constructor(props) {
    super(props);
    this.handleRemoveCategory = this.handleRemoveCategory.bind(this);
  }

  handleRemoveCategory = (id) => {
    this.props.onChange(id);
  };

  render() {
    let {id, name, label} = this.props;
    return (
      <div className="input-group mt-3">
        <label className="input-group-addon input-group-text">{label}</label>
        <div className="form-control">
          <span>{name}</span>
          <button type="button" className="close" onClick={() => this.handleRemoveCategory(id)}>
            <span>&times;</span>
          </button>
        </div>
      </div>)
  }
}

export default CategoryListItem;
