import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import CardHeaderButton from "../layout/card/CardHeaderButton";
import StandardStatisticsSchemaCard from "./StandardStatisticsSchemaCard";
import CustomStatisticsSchemaCard from "./CustomStatisticsSchemaCard";
import CreateNewSchemaCard from "./CreateNewSchemaCard";

class Schemas extends Component {

  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
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

  render() {
    const {statisticsHolder} = this.props;

    let createNewSchema = "createNewSchemaContainer";
    let createNewSchemaCard = statisticsHolder.customStatisticsSchemas === null
    || statisticsHolder.customStatisticsSchemas === undefined ? null
      : <CreateNewSchemaCard target={createNewSchema}/>;

    let showStandardSchema = "standardSchemaContainer";
    let standardStatisticsCard = statisticsHolder.standardStatisticsSchema === null
    || statisticsHolder.standardStatisticsSchema === undefined ? null
      : <StandardStatisticsSchemaCard target={showStandardSchema} schema={statisticsHolder.standardStatisticsSchema}/>;

    let showCustomSchemas = "customSchemasContainer";
    let customStatisticsCard = statisticsHolder.customStatisticsSchemas === null
    || statisticsHolder.customStatisticsSchemas === undefined ? null
      : <CustomStatisticsSchemaCard target={showCustomSchemas} schemas={statisticsHolder.customStatisticsSchemas}/>;

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

