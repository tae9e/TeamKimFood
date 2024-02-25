import React, {useEffect} from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function LoginHandler() {
  const navigate = useNavigate();

       const handleKakaoLogin = async () => {
              const code = new URL(window.location.href).searchParams.get('code');
              if (!code) {
                  console.log('인증 코드가 없습니다.');
                  navigate("/");
              } else {
                  try {
                      const res = await axios.get('http://localhost:8080/public/auth/kakao/callback', {
                      params: { code } });
                      if (res.status === 200 && res.data.redirectUrl) {
                          window.location.href = res.data.redirectUrl; // 백엔드에서 제공하는 URL로 리디렉션
                      }
                  } catch (error) {
                      console.error('카카오 로그인 에러', error);
                      navigate("/");
                  }
              }
          };

          useEffect(() => {
              handleKakaoLogin();
          }, [navigate]);

          return (
              <div className="LoginHandler">
                  <div className="notice">
                      <p>카카오 로그인 처리 중...</p>
                  </div>
              </div>
          );
      }


  export default LoginHandler;