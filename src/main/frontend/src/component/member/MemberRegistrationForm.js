import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const MemberRegistrationForm = () => {
    const [memberData, setMemberData] = useState({
        name: '',
        password: '',
        email: '',
        nickname: '',
        phoneNumber: ''
    });
    const [errors, setErrors] = useState({}); // 유효성 검사 오류를 저장할 상태
    const navigate = useNavigate();
    const handleServerError = (error) => {
        if (error.response && error.response.data) {
            // 오류 메시지가 객체 형태라고 가정
            const errors = error.response.data;
            setErrors(errors);
        }
    };
    const handleChange = (e) => {
        setMemberData({ ...memberData, [e.target.name]: e.target.value });
        // 오류 상태 업데이트 (해당 필드의 오류 제거)
        setErrors({ ...errors, [e.target.name]: '' });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post('/login/member', memberData);
            alert(response.data); // 회원가입 성공 메시지
            navigate('/login'); // 로그인 페이지로 이동
        } catch (error) {
            if (error.response && error.response.data) {
                // 서버로부터 받은 오류 메시지를 상태에 저장
                // setErrors(error.response.data);
                handleServerError(error);
            }
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>이름:</label>
                <input
                    type="text"
                    name="name"
                    value={memberData.name}
                    onChange={handleChange}
                    required
                    style={{ borderColor: errors.name ? 'red' : 'initial' }}
                />
                {errors.name && <p style={{ color: 'red' }}>{errors.name}</p>}
            </div>
            <div>
                <label>비밀번호:</label>
                <input type="password" name="password" value={memberData.password} onChange={handleChange} required
                       style={{ borderColor: errors.password ? 'red' : 'initial' }}/>
            </div>
            <div>
                <label>이메일:</label>
                <input type="email" name="email" value={memberData.email} onChange={handleChange} required
                       style={{ borderColor: errors.email ? 'red' : 'initial' }}/>/>
            </div>
            <div>
                <label>닉네임:</label>
                <input type="text" name="nickname" value={memberData.nickname} onChange={handleChange} required
                       style={{ borderColor: errors.nickname ? 'red' : 'initial' }}/>/>
            </div>
            <div>
                <label>전화번호:</label>
                <input type="text" name="phoneNumber" value={memberData.phoneNumber} onChange={handleChange} required
                       style={{ borderColor: errors.phoneNumber ? 'red' : 'initial' }}/>/>
            </div>
            <button type="submit">회원가입</button>
        </form>
    );
};

export default MemberRegistrationForm;