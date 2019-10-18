import React, {Component} from 'react';
import XYPlot from "react-vis/es/plot/xy-plot";
import {LineSeries, VerticalGridLines} from "react-vis/es";
import {Crosshair, HorizontalGridLines, VerticalBarSeries, XAxis, YAxis} from "react-vis";

class CustomChart extends Component {

  state = {
    crossHairValues: []
  };

  onMouseLeave = () => {
    this.setState({
      crossHairValues: []
    });
  };

  onNearestX(value, dataPoints) {
    let target = [];
    target.push(this.getCrossHair(dataPoints, value.x));
    this.setState({
      crossHairValues: target
    });
  }

  getCrossHair(data, index) {
    for (let i = 0; i < data.length; i++) {
      let actual = data[i];
      if (actual.x === index) {
        return actual;
      }
    }
  }

  render() {
    const {crossHairValues} = this.state;
    const {chartType, currency, title} = this.props.schema;
    const {height, width} = this.props.chartDetails;
    const {dataPoints} = this.props.chartData;

    let data;
    if (chartType === 'LINEAR') {
      data = (<LineSeries data={dataPoints} color='#064789' onNearestX={value => this.onNearestX(value, dataPoints)}/>);
    } else {
      data = (<VerticalBarSeries data={dataPoints} color='#064789' onNearestX={value => this.onNearestX(value, dataPoints)}/>);
    }

    let crossHairElement = crossHairValues.length < 1 ? null :
      (<Crosshair values={crossHairValues}>
        <div className="custom-crosshair">
          <div>{"Point " + crossHairValues[0].x + ":"}</div>
          <div>{crossHairValues[0].date + ": "}</div>
          <div>{crossHairValues[0].label}</div>
          <div>{crossHairValues[0].y + " " + currency}</div>
        </div>
      </Crosshair>);

    return (

      <div className="custom-chart-container">
        <XYPlot className="my-3 mx-auto" height={height} width={width}
                onMouseLeave={this.onMouseLeave}>
          <HorizontalGridLines/>
          <VerticalGridLines/>
          <XAxis/>
          <YAxis/>
          {data}
          {crossHairElement}
        </XYPlot>
      </div>
    );
  }

}

export default CustomChart;
