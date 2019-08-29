import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import * as serviceWorker from './serviceWorker';
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap/dist/js/bootstrap.js";
import "bootstrap/scss/bootstrap.scss";
import "./styles/style.scss";
import "./styles/my-custom-style.css"
import "jquery"
import "popper.js"
import {applyMiddleware, createStore} from 'redux';
import thunkMiddleware from 'redux-thunk';
import {Provider} from 'react-redux';
import RootReducer from './store/RootReducer';

const store = createStore(
  RootReducer,
  applyMiddleware(
    thunkMiddleware
  )
)

ReactDOM.render(<Provider store={store}><App/></Provider>, document.getElementById('root'));

serviceWorker.unregister();
