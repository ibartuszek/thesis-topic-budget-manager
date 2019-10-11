import React, {Component} from 'react';
import {connect} from "react-redux";
import {createContext} from "../../../actions/common/createContext";
import {findElementById} from "../../../actions/common/listActions";
import {getPicture} from "../../../actions/picture/getPicture";

class GetPicture extends Component {

  constructor(props) {
    super(props);
    this.getPicture = this.getPicture.bind(this);
  }

  getPicture() {
    const {userHolder, logHolder, pictureHolder, getPicture, pictureId, showPicture} = this.props;
    let context = createContext(userHolder, logHolder);
    let picture = findElementById(pictureHolder.pictures, pictureId);
    console.log(picture);
    if (picture === undefined || picture === null) {
      console.log(picture);
      getPicture(pictureId, context).then(function (response) {
        if ('GET_PICTURE_SUCCESS' === response['type']) {
          showPicture(response['picture'])
        }
      })
    } else {
      showPicture(picture);
    }
  }

  render() {
    return (
      <button className="btn btn-outline-info mt-3 mb-2 mr-2" onClick={this.getPicture}>
        <span className="far fa-image"/>
        <span> Show picture </span>
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
    getPicture: (pictureId, context) => dispatch(getPicture(pictureId, context)),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(GetPicture);
