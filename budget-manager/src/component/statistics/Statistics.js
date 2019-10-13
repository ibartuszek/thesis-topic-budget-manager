import React, {Component} from 'react';
import CustomChart from "./CustomChart";

class Statistics extends Component {

  render() {

    const customStatistics = {
      id: 1,
      title: "Example chart",
      type: ["INCOME, OUTCOME"],
      startDate: "2019-10-01",
      endDate: "2019-10-31",
      currency: "EUR",
      mainCategory: "test main category",
      subCategory: "test supplementary category",
      monthly: true,
      chartType: "linear",
      xAxisName: "day",
      yAxisName: "amount",
      height: 300,
      width: 500,
      data: [
        [
          {x: 0, y: 8},
          {x: 1, y: 5},
          {x: 2, y: 4},
          {x: 3, y: 9},
          {x: 4, y: 1},
          {x: 5, y: 7},
          {x: 6, y: 6},
          {x: 7, y: 3},
          {x: 8, y: 2},
          {x: 9, y: 0}
        ], [
          {x: 0, y: 18},
          {x: 1, y: 15},
          {x: 2, y: 14},
          {x: 3, y: 19},
          {x: 4, y: 11},
          {x: 5, y: 17},
          {x: 6, y: 16},
          {x: 7, y: 13},
          {x: 8, y: 12},
          {x: 9, y: 10}
        ]]
    };

    return (
      <main>
        <div className="card card-body mt-3 min-w-600">
          <CustomChart data={customStatistics}/>
        </div>
      </main>);
  }

}

export default Statistics;
