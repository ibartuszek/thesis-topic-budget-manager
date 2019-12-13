import React, {Component} from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import Home from './component/home/Home';
import Incomes from './component/incomes/Incomes';
import LogIn from './component/auth/LogIn';
import Navbar from './component/layout/Navbar';
import Outcomes from "./component/outcomes/Outcomes";
import Register from './component/auth/SignUp';
import Schemas from "./component/schema/Schemas";
import Settings from './component/settings/Settings';
import Statistics from './component/statistics/Statistics';

class App extends Component {
  render() {
    return (
      <BrowserRouter>
        <div className="App">
          <Navbar/>
          <Switch>
            <Route exact path='/' component={Home}/>
            <Route path='/login' component={LogIn}/>
            <Route path='/register' component={Register}/>
            <Route path='/incomes' component={Incomes}/>
            <Route path='/expenses' component={Outcomes}/>
            <Route path='/statistics-schema' component={Schemas}/>
            <Route path='/statistics' component={Statistics}/>
            <Route path='/settings' component={Settings}/>
          </Switch>
        </div>
      </BrowserRouter>
    );
  }
}

export default App;
