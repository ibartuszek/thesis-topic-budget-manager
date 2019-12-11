import React, {Component} from 'react';
import logo from '../styles/icons8-loader.svg';


class Loading extends Component {
  render() {
    return (
      <div className="row d-flex justify-content-center">
        <div>
          <img src={logo} className="Loading mx-auto my-5" alt="loading"/>
        </div>
      </div>
    )
  }
}

export default Loading;
