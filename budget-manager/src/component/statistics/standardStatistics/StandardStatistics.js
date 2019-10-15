import React, {Component} from 'react';
import CustomRadialChart from "./CustomRadialChart";
import Loading from "../../Loading";

class StandardStatistics extends Component {

  chartDetails = {
    height: 350,
    width: 400,
  };

  render() {
    const {standardStatistics} = this.props;

    return (
      <div className="card card-body custom-chart-details-container my-3 mx-auto">
        <div className="clearfix my-3 mx-auto container">
          <h3 className="mx-auto">Main statistics</h3>
          <div className="row">
            <div className="col-12 col-lg-6 my-3 text-center">
              <CustomRadialChart chartData={standardStatistics.chartData} chartDetails={this.chartDetails}
                                 schema={standardStatistics.schema}/>
            </div>
            <div className="col-12 col-lg-6 my-3 text-center">
              <Loading/>
            </div>
          </div>
        </div>
      </div>
    );
  }

}

export default StandardStatistics;
