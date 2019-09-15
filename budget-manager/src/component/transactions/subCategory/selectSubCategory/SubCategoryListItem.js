import React, {Component} from 'react';

class SubCategoryListItem extends Component {

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
    const {id, name, label, category, editable} = this.props;
    const edit = !editable ? null :
      (
        <div className="my-1">
          <button type="button" className="btn btn-warning btn-sm fas fa-edit ml-3" onClick={() => this.showCategoryEdit(category)}/>
        </div>
      );

    return (
      <div className="input-group mt-3">
        <label className="input-group-addon input-group-text">{label}</label>
        <div className="form-control">
          <span>{name}</span>
          <button type="button" className="close" onClick={() => this.handleRemoveCategory(id)}>
            <span>&times;</span>
          </button>
        </div>
        {edit}
      </div>)
  }

}

export default SubCategoryListItem;
