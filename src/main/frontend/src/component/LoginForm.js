import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { KAKAO_AUTH_URL } from './OAuth';

function LoginForm() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const submitClick = async (e) => {
        e.preventDefault();
        const loginData = { username: email, password: password };

        try {
            const response = await fetch('http://localhost:8080/public/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginData),
            });

            if(response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token);
                navigate('/boardlist');
            } else {
                setErrorMessage('로그인 실패. 이메일 또는 비밀번호를 확인해주세요.');
            }
        } catch(error) {
            console.error('로그인 요청 중 오류 발생: ', error);
            setErrorMessage('로그인 중 문제가 발생했습니다. 나중에 다시 시도해주세요.');
        }
    };

     return (
            <section className="main">
                <div className="m_login signin">
                    <div className="log_box flex flex-col items-center">
                        <div className="mb-4 mt-4">
                            <input
                                type="text"
                                placeholder="이메일"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="w-64 py-2 px-3 rounded-md border" />
                        </div>
                        <div className="mb-4">
                            <input
                                type="password"
                                placeholder="비밀번호"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-64 py-2 px-3 rounded-md border" />
                        </div>
                        <div className="mb-4">
                            <button
                                onClick={submitClick}
                                className="w-64 bg-black hover:bg-blue-700 text-white font-bold py-2 px-4 rounded cursor-pointer">
                                로그인
                            </button>
                        </div>
                        {errorMessage && (
                            <div className="text-red-500">
                                {errorMessage}
                            </div>
                        )}
                        <div className="mt-0.1">
                            <a href={KAKAO_AUTH_URL} className="kakaobtn w-64 h-10 rounded-lg flex items-center justify-center text-lg">
                                <img src={`${process.env.PUBLIC_URL}/kakao_login.png`} alt="카카오 로그인" />
                            </a>
                        </div>
                    </div>
                </div>
            </section>
        );
    }

    export default LoginForm;
