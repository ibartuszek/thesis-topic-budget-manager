import React, {Component} from 'react';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import SchemaForm from "./SchemaForm";
import {createContext} from "../../actions/common/createContext";
import {validateSchema} from "../../actions/validation/validateSchema";
import {createSchema} from "../../actions/schema/createSchema";

class CreateNewSchemaCard extends Component {

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(schema) {
    const {userHolder, logHolder, createSchema} = this.props;
    if (validateSchema(schema)) {
      let context = createContext(userHolder, logHolder);
      createSchema(context, schema);
    }
  }

  render() {
    const {categoryHolder, target} = this.props;

    let outcomeMainCategories = categoryHolder.outcomeMainCategories;

    console.log(this.props.statisticsHolder);

    return (
      <React.Fragment>
        <div className="collapse multi-collapse" id={target}>
          <div className="card card-body">
            <SchemaForm formTitle="Create schema" mainCategoryList={outcomeMainCategories} handleSubmit={this.handleSubmit}/>
          </div>
        </div>
      </React.Fragment>
    )
  }

}

const mapStateToProps = (state) => {
  return {
    logHolder: state.logHolder,
    categoryHolder: state.categoryHolder,
    statisticsHolder: state.statisticsHolder,
    userHolder: state.userHolder,
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    dispatch,
    ...bindActionCreators(
      {createSchema: createSchema},
      dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(CreateNewSchemaCard)
