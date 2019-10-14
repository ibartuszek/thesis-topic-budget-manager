import React, {Component} from 'react';
import {RadialChart} from "react-vis/es";
import {Hint} from "react-vis";

// import {HorizontalGridLines, LineSeries, MarkSeries, VerticalBarSeries, VerticalGridLines, XAxis, XYPlot, YAxis} from "react-vis";

class CustomRadialChart extends Component {

  state = {
    value: false
  };

  render() {
    const {value} = this.state;
    const {chartType, currency, data, height, title, type, width} = this.props;

    console.log(this.props);

    let hint = value !== false ?
      (<Hint value={value}>
        <div className="custom-crosshair">
          <div>{value.label + ":"}</div>
          <div>{this.getHint(value, data[0].data) + " " + currency}</div>
        </div>
      </Hint>) : null;

    return (
      <div className="clearfix mx-auto my-3">
        <div className="custom-chart-container my-3 mx-lg-auto">
          <h4 className="text-center mt-3">{data[0].legend}</h4>
          <RadialChart data={data[0].data}
                       onValueMouseOver={v => this.setState({value: v})}
                       onSeriesMouseOut={v => this.setState({value: false})}
                       className="mx-auto my-3" height={height} width={width}
                       showLabels={true} labelsRadiusMultiplier={1.4}
                       padAngle={0.05} innerRadius={100} radius={140}>
            {hint}
          </RadialChart>
        </div>
      </div>
    );
  }

  getHint(value, data) {
    console.log(value);
    console.log(data);
    let amount = null;
    for (let index = 0; index < data.length && amount === null; index++) {
      let current = data[index];
      if (current.label === value.label) {
        amount = current.angle;
      }
    }
    return amount;
  }

}

export default CustomRadialChart;
