import 'bootstrap/dist/css/bootstrap.min.css';
import '../Css/Common.css';

import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Navbar from 'react-bootstrap/Navbar';
import { SlLogin, SlPencil  } from "react-icons/sl";
import { BsFillQuestionCircleFill } from "react-icons/bs";
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { KAKAO_AUTH_URL } from '../OAuth';

//JWT 디코딩
function TopNav() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const navigate = useNavigate();

    const decodeToken = (token) => {
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(
                atob(base64)
                    .split('')
                    .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                    .join('')
            );

            return JSON.parse(jsonPayload);

        } catch (error) {
            console.error('Error decoding token:', error);
            return null;
        }
    };

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            const decodedToken = decodeToken(token);
            if (decodedToken) {
                const isAdmin = decodedToken.role === 'ADMIN';
                setIsLoggedIn(true);
                setIsAdmin(isAdmin);
            } else {
                // 토큰 디코딩에 실패한 경우 처리
                console.error('Token decoding failed.');
            }
        }
    }, []);

    //로그인
    const handleLogin = (loginCredentials) => {
        console.log('ok');
        axios.post('/login', loginCredentials)
            .then(response => {
                localStorage.setItem('token', response.data.token);
                setIsLoggedIn(true);
                setIsAdmin(response.data.isAdmin === 'true');
                console.log('응답 확인', response.data);
                // 로그인 후 처리
                handleAdminOrMyPageClick();
            })
            .catch(error => {
                console.log('에러 발생');
            });
    };

    //로그아웃
    const handleLogout = () => {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
        setIsAdmin(false);
        //로그아웃 후 홈페이지로 이동
        navigate('/');
    };

    //로그인 후 처리
    const handleAdminOrMyPageClick = () => {
        console.log('로그인 user', isAdmin)
        if (isAdmin) {
            console.log('관리자 페이지 이동 완료')
            navigate('/admin');
        } else {
            console.log('마이 페이지로 이동 완료')
            navigate('/main');
        }
    };

    //로그인 버튼 클릭 시 호출될 함수
    const handleLoginClick = () => {
        const loginCredentials = {
            username: 'username',
            password: 'password',
        };
        handleLogin(loginCredentials);
    };

    return (
        <header>
            <div className="hd_top flex items-center justify-between">
                <span className="logo"><a href="/">YoriJori</a></span>
                <Form className="d-flex">
                    <Form.Control
                        type="search"
                        placeholder="Search"
                        className="me-2"
                        aria-label="Search"
                    />
                    <Button className="btn_regular" variant="outline-success">Search</Button>
                </Form>
                <ul className="signInUp flex items-center space-x-4">
                    {!isLoggedIn ? (
                        <>
                            <li><a href="/signin"><SlPencil /> 회원가입</a></li>
                            <li className="flex items-center space-x-2">
                                <a href="/login" onClick={handleLoginClick}><SlLogin /> 로그인</a>
                                <a
                                    href="/survey"
                                    className="hover:underline"
                                    onClick={(e) => { e.preventDefault(); navigate('/survey'); }}>
                                    <BsFillQuestionCircleFill /> 설문조사
                                </a>
                            </li>
                            {/*<li>*/}
                            {/*    <a href={KAKAO_AUTH_URL} className="kakaobtn w-64 h-10 rounded-lg flex items-center justify-center text-lg">*/}
                            {/*        <img src={`${process.env.PUBLIC_URL}/kakao_login.png`} alt="카카오 로그인" />*/}
                            {/*    </a>*/}
                            {/*</li>*/}
                            {/* 카카오 로그인 버튼 */}
                            <li> <a href={KAKAO_AUTH_URL} className="kakaobtn">
                                <img src={`${process.env.PUBLIC_URL}/kakao_login.png`} alt="카카오 로그인" />
                            </a>
                            </li>
                        </>
                    ) : (
                        <>
                            <li className="flex items-center space-x-2">
                                {isAdmin ? (
                                    <a onClick={handleAdminOrMyPageClick}>관리자 페이지</a>
                                ) : (
                                    <a onClick={handleAdminOrMyPageClick}>마이 페이지</a>
                                )}
                                <a
                                    href="/survey"
                                    className="hover:underline"
                                    onClick={(e) => { e.preventDefault(); navigate('/survey'); }}>
                                    <BsFillQuestionCircleFill /> 설문조사
                                </a>
                            </li>
                            <li><a href="/" onClick={handleLogout}><SlLogin /> 로그아웃</a></li>
                        </>
                    )}
                </ul>
                <img src={`${process.env.PUBLIC_URL}/menu-icon.png`} className="ui-menu-icon" />

            </div>

            <Navbar expand="lg" className="navbar">
                <Container fluid="true" id="contNav">

                    <ul className="navbar-nav">
                        <li className="depth-1 navbar-text"><a href="#">오늘의 요리</a></li>
                        <div className="relative group">
                            <a href="#"
                               className="inline-block mt-1 py-2 px-4 text-sm font-medium font-[SpoqaHanSansNeo-B]">인기
                                레시피</a>
                            <div
                                className="absolute hidden group-hover:block z-10 bg-white divide-y divide-gray-100 rounded shadow w-40">
                                <ul className="py-1 text-center">
                                    <li>
                                        <a href="/rank/recipe/recommend"
                                           className="block py-2 px-4 text-sm text-gray-700 hover:bg-gray-50 hover:text-gray-900">추천수
                                            랭킹</a>
                                    </li>
                                    <li>
                                        <a href="/rank/recipe/totalcount"
                                           className="block py-2 px-4 text-sm text-gray-700 hover:bg-gray-50 hover:text-gray-900">조회수
                                            랭킹</a>
                                    </li>
                                    <li>
                                        <a href="/rank/member/recommend"
                                           className="block py-2 px-4 text-sm text-gray-700 hover:bg-gray-50 hover:text-gray-900">유저
                                            랭킹</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <li className="depth-1 navbar-text"><a href="#">매거진</a></li>
                    </ul>

                </Container>
            </Navbar>

        </header>
    );
}

export default TopNav;