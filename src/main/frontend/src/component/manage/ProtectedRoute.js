import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
    //컨트롤러에서 설정한 key값 확인하고 수정하기.
    const userRole = localStorage.getItem('userRole'); // localStorage에서 사용자 역할 가져오기

    if (userRole !== 'ADMIN') {
        // 사용자 역할이 'ADMIN'이 아닐 경우 메인 페이지로 리디렉트
        return <Navigate to="/" />;
    }

    return children; // 'ADMIN'일 경우 자식 컴포넌트 렌더링
};

export default ProtectedRoute;