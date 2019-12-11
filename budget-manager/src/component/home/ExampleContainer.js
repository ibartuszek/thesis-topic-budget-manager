import React, {Component} from 'react';

class ExampleContainer extends Component {

  render() {
    const {video, title, message} = this.props;
    let videoElement = video === null ? null : (
      <video className="mx-auto d-block mb-3" width="500" height="280" src={video} controls/>
    );
    let titleElement = title === null ? null : <h5 className="mx-2">{title}</h5>;
    let mainClassName = "my-2 p-2 border border-success border-1 rounded custom-example-container";

    return (
      <div className={mainClassName}>
        <div className="row justify-content-center">
          <div className="col-12 col-lg-5 text-center">
            {titleElement}
            <p className="list-group-item-secondary my-2 p-2 border border-success border-1 rounded text-justify">{message}</p>
          </div>
          <div className="col-12 col-lg-5 my-3">
            {videoElement}
          </div>
        </div>
      </div>
    )
  }

}

export default ExampleContainer;
