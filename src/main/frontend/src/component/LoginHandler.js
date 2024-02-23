import React from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function LoginHandler() {
  const navigate = useNavigate();

  const handleKakaoLogin = async () => {
    try {
      const code = new URL(window.location.href).searchParams.get('code');
      const backendUrl = 'http://localhost:8080';

      //카카오 인증 코드 전달
       const res = await axios.get(`${backendUrl}/public/auth/kakao/callback`, {
              params: { code }
            });

         if (res.status === 200) {
                // JWT 토큰 저장 및 사용자 정보 처리
                localStorage.setItem('jwtToken', res.data.jwtToken);
                localStorage.setItem('userInfo', JSON.stringify(res.data.userInfo));
                navigate('/BoardList');
              } else {
                console.error('로그인 실패');
              }
            } catch (error) {
              console.error('카카오 로그인 에러', error);
              // 에러 처리 로직
            }
          };

          React.useEffect(()=>{
          handleKakaoLogin();
          },[]);

          return(
                <div className="LoginHandler">
                      <div className="notice">
                        <p>카카오 로그인 처리 중...</p>
                      </div>
                    </div>
          );
          }
