
import React, { useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';

// import BoardList from './component/BoardList';
import LoginForm from './component/LoginForm';
import Header from './component/Header/Header';
import Footer from './component/Footer/Footer';
import ProtectedRoute from "./component/manage/ProtectedRoute";
import ManagePage from "./component/manage/ManagePage";
import 'bootstrap/dist/css/bootstrap.min.css';
import './component/Css/Common.css';
import './component/Css/Layout.css';
import MemberManagement from "./component/manage/MemberManagement";
import RecipeManagement from "./component/manage/RecipeManagement";
import Dashboard from "./component/manage/Dashboard";
import PersonalTreat from "./component/Footer/PersonalTreat";
import RecipeSave from "./component/recipe/RecipeSave";
import MemberRegistrationForm from "./component/member/MemberRegistrationForm";
import RecipeList from "./component/recipe/RecipeList";
import ViewRecipe from "./component/recipe/ViewRecipe";
import AdminPage from './component/AdminPage';
function App() {

    return(
            <div className="App flex flex-col min-h-screen">
                <Header />
                <div className={'flex-grow mb-5'}>
                <Routes>
                    <Route path='/login' element={ <LoginForm/> } />
                    <Route path='/personaltreat' element={ <PersonalTreat/> } />
                    <Route path="/signin" element={<MemberRegistrationForm/>} />
                    <Route path="/api/recipes/save" element={<RecipeSave/>} />
                    <Route path="/api/recipe/:id" element={<ViewRecipe/>} />
                    <Route path='/api/recipe/:id/update' element={<RecipeSave/>} />
                    <Route path="/main" element={<RecipeList/>} />
                    <Route path="/admin" element={<ProtectedRoute><ManagePage/></ProtectedRoute>} />
                    <Route path="/admin/members" element={<ProtectedRoute><MemberManagement/></ProtectedRoute>} />
                    <Route path="/admin/recipes" element={<ProtectedRoute><RecipeManagement/></ProtectedRoute>} />
                    <Route path="/admin/dashboard" element={<ProtectedRoute><Dashboard/></ProtectedRoute>} />
                </Routes>
                </div>

                <Footer />
            </div>
        )
    }

export default App;
