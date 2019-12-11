import React, {Component} from 'react';

class SchemaContainer extends Component {

  constructor(props) {
    super(props);
    this.showEditSchema = this.showEditSchema.bind(this);
    this.showDeleteSchema = this.showDeleteSchema.bind(this);
  }

  showEditSchema(schema) {
    this.props.showSchemaEdit(schema);
  }

  showDeleteSchema(schema) {
    this.props.showSchemaDelete(schema);
  }

  render() {
    const {editable} = this.props;
    const {chartType, currency, mainCategory, subCategory, title, type} = this.props.schema;

    let mainCategoryContainer = mainCategory === null || mainCategory === undefined ? null :
      (
        <div className="list-group-item my-2 p-2 border border-secondary rounded">
          <div className="float-left d-inline-block">Filtered to expenses' main category</div>
          <div className="float-right d-inline-block">{mainCategory.name.value}</div>
        </div>
      );

    let subCategoryContainer = subCategory === null || subCategory === undefined ? null :
      (
        <div className="list-group-item my-2 p-2 border border-secondary rounded">
          <div className="float-left d-inline-block">Filtered to expenses' supplementary category</div>
          <div className="float-right d-inline-block">{subCategory.name.value}</div>
        </div>
      );
    let editButton;
    if (editable === true) {
      editButton =
        (<div className="d-inline-block float-right">
          <button className="btn btn-warning btn-sm fas fa-edit mr-2" onClick={() => this.showEditSchema(this.props.schema)}/>
          <button className="btn btn-danger btn-sm fas fa-trash-alt" onClick={() => this.showDeleteSchema(this.props.schema)}/>
        </div>)
    }

    return (
      <React.Fragment>
        <div>
          <h4 className="d-inline-block">{title.value}</h4>
          {editButton}
        </div>
        <div className="list-group">
          <div className="list-group-item my-2 p-2 border border-secondary rounded">
            <div className="float-left d-inline-block">Currency</div>
            <div className="float-right d-inline-block">{currency.value}</div>
          </div>
          <div className="list-group-item my-2 p-2 border border-secondary rounded">
            <div className="float-left d-inline-block">Type of data to show</div>
            <div className="float-right d-inline-block">{type.value}</div>
          </div>
          <div className="list-group-item my-2 p-2 border border-secondary rounded">
            <div className="float-left d-inline-block">Type of the chart</div>
            <div className="float-right d-inline-block">{chartType.value}</div>
          </div>
          {mainCategoryContainer}
          {subCategoryContainer}
        </div>
      </React.Fragment>
    )
  }
}

export default SchemaContainer;
