import React, {Component} from 'react';

// import {Image} from 'react-native';

class ShowPicture extends Component {

  constructor(props) {
    super(props);
    this.showPicture = this.showPicture.bind(this);
  }

  showPicture() {
    this.props.showPicture(null);
  }

  render() {
    const {picture} = this.props.picture;

    return (
      <div className='custom-popup zindex-dropdown position-fixed'>
        <div className="card card-body custom-popup-inner-picture overflow-auto">
          <button type="button" className="close picture-close-button" onClick={this.showPicture}>
            <span>&times;</span>
          </button>
          <img src={"data:image/jpeg;base64," + picture} width="400" className="img-thumbnail"/>
        </div>
      </div>
    );
  }

}

export default ShowPicture;
