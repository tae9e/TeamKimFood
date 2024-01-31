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
        <div className={'flex justify-center items-center my-20 '}>
        <form onSubmit={handleSubmit} className={'w-full max-w-xs border p-3 rounded-lg'}>
            <div className={'mb-1 border p-3 rounded-lg'}>
                <label className={'block text-gray-700 text-sm font-bold mb-2'}>이름:</label>
                <input
                    type="text"
                    name="name"
                    value={memberData.name}
                    onChange={handleChange}
                    required
                    placeholder={'ex)홍길동'}
                    className={'shadow appearance-none border ${errors.name ? \'border-red-500\' : \'\'} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}
                    // style={{ borderColor: errors.name ? 'red' : 'initial' }}
                />
                {errors.name && <p style={{ color: 'red' }}>{errors.name}</p>}
            </div>
            <div className={' mb-1 border p-3 rounded-lg'}>
                <label className={'block text-gray-700 text-sm font-bold mb-2'}>비밀번호:</label>
                <input type="password" name="password" value={memberData.password} onChange={handleChange} required placeholder={'비밀번호를 입력해주세요!'}
                       className={'shadow appearance-none border ${errors.password ? \'border-red-500\' : \'\'} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}
                       // style={{ borderColor: errors.password ? 'red' : 'initial' }}
                />

            </div>
            <div className={'mb-1 border p-3 rounded-lg'}>
                <label className={'block text-gray-700 text-sm font-bold mb-2'}>이메일:</label>
                <input type="email" name="email" value={memberData.email} onChange={handleChange} required
                       placeholder={'ex) hong@yorijori.shop'}
                       className={'shadow appearance-none border ${errors.email ? \'border-red-500\' : \'\'} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}
                       // style={{ borderColor: errors.email ? 'red' : 'initial' }}
                />
            </div>
            <div className={'mb-1 border p-3 rounded-lg'}>
                <label className={'block text-gray-700 text-sm font-bold mb-2'}>닉네임:</label>
                <input type="text" name="nickname" value={memberData.nickname} onChange={handleChange} required
                       placeholder={'별명을 만들어주세요!'}
                       className={'shadow appearance-none border ${errors.nickname ? \'border-red-500\' : \'\'} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}
                       // style={{ borderColor: errors.nickname ? 'red' : 'initial' }}
                />
            </div>
            <div className={'mb-1 border p-3 rounded-lg'}>
                <label className={'block text-gray-700 text-sm font-bold mb-2'}>전화번호:</label>
                <input type="text" name="phoneNumber" value={memberData.phoneNumber} onChange={handleChange} required
                       placeholder={'010-0000-0000'}
                       className={'shadow appearance-none border ${errors.phoneNumber ? \'border-red-500\' : \'\'} rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'}
                       // style={{ borderColor: errors.phoneNumber ? 'red' : 'initial' }}
                />
            </div>
            <div className={'mb-4'}>
                <button type="submit"
                className={"bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"}
                >회원가입</button>
            </div>
        </form>
        </div>
    );
};

export default MemberRegistrationForm;