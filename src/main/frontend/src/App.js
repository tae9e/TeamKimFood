import React, { Component } from 'react';
import { Route, Routes } from 'react-router-dom';
import Main from './component/Main';
import LoginForm from './component/LoginForm';
import Header from './component/Header/Header';
import Footer from './component/Footer/Footer';
import 'bootstrap/dist/css/bootstrap.min.css';
import './component/Css/Layout.css';

class App extends Component {

    render() {
        return(
            <div className="App">
                <Header />

                <Routes>
                    <Route exact path='/' Component={ Main } />
                    <Route path='/login' Component={ LoginForm } />

                </Routes>

                <Footer />
            </div>
        )
    }
}

export default App;