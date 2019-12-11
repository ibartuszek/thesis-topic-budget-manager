import React, {Component} from 'react';
import {connect} from "react-redux";
import {createContext} from "../../../actions/common/createContext";
import {uploadPicture} from "../../../actions/picture/uploadPicture";

class UploadPicture extends Component {

  constructor(props) {
    super(props);
    this.uploadPicture = this.uploadPicture.bind(this);
  }

  uploadPicture(picture) {
    const {userHolder, logHolder, uploadPicture, getPictureId} = this.props;
    const regex = /data:image\/jpe?g;base64,|data:image\/png;base64,|data:image\/gif;base64,|data:image\/bmp;base64,/gi;
    let file = picture[0];
    let reader = new FileReader();
    let context = createContext(userHolder, logHolder);
    reader.addEventListener("load", function () {
      let result = reader.result;
      result = result.replace(regex, "");
      uploadPicture(result, context).then(function (response) {
        if ("UPLOAD_PICTURE_SUCCESS" === response['type']) {
          getPictureId(response['picture'].id);
        }
      });
    }, false);

    if (file) {
      reader.readAsDataURL(file);
    }
  }

  render() {
    return (
      <label className="btn btn-outline-primary mt-3 mb-2">
        <span className="far fa-image"/>
        <span> Upload picture </span>
        <input type="file" hidden onChange={(e) => this.uploadPicture(e.target.files)}/>
      </label>
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
    uploadPicture: (picture, context) => dispatch(uploadPicture(picture, context)),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(UploadPicture);
