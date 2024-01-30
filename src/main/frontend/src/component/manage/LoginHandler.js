import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const LoginHandler = (props) => {
    const navigate = useNavigate();
     const code = new URL(window.location.href).searchParams.get("code");

    useEffect(() => {
        const kakaoLogin = async () => {
            try {
                const res = await axios({
                    method: "GET",
                    url: `http://localhost:8080/public/auth/kakao/callback?code=${code}`,
                    headers: {
                        "Content-Type": "application/json;charset=utf-8", //json형태로 데이터 보냄
                    },
                });
                console.log("Response", res);
                localStorage.setItem("name", res.data.account.kakaoName);
                navigate("/boardList");
            } catch (error) {
                console.error('로그인 에러', error);
                // 에러 처리 로직
            }
        };
        kakaoLogin();
    }, [props.history]);

    return (
        <div className="LoginHandler">
            <div className="notice">
                <p>잠시만 기다려주세요.</p>
                <div className="spinner"></div>
            </div>
        </div>
    );
};

export default LoginHandler;
