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
        <div className={'container mx-auto mt-10 border p-3 rounded-lg'}>
            <h2 className={'text-xl font-bold mb-4'}>멤버 관리</h2>
            <table className={'min-w-full table-auto'}>
                <thead >
                <tr>
                    <th className={'px-4 py-2'}>번호</th>
                    <th className={'px-4 py-2'}>이름</th>
                    <th className={'px-4 py-2'}>이메일</th>
                    <th className={'px-4 py-2'}>닉네임</th>
                    <th className={'px-4 py-2'}>전화번호</th>
                    <th className={'px-4 py-2'}>작업</th>
                </tr>
                </thead>
                <tbody >
                {members.map((member, index) => (
                    <tr key={member.id} className={'border-b'}>
                        <td className={'px04 py-2'}>{index + 1}</td>
                        <td className={'px04 py-2'}>{member.name}</td>
                        <td className={'px04 py-2'}>{member.email}</td>
                        <td className={'px04 py-2'}>{member.nickname}</td>
                        <td className={'px04 py-2'}>{member.phoneNumber}</td>
                        <td className={'px04 py-2'}>
                            <button onClick={() => handleMemberRemoval(member.email)}
                            className={'bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-3 rounded'}>
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