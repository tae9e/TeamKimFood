import React, { useState, useEffect } from 'react';
import axios from 'axios';

const MemberManagement = () => {
    const [members, setMembers] = useState([]);

    useEffect(() => {
        // 멤버 목록을 불러오는 함수
        const fetchMembers = async () => {
            try {
                const response = await axios.get('/admin/dashboard/memberManagement');
                setMembers(response.data);
            } catch (err) {
                console.error('멤버 목록을 불러오는 데 실패했습니다.', err);
                // 오류 처리
            }
        };

        fetchMembers();
    }, []);

    // 멤버를 탈퇴시키는 함수
    const handleMemberRemoval = async (id) => {
        try {
            await axios.delete(`/login/member/${id}`);
            // 멤버 목록을 갱신하기 위해 멤버 목록을 다시 불러옴
            setMembers(members.filter(member => member.id !== id));
        } catch (err) {
            console.error('멤버를 삭제하는 데 실패했습니다.', err);
            // 오류 처리
        }
    };

    return (
        <div>
            <h2>멤버 관리</h2>
            <table>
                <thead>
                <tr>
                    <th>번호</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>닉네임</th>
                    <th>전화번호</th>
                    <th>작업</th>
                </tr>
                </thead>
                <tbody>
                {members.map(member => (
                    <tr key={member.id}>
                        <td>{index + 1}</td>
                        <td>{member.name}</td>
                        <td>{member.email}</td>
                        <td>{member.nickname}</td>
                        <td>{member.phoneNumber}</td>
                        <td>
                            <button onClick={() => handleMemberRemoval(member.email)}>
                                탈퇴시키기
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default MemberManagement;