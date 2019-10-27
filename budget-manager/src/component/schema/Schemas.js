import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import CardHeaderButton from "../layout/card/CardHeaderButton";
import StandardStatisticsSchemaCard from "./StandardStatisticsSchemaCard";
import CustomStatisticsSchemaCard from "./CustomStatisticsSchemaCard";
import CreateNewSchemaCard from "./CreateNewSchemaCard";
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
    const {statisticsHolder} = this.props;
    const {deletableSchema, editableSchema} = this.state;

    let createNewSchema = "createNewSchemaContainer";
    let createNewSchemaCard = statisticsHolder.customSchemas === null
    || statisticsHolder.customSchemas === undefined ? null
      : <CreateNewSchemaCard target={createNewSchema}/>;

    let showStandardSchema = "standardSchemaContainer";
    let standardStatisticsCard = statisticsHolder.standardSchema === null
    || statisticsHolder.standardSchema === undefined ? null
      : <StandardStatisticsSchemaCard target={showStandardSchema} schema={statisticsHolder.standardSchema}/>;

    let showCustomSchemas = "customSchemasContainer";
    let customStatisticsCard = statisticsHolder.customSchemas === null
    || statisticsHolder.customSchemas === undefined ? null
      : <CustomStatisticsSchemaCard target={showCustomSchemas} schemas={statisticsHolder.customSchemas}
                                    showSchemaEdit={this.showSchemaEdit}/>;

    let editAbleSchemaContainer = editableSchema === null ? null
      : <UpdateSchemaPopUp schema={editableSchema} showSchemaEdit={this.showSchemaEdit}/>;

    return (
      <main>
        <div className="card mt-3">
          <div className="card-header float-left my-auto">
            <CardHeaderButton target={createNewSchema} buttonName="Create new schema"/>
            <CardHeaderButton target={showStandardSchema} buttonName="Show standard schema"/>
            <CardHeaderButton target={showCustomSchemas} buttonName="Show custom schemas"/>
          </div>
          {createNewSchemaCard}
          {standardStatisticsCard}
          {customStatisticsCard}
          {editAbleSchemaContainer}
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

