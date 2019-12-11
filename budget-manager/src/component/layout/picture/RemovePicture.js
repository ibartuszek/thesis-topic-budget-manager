import React, {Component} from 'react';
import {connect} from "react-redux";
import {createContext} from "../../../actions/common/createContext";
import {deletePicture} from "../../../actions/picture/deletePicture";
import {findElementById} from "../../../actions/common/listActions";

class RemovePicture extends Component {

  constructor(props) {
    super(props);
    this.deletePicture = this.deletePicture.bind(this);
  }

  deletePicture() {
    const {userHolder, logHolder, pictureHolder, getPictureId, pictureId, deletePicture} = this.props;
    let context = createContext(userHolder, logHolder);
    let picture = findElementById(pictureHolder.pictures, pictureId);
    deletePicture(picture, context).then(function (response) {
      if ('DELETE_PICTURE_SUCCESS' === response['type']) {
        getPictureId(null);
      }
    });
  }

  render() {
    return (
      <button className="btn btn-outline-danger mt-3 mb-2 mx-2" onClick={this.deletePicture}>
        <span className="far fa-image"/>
        <span> Delete picture </span>
      </button>
    );
  }

}

const mapStateToProps = (state) => {
  return {
    logHolder: state.logHolder,
    userHolder: state.userHolder,
    pictureHolder: state.pictureHolder
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    deletePicture: (picture, context) => dispatch(deletePicture(picture, context)),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(RemovePicture);
