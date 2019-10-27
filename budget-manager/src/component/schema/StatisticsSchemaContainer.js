import React, {Component} from 'react';

class StatisticsSchemaContainer extends Component {

  constructor(props) {
    super(props);
    this.showEditSchema = this.showEditSchema.bind(this);
    this.showDeleteSchema = this.showDeleteSchema.bind(this);
  }

  showEditSchema(schema) {
    // TODO:
    console.log("EDIT SCHEMA");
    console.log(schema);
  }

  showDeleteSchema(schema) {
    // TODO:
    console.log("DELETE SCHEMA");
    console.log(schema);
  }

  render() {
    const {editable} = this.props;
    const {chartType, currency, mainCategoryName, subCategoryName, title, type} = this.props.schema;

    let mainCategory = mainCategoryName === null || mainCategoryName === undefined ? null :
      (
        <div className="list-group-item my-2 p-2 border border-secondary rounded">
          <div className="float-left d-inline-block">Filtered to expenses' main category</div>
          <div className="float-right d-inline-block">{mainCategoryName}</div>
        </div>
      );

    let subCategory = subCategoryName === null || subCategoryName === undefined ? null :
      (
        <div className="list-group-item my-2 p-2 border border-secondary rounded">
          <div className="float-left d-inline-block">Filtered to expenses' supplementary category</div>
          <div className="float-right d-inline-block">{subCategoryName}</div>
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
          <h4 className="d-inline-block">{title}</h4>
          {editButton}
        </div>
        <div className="list-group">
          <div className="list-group-item my-2 p-2 border border-secondary rounded">
            <div className="float-left d-inline-block">Currency</div>
            <div className="float-right d-inline-block">{currency}</div>
          </div>
          <div className="list-group-item my-2 p-2 border border-secondary rounded">
            <div className="float-left d-inline-block">Type of data to show</div>
            <div className="float-right d-inline-block">{type}</div>
          </div>
          <div className="list-group-item my-2 p-2 border border-secondary rounded">
            <div className="float-left d-inline-block">Type of the chart</div>
            <div className="float-right d-inline-block">{chartType}</div>
          </div>
          {mainCategory}
          {subCategory}
        </div>
      </React.Fragment>
    )
  }
}

export default StatisticsSchemaContainer;
