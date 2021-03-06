import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import CardHeaderButton from "../layout/card/CardHeaderButton";
import CreateNewSchemaCard from "./CreateNewSchemaCard";
import CustomSchemaCard from "./CustomSchemaCard";
import DeleteSchemaPopUp from "./DeleteSchemaPopUp";
import StandardSchemaCard from "./StandardSchemaCard";
import UpdateSchemaPopUp from "./UpdateSchemaPopUp";

class Schemas extends Component {

  state = {
    editableSchema: null,
    deletableSchema: null
  };

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
    this.showSchemaDelete = this.showSchemaDelete.bind(this);
    this.showSchemaEdit = this.showSchemaEdit.bind(this);
  }

  /*
    componentDidUpdate(oldProps) {
      const newProps = this.props;
      if (this.state.standardStatisticsAreLoaded === false && newProps.statisticsHolder['standardStatisticsAreLoaded'] === true) {
        this.setState({
          ...this.state,
          standardStatisticsAreLoaded: true
        })
      }
    }
  */
  handleChange = (id, value) => {
    this.setState({
      [id]: value
    });
  };

  showSchemaEdit = (schema) => {
    let editableSchema = schema !== null ? schema : null;
    this.setState({
      editableSchema: editableSchema,
    })
  };

  showSchemaDelete = (schema) => {
    let deletableSchema = schema !== null ? schema : null;
    this.setState({
      deletableSchema: deletableSchema,
    })
  };

  render() {
    const {statisticsHolder, userHolder} = this.props;
    const {deletableSchema, editableSchema} = this.state;

    if (userHolder == null || !userHolder.userIsLoggedIn) {
      return <Redirect to='/login'/>;
    }

    let createNewSchema = "createNewSchemaContainer";
    let createNewSchemaCard = statisticsHolder.customSchemas === null
    || statisticsHolder.customSchemas === undefined ? null
      : <CreateNewSchemaCard target={createNewSchema}/>;

    let showStandardSchema = "standardSchemaContainer";
    let standardSchemasCard = statisticsHolder.standardSchema === null
    || statisticsHolder.standardSchema === undefined ? null
      : <StandardSchemaCard target={showStandardSchema} schema={statisticsHolder.standardSchema}/>;

    let showCustomSchemas = "customSchemasContainer";
    let customSchemasCard = statisticsHolder.customSchemas === null
    || statisticsHolder.customSchemas === undefined ? null
      : <CustomSchemaCard target={showCustomSchemas} schemas={statisticsHolder.customSchemas}
                          showSchemaEdit={this.showSchemaEdit} showSchemaDelete={this.showSchemaDelete}/>;

    let editableSchemaContainer = editableSchema === null ? null
      : <UpdateSchemaPopUp schema={editableSchema} showSchemaEdit={this.showSchemaEdit}/>;

    let deletableSchemaContainer = deletableSchema === null ? null
      : <DeleteSchemaPopUp schema={deletableSchema} showSchemaDelete={this.showSchemaDelete}/>;

    return (
      <main>
        <div className="card mt-3">
          <div className="card-header float-left my-auto">
            <CardHeaderButton target={createNewSchema} buttonName="Create new schema"/>
            <CardHeaderButton target={showStandardSchema} buttonName="Show standard schema"/>
            <CardHeaderButton target={showCustomSchemas} buttonName="Show custom schemas"/>
          </div>
          {createNewSchemaCard}
          {standardSchemasCard}
          {customSchemasCard}
          {editableSchemaContainer}
          {deletableSchemaContainer}
        </div>
      </main>);
  }

}

const mapStateToProps = (state) => {
  return {
    logHolder: state.logHolder,
    statisticsHolder: state.statisticsHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {},
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(Schemas)

